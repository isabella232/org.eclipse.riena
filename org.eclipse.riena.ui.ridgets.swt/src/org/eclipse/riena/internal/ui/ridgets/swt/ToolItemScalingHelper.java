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
	 * Creates SeparatorContributions for every toolbar in the given list.
	 * 
	 * @param toolBars
	 */
	public void createSeparatorContributionsForToolBars(final List<ToolBar> toolBars) {
		int coolItemIndex = 0;
		boolean needFirstSeparator = false;
		for (final ToolBar toolBar : toolBars) {
			//the first toolbar doesnt need an Separator at the beginning
			if (coolItemIndex > 0) {
				needFirstSeparator = true;
			}
			createContributionForToolBarSeparators(toolBar, coolItemIndex, needFirstSeparator);
			coolItemIndex++;
		}
	}

	/**
	 * Create a new Contributions and add them to the toolbarManager.
	 * 
	 * @param toolItem
	 *            the toolItem which to attach the contribution
	 * @param index
	 *            the index where to add the contribution in the toolarManager
	 */
	private void createContributionForToolBarSeparators(final ToolBar toolbar, final int coolItemIndex, final boolean firstSeparatorNeeded) {
		final ContributionManager toolbarManager = getContributionManagerFromToolBar(toolbar, coolItemIndex);
		if (toolbarManager != null) {
			int indexCounter = 0;

			//Create and insert an ToolItemContribution at the beginning of the toolbar which is no Separator to avoid being kept of the cleanlist 
			if (firstSeparatorNeeded && !(toolbarManager.getItems()[0] instanceof ToolbarItemContribution)) {
				final ToolbarItemContribution FirstSeparatorContribution = new ToolbarItemContribution();
				FirstSeparatorContribution.setIsSeparator(false);
				toolbarManager.insert(indexCounter, FirstSeparatorContribution);
			}

			//Insert Contribution for TBManager to avoid being kicked off the cleanlist
			final Iterator<IContributionItem> iterator = Arrays.asList(toolbarManager.getItems()).iterator();
			while (iterator.hasNext()) {
				indexCounter++;
				if (!(iterator.next() instanceof ToolbarItemContribution)) {
					toolbarManager.insert(indexCounter, new ToolbarItemContribution());
					indexCounter++;
				}
			}

			//Add the contribution to the toolbarItem to avoid being kicked off the cleanlist 
			for (int i = 0; i < toolbar.getItems().length; i++) {
				toolbar.getItem(i).setData("toolItemSeparatorContribution", new ToolbarItemContribution()); //$NON-NLS-1$
			}
		}

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

			final ToolItem separator = new ToolItem(toolbar, SWT.SEPARATOR, index);

			separator.setWidth(width);
			final Composite composite = new Composite(toolbar, SWT.NONE);
			//			composite.setBackground(new Color(SwtUtilities.getDisplay(), 255, 0, 0));
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
			//			composite.setBackground(new Color(SwtUtilities.getDisplay(), 255, 0, 0));
			composite.setData("Separator", "Separator Composite"); //$NON-NLS-1$ //$NON-NLS-2$
			separator.setControl(composite);
			separator.setEnabled(false);
			toolItem.setData("Separator", separator); //$NON-NLS-1$
			return toolItem;
		}
		return null;
	}

	private ContributionManager getContributionManagerFromToolBar(final ToolBar toolbar, final int coolItemIndex) {
		final CoolBar manager = ((CoolBar) toolbar.getParent());
		final ArrayList<CoolItem> coolItems = new ArrayList<CoolItem>();
		coolItems.addAll(Arrays.asList(manager.getItems()));
		if (toolbar.getItemCount() != 0 && coolItems.size() > coolItemIndex) {
			final ToolBarContributionItem2 contributionItem = (ToolBarContributionItem2) coolItems.get(coolItemIndex).getData();
			final ContributionManager toolbarManager = (ContributionManager) contributionItem.getToolBarManager();
			return toolbarManager;
		}
		return null;
	}

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
	 * 
	 * @returns the spacing
	 */
	public int calculateScalingBasedSpacing() {
		final float[] dpiFactors = SwtUtilities.getDpiFactors();
		final int separatorSpacing = (int) (dpiFactors[0] * LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.MENUBAR_SPACING, 4));
		return separatorSpacing;
	}

	/**
	 * Calculates the needed spacing between two toolbars depending on the windows scaling
	 * 
	 * @return the spacing
	 */
	public int calculateSclaingBasedSpacingBetweenToolBars() {
		final float[] dpiFactors = SwtUtilities.getDpiFactors();
		final int separatorSpacing = (int) (dpiFactors[0] * LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.TOOLBAR_SPACING, 8));
		return separatorSpacing;
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
