// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch;

import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class acts as the base for machine code output which is implemented by
 * each architecture. The machine code output does not care about any
 * optimizations that were performed, it just writes whatever instructions to
 * some output target. It should be noted that in most cases the output here
 * is intended really only to be written once rather than having multiple
 * variants of it. The machine code outputs are not intended in any way to
 * optimize what is input.
 *
 * @since 2017/08/07
 */
public interface MachineCodeOutput
	extends AutoCloseable
{
	/**
	 * This closes the machine code output which then causes any machine code
	 * which has been generated to be output if delayed output is utilized.
	 *
	 * @throws JITException If it could not be closed.
	 * @since 2017/08/13
	 */
	@Override
	public abstract void close()
		throws JITException;
	
	/**
	 * Returns the used JIT configuration.
	 *
	 * @return The JIT configuration.
	 * @since 2017/08/10
	 */
	public abstract JITConfig config();
}

