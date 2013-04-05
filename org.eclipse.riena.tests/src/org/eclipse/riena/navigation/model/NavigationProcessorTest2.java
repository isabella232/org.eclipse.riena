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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.NavigationProcessorTest.TestSubModuleNode;

/**
 * Manual Tests for the NavigationProcessor that use addPluginXml.
 */
@NonUITestCase
public class NavigationProcessorTest2 extends RienaTestCase {

	private static final String TARGET_MODULE_GROUP = "org.eclipse.riena.navigation.model.test.moduleGroup.2";
	private NavigationProcessor navigationProcessor;
	private IApplicationNode applicationNode;
	private ISubApplicationNode subApplication;
	private IModuleGroupNode moduleGroup;
	private IModuleNode module;
	private ISubModuleNode subModule1;
	private ISubModuleNode subModule2;
	private ISubModuleNode subModule3;
	private TestSubModuleNode subModule4;
	private NodeProviderMock nodeProviderMock;

	private ModuleGroupNode moduleGroup2;
	private ModuleNode module2;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		nodeProviderMock = new NodeProviderMock();
		initializeNavigationStructure();
	}

	final class NodeProviderMock extends SimpleNavigationNodeProvider {

		private NodeProviderMock() {
			initAssemblers();
		}

		private void initAssemblers() {
			registerNavigationAssembler("1", new TestSecondSubApplicationNodeAssembler() {
				@Override
				public String getParentNodeId() {
					return "org.eclipse.riena.navigation.model.test.application";
				}
			});
			registerNavigationAssembler("2", new TestSecondModuleGroupNodeAssembler() {
				@Override
				public String getParentNodeId() {
					return "org.eclipse.riena.navigation.model.test.secondSubApplication";
				}
			});
		}

	}

	private void initializeNavigationStructure() {
		applicationNode = new ApplicationNode(new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.application"));
		navigationProcessor = new NavigationProcessor() {
			@Override
			protected org.eclipse.riena.navigation.INavigationNodeProvider getNavigationNodeProvider() {
				return nodeProviderMock;

			};
		};
		applicationNode.setNavigationProcessor(navigationProcessor);

		subApplication = new SubApplicationNode(new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.subApplication"));
		applicationNode.addChild(subApplication);
		moduleGroup = new ModuleGroupNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.moduleGroup"));
		subApplication.addChild(moduleGroup);
		moduleGroup2 = new ModuleGroupNode(new NavigationNodeId(TARGET_MODULE_GROUP));
		subApplication.addChild(moduleGroup2);

		module = new ModuleNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.module"));
		moduleGroup.addChild(module);
		module2 = new ModuleNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.module.2"));
		moduleGroup.addChild(module2);
		subModule1 = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.subModule"));
		module.addChild(subModule1);
		subModule2 = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.subModule2"));
		module.addChild(subModule2);
		subModule3 = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.subModule3"));
		module.addChild(subModule3);
		subModule4 = new TestSubModuleNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.subModule4"));
		module2.addChild(subModule4);
	}

	@Override
	protected void tearDown() throws Exception {
		applicationNode = null;
		super.tearDown();
	}

	public void testNavigate() throws Exception {

		subModule1.activate();

		System.err.println("NODE: " + applicationNode);

		assertEquals(1, applicationNode.getChildren().size());
		assertTrue(subApplication.isActivated());

		subModule1.navigate(new NavigationNodeId("org.eclipse.riena.navigation.model.test.secondModuleGroup"));

		assertEquals(2, applicationNode.getChildren().size());
		assertFalse(subApplication.isActivated());
		final ISubApplicationNode secondSubApplication = applicationNode.getChild(1);
		assertEquals(new NavigationNodeId("org.eclipse.riena.navigation.model.test.secondSubApplication"),
				secondSubApplication.getNodeId());
		assertTrue(secondSubApplication.isActivated());
		assertEquals(1, secondSubApplication.getChildren().size());
		final IModuleGroupNode secondModuleGroup = secondSubApplication.getChild(0);
		assertEquals(new NavigationNodeId("org.eclipse.riena.navigation.model.test.secondModuleGroup"),
				secondModuleGroup.getNodeId());
		assertTrue(secondModuleGroup.isActivated());
		final IModuleNode secondModule = secondModuleGroup.getChild(0);
		final ISubModuleNode secondSubModule = secondModule.getChild(0);
		assertTrue(secondSubModule.isActivated());

		secondSubModule.navigateBack();

		assertFalse(secondSubApplication.isActivated());
		assertFalse(secondSubModule.isActivated());
		assertTrue(subApplication.isActivated());
		assertTrue(subModule1.isActivated());

		subModule1.navigate(new NavigationNodeId("org.eclipse.riena.navigation.model.test.secondModuleGroup"));

		assertFalse(subApplication.isActivated());
		assertFalse(subModule1.isActivated());
		assertEquals(2, applicationNode.getChildren().size());
		assertSame(secondSubApplication, applicationNode.getChild(1));
		assertTrue(secondSubApplication.isActivated());
		assertEquals(1, secondSubApplication.getChildren().size());
		assertSame(secondModuleGroup, secondSubApplication.getChild(0));
		assertTrue(secondModuleGroup.isActivated());
		assertTrue(secondSubModule.isActivated());
	}

	/**
	 * Tests the method {@code create}.
	 */
	public void testCreate() throws Exception {
		INavigationNode<?> targetNode = navigationProcessor.create(module, new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.subModule"), null);
		assertEquals(subModule1, targetNode);

		targetNode = navigationProcessor.create(applicationNode, new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.secondModuleGroup"), null);
		assertEquals("org.eclipse.riena.navigation.model.test.secondModuleGroup", targetNode.getNodeId().getTypeId());
	}
}
