// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

import java.lang.ref.Reference;

/**
 * This is an internal representation of a menu item.
 *
 * @since 2016/05/23
 */
public interface PIMenuItem
	extends PIBase
{
	/**
	 * Sets the icon of the menu item.
	 *
	 * @param __icon The icon to set.
	 * @throws UIException If the icon could not be set.
	 * @since 2016/05/23
	 */
	public abstract void setIcon(UIImage __icon)
		throws UIException;
	
	/**
	 * Sets the text of the menu item.
	 *
	 * @param __text The text to set.
	 * @throws UIException If the text could not be set.
	 * @since 2016/05/23
	 */
	public abstract void setText(String __text)
		throws UIException;
}

