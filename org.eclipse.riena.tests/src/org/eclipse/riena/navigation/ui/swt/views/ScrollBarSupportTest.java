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

import junit.framework.TestCase;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link ScrollBarSupport}.
 */
@UITestCase
public class ScrollBarSupportTest extends TestCase {

	private Display display;
	private Shell shell;
	private ScrolledComposite scrolledComposite;
	private Composite content;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell(SWT.SYSTEM_MODAL | SWT.ON_TOP);
		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(shell);
		scrolledComposite = new ScrolledComposite(shell, SWT.VERTICAL | SWT.BORDER);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setShowFocusedControl(false);
		content = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(content);
		GridDataFactory.fillDefaults().hint(30, 60).applyTo(scrolledComposite);
		display = shell.getDisplay();
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
		super.tearDown();
	}

	/**
	 * Tests the <i>protected</i> method {@code scrollUp(int)}.
	 */
	public void testScrollUp() {
		final ScrollBarSupport support = new ScrollBarSupport(scrolledComposite, new MockModuleNavigationComponentProvider());
		Point origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(0, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollDown", 30); //$NON-NLS-1$
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(30, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollUp", 20); //$NON-NLS-1$
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(10, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollUp", 20); //$NON-NLS-1$
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(0, origin.y);

	}

	/**
	 * Tests the <i>protected</i> method {@code scrollDown(int)}.
	 */
	public void testScrollDonw() {
		final ScrollBarSupport support = new ScrollBarSupport(scrolledComposite, new MockModuleNavigationComponentProvider());
		Point origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(0, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollDown", 30); //$NON-NLS-1$
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(30, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollDown", 20); //$NON-NLS-1$
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(50, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollDown", 20); //$NON-NLS-1$
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(70, origin.y);

	}

	/**
	 * Tests the <i>private</i> method {@code getScrollPixels(Composite,Composite)}.
	 */
	public void testGetScrollPixels() {

		final ScrollBarSupport support = new ScrollBarSupport(scrolledComposite, new MockModuleNavigationComponentProvider());

		scrolledComposite.setMinSize(30, 80);

		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(content);
		final Composite one = new Composite(content, SWT.NONE);
		Color bgColor = display.getSystemColor(SWT.COLOR_BLUE);
		one.setBackground(bgColor);
		GridDataFactory.fillDefaults().hint(20, 20).applyTo(one);
		final Composite two = new Composite(content, SWT.NONE);
		bgColor = display.getSystemColor(SWT.COLOR_GREEN);
		two.setBackground(bgColor);
		GridDataFactory.fillDefaults().hint(20, 20).applyTo(two);
		final Composite three = new Composite(content, SWT.NONE);
		bgColor = display.getSystemColor(SWT.COLOR_YELLOW);
		three.setBackground(bgColor);
		GridDataFactory.fillDefaults().hint(20, 20).applyTo(three);
		final Composite four = new Composite(content, SWT.NONE);
		bgColor = display.getSystemColor(SWT.COLOR_RED);
		four.setBackground(bgColor);
		GridDataFactory.fillDefaults().hint(20, 20).applyTo(four);

		shell.setLocation(0, 0);
		shell.pack();
		shell.open();

		scrolledComposite.setOrigin(0, 0);
		int pixels = ReflectionUtils.invokeHidden(support, "getScrollPixels", one, two); //$NON-NLS-1$
		assertEquals(0, pixels);

		pixels = ReflectionUtils.invokeHidden(support, "getScrollPixels", three, four); //$NON-NLS-1$
		assertEquals(20, pixels);

		scrolledComposite.setOrigin(0, 10);
		pixels = ReflectionUtils.invokeHidden(support, "getScrollPixels", one, two); //$NON-NLS-1$
		assertEquals(-10, pixels);

	}

	/**
	 * Tests the <i>private</i> method {@code getScrollPixels(Tree)}.
	 */
	public void testGetScrollPixelsTree() {

		final ScrollBarSupport support = new ScrollBarSupport(scrolledComposite, new MockModuleNavigationComponentProvider());

		GridDataFactory.fillDefaults().hint(75, 60).applyTo(scrolledComposite);
		scrolledComposite.setMinSize(30, 80);

		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(content);
		final Tree tree = new Tree(content, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 0).margins(0, 0).applyTo(tree);
		final TreeItem item00 = new TreeItem(tree, SWT.NONE);
		item00.setText("one"); //$NON-NLS-1$
		final TreeItem item01 = new TreeItem(tree, SWT.NONE);
		item01.setText("two"); //$NON-NLS-1$
		final TreeItem item02 = new TreeItem(tree, SWT.NONE);
		item02.setText("three"); //$NON-NLS-1$
		final TreeItem item03 = new TreeItem(tree, SWT.NONE);
		item03.setText("four"); //$NON-NLS-1$
		final TreeItem item04 = new TreeItem(tree, SWT.NONE);
		item04.setText("five"); //$NON-NLS-1$
		final TreeItem item05 = new TreeItem(tree, SWT.NONE);
		item05.setText("six"); //$NON-NLS-1$

		shell.setLocation(0, 0);
		shell.pack();
		shell.open();

		final int itemHeight = item00.getBounds().height;
		final int clientHeight = scrolledComposite.getClientArea().height;

		scrolledComposite.setOrigin(0, 0);
		tree.setSelection(item00);
		int pixels = ReflectionUtils.invokeHidden(support, "getScrollPixels", tree); //$NON-NLS-1$
		assertEquals(0, pixels);

		tree.setSelection(item01);
		pixels = ReflectionUtils.invokeHidden(support, "getScrollPixels", tree); //$NON-NLS-1$
		assertEquals(0, pixels);

		tree.setSelection(item02);
		pixels = ReflectionUtils.invokeHidden(support, "getScrollPixels", tree); //$NON-NLS-1$
		assertEquals(0, pixels);

		tree.setSelection(item03);
		pixels = ReflectionUtils.invokeHidden(support, "getScrollPixels", tree); //$NON-NLS-1$
		final int expectedPixels = (itemHeight * 4) - clientHeight;
		assertEquals(expectedPixels, pixels);

	}

	/**
	 * Tests the <i>private</i> method {@code getActiveModule(INavigationNode<?>)}.
	 */
	public void testGetActiveModule() {

		final ScrollBarSupport support = new ScrollBarSupport(scrolledComposite, new MockModuleNavigationComponentProvider());

		IModuleNode module = ReflectionUtils.invokeHidden(support, "getActiveModule", new ApplicationNode()); //$NON-NLS-1$
		assertNull(module);

		module = ReflectionUtils.invokeHidden(support, "getActiveModule", new SubApplicationNode()); //$NON-NLS-1$
		assertNull(module);

		module = ReflectionUtils.invokeHidden(support, "getActiveModule", new ModuleGroupNode()); //$NON-NLS-1$
		assertNull(module);

		final IModuleNode module0 = new ModuleNode("node0"); //$NON-NLS-1$
		module = ReflectionUtils.invokeHidden(support, "getActiveModule", module0); //$NON-NLS-1$
		assertSame(module, module0);

		final ISubModuleNode subModule1 = new SubModuleNode("node1"); //$NON-NLS-1$
		module = ReflectionUtils.invokeHidden(support, "getActiveModule", subModule1); //$NON-NLS-1$
		assertNull(module);

		subModule1.setParent(module0);
		module = ReflectionUtils.invokeHidden(support, "getActiveModule", subModule1); //$NON-NLS-1$
		assertSame(module, module0);

		final ISubModuleNode subModule2 = new SubModuleNode("node2"); //$NON-NLS-1$
		subModule2.setParent(subModule1);
		module = ReflectionUtils.invokeHidden(support, "getActiveModule", subModule2); //$NON-NLS-1$
		assertSame(module, module0);

	}

	private class MockModuleNavigationComponentProvider implements IModuleNavigationComponentProvider {

		public Composite getNavigationComponent() {
			return shell;
		}

		public Composite getScrolledComponent() {
			return null;
		}

		public int calculateHeight() {
			return 0;
		}

		public ModuleGroupView getModuleGroupViewForNode(final IModuleGroupNode moduleGroupNode) {
			return null;
		}

		public ModuleView getModuleViewForNode(final IModuleNode moduleGroupNode) {
			return null;
		}

		public IModuleGroupNode getActiveModuleGroupNode() {
			return null;
		}

		public ISubApplicationNode getSubApplicationNode() {
			return null;
		}

	}

}
