/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.model;

import java.util.List;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationAssembler;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.StartupNodeInfo;
import org.eclipse.riena.navigation.StartupNodeInfo.Level;
import org.eclipse.riena.navigation.extension.IModuleNode2Extension;
import org.eclipse.riena.navigation.extension.ModuleNode2Extension;
import org.eclipse.riena.navigation.extension.NavigationAssembly2Extension;

/**
 * Tests of the class {@link SimpleNavigationNodeProvider} and
 * {@link AbstractSimpleNavigationNodeProvider}.
 */
@NonUITestCase
public class SimpleNavigationNodeProviderTest extends RienaTestCase {

	private IApplicationNode applicationNode;
	private ISubApplicationNode subApplication;
	private IModuleGroupNode moduleGroup;
	private IModuleNode module;
	private ISubModuleNode subModule;

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		applicationNode = new ApplicationNode(new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.application"));

		applicationNode.setNavigationProcessor(new NavigationProcessor());

		subApplication = new SubApplicationNode(new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.subApplication"));
		applicationNode.addChild(subApplication);
		moduleGroup = new ModuleGroupNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.moduleGroup"));
		subApplication.addChild(moduleGroup);
		module = new ModuleNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.module"));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.navigation.model.test.subModule"));
		module.addChild(subModule);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		applicationNode = null;
	}

	/**
	 * Tests the method {@code provideNode}.
	 */
	public void testProvideNode() {

		subModule.activate();

		assertEquals(1, subApplication.getChildren().size());
		assertTrue(subApplication.isActivated());

		final SimpleNavigationNodeProvider provider = new SimpleNavigationNodeProvider();
		final INavigationAssembler assembler = new TestSecondModuleGroupNodeAssembler();
		provider.registerNavigationAssembler("myTestAssemberId", assembler);

		final NavigationArgument naviArg = new NavigationArgument(new Integer(4711), new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.subApplication"));
		INavigationNode<?> node = provider.provideNode(subModule, new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.secondModuleGroup"), naviArg);
		assertNotNull(node);
		assertSame("org.eclipse.riena.navigation.model.test.secondModuleGroup", node.getNodeId().getTypeId());
		assertEquals(2, subApplication.getChildren().size());
		INavigationNode<?> subModuleChild = node.getChild(0).getChild(0);
		assertFalse(subModuleChild.isPrepared());
		assertEquals(4711, node.getNavigationArgument().getParameter());

		subApplication.removeChild(node);
		assertEquals(1, subApplication.getChildren().size());

		naviArg.setPrepareAll(true);
		node = provider.provideNode(subModule, new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.secondModuleGroup"), naviArg);
		assertNotNull(node);
		assertSame("org.eclipse.riena.navigation.model.test.secondModuleGroup", node.getNodeId().getTypeId());
		assertEquals(2, subApplication.getChildren().size());
		subModuleChild = node.getChild(0).getChild(0);
		assertTrue(subModuleChild.isPrepared());
		assertEquals(4711, node.getNavigationArgument().getParameter());

	}

	/**
	 * Tests the method {@code register(INavigationAssemblyExtension)}.
	 */
	public void testRegister() {

		final SimpleNavigationNodeProvider provider = new SimpleNavigationNodeProvider();
		final NavigationAssembly2Extension assembly = new NavigationAssembly2Extension();
		assembly.setId("test4711");
		assembly.setParentNodeId("parent0815");
		assembly.setStartOrder(123);
		provider.register(assembly);
		INavigationAssembler assembler = provider.getNavigationAssembler(assembly.getId());
		assertTrue(assembler instanceof GenericNavigationAssembler);
		assertSame(assembly, assembler.getAssembly());
		assertEquals("test4711", assembler.getId());
		assertEquals("parent0815", assembler.getParentNodeId());
		assertEquals(123, assembler.getStartOrder());

		provider.cleanUp();
		final INavigationAssembler naviAssembler = new TestSecondModuleGroupNodeAssembler();
		assembly.setAssembler(naviAssembler);
		provider.register(assembly);
		assembler = provider.getNavigationAssembler(assembly.getId());
		assertSame(naviAssembler, assembler);
		assertSame(assembly, assembler.getAssembly());

	}

	/**
	 * Tests the method {@code findNode(INavigationNode<?>, NavigationNodeId)}.
	 */
	public void testFindNode() {

		final MyProvider provider = new MyProvider();
		INavigationNode<?> node = provider.findNode(applicationNode, new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.module"));
		assertSame(module, node);

		node = provider.findNode(applicationNode, new NavigationNodeId("dummy"));
		assertNull(node);

		node = provider.findNode(applicationNode, null);
		assertNull(node);

	}

	/**
	 * Tests the <i>private</i> method
	 * {@code prepareAll(INavigationNode<?>,NavigationArgument)}.
	 */
	public void testPrepareAll() {

		final SimpleNavigationNodeProvider provider = new SimpleNavigationNodeProvider();
		ReflectionUtils.invokeHidden(provider, "prepareAll", subApplication);
		assertFalse(applicationNode.isPrepared());
		assertTrue(subApplication.isPrepared());
		assertTrue(moduleGroup.isPrepared());
		assertTrue(module.isPrepared());
		assertTrue(subModule.isPrepared());

	}

	/**
	 * Tests the <i>private</i> method
	 * {@code getParentTypeId(NavigationArgument, INavigationAssembler)}.
	 */
	public void testGetParentTypeId() {

		final SimpleNavigationNodeProvider provider = new SimpleNavigationNodeProvider();
		NavigationArgument naviArg = new NavigationArgument(null, new NavigationNodeId("one"));
		final INavigationAssembler naviAssembler = new TestSecondModuleGroupNodeAssembler() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.riena.navigation.AbstractNavigationAssembler#
			 * getParentNodeId()
			 */
			@Override
			public String getParentNodeId() {
				return "application";
			}
		};
		NavigationNodeId parentId = ReflectionUtils.invokeHidden(provider, "getParentTypeId", naviArg, naviAssembler);
		assertEquals("one", parentId.getTypeId());

		naviArg = new NavigationArgument();
		parentId = ReflectionUtils.invokeHidden(provider, "getParentTypeId", naviArg, naviAssembler);
		assertEquals("application", parentId.getTypeId());

	}

	/**
	 * Tests the method {@code getSortedStartupNodeInfos()}.
	 */
	public void testGetSortedStartupNodeInfos() {

		// no assembler is registered => empty list
		final SimpleNavigationNodeProvider provider = new SimpleNavigationNodeProvider();
		provider.cleanUp();
		List<StartupNodeInfo> startupNodeInfos = provider.getSortedStartupNodeInfos();
		assertNotNull(startupNodeInfos);
		assertTrue(startupNodeInfos.isEmpty());

		// one assembler without an assembly is registered => empty list
		final INavigationAssembler naviAssembler = new TestSecondModuleGroupNodeAssembler();
		naviAssembler.setId("ass1");
		naviAssembler.setStartOrder(1);
		provider.registerNavigationAssembler(naviAssembler.getId(), naviAssembler);
		startupNodeInfos = provider.getSortedStartupNodeInfos();
		assertNotNull(startupNodeInfos);
		assertTrue(startupNodeInfos.isEmpty());

		// one assembler with an assembly is registered but assembly has no children => list with one "custom" entry 
		final NavigationAssembly2Extension assembly = new NavigationAssembly2Extension();
		naviAssembler.setAssembly(assembly);
		assembly.setNavigationAssembler(naviAssembler.getClass().getName());
		startupNodeInfos = provider.getSortedStartupNodeInfos();
		assertNotNull(startupNodeInfos);
		assertEquals(1, startupNodeInfos.size());
		StartupNodeInfo startupNodeInfo = startupNodeInfos.get(0);
		assertEquals(new StartupNodeInfo(Level.CUSTOM, 1, "ass1"), startupNodeInfo);

		// one assembler with an assembly is registered; the assembly has one child module => list with one "module" entry 
		final ModuleNode2Extension moduleExt = new ModuleNode2Extension();
		moduleExt.setNodeId("mod1");
		assembly.setModules(new IModuleNode2Extension[] { moduleExt });
		startupNodeInfos = provider.getSortedStartupNodeInfos();
		assertNotNull(startupNodeInfos);
		startupNodeInfo = startupNodeInfos.get(0);
		assertEquals(new StartupNodeInfo(Level.MODULE, 1, "mod1"), startupNodeInfo);

	}

	/**
	 * Tests the method {@code getRootNode(INavigationNode<?>)}.
	 */
	public void testGetRootNode() {

		final MyProvider provider = new MyProvider();
		final INavigationNode<?> root = provider.getRootNode(subModule);
		assertSame(applicationNode, root);

	}

	/**
	 * This class only makes some protected methods public for testing.
	 */
	private static class MyProvider extends SimpleNavigationNodeProvider {

		/**
		 * {@inheritDoc}
		 * <p>
		 * Visibility changed.
		 */
		@Override
		public INavigationNode<?> findNode(final INavigationNode<?> node, final NavigationNodeId targetId) {
			return super.findNode(node, targetId);
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * Visibility changed.
		 */
		@Override
		protected INavigationNode<?> getRootNode(final INavigationNode<?> node) {
			return super.getRootNode(node);
		}

	}

}
