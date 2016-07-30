// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.linux.mips;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Path;
import net.multiphasicapps.squirreljme.builder.BuildConfig;
import net.multiphasicapps.squirreljme.builder.TargetBuilder;
import net.multiphasicapps.squirreljme.builder.TargetEmulator;
import net.multiphasicapps.squirreljme.builder.TargetEmulatorArguments;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.zip.blockreader.ZipFile;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;
import net.multiphasicapps.zip.ZipCompressionType;

/**
 * This is used to build SquirrelJME for MIPS based Linux systems.
 *
 * @since 2016/07/30
 */
public class LinuxMIPSBuilder
	extends TargetBuilder
{
	/**
	 * Initializes the Linux MIPS target builder.
	 *
	 * @since 2016/07/30
	 */
	public LinuxMIPSBuilder()
	{
		super(false,
			"mips-32+i,little.linux.generic",
				"Generic Linux MIPS (Little Endian)",
			"mips-32+mips32,little.linux.gcwzero",
				"GCW Zero");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public TargetEmulator emulate(TargetEmulatorArguments __args)
		throws IllegalArgumentException, NullPointerException
	{
		return new LinuxMIPSTargetEmulator(__args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public void linkBinary(ZipStreamWriter __zsw, String[] __names,
		InputStream[] __blobs, BuildConfig __conf, String[] __vmcp)
		throws JITException, IOException, NullPointerException
	{
		// Check
		if (__zsw == null || __names == null || __blobs == null ||
			__conf == null || __vmcp == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public void outputConfig(JITOutputConfig __conf, BuildConfig __bc)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __bc == null)
			throw new NullPointerException("NARG");
		
		// Nothing needs to be done
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public boolean supportsConfig(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Any Linux MIPS system
		JITTriplet triplet = __conf.triplet();
		return triplet.architecture().equals("mips") &&
			triplet.operatingSystem().equals("linux");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public String targetPackageGroup(BuildConfig __conf)
		throws NullPointerException
	{
		// Check
		if (__conf == null)
			throw new NullPointerException("NARG");
		
		// Linux something
		return "linux-mips-" + __conf.triplet().operatingSystemVariant();
	}
}

