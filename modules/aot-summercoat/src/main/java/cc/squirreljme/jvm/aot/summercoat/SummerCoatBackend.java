// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat;

import cc.squirreljme.jvm.aot.Backend;

/**
 * This is the backend for SummerCoat.
 *
 * @since 2020/11/22
 */
public class SummerCoatBackend
	implements Backend
{
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public String name()
	{
		return "summercoat";
	}
}
