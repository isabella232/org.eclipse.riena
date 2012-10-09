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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.riena.ui.ridgets.IMenuItemRidget;

public class ContextMenuDelegate {

	private final List<IMenuItemRidget> menuItems;

	public ContextMenuDelegate() {
		menuItems = new LinkedList<IMenuItemRidget>();
	}

	public List<IMenuItemRidget> getMenuItems() {
		return menuItems;
	}

	public IMenuItemRidget addMenuItem(final String menuText) {
		return addMenuItem(menuText, null);
	}

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

	public void removeMenuItem(final String menuItemText) {
		removeMenuItem(getMenuRidget(menuItemText));
	}

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

	public IMenuItemRidget getMenuItem(final int index) {
		return menuItems.get(index);
	}

	public int getMenuItemCount() {
		return menuItems.size();
	}

}