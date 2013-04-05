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

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link ScrollButtonsNavigationCompositeDeligation}.
 */
@UITestCase
public class ScrollButtonsNavigationCompositeDeligationTest extends TestCase {

	private Shell shell;
	private Composite composite;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		composite = new Composite(shell, SWT.DEFAULT);
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
		super.tearDown();
	}

	/**
	 * Tests the method {@code updateSize(int)}.
	 */
	public void testCreateNavigationComposite() {
		final MockModuleNavigationComponentProvider navigationProvider = new MockModuleNavigationComponentProvider();
		final ScrollButtonsNavigationCompositeDeligation deligation = new ScrollButtonsNavigationCompositeDeligation(
				shell, composite, navigationProvider);
		final Composite comp = ReflectionUtils.invokeHidden(deligation, "createNavigationComposite", composite);
		assertSame(composite, comp.getParent());
		final Composite scrolledComposite = ReflectionUtils.invokeHidden(deligation, "getScrolledComposite");
		assertNotNull(scrolledComposite);
		final ScrollButtonsSupport scrollingSupport = ReflectionUtils.invokeHidden(deligation, "getScrollingSupport");
		assertEquals(ScrollButtonsSupport.class, scrollingSupport.getClass());
		boolean scrollCompositeExits = false;
		final Control[] children = shell.getChildren();
		for (final Control child : children) {
			if (child == scrollingSupport.getScrollComposite()) {
				scrollCompositeExits = true;
				break;
			}
		}
		assertTrue(scrollCompositeExits);
	}

	/**
	 * Tests the method {@code getScrolledComposite()}.
	 */
	public void testGetScrolledComposite() {
		final MockModuleNavigationComponentProvider navigationProvider = new MockModuleNavigationComponentProvider();
		final ScrollButtonsNavigationCompositeDeligation deligation = new ScrollButtonsNavigationCompositeDeligation(
				shell, composite, navigationProvider);
		final Composite scrolledComposite = ReflectionUtils.invokeHidden(deligation, "getScrolledComposite");
		assertNotNull(scrolledComposite);
		assertFalse(ScrolledComposite.class.equals(scrolledComposite.getClass()));
		final ScrollButtonsSupport scrollingSupport = ReflectionUtils.invokeHidden(deligation, "getScrollingSupport");
		assertSame(scrollingSupport.getScrollComposite(), scrolledComposite);
	}

	/**
	 * Tests the method {@code getScrollingSupport()}.
	 */
	public void testGetScrollingSupport() {
		final MockModuleNavigationComponentProvider navigationProvider = new MockModuleNavigationComponentProvider();
		final ScrollButtonsNavigationCompositeDeligation deligation = new ScrollButtonsNavigationCompositeDeligation(
				shell, composite, navigationProvider);
		final ScrollButtonsSupport scrollingSupport = ReflectionUtils.invokeHidden(deligation, "getScrollingSupport");
		assertNotNull(scrollingSupport);
		assertEquals(ScrollButtonsSupport.class, scrollingSupport.getClass());
	}

	private class MockModuleNavigationComponentProvider implements IModuleNavigationComponentProvider {

		public Composite getNavigationComponent() {
			return composite;
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
