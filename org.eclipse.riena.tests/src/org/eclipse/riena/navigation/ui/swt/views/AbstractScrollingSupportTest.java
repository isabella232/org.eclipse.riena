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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link AbstractScrollingSupport}.
 */
@UITestCase
public class AbstractScrollingSupportTest extends TestCase {

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
	 * Tests the method {@code getActiveNode()}.
	 */
	public void testGetActiveNode() {

		final IModuleGroupNode mg1 = new ModuleGroupNode();
		final MockModuleNavigationComponentProvider provider = new MockModuleNavigationComponentProvider();
		provider.setActiveModuleGroupNode(mg1);
		final MockScrollingSupport support = new MockScrollingSupport(provider);

		INavigationNode<?> activeNode = ReflectionUtils.invokeHidden(support, "getActiveNode");
		assertSame(mg1, activeNode);

		final IModuleNode m1 = new ModuleNode(new NavigationNodeId("m1"));
		mg1.addChild(m1);
		final IModuleNode m2 = new ModuleNode(new NavigationNodeId("m2"));
		mg1.addChild(m2);
		final IModuleNode m3 = new ModuleNode(new NavigationNodeId("m3"));
		mg1.addChild(m3);
		activeNode = ReflectionUtils.invokeHidden(support, "getActiveNode");
		assertSame(mg1, activeNode);

		m2.activate(null);
		activeNode = ReflectionUtils.invokeHidden(support, "getActiveNode");
		assertSame(m2, activeNode);

		final ISubModuleNode sm1 = new SubModuleNode();
		m2.addChild(sm1);
		activeNode = ReflectionUtils.invokeHidden(support, "getActiveNode");
		assertSame(m2, activeNode);

		sm1.activate(null);
		activeNode = ReflectionUtils.invokeHidden(support, "getActiveNode");
		assertSame(sm1, activeNode);

	}

	/**
	 * Tests the method {@code getActiveSubModuleNode}.
	 */
	public void testGetActiveSubModuleNode() {

		final MockScrollingSupport support = new MockScrollingSupport(new MockModuleNavigationComponentProvider());

		final List<ISubModuleNode> nodes = new ArrayList<ISubModuleNode>();
		ISubModuleNode activeNode = ReflectionUtils.invokeHidden(support, "getActiveSubModuleNode", nodes);
		assertNull(activeNode);

		final SubModuleNode sm1 = new SubModuleNode();
		nodes.add(sm1);
		activeNode = ReflectionUtils.invokeHidden(support, "getActiveSubModuleNode", nodes);
		assertNull(activeNode);

		sm1.activate(null);
		activeNode = ReflectionUtils.invokeHidden(support, "getActiveSubModuleNode", nodes);
		assertSame(sm1, activeNode);

		final SubModuleNode sm2 = new SubModuleNode();
		sm1.addChild(sm2);
		activeNode = ReflectionUtils.invokeHidden(support, "getActiveSubModuleNode", nodes);
		assertSame(sm1, activeNode);

		sm2.activate(null);
		activeNode = ReflectionUtils.invokeHidden(support, "getActiveSubModuleNode", nodes);
		assertSame(sm2, activeNode);

	}

	private class MockScrollingSupport extends AbstractScrollingSupport {

		public MockScrollingSupport(final IModuleNavigationComponentProvider navigationComponentProvider) {
			super(navigationComponentProvider);
		}

		@Override
		public void scroll() {
		}

		@Override
		protected boolean scrollTo(final Composite topComp, final Composite bottomComp) {
			return false;
		}

		@Override
		protected boolean scrollTo(final Tree tree) {
			return false;
		}

		@Override
		protected void scrollUp(final int pixels) {
		}

		@Override
		protected void scrollDown(final int pixels) {
		}

	}

	private class MockModuleNavigationComponentProvider implements IModuleNavigationComponentProvider {

		private IModuleGroupNode moduleGroup;

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
			return moduleGroup;
		}

		public ISubApplicationNode getSubApplicationNode() {
			return null;
		}

		public void setActiveModuleGroupNode(final IModuleGroupNode moduleGroup) {
			this.moduleGroup = moduleGroup;
		}

	}

}
