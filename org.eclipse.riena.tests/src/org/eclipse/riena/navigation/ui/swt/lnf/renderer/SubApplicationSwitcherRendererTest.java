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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationItem;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link SubApplicationSwitcherRenderer}.
 */
@UITestCase
public class SubApplicationSwitcherRendererTest extends TestCase {

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
	 * Tests the method {@code getVisibleItems()}.
	 */
	public void testGetVisibleItems() {

		final SubApplicationSwitcherRenderer renderer = new SubApplicationSwitcherRenderer();
		List<SubApplicationItem> visibleItems = ReflectionUtils.invokeHidden(renderer, "getVisibleItems",
				new Object[] {});
		assertTrue(visibleItems.isEmpty());

		final SubApplicationNode node1 = new SubApplicationNode();
		node1.setNavigationProcessor(new NavigationProcessor());
		final SubApplicationItem item1 = new SubApplicationItem(shell, node1);
		final SubApplicationNode node2 = new SubApplicationNode();
		node2.setNavigationProcessor(new NavigationProcessor());
		final SubApplicationItem item2 = new SubApplicationItem(shell, node2);
		final SubApplicationNode node3 = new SubApplicationNode();
		node3.setNavigationProcessor(new NavigationProcessor());
		final SubApplicationItem item3 = new SubApplicationItem(shell, node3);
		final List<SubApplicationItem> items = new ArrayList<SubApplicationItem>(3);
		items.add(item1);
		items.add(item2);
		items.add(item3);
		renderer.setItems(items);
		visibleItems = ReflectionUtils.invokeHidden(renderer, "getVisibleItems", new Object[] {});
		assertFalse(visibleItems.isEmpty());
		assertEquals(3, visibleItems.size());
		assertTrue(visibleItems.contains(item1));
		assertTrue(visibleItems.contains(item2));
		assertTrue(visibleItems.contains(item3));

		node1.addMarker(new HiddenMarker());
		node2.addMarker(new DisabledMarker());
		node3.addMarker(new ErrorMarker());
		visibleItems = ReflectionUtils.invokeHidden(renderer, "getVisibleItems", new Object[] {});
		assertFalse(visibleItems.isEmpty());
		assertEquals(2, visibleItems.size());
		assertFalse(visibleItems.contains(item1));
		assertTrue(visibleItems.contains(item2));
		assertTrue(visibleItems.contains(item3));

	}

	/**
	 * Tests the method {@code getItems()}.
	 */
	public void testGetItems() {

		final SubApplicationSwitcherRenderer renderer = new SubApplicationSwitcherRenderer();
		final List<SubApplicationItem> items = ReflectionUtils.invokeHidden(renderer, "getItems", new Object[] {});
		assertTrue(items.isEmpty());

		List<SubApplicationItem> items2 = ReflectionUtils.invokeHidden(renderer, "getItems", new Object[] {});
		assertTrue(items2.isEmpty());
		assertSame(items, items2);

		renderer.setItems(null);
		items2 = ReflectionUtils.invokeHidden(renderer, "getItems", new Object[] {});
		assertTrue(items2.isEmpty());
		assertNotSame(items, items2);

		final SubApplicationNode node1 = new SubApplicationNode();
		final SubApplicationItem item1 = new SubApplicationItem(shell, node1);
		final SubApplicationNode node2 = new SubApplicationNode();
		final SubApplicationItem item2 = new SubApplicationItem(shell, node2);
		final List<SubApplicationItem> items3 = new ArrayList<SubApplicationItem>(2);
		items3.add(item1);
		items3.add(item2);
		renderer.setItems(items3);
		items2 = ReflectionUtils.invokeHidden(renderer, "getItems", new Object[] {});
		assertFalse(items2.isEmpty());
		assertEquals(2, items2.size());
		assertSame(items3, items2);

	}

}
