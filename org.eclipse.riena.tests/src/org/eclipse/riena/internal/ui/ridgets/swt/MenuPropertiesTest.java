/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link MenuProperties}.
 */
@UITestCase
public class MenuPropertiesTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the constructor of {@code MenuProperties}
	 */
	public void testMenuProperties() {

		final Menu menu = new Menu(shell);
		final MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		final Menu menu2 = new Menu(shell, SWT.DROP_DOWN);
		menuItem.setMenu(menu2);
		final MenuRidget ridget = new MenuRidget();
		ridget.setUIControl(menuItem);

		MenuProperties prop = new MenuProperties(ridget);
		final Decorations menuParent = ReflectionUtils.getHidden(prop, "menuParent");
		assertSame(shell, menuParent);
		final int menuStyle = ReflectionUtils.getHidden(prop, "menuStyle");
		assertEquals(menu2.getStyle(), menuStyle);
		MenuItemProperties[] children = ReflectionUtils.getHidden(prop, "children");
		assertEquals(0, children.length);

		final MenuItem menuItem2 = new MenuItem(menu2, SWT.NONE);
		final MenuItemRidget childRidget = new MenuItemRidget();
		childRidget.setUIControl(menuItem2);
		ridget.addChild(childRidget);
		prop = new MenuProperties(ridget);
		children = ReflectionUtils.getHidden(prop, "children");
		assertEquals(1, children.length);
		final IRidget retRidget = ReflectionUtils.invokeHidden(children[0], "getRidget");
		assertSame(childRidget, retRidget);

	}

	/**
	 * Tests the method {@code createItem()}
	 */
	public void testCreateItem() {

		final Menu menu = new Menu(shell);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(menu, "menu");
		final MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(menuItem, "menuItem");
		final Menu menu2 = new Menu(shell, SWT.DROP_DOWN);
		menuItem.setMenu(menu2);
		final MenuRidget ridget = new MenuRidget();
		ridget.setUIControl(menuItem);
		final MenuItem menuItem2 = new MenuItem(menu2, SWT.NONE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(menuItem2, "menuItem2");
		menuItem2.setText("Simple menu item");
		final MenuItemRidget childRidget = new MenuItemRidget();
		childRidget.setUIControl(menuItem2);
		ridget.addChild(childRidget);
		final MyMenuProperties prop = new MyMenuProperties(ridget);

		final MenuItem retMenuItem = prop.createItem();
		assertNotNull(retMenuItem);
		assertNotSame(menuItem, retMenuItem);
		assertNotNull(retMenuItem.getMenu());
		assertNotSame(menu2, retMenuItem.getMenu());
		assertEquals(1, retMenuItem.getMenu().getItemCount());
		assertEquals("menuItem", SWTBindingPropertyLocator.getInstance().locateBindingProperty(retMenuItem));
		final MenuItem retChildMenuItem = retMenuItem.getMenu().getItems()[0];
		assertEquals(menuItem2.getText(), retChildMenuItem.getText());

	}

	/**
	 * This class changes the visibility of some protected methods to public. So
	 * the methods can be tested better.
	 */
	private static class MyMenuProperties extends MenuProperties {

		public MyMenuProperties(final MenuRidget ridget) {
			super(ridget);
		}

		@Override
		public MenuItem createItem() {
			return super.createItem();
		}

	}

}
