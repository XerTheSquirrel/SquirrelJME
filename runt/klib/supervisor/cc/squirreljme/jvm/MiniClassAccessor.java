// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This class is used as a utility to access the data contained within the
 * mini-class format.
 *
 * @since 2019/07/11
 */
public final class MiniClassAccessor
{
	/** The mini-class address. */
	protected final int address;
	
	/**
	 * Initializes the mini class accessor.
	 *
	 * @param __a The address of the mini-class.
	 * @since 2019/07/11
	 */
	public MiniClassAccessor(int __a)
	{
		this.address = __a;
		
		todo.DEBUG.code('m', 'd', __a);
	}
}
