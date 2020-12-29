// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.swm.launch;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.swm.SuiteInfo;

/**
 * Lazily loaded library handling, for later dependency handling.
 *
 * @since 2020/12/29
 */
final class __Libraries__
{
	/**
	 * Registers a library for later dependency handling.
	 * 
	 * @param __info The JAR information.
	 * @param __jar The JAR itself.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/29
	 */
	final void __register(SuiteInfo __info, JarPackageBracket __jar)
		throws NullPointerException
	{
		if (__info == null || __jar == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
}
