// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

/**
 * This class contains information that is needed by the class object.
 *
 * The purpose is to allow newer versions of the SquirrelJME library to be
 * used on older virtual machines so they can benefit from an updated library
 * without breaking backwards compatiblity by missing things.
 *
 * @since 2018/12/04
 */
public abstract class ClassData
{
	/** The version of the class data. */
	public final int version;
	
	/**
	 * Initializes the base class data.
	 *
	 * @param __v The version.
	 * @since 2018/12/04
	 */
	public ClassData(int __v)
	{
		this.version = __v;
	}
	
	/**
	 * Returns the binary name.
	 *
	 * @return The binary name.
	 * @since 2018/12/04
	 */
	public abstract String binaryName();
	
	/**
	 * Returns the component of the array.
	 *
	 * @return The array component.
	 * @since 2018/12/04
	 */
	public abstract Class<?> component();
	
	/**
	 * Returns the number of dimensions for the array.
	 *
	 * @return The dimension count.
	 * @since 2018/12/04
	 */
	public abstract int dimensions();
	
	/**
	 * Returns the class flags.
	 *
	 * @return The class flags.
	 * @since 2018/12/04
	 */
	public abstract int flags();
	
	/**
	 * Returns the JAR this class is in.
	 *
	 * @return The JAR this class is in.
	 * @since 2018/12/04
	 */
	public abstract String inJar();
	
	/**
	 * Returns the the interfaces this class implements.
	 *
	 * @return The interface classes.
	 * @since 2018/12/04
	 */
	public abstract Class<?>[] interfaceClasses();
	
	/**
	 * Returns the super class.
	 *
	 * @return The super class.
	 * @since 2018/12/04
	 */
	public abstract Class<?> superClass();
}
