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
package org.eclipse.riena.navigation.ui.swt.component;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TypedListener;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link MenuCoolBarComposite}.
 */
@UITestCase
public class MenuCoolBarCompositeTest extends TestCase {

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
	 * Tests the constructor of {@code MenuCoolBarComposite} and also the <i>private</i> method {@code create()}.
	 */
	public void testMenuCoolBarComposite() {

		final IEntriesProvider entriesProvider = new IEntriesProvider() {
			public IContributionItem[] getTopLevelEntries() {
				return new IContributionItem[0];
			}
		};
		final MenuCoolBarComposite composite = new MenuCoolBarComposite(shell, SWT.NONE, entriesProvider);
		final ToolBar toolBar = ReflectionUtils.getHidden(composite, "menuToolBar");
		checkListenerCountAndType(toolBar, SWT.MouseDown);
		checkListenerCountAndType(toolBar, SWT.MouseEnter);

		final CoolBar coolBar = (CoolBar) toolBar.getParent();
		assertEquals(1, coolBar.getItemCount());
		final CoolItem item = coolBar.getItem(0);
		assertSame(toolBar, item.getControl());

	}

	/**
	 * @param toolBar
	 * @param eventType
	 */
	private void checkListenerCountAndType(final ToolBar toolBar, final int eventType) {
		final Listener[] listeners = toolBar.getListeners(eventType);
		assertEquals(1, listeners.length);
		if (listeners[0] instanceof TypedListener) {
			final TypedListener l = (TypedListener) listeners[0];
			final SWTEventListener eventListener = l.getEventListener();
			assertTrue(eventListener instanceof ToolBarMenuListener);
		} else {
			assertTrue(listeners[0] instanceof ToolBarMenuListener);
		}
	}

	/**
	 * Tests the method {@code createAndAddMenu}.
	 */
	public void testCreateAndAddMenu() {

		final IEntriesProvider entriesProvider = new IEntriesProvider() {
			public IContributionItem[] getTopLevelEntries() {
				return new IContributionItem[0];
			}
		};
		final MenuCoolBarComposite composite = new MenuCoolBarComposite(shell, SWT.NONE, entriesProvider);
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();

		final MenuManager manager = getMenuManager("TestMenu", "0815");
		final ToolItem topItem = ReflectionUtils.invokeHidden(composite, "createAndAddMenu", manager, new ToolBarMenuListener());
		assertEquals("TestMenu", topItem.getText());

		final MenuManager invisibleMenuManager = getMenuManager("TestMenu2", "4711");
		invisibleMenuManager.setVisible(false);
		final ToolItem topItem2 = ReflectionUtils.invokeHidden(composite, "createAndAddMenu", invisibleMenuManager, new ToolBarMenuListener());
		assertNull(locator.locateBindingProperty(topItem2));
		assertNull(topItem2);
	}

	/**
	 * Tests the method {@code getTopLevelItems()}.
	 */
	public void testGetTopLevelItems() {

		final IEntriesProvider entriesProvider = new IEntriesProvider() {
			public IContributionItem[] getTopLevelEntries() {
				return new IContributionItem[0];
			}
		};
		final MenuCoolBarComposite composite = new MenuCoolBarComposite(shell, SWT.NONE, entriesProvider);

		final MenuManager manager = getMenuManager("TestMenu", "0815");
		final ToolItem topItem = ReflectionUtils.invokeHidden(composite, "createAndAddMenu", manager, new ToolBarMenuListener());
		final List<ToolItem> items = composite.getTopLevelItems();
		assertEquals(1, items.size());
		assertTrue(items.contains(topItem));
	}

	private MenuManager getMenuManager(final String text, final String id) {
		final MenuManager manager = new MenuManager("TestMenu", "0815");
		// a MenuManager is only visible if also all of his children are visible
		manager.add(new ToolBarContributionItem());
		return manager;
	}

}
