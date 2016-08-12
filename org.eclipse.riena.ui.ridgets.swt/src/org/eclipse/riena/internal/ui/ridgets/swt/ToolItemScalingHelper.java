/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * This class has some utility functions to support scaling the menu items in sync with Windows scaling factors
 */
public class ToolItemScalingHelper {

	/**
	 * Checks if windows scaling is greater than 100%. If scaling is greather than 100% scaling is needed.
	 * 
	 * @return true if spacing is needed. Otherwise false.
	 */
	public Boolean needScaleBasedSpacing() {
		final float[] dpi = SwtUtilities.getDpiFactors();
		final boolean needSpacing = (dpi[0] > 1.0) ? true : false;
		return needSpacing;
	}

	/**
	 * Calculates the needed spacing between two menu items depending on the windows scaling
	 */
	public int calculateScalingBasedSpacing() {
		final float[] dpiFactors = SwtUtilities.getDpiFactors();
		final int separatorSpacing = (int) (dpiFactors[0] * LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.MENUBAR_SPACING, 4));
		return separatorSpacing;
	}

	/**
	 * Adds a seperator to allow for smooth scaling of the spacing between menu items when changing the windows scaling. seperators are only created for scaling
	 * greater than 100%
	 * 
	 * @param toolbar
	 *            toolbar into which to add the menu item
	 * @param toolItem
	 *            item to which the separator must be appended if scaling is greater than 100%
	 * @param index
	 *            index at which the separator is added
	 * @param width
	 *            width if fixed, -1 to calculate use scaling
	 * @return a separator if scaling is needed. Returns null if scaling is not needed.
	 */
	public ToolItem createSeparatorForScaling(final ToolBar toolbar, final ToolItem toolItem, final int index, int width) {
		if (needScaleBasedSpacing()) {

			if (width == -1) {
				width = calculateScalingBasedSpacing();
			}
			final ToolItem separator = new ToolItem(toolbar, SWT.SEPARATOR, index);

			separator.setWidth(width);
			final Composite composite = new Composite(toolbar, SWT.NONE);
			separator.setControl(composite);
			separator.setEnabled(false);
			toolItem.setData("Separator", separator);
			return toolItem;
		}
		return null;
	}

	/**
	 * Remove the separator created for scaling if menu item is disposed
	 * 
	 * @param item
	 * @since 6.2
	 */
	public void disposeSeparatorForMenuItem(final ToolItem item) {
		final ToolItem sep = (ToolItem) item.getData("Separator");
		if (sep != null) {
			sep.getControl().dispose();
			sep.dispose();
		}
	}

}
