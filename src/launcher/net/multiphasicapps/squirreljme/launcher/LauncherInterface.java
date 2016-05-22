// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launcher;

import java.io.Closeable;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KIOException;
import net.multiphasicapps.squirreljme.kernel.KIOSocket;
import net.multiphasicapps.squirreljme.ui.UIDisplay;
import net.multiphasicapps.squirreljme.ui.UIDisplayManager;

/**
 * This class is the standard launcher which is used to run list programs,
 * launch programs, and perform other various things.
 *
 * Due to the design of SquirrelJME, only a single launcher is required
 * because the heavy lifting of UI code is done by the implementation
 * specific display manager.
 *
 * @since 2016/05/20
 */
public class LauncherInterface
	implements Runnable
{
	/** The kernel to launch and control for. */
	protected final Kernel kernel;
	
	/** The process of the kernel. */
	protected final KernelProcess kernelprocess;
	
	/** The display manager to use to interact with the user. */
	protected final UIDisplayManager displaymanager;
	
	/** The primary display. */
	private volatile UIDisplay _maindisp;
	
	/**
	 * Initializes the launcher interface.
	 *
	 * @param __k The kernel to provide a launcher for.
	 * @param __dm The kernel display manager.
	 * @throws IllegalStateException If the server could not be found or did
	 * not permit a connection.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public LauncherInterface(Kernel __k, UIDisplayManager __dm)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__k == null || __dm == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		KernelProcess kernelprocess = __k.kernelProcess();
		this.kernelprocess = kernelprocess;
		this.displaymanager = __dm;
		
		// Setup new launcher thread which runs under the kernel
		kernelprocess.createThread(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public void run()
	{
		// Setup the local user interface
		setup();
		
		// Infinite loop
		for (;;)
		{
			// Sleep for a bit
			try
			{
				Thread.sleep(100L);
			}
			
			// Yield to let other threads besides the launcher run/
			catch (InterruptedException e)
			{
				Thread.yield();
			}
		}
	}
	
	/**
	 * Sets up the local launcher user interface and initailizes basic
	 * widgets to provide an interface to the user.
	 *
	 * @since 2016/05/21
	 */
	public void setup()
	{
		// Create new display to be shown to the user
		UIDisplay maindisp = displaymanager.createDisplay();
		this._maindisp = maindisp;
		
		// Done, make it visible
		maindisp.setVisible(true);
	}
}

