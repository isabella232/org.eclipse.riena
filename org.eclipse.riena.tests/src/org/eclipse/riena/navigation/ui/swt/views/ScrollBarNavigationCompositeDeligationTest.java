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

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link ScrollBarNavigationCompositeDeligation}.
 */
@UITestCase
public class ScrollBarNavigationCompositeDeligationTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
		super.tearDown();
	}

	/**
	 * Tests the method {@code createNavigationComposite(Composite} .
	 */
	public void testCreateNavigationComposite() {
		final ScrollBarNavigationCompositeDeligation deligation = new ScrollBarNavigationCompositeDeligation(null,
				shell, new MockModuleNavigationComponentProvider());
		final Composite comp = ReflectionUtils.invokeHidden(deligation, "createNavigationComposite", shell);
		assertEquals(ScrolledComposite.class, comp.getParent().getClass());
		final ScrolledComposite sc = (ScrolledComposite) comp.getParent();
		assertSame(comp, sc.getContent());
		final AbstractScrollingSupport scrollingSupport = ReflectionUtils.invokeHidden(deligation,
				"getScrollingSupport");
		assertEquals(ScrollBarSupport.class, scrollingSupport.getClass());
	}

	/**
	 * Tests the method {@code updateSize(int)}.
	 */
	public void testUpdateSize() {
		final MockModuleNavigationComponentProvider navigationProvider = new MockModuleNavigationComponentProvider();
		final ScrollBarNavigationCompositeDeligation deligation = new ScrollBarNavigationCompositeDeligation(null,
				shell, navigationProvider);
		final int height = 123;
		deligation.updateSize(height);
		final ScrolledComposite scrolledComposite = ReflectionUtils.invokeHidden(deligation, "getScrolledComposite");
		assertEquals(height, scrolledComposite.getMinHeight());
		final int width = deligation.getNavigationComposite().getSize().x;
		assertEquals(width, scrolledComposite.getMinWidth());
	}

	/**
	 * Tests the method {@code getScrolledComposite()}.
	 */
	public void testGetScrolledComposite() {
		final MockModuleNavigationComponentProvider navigationProvider = new MockModuleNavigationComponentProvider();
		final ScrollBarNavigationCompositeDeligation deligation = new ScrollBarNavigationCompositeDeligation(null,
				shell, navigationProvider);
		final ScrolledComposite scrolledComposite = ReflectionUtils.invokeHidden(deligation, "getScrolledComposite");
		assertNotNull(scrolledComposite);
		assertEquals(ScrolledComposite.class, scrolledComposite.getClass());
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
