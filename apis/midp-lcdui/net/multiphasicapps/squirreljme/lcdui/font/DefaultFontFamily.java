// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.font;

/**
 * This supports handling of font families for the default font manager which
 * provides fallback fonts if no system fonts are available.
 *
 * @since 2017/10/21
 */
public class DefaultFontFamily
	extends FontFamily
{
	/**
	 * Initializes the base font family.
	 *
	 * @param __n The name of the font.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/21
	 */
	public DefaultFontFamily(FontFamilyName __n)
		throws NullPointerException
	{
		super(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/24
	 */
	@Override
	public int sequencePixelWidth(FontHandle __h, CharSequence __s,
		int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		return FontFamily.INVALID_WIDTH;
	}
}

