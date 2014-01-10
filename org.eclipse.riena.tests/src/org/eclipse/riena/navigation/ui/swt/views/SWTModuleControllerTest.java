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

import org.eclipse.core.databinding.observable.Realm;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.TreeRidget;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.IModuleGroupNodeListener;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;

/**
 * Test of the class {@link SWTModuleController}.
 */
@NonUITestCase
public class SWTModuleControllerTest extends TestCase {

	private TestSWTModuleController controller;
	private IModuleNode moduleNode;

	/**
	 * {@inheritDoc}
	 * <p>
	 * Creates a {@code SWTModuleController}.
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		moduleNode = new ModuleNode();
		controller = new TestSWTModuleController(moduleNode);
	}

	private class TestSWTModuleController extends SWTModuleController {

		int expandCalledTimes = 0;

		public TestSWTModuleController(final IModuleNode node) {
			super(node);
		}

		void reset() {
			expandCalledTimes = 0;
		}

		@Override
		protected void expandTree(final ISubModuleNode source) {
			expandCalledTimes++;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		moduleNode.dispose();
		moduleNode = null;
		controller = null;
	}

	/**
	 * Tests the <i>private</i> method {@code collapseSibling(ISubModuleNode)}.
	 * <p>
	 * 
	 * Navigation Model:
	 * 
	 * <pre>
	 *   m
	 *   |
	 *   +-sm0
	 *   |
	 *   +-sm1
	 *   |  |
	 *   |  +-sm11
	 *   |  |  |
	 *   |  |  +-sm111
	 *   |  |
	 *   |  +-sm12
	 *   |     |
	 *   |     +-sm121
	 *   |
	 *   +-sm2
	 *   |  |
	 *   |  +-sm21
	 *   |  |  |
	 *   |  |  +-sm211
	 *   |  |
	 *   |  +-sm22
	 *   |     |
	 *   |     +-sm221
	 *   |
	 *   +-sm3
	 *      |
	 *      +-sm31
	 *      |  |
	 *      |  +-sm311
	 *      |
	 *      +-sm32
	 *         |
	 *         +-sm321
	 * </pre>
	 */
	public void testCollapseSibling() {
		// level 0
		final ISubModuleNode sm0 = new SubModuleNode(new NavigationNodeId("sm0"));
		moduleNode.addChild(sm0);

		// level 1
		final ISubModuleNode sm1 = new SubModuleNode(new NavigationNodeId("sm1"));
		moduleNode.addChild(sm1);
		final ISubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("sm2"));
		moduleNode.addChild(sm2);
		final ISubModuleNode sm3 = new SubModuleNode(new NavigationNodeId("sm3"));
		moduleNode.addChild(sm3);

		// level 2
		final ISubModuleNode sm11 = new SubModuleNode(new NavigationNodeId("sm11"));
		sm1.addChild(sm11);
		final ISubModuleNode sm12 = new SubModuleNode(new NavigationNodeId("sm12"));
		sm1.addChild(sm12);
		final ISubModuleNode sm21 = new SubModuleNode(new NavigationNodeId("sm21"));
		sm2.addChild(sm21);
		final ISubModuleNode sm22 = new SubModuleNode(new NavigationNodeId("sm22"));
		sm2.addChild(sm22);
		final ISubModuleNode sm31 = new SubModuleNode(new NavigationNodeId("sm31"));
		sm3.addChild(sm31);
		final ISubModuleNode sm32 = new SubModuleNode(new NavigationNodeId("sm32"));
		sm3.addChild(sm32);

		// level 3
		final ISubModuleNode sm111 = new SubModuleNode(new NavigationNodeId("sm111"));
		sm11.addChild(sm111);
		final ISubModuleNode sm121 = new SubModuleNode(new NavigationNodeId("sm121"));
		sm12.addChild(sm121);
		final ISubModuleNode sm211 = new SubModuleNode(new NavigationNodeId("sm211"));
		sm21.addChild(sm211);
		final ISubModuleNode sm221 = new SubModuleNode(new NavigationNodeId("sm221"));
		sm22.addChild(sm221);
		final ISubModuleNode sm311 = new SubModuleNode(new NavigationNodeId("sm311"));
		sm31.addChild(sm311);
		final ISubModuleNode sm321 = new SubModuleNode(new NavigationNodeId("sm321"));
		sm32.addChild(sm321);

		expandAll(moduleNode, true);
		ReflectionUtils.invokeHidden(controller, "collapseSibling", sm0);
		assertTrue(sm1.isExpanded());
		assertTrue(sm11.isExpanded());
		assertTrue(sm111.isExpanded());
		assertTrue(sm12.isExpanded());
		assertTrue(sm121.isExpanded());
		assertTrue(sm2.isExpanded());
		assertTrue(sm3.isExpanded());
		assertTrue(sm21.isExpanded());
		assertTrue(sm211.isExpanded());
		assertTrue(sm22.isExpanded());

		expandAll(moduleNode, true);
		sm111.setCloseSubTree(true);
		ReflectionUtils.invokeHidden(controller, "collapseSibling", sm0);
		assertTrue(sm1.isExpanded());
		assertTrue(sm11.isExpanded());
		assertFalse(sm111.isExpanded());
		assertTrue(sm12.isExpanded());
		assertTrue(sm121.isExpanded());
		assertTrue(sm2.isExpanded());
		assertTrue(sm3.isExpanded());
		assertTrue(sm21.isExpanded());
		assertTrue(sm211.isExpanded());
		assertTrue(sm22.isExpanded());

		expandAll(moduleNode, true);
		sm1.setCloseSubTree(true);
		ReflectionUtils.invokeHidden(controller, "collapseSibling", sm211);
		assertFalse(sm1.isExpanded());
		assertTrue(sm11.isExpanded());
		assertTrue(sm111.isExpanded());
		assertTrue(sm12.isExpanded());
		assertTrue(sm121.isExpanded());
		assertTrue(sm2.isExpanded());
		assertTrue(sm3.isExpanded());
		assertTrue(sm21.isExpanded());
		assertTrue(sm211.isExpanded());
		assertTrue(sm22.isExpanded());

		expandAll(moduleNode, true);
		sm11.setCloseSubTree(true);
		ReflectionUtils.invokeHidden(controller, "collapseSibling", sm211);
		assertTrue(sm1.isExpanded());
		assertFalse(sm11.isExpanded());
		assertTrue(sm2.isExpanded());
		assertTrue(sm3.isExpanded());
		assertTrue(sm31.isExpanded());
		assertTrue(sm32.isExpanded());

		expandAll(moduleNode, false);
		sm221.setExpanded(true);
		sm22.setCloseSubTree(true);
		ReflectionUtils.invokeHidden(controller, "collapseSibling", sm0);
		assertFalse(sm1.isExpanded());
		assertFalse(sm11.isExpanded());
		assertFalse(sm111.isExpanded());
		assertFalse(sm12.isExpanded());
		assertFalse(sm121.isExpanded());
		assertFalse(sm22.isExpanded());
		assertTrue(sm221.isExpanded());
		assertFalse(sm21.isExpanded());
		assertFalse(sm211.isExpanded());
		assertFalse(sm31.isExpanded());
		assertFalse(sm32.isExpanded());
	}

	public void testExpandNode() {

		final ISubModuleNode sm0 = new SubModuleNode(new NavigationNodeId("sm0"));

		final ISubModuleNode sm1 = new SubModuleNode(new NavigationNodeId("sm1"));
		sm1.setExpanded(true);
		sm0.addChild(sm1);
		final ISubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("sm2"));
		sm1.addChild(sm2);
		sm2.setExpanded(true);
		final ISubModuleNode sm3 = new SubModuleNode(new NavigationNodeId("sm3"));
		sm3.setExpanded(true);
		sm2.addChild(sm3);
		moduleNode.addChild(sm0);
		controller.reset();
		Realm.runWithDefault(new DefaultRealm(), new Runnable() {

			public void run() {
				controller.setTree(new TreeRidget());

			}
		});
		sm0.setExpanded(true);
		assertEquals(4, controller.expandCalledTimes);

	}

	public void testModuleGroupListenerisAddedRemoved() {

		final IModuleGroupNode mg = new ModuleGroupNode();
		List<IModuleGroupNodeListener> listeners = ReflectionUtils.invokeHidden(mg, "getListeners");
		assertTrue(listeners.isEmpty());

		moduleNode.setParent(mg);
		listeners = ReflectionUtils.invokeHidden(mg, "getListeners");
		assertEquals(1, listeners.size());

		moduleNode.setParent(null);
		listeners = ReflectionUtils.invokeHidden(mg, "getListeners");
		assertTrue(listeners.isEmpty());

	}

	/**
	 * Expands or collapses the given node and all it's children.
	 * 
	 * @param node
	 *            navigation node
	 * @param expanded
	 *            {@code true} expand nodes; {@code false} collapse nodes
	 */
	private void expandAll(final INavigationNode<?> node, final boolean expanded) {
		node.setExpanded(expanded);
		for (final INavigationNode<?> child : node.getChildren()) {
			expandAll(child, expanded);
		}
	}

}
