/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Base JVM.
 * 
 * @since 2021/02/27
 */

#include "sjmerc.h"
#include "jvm.h"
#include "cpu.h"
#include "corefont.h"
#include "oldstuff.h"
#include "bootrom.h"
#include "memory.h"

struct sjme_jvm
{
	/** Virtual memory information. */
	sjme_vmem* vmem;
	
	/** RAM. */
	sjme_vmemmap* ram;
	
	/** ROM. */
	sjme_vmemmap* rom;
	
	/** Configuration space. */
	sjme_vmemmap* config;
	
	/** Framebuffer. */
	sjme_vmemmap* framebuffer;
	
	/** OptionJAR. */
	sjme_vmemmap* optionjar;
	
	/** ROM Data. */
	const sjme_ubyte* romData;
	
	/** Framebuffer info. */
	sjme_framebuffer* fbinfo;
	
	/** Native functions. */
	sjme_nativefuncs* nativefuncs;
	
	/** Linearly fair CPU execution engine. */
	sjme_jint fairthreadid;
	
	/** Threads. */
	sjme_cpu threads[SJME_THREAD_MAX];
	
	/** Did the supervisor boot okay? */
	sjme_jint supervisorokay;
	
	/** System call static field pointer. */
	sjme_vmemptr syscallsfp;
	
	/** System call code pointer. */
	sjme_vmemptr syscallcode;
	
	/** System call pool pointer. */
	sjme_vmemptr syscallpool;
	
	/** Is debugging enabled? */
	sjme_jint enabledebug;
	
	/** Squelch the framebuffer console? */
	sjme_jint squelchfbconsole;
	
	/** CPU Metrics. */
	sjme_cpuMetrics metrics;
};

sjme_returnFail sjme_jvmDestroy(sjme_jvm* jvm, sjme_error* error)
{
	sjme_cpuframe* cpu;
	sjme_cpuframe* oldcpu;
	sjme_jint i;
	
	/* Missing this? */
	if (jvm == NULL)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, 0);
		
		return SJME_RETURN_FAIL;
	}
	
	/* Reset error. */
	sjme_setError(error, SJME_ERROR_NONE, 0);
	
	/* Go through and cleanup CPUs. */
	for (i = 0; i < SJME_THREAD_MAX; i++)
	{
		/* Get CPU here. */
		cpu = &jvm->threads[i].state;
		
		/* Recursively clear CPU stacks. */
		while (cpu->parent != NULL)
		{
			/* Keep for later free. */
			oldcpu = cpu->parent;
			
			/* Copy down. */
			*cpu = *oldcpu;
			
			/* Free CPU state. */
			if (oldcpu != &jvm->threads[i].state)
				sjme_free(oldcpu);
		}
	}
	
	/* Delete major JVM data areas. */
	sjme_free(jvm->ram);
	
	/* Destroyed okay. */
	return SJME_RETURN_SUCCESS;
}

sjme_jint sjme_jvmexec(sjme_jvm* jvm, sjme_error* error, sjme_jint cycles)
{
	sjme_jint threadid;
	sjme_cpu* cpu;
	sjme_error xerror;
	
	/* Fallback error state. */
	if (error == NULL)
	{
		memset(&xerror, 0, sizeof(xerror));
		error = &xerror;
	}
	
	/* Do nothing. */
	if (jvm == NULL)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, 0);
		
		return 0;
	}
	
	/* Run cooperatively threaded style CPU. */
	for (threadid = jvm->fairthreadid;;
		threadid = ((threadid + 1) & SJME_THREAD_MASK))
	{
		/* Have we used all our execution cycles? */
		if (cycles >= 0)
		{
			if (cycles == 0)
				break;
			if ((--cycles) <= 0)
				break;
		}
		
		/* Ignore CPUs which are not turned on. */
		cpu = &jvm->threads[threadid];
		if (cpu->threadstate == SJME_THREAD_STATE_NONE)
			continue;
		
		/* Execute CPU engine. */
		cycles = sjme_cpuexec(jvm, cpu, error, cycles);
		
		/* CPU fault, stop! */
		if (error->code != SJME_ERROR_NONE)
			break;
	}
	
	/* Start next run on the CPU that was last executing. */
	jvm->fairthreadid = (threadid & SJME_THREAD_MASK);
	
	/* Print error state to console? */
	if (error->code != SJME_ERROR_NONE)
	{
		/* Force error to be on-screen. */
		jvm->supervisorokay = 0;
		sjme_printerror(jvm, error);
	}
	
	/* Returning remaining number of cycles. */
	return cycles;
}

sjme_returnFail sjme_jvmNew(sjme_jvm** outJvm, sjme_jvmoptions* options,
	sjme_nativefuncs* nativefuncs, sjme_error* error)
{
	sjme_jvmoptions nullOptions;
	void* ram;
	const sjme_ubyte* rom;
	void* conf;
	void* optionjar;
	sjme_jvm* rv;
	sjme_jint i, l, romsize;
	sjme_framebuffer* fbinfo;
	sjme_vmem* vmem;
	
	/* Missing important arguments. */
	if (outJvm == NULL || nativefuncs == NULL || options == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS,
			((!!nativefuncs)) * 2 + (!!options));
		return SJME_RETURN_FAIL;
	}
	
	/* No ROM Specified, since it is now required. */
	if (options->romData == NULL || options->romSize <= 0)
	{
		sjme_setError(error, SJME_ERROR_NONATIVEROM, 0);
		return SJME_RETURN_FAIL;
	}
	
	/* Allocate virtual memory manager. */
	vmem = sjme_vmmnew(error);
	if (vmem == NULL)
	{
		sjme_setError(error, SJME_ERROR_VMMNEWFAIL, error->code);
		return SJME_RETURN_FAIL;
	}
	
	/* Allocate VM state. */
	rv = sjme_malloc(sizeof(*rv));
	if (rv == NULL)
	{
		sjme_free(rv);
		
		sjme_setError(error, SJME_ERROR_NOMEMORY,
					  sizeof(*rv));
		
		return SJME_RETURN_FAIL;
	}
	
	/* Store virtual memory area. */
	rv->vmem = vmem;
	
	/* If no RAM size was specified then use the default. */
	if (options->ramSize <= 0)
		options->ramSize = SJME_DEFAULT_RAM_SIZE;
	
	/* Allocate RAM, or at least keep trying to. */
	while (options->ramSize >= SJME_MINIMUM_RAM_SIZE)
	{
		/* Attempt to allocate the RAM. */
		ram = sjme_malloc(options->ramSize);
		
		/* Ram allocated! So stop. */
		if (ram != NULL)
			break;
		
		/* Cut RAM allocation size down in half. */
		options->ramSize /= 2;
	}
	
	/* Failed to allocate the RAM. */
	if (ram == NULL)
	{
		sjme_setError(error, SJME_ERROR_NOMEMORY, options->ramSize);
			
		sjme_free(rv);
		
		return SJME_RETURN_FAIL;
	}
	
	/* Virtual map RAM. */
	rv->ram = sjme_vmmmap(vmem, 0, ram, options->ramSize, error);
	if (rv->ram == NULL)
	{
		sjme_setError(error, SJME_ERROR_VMMMAPFAIL, 0);
		
		sjme_free(rv);
		
		return SJME_RETURN_FAIL;
	}
	
	/* Set native functions. */
	rv->nativefuncs = nativefuncs;
	
	/* Initialize the framebuffer info, if available. */
	fbinfo = NULL;
	if (nativefuncs->framebuffer != NULL)
		fbinfo = nativefuncs->framebuffer();
	
	/* Initialize framebuffer, done a bit early to show errors. */
	rv->fbinfo = fbinfo;
	if (fbinfo != NULL)
	{
		/* If scan-line is not specified, default to the display width. */
		if (fbinfo->scanlen == 0)
			fbinfo->scanlen = fbinfo->width;
		
		/* Number of available pixels. */
		if (fbinfo->numpixels == 0)
			fbinfo->numpixels = fbinfo->scanlen * fbinfo->height;
		
		/* Bytes per pixel must be specified. */
		if (fbinfo->bitsperpixel == 0)
			switch (fbinfo->format)
			{
				case SJME_PIXELFORMAT_PACKED_ONE:
					fbinfo->bitsperpixel = 1;
					break;
				
				case SJME_PIXELFORMAT_PACKED_TWO:
					fbinfo->bitsperpixel = 2;
					break;
				
				case SJME_PIXELFORMAT_PACKED_FOUR:
					fbinfo->bitsperpixel = 4;
					break;
				
				case SJME_PIXELFORMAT_BYTE_INDEXED:
					fbinfo->bitsperpixel = 8;
					break;
				
				case SJME_PIXELFORMAT_SHORT_RGB565:
					fbinfo->bitsperpixel = 16;
					break;
				
				default:
				case SJME_PIXELFORMAT_INTEGER_RGB888:
					fbinfo->bitsperpixel = 32;
					break;
			}
		
		/* Scan line in bytes is based on the bytes per pixel. */
		if (fbinfo->scanlenbytes == 0)
			fbinfo->scanlenbytes =
				(fbinfo->scanlen * fbinfo->bitsperpixel) / 8;
		
		/* Console positions and size. */
		fbinfo->conx = 0;
		fbinfo->cony = 0;
		fbinfo->conw = fbinfo->width / sjme_font.charwidths[0];
		fbinfo->conh = fbinfo->height / sjme_font.pixelheight;
	}
	
	/* Virtual map framebuffer, if available. */
	if (fbinfo != NULL)
	{
		fbinfo->framebuffer = sjme_vmmmap(vmem, 0, fbinfo->pixels,
			(fbinfo->numpixels * fbinfo->bitsperpixel) / 8, error);
		if (fbinfo->framebuffer == NULL)
		{
			sjme_setError(error, SJME_ERROR_VMMMAPFAIL, 0);
			
			sjme_free(rv);
			sjme_free(ram);
			
			return SJME_RETURN_FAIL;
		}
	}
	
	/* Needed by destruction later. */
	rv->romData = options->romData;
	rom = options->romData;
	romsize = options->romSize;
	
	/* Virtual map ROM. */
	rv->rom = sjme_vmmmap(vmem, 0, rom, romsize, error);
	if (rv->rom == NULL)
	{
		sjme_setError(error, SJME_ERROR_VMMMAPFAIL, 0);
		
		sjme_free(rv);
		sjme_free(ram);
		sjme_free(conf);
		if (rv->romData == NULL)
			sjme_free(rom);
		
		return SJME_RETURN_FAIL;
	}
	
	/* Initialize the BootRAM and boot the CPU. */
	if (sjme_loadBootRom(rv, error) == 0)
	{
		/* Write the Boot failure message! */
		sjme_console_pipewrite(rv, (nativefuncs != NULL ?
			nativefuncs->stderr_write : NULL), sjme_bootfailmessage, 0,
			sjme_bootfailmessageSizeOf, error);
		
		/* Force error to be on-screen. */
		rv->supervisorokay = 0;
		sjme_printerror(rv, error);
		
		/* Cleanup. */
		sjme_free(rv);
		sjme_free(ram);
		sjme_free(conf);
		
		/* If a pre-set ROM is not being used, make sure it gets cleared. */
		if (rv->romData == NULL)
			sjme_free(rom);
		
		return SJME_RETURN_FAIL;
	}
	
	/* Memory map the option JAR, if available. */
	if (nativefuncs->optional_jar != NULL)
		if (nativefuncs->optional_jar(&optionjar, &i) != 0)
		{
			rv->optionjar = sjme_vmmmap(vmem, 0, optionjar, i, error);
			if (rv->rom == NULL)
			{
				sjme_setError(error, SJME_ERROR_VMMMAPFAIL, 0);
				
				sjme_free(rv);
				sjme_free(ram);
				sjme_free(conf);
				if (rv->romData == NULL)
					sjme_free(rom);
				
				return SJME_RETURN_FAIL;
			}
		}
	
	/* The JVM is ready to use. */
	*outJvm = rv;
	return SJME_RETURN_SUCCESS;
}

sjme_vmem* sjme_jvmVMem(sjme_jvm* jvm)
{
	return jvm->vmem;
}

struct sjme_cpuMetrics* sjme_jvmCpuMetrics(sjme_jvm* jvm)
{
	return &jvm->metrics;
}

sjme_jboolean sjme_jvmIsDebug(sjme_jvm* jvm)
{
	return jvm->enabledebug;
}

sjme_framebuffer* sjme_jvmFramebuffer(sjme_jvm* jvm)
{
	return jvm->fbinfo;
}

sjme_nativefuncs* sjme_jvmNativeFuncs(sjme_jvm* jvm)
{
	return jvm->nativefuncs;
}
