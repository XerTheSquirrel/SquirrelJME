// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import net.multiphasicapps.squirreljme.classpath.ClassPath;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import __squirreljme.IPCAlternative;
import __squirreljme.IPCException;
import __squirreljme.IPCClient;
import __squirreljme.IPCServer;

/**
 * This is the base class for the kernel interfaces which are defined by
 * systems to provide anything that the default kernel does not provide
 * when it comes to interfaces. The kernel manages the processes for the
 * system along with inter-process communication and inter-process objects.
 *
 * @since 2016/05/14
 */
public abstract class Kernel
{
	/**
	 * This is the class unit which should be used by default for the
	 * launcher.
	 */
	public static final String DEFAULT_LAUNCHER =
		"launcher.jar";
	
	/** The implementation specific execution core (optional). */
	protected final Object executioncore;
	
	/** Threads in the kernel, this list must remain sorted by ID. */
	private final LinkedList<KernelThread> _threads =
		new LinkedList<>();
	
	/** Processes in the kernel, this list must remain sorted by ID. */
	private final List<KernelProcess> _processes =
		new LinkedList<>();
	
	/** The next thread ID to use. */
	private volatile int _nextthreadid;
	
	/** The next process ID to use. */
	private volatile int _nextprocessid;
	
	/** Services started at the root? */
	private volatile boolean _svstarted;
	
	/**
	 * Initializes the base kernel interface.
	 *
	 * @param __args Arguments which match the Java standard which specifies
	 * the potential initial program to launch once the kernel has been
	 * initialized.
	 * @since 2016/05/16
	 */
	public Kernel(Object __exec, String... __args)
	{
		// Must always exist
		if (__args == null)
			__args = new String[0];
		
		// Set the optional execution core which may be used as a single
		// parameter that an implementation may require (since the
		// constructors of sub-classes run AFTER this constructor which needs
		// the execution core stuff).
		this.executioncore = __exec;
		
		// Start the services which the kernel uses (such as the filesystem or
		// display server)
		// {@squirreljme.error AY02 The implementation of the kernel never
		// called the super-class startServices() method.}
		startServices();
		if (!_svstarted)
			throw new KernelException("AY02");
		
		// Get the list of class unit providers
		ClassUnitProvider[] cups = internalClassUnitProviders();
		
		// Determine if there is a chance the user wants to use an alternative
		// launcher interface
		String uselauncher = DEFAULT_LAUNCHER;
		for (String a : __args)
		{
			// Stop on main class or JAR
			if (!a.startsWith("-") || a.startsWith("-jar"))
				break;
			
			// {@squirreljme.cmdline -Xsquirreljme-launcher=(jar) This is the
			// alternative JAR file which should be loaded and initialized for
			// the launcher interface. This option would be specified if for
			// example the default launcher is not desired for usage.}
			if (a.startsWith("-Xsquirreljme-launcher="))
				uselauncher = a.substring("-Xsquirreljme-launcher=".length());
		}
		
		// Search for the launcher and determine the launcher dependencies
		// so that a user interface may be provided
		if (true)
			throw new Error("TODO");
		
		// Initialize the kernel IPC interface that the launcher will use
		// to launch programs.
		if (true)
			throw new Error("TODO");
		
		// Start the launcher which will automatically pickup the main
		// arguments passed to the kernel along with other details
		if (true)
			throw new Error("TODO");
	}
	
	/**
	 * Returns the class unit providers which are available for this kernel so
	 * that classes may be loaded.
	 *
	 * @return The array of class unit providers.
	 * @throws KernelException If the class unit providers could not be
	 * obtained.
	 * @since 2016/05/30
	 */
	protected abstract ClassUnitProvider[] internalClassUnitProviders()
		throws KernelException;
	
	/**
	 * Internally creates a new process which may be executed.
	 *
	 * @return The newly created process which is implementation specific.
	 * @throws KernelException If the process could not be created.
	 * @sicne 2016/05/29
	 */
	protected abstract KernelProcess internalCreateProcess()
		throws KernelException;
	
	/**
	 * Internally creates a new thread which may be executed.
	 *
	 * @return A kernel based thread which is implementation specific.
	 * @throws KernelException If the thread could not be created.
	 * @since 2016/05/28
	 */
	protected abstract KernelThread internalCreateThread()
		throws KernelException;
	
	/**
	 * Internally determines the current kernel thread which has called this
	 * method.
	 *
	 * @return The current kernel thread which called this method.
	 * @throws KernelException If the current thread could not be determined.
	 * @since 2016/05/28
	 */
	protected abstract KernelThread internalCurrentThread()
		throws KernelException;
	
	/**
	 * Runs an internal cycle within the kernel.
	 *
	 * @throws KernelException If the cycle could not be ran.
	 * @since 2016/05/29
	 */
	protected abstract void internalRunCycle()
		throws KernelException;
	
	/**
	 * Attempts to quit the kernel, if the kernel cannot be quit then nothing
	 * happens.
	 *
	 * @since 2016/05/18
	 */
	public abstract void quitKernel();
	
	/**
	 * This creates a new thread which is to be managed by the kernel.
	 *
	 * @return The newly created thread.
	 * @since 2016/05/29
	 */
	public final KernelThread createThread()
	{
		// Lock on threads
		LinkedList<KernelThread> threads = this._threads;
		synchronized (threads)
		{
			// Internally create a thread
			KernelThread rv = internalCreateThread();
			
			// Easily place the thread at the end?
			int id = rv.id();
			if (threads.isEmpty() || threads.getLast().id() < id)
				threads.addLast(rv);
			
			// Otherwise go through the list to find where it is inserted
			else
			{
				// Add into the position that
				ListIterator<KernelThread> it = threads.listIterator();
				boolean ok = false;
				while (it.hasNext())
				{
					// Get the ID here
					int tid = it.next().id();
					
					// Passed placement ID, place before this one
					if (tid > id)
					{
						it.previous();
						it.add(rv);
						ok = true;
						break;
					}
					
					// {@squirreljme.error AY08 Attempted to create a thread
					// which shares an ID with another thread. (The shared
					// thread ID)}
					else if (tid == id)
						throw new KernelException(String.format("AY08 %d",
							id));
				}
				
				// {@squirreljme.error AY01 Did not add a newly created thread
				// into the thread list.}
				if (!ok)
					throw new KernelException("AY01");
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Runs an a single cycle within the kernel.
	 *
	 * @since 2016/05/29
	 */
	public final void runCycle()
	{
		// Perform internal cycling as required
		internalRunCycle();
	}
	
	/**
	 * This may be used to start services which are provided by implementations
	 * of the kernel.
	 *
	 * Calling of the super class must be performed!
	 *
	 * @since 2016/05/30
	 */
	protected void startServices()
	{
		// Set as started
		_svstarted = true;
	}
	
	/**
	 * Finds the next free ID that is available for usage.
	 *
	 * @param __idl The list of identifiable to find an ID for.
	 * @throws KernelException If an ID could not be found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/29
	 */
	private final int __nextIdentifiableId(
		List<? extends __Identifiable__> __idl)
		throws KernelException, NullPointerException
	{
		// Check
		if (__idl == null)
			throw new NullPointerException("NARG");
		
		// Go through all the sorted IDs to find an unused ID
		int at = 0;
		for (Iterator<? extends __Identifiable__> it = __idl.iterator();
			it.hasNext();)
		{
			// Obtain the given identifiable ID
			int tid = it.next().id();
			
			// Place here?
			if (at < tid)
				return at;
			
			// Try the next ID
			at++;
		}
		
		// {@squirreljme.error AY06 Could not find a free ID that
		// is available for usage.}
		throw new KernelException("AY06");
	}
	
	/**
	 * Returns the next available process ID that is free for usage by a new
	 * process.
	 *
	 * @return The next process ID.
	 * @since 2016/05/29
	 */
	final int __nextProcessId()
	{
		// Lock on threads
		List<KernelProcess> processes = this._processes;
		synchronized (processes)
		{
			// Determine the next value
			int next = _nextprocessid;
			
			// Overflows? Find an ID that is positive and not used
			if (next < 0 || next == Integer.MAX_VALUE)
				return __nextIdentifiableId(processes);
			
			// Set next to use next time
			else
				_nextprocessid = next + 1;
			
			// Use it
			return next;
		}
	}
	
	/**
	 * Returns the next thread ID to use for a newly created thread.
	 *
	 * @return The next ID to use.
	 * @since 2016/05/29
	 */
	final int __nextThreadId()
	{
		// Lock on threads
		List<KernelThread> threads = this._threads;
		synchronized (threads)
		{
			// Determine the next value
			int next = _nextthreadid;
			
			// Overflows? Find an ID that is positive and not used
			if (next < 0 || next == Integer.MAX_VALUE)
				return __nextIdentifiableId(threads);
			
			// Set next to use next time
			else
				_nextthreadid = next + 1;
			
			// Use it
			return next;
		}
	}
}

