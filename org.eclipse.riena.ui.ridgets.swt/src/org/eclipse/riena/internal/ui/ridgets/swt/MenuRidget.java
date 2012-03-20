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

import org.eclipse.core.databinding.BindingException;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.riena.ui.ridgets.IMenuRidget;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * Ridget of a menu item that is a cascade menu item.
 */
public class MenuRidget extends MenuItemRidget implements IMenuRidget {

	private final List<MenuItemRidget> children;
	private final DisposeListener disposeListener;

	public MenuRidget() {
		super();
		children = new ArrayList<MenuItemRidget>();
		disposeListener = new ChildDisposeListener();
	}

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, MenuItem.class);
		if (uiControl != null && !isMenu((MenuItem) uiControl)) {
			throw new BindingException("Menu item is not a cascade menu item!"); //$NON-NLS-1$
		}
	}

	@Override
	AbstractItemProperties createProperties() {
		return new MenuProperties(this);
	}

	/**
	 * Adds the given ridget as child to this menu ridget.
	 * 
	 * @param child
	 *            ridget to added
	 */
	public void addChild(final MenuItemRidget child) {
		children.add(child);
		child.getUIControl().addDisposeListener(disposeListener);
	}

	/**
	 * Returns all children of this menu ridget.
	 * 
	 * @return children
	 */
	public List<MenuItemRidget> getChildren() {
		return children;
	}

	/**
	 * Returns the child ridget with the given id.
	 * 
	 * @param id
	 * @return child ridget or {@code null} if none was found
	 */
	private MenuItemRidget getChild(final String id) {
		final List<MenuItemRidget> menuItems = getChildren();
		for (final MenuItemRidget menuItem : menuItems) {
			if ((menuItem.getID() != null) && menuItem.getID().equals(id)) {
				return menuItem;
			}
		}
		return null;
	}

	/**
	 * Removes the ridget of the given item form the list of children.
	 * 
	 * @param item
	 *            item of ridget
	 */
	private void removeChild(final MenuItem item) {
		final String id = SWTBindingPropertyLocator.getInstance().locateBindingProperty(item);
		final MenuItemRidget child = getChild(id);
		if (child != null) {
			getChildren().remove(child);
		}
	}

	private class ChildDisposeListener implements DisposeListener {

		public void widgetDisposed(final DisposeEvent e) {
			if (e.getSource() instanceof MenuItem) {
				final MenuItem item = (MenuItem) e.getSource();
				removeChild(item);
			}
		}

	}

}
