// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.descriptors;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a symbol as it appears to the {@link ClassLoader}.
 *
 * @since 2016/04/06
 */
public final class ClassLoaderNameSymbol
	extends __BaseSymbol__
{
	/** The class name of this symbol. */
	private volatile Reference<ClassNameSymbol> _cname;
	
	/**
	 * Initializes the class loader symbol.
	 *
	 * @param __s The class loader name.
	 * @throws IllegalSymbolException If the symbol contains invalid
	 * characters.
	 * @since 2016/04/06
	 */
	public ClassLoaderNameSymbol(String __s)
		throws IllegalSymbolException
	{
		this(__s, null);
	}
	
	/**
	 * Initializes the class loader symbol with an optional cache.
	 *
	 * @param __s The class loader name.
	 * @param __cl The optional class name to cache.
	 * @throws IllegalSymbolException If the symbol contains invalid
	 * characters.
	 * @since 2016/04/06
	 */
	ClassLoaderNameSymbol(String __s, ClassNameSymbol __cl)
		throws IllegalSymbolException
	{
		super(__s);
		
		// If not an array, it cannot contain any forward slashes
		boolean isarray = ('[' == charAt(0));
		if (!isarray)
		{
			// {@squirreljme.error DS0e Non-array class loader names cannot
			// contain forward slashes. (This symbol; The illegal character)}
			int n = length();
			char c;
			for (int i = 0; i < n; i++)
				if ((c = charAt(i)) == '/')
					throw new IllegalSymbolException(String.format(
						"DS0e %s %c", this, c));
		}
		
		// Pre-cache?
		if (__cl != null)
			_cname = new WeakReference<>(__cl);
		
		// Check for valid class name, since this could be an array too
		asClassName();
	}
	
	/**
	 * Returns this class loader symbol as a class name symbol.
	 *
	 * @return The symbol as a class name.
	 * @since 2016/04/06
	 */
	public ClassNameSymbol asClassName()
	{
		// Get reference
		Reference<ClassNameSymbol> ref = _cname;
		ClassNameSymbol rv;
		
		// Needs to be created?
		if (ref == null || null == (rv = ref.get()))
		{
			// Is this an array?
			boolean isarray = ('[' == charAt(0));	
			
			// Arrays are treated like fields, otherwise the names of classes
			// get their dots replaced with slashes
			_cname = new WeakReference<>((rv = new ClassNameSymbol(
				(isarray ? toString() :  toString(). replace('.', '/')),
				false, this)));
		}
		
		// Return it
		return rv;
	}
}

