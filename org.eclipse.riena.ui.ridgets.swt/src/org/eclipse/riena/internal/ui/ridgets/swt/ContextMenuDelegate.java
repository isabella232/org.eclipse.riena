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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.riena.ui.ridgets.IMenuItemRidget;

/**
 * Every ridget has its own delegate. A SWT ridget object delegates all of its instance methods for modifying its context menu to its delegate.
 */
public class ContextMenuDelegate {

	private final List<IMenuItemRidget> menuItems;

	public ContextMenuDelegate() {
		menuItems = new LinkedList<IMenuItemRidget>();
	}

	public List<IMenuItemRidget> getMenuItems() {
		return menuItems;
	}

	/**
	 * Adds a menu item to the ridget.
	 * 
	 * @param menuItemText
	 *            The text of the menu item to be added.
	 * @param iconName
	 *            The name of the icon for the menu item to be added.
	 * @return the menu item rigdet.
	 * 
	 */
	public IMenuItemRidget addMenuItem(final String menuText, final String iconName) {
		final IMenuItemRidget mi = getMenuRidget(menuText);
		return addMenuItem(mi, menuText, iconName);
	}

	private IMenuItemRidget addMenuItem(final IMenuItemRidget menuItemRidget, final String menuText, final String iconName) {
		menuItemRidget.setText(menuText);
		if (iconName != null) {
			menuItemRidget.setIcon(iconName);
		}
		if (isNotInList(menuText)) {
			menuItems.add(menuItemRidget);
		}
		return menuItemRidget;
	}

	/**
	 * Removes a specific menu item from the contextmenu of the ridget.
	 * 
	 * @param menuItemText
	 *            The text of the menu item to be removed.
	 */
	public void removeMenuItem(final String menuItemText) {
		removeMenuItem(getMenuRidget(menuItemText));
	}

	/**
	 * Removes a specific menu item from the contextmenu of the ridget.
	 * 
	 * @param menuItemRidget
	 *            The menu item to be removed.
	 */
	public void removeMenuItem(final IMenuItemRidget menuItemRidget) {
		if (!menuItems.isEmpty() && menuItemRidget != null) {
			if (menuItemRidget.getUIControl() != null) {
				((MenuItem) menuItemRidget.getUIControl()).dispose();
			}
			menuItems.remove(menuItemRidget);
		}
	}

	private boolean isNotInList(final String id) {
		for (final IMenuItemRidget menuItemRidget : menuItems) {
			if (menuItemRidget.getText().equals(id)) {
				return false;
			}
		}
		return true;

	}

	private IMenuItemRidget getMenuRidget(final String id) {
		for (final IMenuItemRidget menuItemRidget : menuItems) {
			if (menuItemRidget.getText().equals(id)) {
				return menuItemRidget;
			}
		}
		IMenuItemRidget ridget = null;
		try {
			ridget = MenuItemRidget.class.newInstance();
		} catch (final InstantiationException e) {
			throw new RuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		return ridget;
	}

	/**
	 * Returns a menu item was added before.
	 * 
	 * @return Menu item of the index.
	 */
	public IMenuItemRidget getMenuItem(final int index) {
		if (menuItems == null || menuItems.isEmpty()) {
			throw new IllegalStateException("No menu items in context menu."); //$NON-NLS-1$
		}
		if (index >= menuItems.size() || index < 0) {
			throw new IllegalArgumentException("No menu item at index " + index + " found"); //$NON-NLS-1$//$NON-NLS-2$
		}
		return menuItems.get(index);
	}

	/**
	 * Returns count of menu items.
	 * 
	 * @return Count of the menu items.
	 */
	public int getMenuItemCount() {
		return menuItems.size();
	}

}