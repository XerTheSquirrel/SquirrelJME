// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

/**
 * This represents the type of compression that is used in a ZIP file.
 *
 * @since 2016/07/15
 */
public enum ZipCompressionType
{
	/** Data is not compressed. */
	NO_COMPRESSION(10, 0),
	
	/** Deflate algorithm. */
	DEFLATE(20, 8),
	
	/** End. */
	;
	
	/** The version needed to extract. */
	protected final int extractversion;
	
	/** The compression method. */
	protected final int method;
	
	/**
	 * Initializes the enumeration.
	 *
	 * @param __xv The version needed to extract.
	 * @param __m The compression method identifier.
	 * @since 2016/07/15
	 */
	private ZipCompressionType(int __xv, int __m)
	{
		this.extractversion = __xv;
		this.method = __m;
	}
	
	/**
	 * Returns the version which is needed to extract this data.
	 *
	 * @return The required version.
	 * @since 2016/07/15
	 */
	public final int extractVersion()
	{
		return this.extractversion;
	}
	
	/**
	 * Returns the compression method.
	 *
	 * @return The compression method.
	 * @since 2016/07/15
	 */
	public final int method()
	{
		return this.method;
	}
}

