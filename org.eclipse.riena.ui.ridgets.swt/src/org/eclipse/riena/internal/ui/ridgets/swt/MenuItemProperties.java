/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
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
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * This class stores the properties of a menu item ridget.
 */
public class MenuItemProperties extends AbstractItemProperties {

	private final Menu parent;
	private List<String> prevSiblingIds;

	/**
	 * Creates a new instance of {@code MenuItemProperties}. The properties of
	 * the given ridget are stored.
	 * 
	 * @param ridget
	 *            menu ridget
	 */
	public MenuItemProperties(final MenuItemRidget ridget) {

		super(ridget);

		final MenuItem item = ridget.getUIControl();
		parent = item.getParent();
		storePreviousSiblings(item);
	}

	/**
	 * Stores all the previous siblings of the given menu item.
	 * 
	 * @param item
	 *            item of tool bar
	 */
	private void storePreviousSiblings(final MenuItem item) {
		final int index = parent.indexOf(item);
		final Item[] siblings = parent.getItems();
		prevSiblingIds = new ArrayList<String>();
		for (int i = 0; i < index; i++) {
			prevSiblingIds.add(SWTBindingPropertyLocator.getInstance().locateBindingProperty(siblings[i]));
		}
	}

	/**
	 * Returns the index of this tool item to insert it at the correct position
	 * in the menu.
	 * 
	 * @return index
	 */
	protected int getIndex() {

		int index = 0;

		if (SwtUtilities.isDisposed(parent)) {
			return index;
		}
		final Item[] siblings = parent.getItems();
		for (final Item sibling : siblings) {
			final String id = SWTBindingPropertyLocator.getInstance().locateBindingProperty(sibling);
			if (prevSiblingIds.contains(id)) {
				index++;
			}
		}

		return index;

	}

	@Override
	protected MenuItem createItem() {
		return createItem(getParent());
	}

	@Override
	protected MenuItemRidget getRidget() {
		return (MenuItemRidget) super.getRidget();
	}

	/**
	 * Creates a new menu item base on the stored properties.
	 * 
	 * @param parent
	 *            parent menu
	 * @return menu item
	 */
	protected MenuItem createItem(final Menu parent) {
		final IContributionItem contributionItem = getContributionItem();
		MenuItem menuItem;
		if (contributionItem != null) {
			final int index = getIndex();
			contributionItem.fill(parent, index);
			menuItem = parent.getItem(index);
			setAllProperties(menuItem, false);
			contributionItem.update();
		} else {
			menuItem = new MenuItem(parent, getStyle(), getIndex());
			setAllProperties(menuItem, true);
		}
		getRidget().setUIControl(menuItem);
		return menuItem;
	}

	protected Menu getParent() {
		return parent;
	}

}
