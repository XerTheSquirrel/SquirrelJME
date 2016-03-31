// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter.program;

import net.multiphasicapps.interpreter.JVMVariableType;

/**
 * This represents the state of a single variable, it may be explicitely
 * generated or implicitely generated.
 *
 * In virtually every case (except for the first instruction) the state of
 * operations are always implicit.
 *
 * @since 2016/03/30
 */
public class VMCVariableState
{
	/** Lock. */
	final Object lock;
	
	/** Owning states. */
	protected final VMCVariableStates states;
	
	/** The index of this state. */
	protected final int index;
	
	/** Implicit state set? */
	private volatile JVMVariableType _implicit;
	
	/**
	 * Initializes the variable state.
	 *
	 * @param __s The owning states.
	 * @param __dx The variable index.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/31
	 */
	VMCVariableState(VMCVariableStates __s, int __dx)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set
		states = __s;
		lock = states.lock;
		index = __dx;
	}
	
	/**
	 * Sets the type of the variable stored here.
	 *
	 * @param __vt The type of variable stored here.
	 * @return {@code this}.
	 * @throws IllegalStateException If this is not the entry state.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/31
	 */
	VMCVariableState __setType(JVMVariableType __vt)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__vt == null)
			throw new NullPointerException("NARG");
		
		// Can only change the entry state
		if (!states._isentrystate)
			throw new IllegalStateException("IN2g");
		
		// Lock
		synchronized (lock)
		{
			_implicit = __vt;
		}
		
		// Self
		return this;
	}
}

