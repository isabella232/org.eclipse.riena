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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link ModuleNavigationListener}.
 */
@UITestCase
public class ModuleNavigationListenerTest extends RienaTestCase {

	private Tree tree;
	private ModuleNavigationListener listener;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		tree = new Tree(new Shell(Display.getDefault()), SWT.NONE);
		listener = new ModuleNavigationListener(tree);
	}

	@Override
	protected void tearDown() throws Exception {
		listener = null;
		SwtUtilities.dispose(tree);
		super.tearDown();
	}

	public void testIsSelectable() throws Exception {
		final TreeItem item = new TreeItem(tree, SWT.NONE);
		final SubModuleNode s1 = new SubModuleNode("s1");
		item.setData(s1);
		assertTrue(invokeIsSelectable(listener, item));
		s1.setSelectable(false);
		assertFalse(invokeIsSelectable(listener, item));
		final SubModuleNode s2 = new SubModuleNode("s2");
		s1.addChild(s2);
		assertTrue(invokeIsSelectable(listener, item));
	}

	/**
	 * Tests the <i>private</i> method {@code findItem}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testFindItem() throws Exception {

		final TreeItem item1 = new TreeItem(tree, SWT.NONE);
		final SubModuleNode s1 = new SubModuleNode("s1");
		item1.setData(s1);

		TreeItem foundItem = ReflectionUtils.invokeHidden(listener, "findItem", tree.getItems(), s1);
		assertSame(item1, foundItem);

		final SubModuleNode s2 = new SubModuleNode("s2");
		foundItem = ReflectionUtils.invokeHidden(listener, "findItem", tree.getItems(), s2);
		assertNull(foundItem);

		s1.addChild(s2);
		foundItem = ReflectionUtils.invokeHidden(listener, "findItem", tree.getItems(), s2);
		assertNull(foundItem);

		final TreeItem item2 = new TreeItem(item1, SWT.NONE);
		item2.setData(s2);
		foundItem = ReflectionUtils.invokeHidden(listener, "findItem", tree.getItems(), s2);
		assertSame(item2, foundItem);

	}

	/**
	 * Tests the <i>private</i> method {@code activateNode}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testActivateNode() throws Exception {

		final ModuleNode m = new ModuleNode();
		final TreeItem item1 = new TreeItem(tree, SWT.NONE);
		final SubModuleNode s1 = new SubModuleNode(new NavigationNodeId("s1"), "s1");
		m.addChild(s1);
		item1.setData(s1);
		final TreeItem item2 = new TreeItem(tree, SWT.NONE);
		final SubModuleNode s2 = new SubModuleNode(new NavigationNodeId("s2"), "s2");
		m.addChild(s2);
		item2.setData(s2);
		final TreeItem item3 = new TreeItem(tree, SWT.NONE);
		final SubModuleNode s3 = new SubModuleNode(new NavigationNodeId("s3"), "s3");
		m.addChild(s3);
		s3.setSelectable(false);
		item3.setData(s3);

		tree.select(item2);
		ReflectionUtils.invokeHidden(listener, "activateNode", s2, tree);
		assertTrue(s2.isActivated());
		assertSame(item2, tree.getSelection()[0]);

		tree.select(item3);
		ReflectionUtils.invokeHidden(listener, "activateNode", s3, tree);
		assertFalse(s3.isActivated());
		assertSame(item2, tree.getSelection()[0]);

		tree.select(item1);
		ReflectionUtils.invokeHidden(listener, "activateNode", s1, tree);
		assertTrue(s1.isActivated());
		assertSame(item1, tree.getSelection()[0]);

	}

	private static Boolean invokeIsSelectable(final ModuleNavigationListener listener, final TreeItem item) {
		return (Boolean) ReflectionUtils.invokeHidden(listener, "isSelectable", item);
	}

}
