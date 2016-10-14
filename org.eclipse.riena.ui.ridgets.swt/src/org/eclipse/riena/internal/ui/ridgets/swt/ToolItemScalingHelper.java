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

import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.internal.provisional.action.ToolBarContributionItem2;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.CommandContributionItem;

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

	public ToolItem createSeparatorForScaling(final ToolBar toolbar, final ToolItem toolItem, final int index, int width,
			final ToolbarItemContribution contribution) {
		if (needScaleBasedSpacing()) {

			if (width == -1) {
				width = calculateScalingBasedSpacing();
			}

			final RienaToolItem separator = new RienaToolItem(toolbar, SWT.SEPARATOR, index);

			separator.setWidth(width);
			final Composite composite = new Composite(toolbar, SWT.NONE);
			composite.setBackground(new Color(SwtUtilities.getDisplay(), 255, 0, 0));
			composite.setData("Separator", "Separator Composite");
			separator.setControl(composite);
			separator.setEnabled(false);
			if (contribution != null) {
				separator.setData(contribution);
			}
			toolItem.setData("Separator", separator);
			return toolItem;
		}
		return null;
	}

	public ToolItem createSeparatorForScalingForToolbar(final ToolBar toolbar, final ToolItem toolItem, final int index, final int width) {
		final ICoolBarManager coolBarManager2 = ((ApplicationWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow()).getCoolBarManager2();
		final ContributionManager toolbarManager2 = (ContributionManager) ((ToolBarContributionItem2) coolBarManager2.getItems()[0]).getToolBarManager();
		final ToolbarItemContribution contribution = new ToolbarItemContribution();

		toolItem.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(final Event event) {
				System.err.println("EVENT: " + event + " " + toolItem.getData() + " " + ((CommandContributionItem) toolItem.getData()).isVisible());
				contribution.setVisible(((CommandContributionItem) toolItem.getData()).isVisible());
			}
		});

		toolbarManager2.insert(index, contribution);

		toolItem.setData("toolContrib", contribution);

		return createSeparatorForScaling(toolbar, toolItem, index, width, contribution);
	}

	public ToolItem createSeparatorForScalingOnPosition(final ToolBar toolbar, final ToolItem toolItem, int width) {
		if (needScaleBasedSpacing()) {

			if (width == -1) {
				width = calculateScalingBasedSpacing();
			}

			final RienaToolItem separator = new RienaToolItem(toolbar, SWT.SEPARATOR);
			final Composite composite = new Composite(toolbar, SWT.NONE);
			composite.setBackground(new Color(SwtUtilities.getDisplay(), 255, 0, 0));
			composite.setData("Separator", "Separator Composite");
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
		final RienaToolItem sep = (RienaToolItem) item.getData("Separator");
		if (sep != null) {
			sep.getControl().dispose();
			sep.disposeToolItem();
		}
	}

	/**
	 * @param originalItem
	 * @return
	 */
	public boolean itemHasSeparator(final ToolItem originalItem) {
		if (originalItem.getData("Separator") != null) {
			return true;
		}
		return false;
	}

}
