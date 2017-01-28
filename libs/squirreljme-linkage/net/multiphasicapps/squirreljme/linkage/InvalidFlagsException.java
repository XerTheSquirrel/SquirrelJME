// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.linkage;

/**
 * This is thrown when the flags for a class, field, or method are not valid.
 *
 * @since 2017/01/28
 */
public class InvalidFlagsException
	extends RuntimeException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2017/01/28
	 */
	public InvalidFlagsException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2017/01/28
	 */
	public InvalidFlagsException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/01/28
	 */
	public InvalidFlagsException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2017/01/28
	 */
	public InvalidFlagsException(Throwable __c)
	{
		super(__c);
	}
}

