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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.internal.provisional.action.ToolBarContributionItem2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
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
	@Deprecated
	public ToolItem createSeparatorForScaling(final ToolBar toolbar, final ToolItem toolItem, final int index, int width,
			final ToolbarItemContribution contribution) {
		if (needScaleBasedSpacing()) {

			if (width == -1) {
				width = calculateScalingBasedSpacing();
			}

			final ToolItem separator = new ToolItem(toolbar, SWT.SEPARATOR, index);

			separator.setWidth(width);
			final Composite composite = new Composite(toolbar, SWT.NONE);
			composite.setBackground(new Color(SwtUtilities.getDisplay(), 255, 0, 0));
			composite.setData("Separator", "Separator Composite"); //$NON-NLS-1$ //$NON-NLS-2$
			separator.setControl(composite);
			separator.setEnabled(false);
			if (contribution != null) {
				separator.setData(contribution);
			}
			toolItem.setData("Separator", separator); //$NON-NLS-1$
			return toolItem;
		}
		return null;
	}

	public void createSeparatorContributionsForToolBars(final List<ToolBar> toolBars) {
		int coolItemIndex = 0;
		for (final ToolBar toolBar : toolBars) {
			createContributionForToolBarSeparators(toolBar, coolItemIndex);
			coolItemIndex++;
		}
	}

	/**
	 * Create a new Contribution to the toolbarManager.
	 * 
	 * @param toolItem
	 *            the toolItem which to attach the contribution
	 * @param index
	 *            the index where to add the contribution in the toolarManager
	 */
	private void createContributionForToolBarSeparators(final ToolBar toolbar, final int coolItemIndex) {

		final ToolbarItemContribution contribution = new ToolbarItemContribution();

		final CoolBar manager = ((CoolBar) toolbar.getParent());
		final ArrayList<CoolItem> items = new ArrayList<CoolItem>();
		items.addAll(Arrays.asList(manager.getItems()));
		final ToolBarContributionItem2 contribItem = (ToolBarContributionItem2) items.get(coolItemIndex).getData();
		final ContributionManager tbManager = (ContributionManager) contribItem.getToolBarManager();

		//Insert Contribution for TBManager to avoid being kickedoff the whitelist
		final Iterator<IContributionItem> iterator = Arrays.asList(tbManager.getItems()).iterator();
		int counter = 0;
		while (iterator.hasNext()) {
			counter++;
			if (!(iterator.next() instanceof ToolbarItemContribution)) {
				tbManager.insert(counter, contribution);
				counter++;
			}
		}

		//Add the contribution to the toolbarItem to avoid being kickedoff the whitelist 
		for (int i = 0; i < toolbar.getItems().length; i++) {
			toolbar.getItem(i).setData("toolItemSeparatorContribution", contribution);
		}

	}

	/**
	 * creates a new Separator on the given position.
	 * 
	 * @param toolbar
	 *            the toolbar where we add the newly created Separator
	 * @param toolItem
	 *            the toolitem where want to attach the separator
	 * @param width
	 *            the width of the separator. If width == -1 the width of the separator will be automatically calculated.
	 * @param index
	 *            the index where the separator is placed within the toolbar.
	 * @return
	 */
	public ToolItem createSeparatorForScalingOnPosition(final ToolBar toolbar, final ToolItem toolItem, int width, final int index) {
		if (needScaleBasedSpacing()) {

			if (width == -1) {
				width = calculateScalingBasedSpacing();
			}

			final ToolItem separator = new ToolItem(toolbar, SWT.SEPARATOR, index);
			separator.setWidth(width);
			final Composite composite = new Composite(toolbar, SWT.NONE);
			composite.setBackground(new Color(SwtUtilities.getDisplay(), 255, 0, 0));
			composite.setData("Separator", "Separator Composite"); //$NON-NLS-1$ //$NON-NLS-2$
			separator.setControl(composite);
			separator.setEnabled(false);
			toolItem.setData("Separator", separator); //$NON-NLS-1$
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
		final ToolItem sep = (ToolItem) item.getData("Separator"); //$NON-NLS-1$
		if (sep != null) {
			sep.getControl().dispose();
			sep.dispose();
		}
	}

	/**
	 * @param originalItem
	 * @return
	 */
	public boolean itemHasSeparator(final ToolItem originalItem) {
		if (originalItem.getData("Separator") != null) { //$NON-NLS-1$
			return true;
		}
		return false;
	}

	/**
	 * @param originalItem
	 * @return
	 */
	public boolean toolbaritemHasSeparator(final ToolItem originalItem) {
		if (originalItem.getData("toolItemSeparatorContribution") != null) { //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
