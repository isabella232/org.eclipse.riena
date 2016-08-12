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
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.ui.ridgets.swt.MenuManagerHelper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * This class stores the properties of a tool item.
 */
public class ToolItemProperties extends AbstractItemProperties {

	private final ToolBar parent;
	private List<String> prevSiblingIds;
	private final Image hotImage;
	private final Image disabledImage;
	private int separatorWidth = -1;
	private final ToolItemScalingHelper menuHelper;

	/**
	 * @param item
	 */
	public ToolItemProperties(final ToolItemRidget ridget) {

		super(ridget);

		final ToolItem item = ridget.getUIControl();
		parent = item.getParent();
		hotImage = item.getHotImage();
		disabledImage = item.getDisabledImage();
		storePreviousSiblings(item);

		final ToolItem sep = (ToolItem) item.getData("Separator");
		if (sep != null) {
			separatorWidth = sep.getWidth();
		}
		menuHelper = new ToolItemScalingHelper();

	}

	/**
	 * Stores all the previous siblings of the given tool item.
	 * 
	 * @param item
	 *            item of tool bar
	 */
	private void storePreviousSiblings(final ToolItem item) {
		final int index = parent.indexOf(item);
		final Item[] siblings = parent.getItems();
		prevSiblingIds = new ArrayList<String>();
		for (int i = 0; i < index; i++) {
			prevSiblingIds.add(SWTBindingPropertyLocator.getInstance().locateBindingProperty(siblings[i]));
		}
	}

	/**
	 * Returns the index of this tool item to insert it at the correct position in the tool bar.
	 * 
	 * @return index
	 */
	private int getIndex() {

		int index = 0;

		final Item[] siblings = parent.getItems();
		for (final Item sibling : siblings) {
			final String id = SWTBindingPropertyLocator.getInstance().locateBindingProperty(sibling);
			if (prevSiblingIds.contains(id)) {
				index++;
			} else {
				break;
			}
		}

		return index;

	}

	@Override
	protected ToolItemRidget getRidget() {
		return (ToolItemRidget) super.getRidget();
	}

	@Override
	protected ToolItem createItem() {

		final IContributionItem contributionItem = getContributionItem();
		final MenuManager menuManager = getMenuManager();
		ToolItem toolItem;
		if ((contributionItem != null) && (menuManager == null)) {
			contributionItem.fill(parent, getIndex());
			toolItem = parent.getItem(getIndex());
			toolItem.setEnabled(true);
			setAllProperties(toolItem, false);
			contributionItem.update();
		} else {
			toolItem = new ToolItem(parent, getStyle(), getIndex());
			setAllProperties(toolItem, true);
			if (menuManager != null) {
				toolItem.setData(menuManager);
				final MenuManagerHelper helper = new MenuManagerHelper();
				helper.addListeners(toolItem, menuManager.getMenu());
			}
			if (separatorWidth > 0) {
				menuHelper.createSeparatorForScaling(parent, toolItem, getIndex() + 1, separatorWidth);
			}
		}
		getRidget().setUIControl(toolItem);
		return toolItem;

	}

	@Override
	protected void setAllProperties(final Item item, final boolean addListeners) {
		super.setAllProperties(item, addListeners);
		final ToolItem toolItem = (ToolItem) item;
		if (hotImage == null || !hotImage.isDisposed()) {
			toolItem.setHotImage(hotImage);
		}
		if (disabledImage == null || !disabledImage.isDisposed()) {
			toolItem.setDisabledImage(disabledImage);
		}

	}

	private MenuManager getMenuManager() {

		final IContributionItem contributionItem = getContributionItem();
		if (contributionItem instanceof MenuManager) {
			return (MenuManager) contributionItem;
		}

		return null;

	}

}
