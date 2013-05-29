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

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPageLayout;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.ModuleGroupNodeListener;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationController;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewId;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link SubApplicationView}.
 */
@UITestCase
public class SubApplicationViewTest extends TestCase {

	private Shell shell;
	private TestSubApplicationView view;
	private SubApplicationNode node;

	@Override
	protected void setUp() throws Exception {
		view = new TestSubApplicationView();
		shell = new Shell();
		final SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		locator.setBindingProperty(shell, ApplicationViewAdvisor.SHELL_RIDGET_PROPERTY);

		node = new SubApplicationNode();
		node.setNavigationProcessor(new NavigationProcessor());
		view.bind(node);
	}

	@Override
	protected void tearDown() throws Exception {
		view = null;
		SwtUtilities.dispose(shell);
	}

	public void testUnbind() throws Exception {

		final List<ModuleGroupNodeListener> listeners = ReflectionUtils.getHidden(node, "listeners");

		assertEquals(1, listeners.size());

		node.dispose();

		assertTrue(listeners.isEmpty());
	}

	public void testDisposeHandling() throws Exception {
		node.setNavigationNodeController(new SubApplicationController(node));
		final ModuleGroupNode mg = new ModuleGroupNode();
		node.addChild(mg);
		final ModuleNode m = new ModuleNode();
		mg.addChild(m);
		view.createInitialLayout(null);
		final SubModuleNode sm1 = new SubModuleNode(new NavigationNodeId("x"));
		m.addChild(sm1);
		final SubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("y"));
		m.addChild(sm2);
		final SubModuleNode sm3 = new SubModuleNode(new NavigationNodeId("z"));
		m.addChild(sm3);
		view.viewUserCount = 1;
		view.providedId = new SwtViewId("test:test");
		view.hiddenId = null;
		sm1.dispose();
		assertSame(view.providedId, view.hiddenId);

		view.viewUserCount = 0;
		view.providedId = new SwtViewId("test:SHARED");
		view.hiddenId = null;
		sm2.dispose();
		assertSame(view.providedId, view.hiddenId);

		view.viewUserCount = 1;
		view.providedId = new SwtViewId("test:SHARED");
		view.hiddenId = null;
		sm2.dispose();
		assertSame(null, view.hiddenId);

	}

	private class TestSubApplicationView extends SubApplicationView {

		private int viewUserCount = 0;
		private SwtViewId hiddenId = null;
		private SwtViewId providedId = null;

		@Override
		public SubApplicationNode getNavigationNode() {
			return node;
		}

		@Override
		protected int getViewUserCount(final SwtViewId id) {
			return viewUserCount;
		}

		@Override
		protected void hideView(final SwtViewId id) {
			hiddenId = id;
		}

		@Override
		protected void doBaseLayout(final IPageLayout layout) {
		}

		@Override
		protected ISubApplicationNode locateSubApplication(final IPageLayout layout) {
			return node;
		}

		@Override
		protected SwtViewId getViewId(final ISubModuleNode node) {
			return providedId;
		}

	}

}
