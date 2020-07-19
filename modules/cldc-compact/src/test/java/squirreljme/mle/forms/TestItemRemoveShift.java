// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.forms;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tests that removing items shifts them accordingly.
 *
 * @since 2020/07/19
 */
public class TestItemRemoveShift
	extends __BaseFormTest__
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	protected void uiTest(UIDisplayBracket __display, UIFormBracket __form)
		throws Throwable
	{
		// Setup all the items and add to the form beforehand
		List<UIItemBracket> items = new ArrayList<>();
		for (int i = 0; i < UIItemType.NUM_TYPES; i++)
		{
			UIItemBracket item = UIFormShelf.itemNew(i);
			
			items.add(item);
			UIFormShelf.formItemPosition(__form, item, i);
		}
		
		// We will be removing random elements!
		Random random = new Random(12);
		int removalCount = 0;
		while (!items.isEmpty())
		{
			int count = items.size();
			
			// Determine index to be removed
			int dx = random.nextInt(count);
			
			// Remove that item
			UIItemBracket old = UIFormShelf.formItemRemove(__form, dx);
			
			// Should both be the same items from the list
			this.secondary("same-" + removalCount,
				UIFormShelf.equals(old, items.remove(dx)));
			
			// Count should be reduced by one
			int subCount = count - 1;
			this.secondary("subcount-" + removalCount,
				subCount == UIFormShelf.formItemCount(__form));
			
			// All of these should be the same item in the list
			boolean[] matches = new boolean[subCount];
			for (int j = 0; j < subCount; j++)
				matches[j] = UIFormShelf.equals(items.get(j),
					UIFormShelf.formItemAtPosition(__form, j));
			this.secondary("sameitems-" + removalCount, matches);
			
			// For the next run (in testing)
			removalCount++;
		}
		
		// Form should be empty
		this.secondary("empty", UIFormShelf.formItemCount(__form));
	}
}
