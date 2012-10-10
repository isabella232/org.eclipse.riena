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

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.riena.ui.ridgets.IMenuItemRidget;

/**
 *
 */
public class ContextMenuDelegateTest extends TestCase {

	private ContextMenuDelegate contextMenuDelegate;

	@Override
	protected void setUp() throws Exception {
		contextMenuDelegate = new ContextMenuDelegate();
	}

	@Override
	protected void tearDown() throws Exception {
		contextMenuDelegate = null;
		super.tearDown();
	}

	public void testGetMenuItemCount() {
		final String menuItemWithoutIconText = "MenuItemWithoutIcon"; //$NON-NLS-1$
		final String menuItemWithIconText = "MenuItemWithIcon"; //$NON-NLS-1$
		final String iconName = "leftArrow"; //$NON-NLS-1$

		assertEquals(0, contextMenuDelegate.getMenuItemCount());

		contextMenuDelegate.addMenuItem(menuItemWithoutIconText);
		assertEquals(1, contextMenuDelegate.getMenuItemCount());

		contextMenuDelegate.addMenuItem(menuItemWithIconText, iconName);
		assertEquals(2, contextMenuDelegate.getMenuItemCount());
	}

	public void testGetMenuItem() {
		final String menuItemWithoutIconText = "MenuItemWithoutIcon"; //$NON-NLS-1$
		final String menuItemWithIconText = "MenuItemWithIcon"; //$NON-NLS-1$
		final String iconName = "leftArrow"; //$NON-NLS-1$

		final IMenuItemRidget menuItemWithoutIcon = contextMenuDelegate.addMenuItem(menuItemWithoutIconText);
		assertEquals(menuItemWithoutIcon, contextMenuDelegate.getMenuItem(0));

		final IMenuItemRidget menuItemWithIcon = contextMenuDelegate.addMenuItem(menuItemWithIconText, iconName);
		assertEquals(menuItemWithIcon, contextMenuDelegate.getMenuItem(1));
	}

	public void testGetMenuItems() {
		final String menuItemWithoutIconText = "MenuItemWithoutIcon"; //$NON-NLS-1$
		final String menuItemWithIconText = "MenuItemWithIcon"; //$NON-NLS-1$
		final String iconName = "leftArrow"; //$NON-NLS-1$

		final IMenuItemRidget menuItemWithoutIcon = contextMenuDelegate.addMenuItem(menuItemWithoutIconText);
		assertEquals(menuItemWithoutIcon, contextMenuDelegate.getMenuItem(0));

		final IMenuItemRidget menuItemWithIcon = contextMenuDelegate.addMenuItem(menuItemWithIconText, iconName);
		assertEquals(menuItemWithIcon, contextMenuDelegate.getMenuItem(1));

		final List<IMenuItemRidget> items = contextMenuDelegate.getMenuItems();
		assertEquals(items.size(), 2);
		for (int i = 0; i < items.size(); i++) {
			final IMenuItemRidget item = items.get(i);
			switch (i) {
			case 0:
				assertEquals(item, menuItemWithoutIcon);
				break;
			case 1:
				assertEquals(item, menuItemWithIcon);
				break;
			default:
				fail("unexpected item count"); //$NON-NLS-1$
				break;
			}
		}
	}

	public void testAddMenuItem() {
		final String menuItemWithoutIconText = "MenuItemWithoutIcon"; //$NON-NLS-1$
		final String menuItemWithIconText = "MenuItemWithIcon"; //$NON-NLS-1$
		final String iconName = "leftArrow"; //$NON-NLS-1$

		final IMenuItemRidget menuItemWithoutIcon = contextMenuDelegate.addMenuItem(menuItemWithoutIconText);
		assertEquals(1, contextMenuDelegate.getMenuItemCount());
		assertEquals(menuItemWithoutIcon, contextMenuDelegate.getMenuItem(0));

		final IMenuItemRidget menuItemWithIcon = contextMenuDelegate.addMenuItem(menuItemWithIconText, iconName);
		assertEquals(2, contextMenuDelegate.getMenuItemCount());
		assertEquals(menuItemWithIcon, contextMenuDelegate.getMenuItem(1));

	}

	public void testRemoveMenuItem() {
		final String menuItemWithoutIconText = "MenuItemWithoutIcon"; //$NON-NLS-1$
		final String menuItemWithIconText = "MenuItemWithIcon"; //$NON-NLS-1$
		final String iconName = "leftArrow"; //$NON-NLS-1$

		final IMenuItemRidget menuItemWithoutIcon = contextMenuDelegate.addMenuItem(menuItemWithoutIconText);
		IMenuItemRidget menuItemWithIcon = contextMenuDelegate.addMenuItem(menuItemWithIconText, iconName);

		assertEquals(2, contextMenuDelegate.getMenuItemCount());
		contextMenuDelegate.removeMenuItem(menuItemWithIconText);
		assertEquals(1, contextMenuDelegate.getMenuItemCount());

		menuItemWithIcon = contextMenuDelegate.addMenuItem(menuItemWithIconText, iconName);
		assertEquals(2, contextMenuDelegate.getMenuItemCount());

		contextMenuDelegate.removeMenuItem(menuItemWithoutIcon);
		assertEquals(1, contextMenuDelegate.getMenuItemCount());
		contextMenuDelegate.removeMenuItem(menuItemWithIcon);
		assertEquals(0, contextMenuDelegate.getMenuItemCount());
	}
}
