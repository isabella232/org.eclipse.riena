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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.riena.core.util.InvocationTargetFailure;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.ISubModuleNodeListener;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationModelFailure;
import org.eclipse.riena.navigation.model.NavigationNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 * Tests of the class {@link NavigationSourceProvider}.
 */
@NonUITestCase
public class NavigationSourceProviderTest extends TestCase {

	private NavigationSourceProvider provider;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		provider = new NavigationSourceProvider();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		provider = null;
		ApplicationNodeManager.clear();
	}

	/**
	 * Tests the method {@code getVariableNameForNodeId}.
	 */
	public void testGetVariableNameForNode() {

		assertNull(ReflectionUtils.invokeHidden(provider, "getVariableNameForNode", (Object) null));

		assertNotNull(ReflectionUtils.invokeHidden(provider, "getVariableNameForNode", new SubModuleNode()));
		assertNotNull(ReflectionUtils.invokeHidden(provider, "getVariableNameForNode", new ModuleNode()));
		assertNotNull(ReflectionUtils.invokeHidden(provider, "getVariableNameForNode", new ModuleGroupNode()));
		assertNotNull(ReflectionUtils.invokeHidden(provider, "getVariableNameForNode", new SubApplicationNode()));

		try {
			ReflectionUtils.invokeHidden(provider, "getVariableNameForNode", new MockNavigationNode(null));
			fail("Expected InvocationTargetFailure, but it wasn't thrown!");
		} catch (final InvocationTargetFailure failure) {
			assertTrue(failure.getTargetException() instanceof NavigationModelFailure);
			Nop.reason("Expected failure");
		}

	}

	/**
	 * Tests the method <i>private</i> {@code getTypeNodeId}.
	 */
	public void testGetTypeNodeId() {

		SubModuleNode subModuleNode = new SubModuleNode();
		String ret = ReflectionUtils.invokeHidden(provider, "getTypeNodeId", subModuleNode);
		assertEquals("", ret);

		subModuleNode = new SubModuleNode(new NavigationNodeId("helloID"));
		ret = ReflectionUtils.invokeHidden(provider, "getTypeNodeId", subModuleNode);
		assertEquals("helloID", ret);

	}

	/**
	 * Tests the method {@code getCurrentState()}.
	 */
	public void testGetCurrentState() {

		// no navigation model
		Map<String, Object> state = provider.getCurrentState();
		assertEquals(8, state.keySet().size());
		assertEquals(8, state.values().size());
		for (final Object value : state.values()) {
			if (value instanceof String) {
				assertTrue(StringUtils.isEmpty((String) value));
			} else {
				assertNull(value);
			}
		}

		// navigation model but no node is active
		final IApplicationNode appNode = new ApplicationNode(new NavigationNodeId("app1"));
		ApplicationNodeManager.registerApplicationNode(appNode);
		final ISubApplicationNode subApp = new SubApplicationNode(new NavigationNodeId("subApp1"));
		appNode.addChild(subApp);
		final IModuleGroupNode mg = new ModuleGroupNode(new NavigationNodeId("mg1"));
		subApp.addChild(mg);
		final IModuleNode mod = new ModuleNode(new NavigationNodeId("mod1"));
		mg.addChild(mod);
		final ISubModuleNode subMod = new SubModuleNode(new NavigationNodeId("subMod1"));
		mod.addChild(subMod);
		final ISubModuleNode subMod2 = new SubModuleNode(new NavigationNodeId("subMod2"));
		mod.addChild(subMod2);

		state = provider.getCurrentState();
		assertEquals(8, state.values().size());
		for (final Object value : state.values()) {
			if (value instanceof String) {
				assertTrue(StringUtils.isEmpty((String) value));
			} else {
				assertNull(value);
			}
		}

		// navigation model with active nodes
		subMod.activate();
		state = provider.getCurrentState();
		assertEquals(8, state.keySet().size());
		assertTrue(state.values().contains("subMod1"));
		assertTrue(state.values().contains("mod1"));
		assertTrue(state.values().contains("mg1"));
		assertTrue(state.values().contains("subApp1"));
		assertFalse(state.values().contains("app1"));
		assertFalse(state.values().contains("subMod2"));

	}

	private static final class MockNavigationNode extends
			NavigationNode<ISubModuleNode, ISubModuleNode, ISubModuleNodeListener> {

		public MockNavigationNode(final NavigationNodeId nodeId) {
			super(nodeId);
		}

		public Class<ISubModuleNode> getValidChildType() {
			return null;
		}
	}
}
