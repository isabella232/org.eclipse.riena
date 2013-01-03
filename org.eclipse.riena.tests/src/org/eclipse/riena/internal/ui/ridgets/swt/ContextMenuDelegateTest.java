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

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.IMenuItemRidget;

/**
 *
 */
@NonUITestCase
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

		contextMenuDelegate.addMenuItem(menuItemWithoutIconText, null);
		assertEquals(1, contextMenuDelegate.getMenuItemCount());

		contextMenuDelegate.addMenuItem(menuItemWithIconText, iconName);
		assertEquals(2, contextMenuDelegate.getMenuItemCount());
	}

	public void testGetMenuItem() {
		final String menuItemWithoutIconText = "MenuItemWithoutIcon"; //$NON-NLS-1$
		final String menuItemWithIconText = "MenuItemWithIcon"; //$NON-NLS-1$
		final String iconName = "leftArrow"; //$NON-NLS-1$

		final IMenuItemRidget menuItemWithoutIcon = contextMenuDelegate.addMenuItem(menuItemWithoutIconText, null);
		assertEquals(menuItemWithoutIcon, contextMenuDelegate.getMenuItem(0));

		final IMenuItemRidget menuItemWithIcon = contextMenuDelegate.addMenuItem(menuItemWithIconText, iconName);
		assertEquals(menuItemWithIcon, contextMenuDelegate.getMenuItem(1));
	}

	public void testGetMenuItems() {
		final String menuItemWithoutIconText = "MenuItemWithoutIcon"; //$NON-NLS-1$
		final String menuItemWithIconText = "MenuItemWithIcon"; //$NON-NLS-1$
		final String iconName = "leftArrow"; //$NON-NLS-1$

		final IMenuItemRidget menuItemWithoutIcon = contextMenuDelegate.addMenuItem(menuItemWithoutIconText, null);
		assertEquals(menuItemWithoutIcon, contextMenuDelegate.getMenuItem(0));

		final IMenuItemRidget menuItemWithIcon = contextMenuDelegate.addMenuItem(menuItemWithIconText, iconName);
		assertEquals(menuItemWithIcon, contextMenuDelegate.getMenuItem(1));

		final List<IMenuItemRidget> items = contextMenuDelegate.getMenuItems();
		assertEquals(items.size(), 2);
		for (int i = 0; i < items.size(); i++) {
			if (i == 0) {
				items.get(i).equals(menuItemWithoutIcon);
			} else if (i == 1) {
				items.get(i).equals(menuItemWithIcon);
			}
		}
	}

	public void testAddMenuItem() {
		final String menuItemWithoutIconText = "MenuItemWithoutIcon"; //$NON-NLS-1$
		final String menuItemWithIconText = "MenuItemWithIcon"; //$NON-NLS-1$
		final String iconName = "leftArrow"; //$NON-NLS-1$

		final IMenuItemRidget menuItemWithoutIcon = contextMenuDelegate.addMenuItem(menuItemWithoutIconText, null);
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

		final IMenuItemRidget menuItemWithoutIcon = contextMenuDelegate.addMenuItem(menuItemWithoutIconText, null);
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
