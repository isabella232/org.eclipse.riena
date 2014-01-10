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

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link MenuItemMarkerSupport}.
 */
@UITestCase
public class MenuItemMarkerSupportTest extends TestCase {

	/**
	 * Tests the method {@code updateMarkers()}.
	 */
	public void testUpdateMarkers() {

		final Shell shell = new Shell();
		final Menu menu = new Menu(shell, SWT.NONE);
		final MenuItem item = new MenuItem(menu, SWT.NONE);
		final MenuItemRidget ridget = new MenuItemRidget();

		final MenuItemMarkerSupport markerSupport = new MenuItemMarkerSupport(ridget, null);
		ridget.setEnabled(false);
		assertTrue(item.isEnabled());

		ridget.setUIControl(item);
		markerSupport.updateMarkers();
		assertFalse(item.isEnabled());

		SwtUtilities.dispose(shell);

	}

	/**
	 * Tests the <i>private</i> method {@code updateVisible}
	 */
	public void testUpdateVisible() {

		final Shell shell = new Shell();
		final Menu menu = new Menu(shell, SWT.NONE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(menu, "menu");
		final MenuItem item = new MenuItem(menu, SWT.CASCADE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(item, "item");
		final Menu menu2 = new Menu(shell, SWT.DROP_DOWN);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(menu2, "menu2");
		item.setMenu(menu2);
		final MenuRidget ridget = new MenuRidget();
		ridget.setUIControl(item);

		final MenuItemMarkerSupport markerSupport = new MenuItemMarkerSupport(ridget, null);
		ridget.setVisible(false);

		ReflectionUtils.invokeHidden(markerSupport, "updateVisible", item);
		assertTrue(item.isDisposed());
		assertTrue(menu2.isDisposed());

		ridget.setVisible(true);
		ReflectionUtils.invokeHidden(markerSupport, "updateVisible", item);

		SwtUtilities.dispose(shell);

	}

}
