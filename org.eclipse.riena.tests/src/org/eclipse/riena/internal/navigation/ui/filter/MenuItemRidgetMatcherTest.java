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
package org.eclipse.riena.internal.navigation.ui.filter;

import junit.framework.TestCase;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.MenuItemRidget;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link MenuItemRidgetMatcher}.
 */
@UITestCase
public class MenuItemRidgetMatcherTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {

		final Display display = Display.getDefault();

		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);

		shell = new Shell();

	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the method {@code matches}.
	 */
	public void testMatches() {

		MenuItemRidgetMatcher matcher = new MenuItemRidgetMatcher("4711");

		final MenuItemRidget ridget = new MenuItemRidget();
		assertFalse(matcher.matches(ridget));

		final Menu menu = new Menu(shell);
		final MenuItem item = new MenuItem(menu, SWT.None);
		ridget.setUIControl(item);
		assertFalse(matcher.matches(ridget));

		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		locator.setBindingProperty(item, "4711");
		assertFalse(matcher.matches(ridget));

		locator.setBindingProperty(item, IActionRidget.BASE_ID_MENUACTION + "4711");
		assertTrue(matcher.matches(ridget));

		matcher = new MenuItemRidgetMatcher(IActionRidget.BASE_ID_MENUACTION + "4711");
		assertTrue(matcher.matches(ridget));

		matcher = new MenuItemRidgetMatcher(IActionRidget.BASE_ID_MENUACTION + "*11");
		assertTrue(matcher.matches(ridget));

		matcher = new MenuItemRidgetMatcher("4711");
		assertTrue(matcher.matches(ridget));

	}

}
