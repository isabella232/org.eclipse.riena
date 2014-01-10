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
package org.eclipse.riena.navigation.ui.swt.views;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.MenuItemRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ToolItemRidget;
import org.eclipse.riena.navigation.ui.swt.component.IEntriesProvider;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractRidgetController;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link RienaMenuHelper}.
 */
@UITestCase
public class RienaMenuHelperTest extends TestCase {

	private Shell shell;
	private RienaMenuHelper rienaMenuHelper;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		locator.setBindingProperty(shell, ApplicationViewAdvisor.SHELL_RIDGET_PROPERTY);
		rienaMenuHelper = new RienaMenuHelper();
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the <i>private</i> method {@code getCoolBars}.
	 */
	public void testGetCoolBars() throws Exception {

		final Composite comp1 = new Composite(shell, SWT.NONE);

		List<CoolBar> coolBars = ReflectionUtils.invokeHidden(rienaMenuHelper, "getCoolBars", shell);
		assertNotNull(coolBars);
		assertTrue(coolBars.isEmpty());

		final IEntriesProvider entriesProvider = new IEntriesProvider() {
			public IContributionItem[] getTopLevelEntries() {
				return new IContributionItem[0];
			}
		};
		new MenuCoolBarComposite(comp1, SWT.NONE, entriesProvider);
		coolBars = ReflectionUtils.invokeHidden(rienaMenuHelper, "getCoolBars", shell);
		assertNotNull(coolBars);
		assertTrue(coolBars.isEmpty());

		final CoolBar coolBar = new CoolBar(comp1, SWT.NONE);
		coolBars = ReflectionUtils.invokeHidden(rienaMenuHelper, "getCoolBars", shell);
		assertNotNull(coolBars);
		assertFalse(coolBars.isEmpty());
		assertEquals(1, coolBars.size());
		assertSame(coolBar, coolBars.get(0));

	}

	/**
	 * Tests the <i>private</i> method {@code getToolBars}.
	 */
	public void testGetToolBars() {

		final CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		List<ToolBar> toolBars = ReflectionUtils.invokeHidden(rienaMenuHelper, "getToolBars", coolBar);
		assertNotNull(toolBars);
		assertTrue(toolBars.isEmpty());

		final ToolBar toolBar = new ToolBar(coolBar, SWT.NONE);
		final ToolBar toolBar2 = new ToolBar(coolBar, SWT.NONE);
		toolBars = ReflectionUtils.invokeHidden(rienaMenuHelper, "getToolBars", coolBar);
		assertNotNull(toolBars);
		assertFalse(toolBars.isEmpty());
		assertEquals(2, toolBars.size());
		assertSame(toolBar, toolBars.get(0));
		assertSame(toolBar2, toolBars.get(1));

	}

	/**
	 * Tests the <i>private</i> method {@code getAllToolItems}.
	 */
	public void testGetAllToolItems() {

		final CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		final ToolBar toolBar = new ToolBar(coolBar, SWT.NONE);
		final ToolBar toolBar2 = new ToolBar(coolBar, SWT.NONE);
		List<ToolItem> items = ReflectionUtils.invokeHidden(rienaMenuHelper, "getAllToolItems", shell);
		assertNotNull(items);
		assertTrue(items.isEmpty());

		final ToolItem item = new ToolItem(toolBar, SWT.NONE);
		final ToolItem item2 = new ToolItem(toolBar2, SWT.NONE);
		items = ReflectionUtils.invokeHidden(rienaMenuHelper, "getAllToolItems", shell);
		assertNotNull(items);
		assertFalse(items.isEmpty());
		assertEquals(2, items.size());
		assertSame(item, items.get(0));
		assertSame(item2, items.get(1));

		final IEntriesProvider entriesProvider = new IEntriesProvider() {
			@SuppressWarnings("restriction")
			public IContributionItem[] getTopLevelEntries() {
				return new IContributionItem[0];
			}
		};

	}

	/**
	 * Tests the <i>private</i> method {@code createRidget(IController, Item)}.
	 */
	public void testCreateRidgetItem() {

		final Controller controller = new Controller();
		final Menu menu = new Menu(shell);
		final MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		MyContributionItem contributionItem = new MyContributionItem();
		contributionItem.setId("4711");
		menuItem.setData(contributionItem);
		ReflectionUtils.invokeHidden(rienaMenuHelper, "createRidget", controller, menuItem);
		assertFalse(controller.getRidgets().isEmpty());
		assertEquals(1, controller.getRidgets().size());
		IRidget ridget = controller.getRidget(IActionRidget.BASE_ID_MENUACTION + "4711");
		assertNotNull(ridget);
		assertTrue(ridget instanceof MenuItemRidget);
		assertEquals(IActionRidget.BASE_ID_MENUACTION + "4711", SWTBindingPropertyLocator.getInstance().locateBindingProperty(menuItem));

		final CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		final ToolBar toolBar = new ToolBar(coolBar, SWT.NONE);
		final ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		contributionItem = new MyContributionItem();
		contributionItem.setId("0815");
		toolItem.setData(contributionItem);
		ReflectionUtils.invokeHidden(rienaMenuHelper, "createRidget", controller, toolItem);
		assertFalse(controller.getRidgets().isEmpty());
		assertEquals(2, controller.getRidgets().size());
		ridget = controller.getRidget(IActionRidget.BASE_ID_TOOLBARACTION + "0815");
		assertNotNull(ridget);
		assertTrue(ridget instanceof ToolItemRidget);
		assertEquals(IActionRidget.BASE_ID_TOOLBARACTION + "0815", SWTBindingPropertyLocator.getInstance().locateBindingProperty(toolItem));

	}

	/**
	 * Tests the <i>private</i> method {@code createRidget(IController, Item)}.
	 */
	public void testCreateRidgetMenu() {

		final Controller controller = new Controller();
		final Menu menu = new Menu(shell);
		final MenuItem item = new MenuItem(menu, SWT.NONE);
		final MyContributionItem contributionItem = new MyContributionItem();
		contributionItem.setId("4711");
		item.setData(contributionItem);
		ReflectionUtils.invokeHidden(rienaMenuHelper, "createRidget", controller, menu);
		assertFalse(controller.getRidgets().isEmpty());
		assertEquals(1, controller.getRidgets().size());
		final IRidget ridget = controller.getRidget(IActionRidget.BASE_ID_MENUACTION + "4711");
		assertNotNull(ridget);
		assertTrue(ridget instanceof MenuItemRidget);
		assertEquals(IActionRidget.BASE_ID_MENUACTION + "4711", SWTBindingPropertyLocator.getInstance().locateBindingProperty(item));

	}

	/**
	 * Tests the <i>private</i> method {@code getItemId(MenuItem)}.
	 */
	public void testGetItemId() {

		final Menu menu = new Menu(shell);
		final MenuItem item = new MenuItem(menu, SWT.NONE);

		String id = ReflectionUtils.invokeHidden(rienaMenuHelper, "getItemId", item);
		assertNotNull(id);
		assertEquals("1", id);

		id = ReflectionUtils.invokeHidden(rienaMenuHelper, "getItemId", item);
		assertNotNull(id);
		assertEquals("2", id);

		final MyContributionItem contributionItem = new MyContributionItem();
		contributionItem.setId("4711");
		item.setData(contributionItem);
		id = ReflectionUtils.invokeHidden(rienaMenuHelper, "getItemId", item);
		assertNotNull(id);
		assertEquals(IActionRidget.BASE_ID_MENUACTION + "4711", id);

		final MenuItem item2 = new MenuItem(menu, SWT.NONE);
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		locator.setBindingProperty(item2, "0815");
		id = ReflectionUtils.invokeHidden(rienaMenuHelper, "getItemId", item2);
		assertNotNull(id);
		assertEquals(IActionRidget.BASE_ID_MENUACTION + "0815", id);

	}

	/**
	 * Tests the <i>private</i> method {@code getMenuCoolBarComposites}.
	 */
	public void testGetMenuCoolBarComposites() {

		final Composite comp1 = new Composite(shell, SWT.NONE);

		List<MenuCoolBarComposite> composites = ReflectionUtils.invokeHidden(rienaMenuHelper, "getMenuCoolBarComposites", shell);
		assertNotNull(composites);
		assertTrue(composites.isEmpty());

		final IEntriesProvider entriesProvider = new IEntriesProvider() {
			public IContributionItem[] getTopLevelEntries() {
				return new IContributionItem[0];
			}
		};
		final MenuCoolBarComposite menuComposite = new MenuCoolBarComposite(comp1, SWT.NONE, entriesProvider);

		composites = ReflectionUtils.invokeHidden(rienaMenuHelper, "getMenuCoolBarComposites", shell);
		assertNotNull(composites);
		assertFalse(composites.isEmpty());
		assertEquals(1, composites.size());
		assertSame(menuComposite, composites.get(0));

	}

	private static class MyContributionItem extends ContributionItem {
	}

	private static class Controller extends AbstractRidgetController {

		@Override
		public void configureRidgets() {
		}

	}

}
