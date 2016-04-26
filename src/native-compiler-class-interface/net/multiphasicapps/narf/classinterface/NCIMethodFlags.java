// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

/**
 * This represents the set of flags for methods.
 *
 * @since 2016/04/23
 */
public final class NCIMethodFlags
	extends NCIMemberFlags<NCIMethodFlag>
{
	/**
	 * Initializes the method flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The method flags.
	 * @since 2016/04/23
	 */
	public NCIMethodFlags(NCIClass __oc, NCIMethodFlag... __fl)
	{
		super(NCIMethodFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Initializes the method flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The method flags.
	 * @since 2016/04/23
	 */
	public NCIMethodFlags(NCIClass __oc, Iterable<NCIMethodFlag> __fl)
	{
		super(NCIMethodFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Returns {@code true} if this is abstract.
	 *
	 * @return {@code true} if abstract.
	 * @since 2016/03/20
	 */
	public boolean isAbstract()
	{
		return contains(NCIMethodFlag.ABSTRACT);
	}
	
	/**
	 * Returns {@code true} if this is a bridge method.
	 *
	 * @return {@code true} if a bridge method.
	 * @since 2016/03/20
	 */
	public boolean isBridge()
	{
		return contains(NCIMethodFlag.BRIDGE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return contains(NCIMethodFlag.FINAL);
	}
	
	/**
	 * Returns {@code true} if this is a native method.
	 *
	 * @return {@code true} if it is native.
	 * @since 2016/03/20
	 */
	public boolean isNative()
	{
		return contains(NCIMethodFlag.NATIVE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return contains(NCIMethodFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return contains(NCIMethodFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return contains(NCIMethodFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return contains(NCIMethodFlag.STATIC);
	}
	
	/**
	 * Returns {@code true} if this is strict.
	 *
	 * @return {@code true} if strict.
	 * @since 2016/03/20
	 */
	public boolean isStrict()
	{
		return contains(NCIMethodFlag.STRICT);
	}
	
	/**
	 * Returns {@code true} if this is synchronized.
	 *
	 * @return {@code true} if synchronized.
	 * @since 2016/03/20
	 */
	public boolean isSynchronized()
	{
		return contains(NCIMethodFlag.SYNCHRONIZED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isSynthetic()
	{
		return contains(NCIMethodFlag.SYNTHETIC);
	}
	
	/**
	 * Returns {@code true} if this uses variable arguments.
	 *
	 * @return {@code true} if it uses variable arguments.
	 * @since 2016/03/20
	 */
	public boolean isVarArgs()
	{
		return contains(NCIMethodFlag.VARARGS);
	}
	/**
	 * Checks that the given flags are valid.
	 *
	 * @param __oc The outer class.
	 * @throws NCIException If they are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	private final void __checkFlags(NCIClass __oc)
		throws NCIException
	{
		// Check
		if (__oc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error NC0z Native methods are not supported in Java ME
		// and as such, methods must not be {@code native}.}
		if (isNative())
			throw new NCIException(NCIException.Issue.NATIVE_METHOD, "NC0z");
		
		// {@squirreljme.error NC10 An {@code abstract} method cannot be
		// {@code private}, {@code static}, {@code final},
		// {@code synchronized}, {@code native}, or {@code strictfp}. (The
		// method flags)}
		if (isAbstract())
			if (isPrivate() || isStatic() || isFinal() || isSynchronized() ||
				isNative() || isStrict())
				throw new NCIException(NCIException.Issue.ILLEGAL_FLAGS,
					String.format("NC10 %s", this));
		
		// If the class is an interface it cannot have specific flags set
		if (__oc.flags().isInterface())
			for (NCIMethodFlag f : NCIMethodFlag.values())
			{
				// Must have these
				boolean must = (f == NCIMethodFlag.PUBLIC ||
					f == NCIMethodFlag.ABSTRACT);
				
				// Could have these
				boolean maybe = (f == NCIMethodFlag.SYNTHETIC ||
					f == NCIMethodFlag.VARARGS || f == NCIMethodFlag.BRIDGE);
				
				// Is it set?
				boolean has = contains(f);
				
				// {@squirreljme.error NC1x Flags for interface method has an
				// incorrect set of flags. (The method flags)}
				if (must != has && !maybe)
					throw new NCIException(NCIException.Issue.ILLEGAL_FLAGS,
						String.format("NC1x %s", this));
			}
	}
}

