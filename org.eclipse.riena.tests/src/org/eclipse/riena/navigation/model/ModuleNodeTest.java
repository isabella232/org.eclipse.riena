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

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.NavigationNodeId;

/**
 * Tests of the class {@link ModuleNode}.
 */
@NonUITestCase
public class ModuleNodeTest extends TestCase {

	public void testCalcDepthNoChild() {
		final ModuleNode module = new ModuleNode();
		assertEquals(0, module.calcDepth());
	}

	public void testCalcDepthOneChild() {
		final ModuleNode module = new ModuleNode();
		final SubModuleNode sm = new SubModuleNode(new NavigationNodeId("module1"));
		module.addChild(sm);
		assertEquals(0, module.calcDepth());
	}

	public void testCalcDepthTwoChilds() {
		final ModuleNode module = new ModuleNode();
		final SubModuleNode sm = new SubModuleNode(new NavigationNodeId("subModule1"));
		final SubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("subModule2"));
		module.addChild(sm);
		module.addChild(sm2);
		assertEquals(2, module.calcDepth());
	}

	public void testCalcDepthTwoChildsAndExpanded() {
		final ModuleNode module = new ModuleNode();
		final SubModuleNode sm = new SubModuleNode(new NavigationNodeId("subModule1"));
		final SubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("subModule2"));
		module.addChild(sm);
		module.addChild(sm2);
		assertEquals(2, module.calcDepth());

		final SubModuleNode sm21 = new SubModuleNode(new NavigationNodeId("module21"));
		sm21.setNavigationProcessor(new NavigationProcessor());
		sm2.addChild(sm21);
		assertEquals(2, module.calcDepth());
		sm2.setExpanded(true);
		assertEquals(3, module.calcDepth());
	}

	public void testCalcDepthTwoChildsAndSetVisible() {
		final ModuleNode module = new ModuleNode();
		final NavigationProcessor navigationProcessor = new NavigationProcessor();
		final SubModuleNode sm = new SubModuleNode(new NavigationNodeId("subModule1"));
		sm.setNavigationProcessor(navigationProcessor);
		final SubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("subModule2"));
		sm2.setNavigationProcessor(navigationProcessor);
		final SubModuleNode sm3 = new SubModuleNode(new NavigationNodeId("subModule3"));
		sm3.setNavigationProcessor(navigationProcessor);
		module.addChild(sm);
		module.addChild(sm2);
		module.addChild(sm3);
		assertEquals(3, module.calcDepth());

		sm3.setVisible(false);
		assertEquals(2, module.calcDepth());
	}

	public void testCalcDepthTwoChildsAndInvisibleWithSubChilds() {
		final ModuleNode module = new ModuleNode();
		final NavigationProcessor navigationProcessor = new NavigationProcessor();
		final SubModuleNode sm = new SubModuleNode(new NavigationNodeId("subModule1"));
		sm.setNavigationProcessor(navigationProcessor);
		sm.setVisible(false);
		final SubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("subModule2"));
		sm2.setNavigationProcessor(navigationProcessor);
		sm2.setExpanded(true);

		final SubModuleNode sm21 = new SubModuleNode(new NavigationNodeId("subModule2.1"));
		sm21.setNavigationProcessor(navigationProcessor);
		module.addChild(sm);
		module.addChild(sm2);
		sm2.addChild(sm21);

		assertEquals(2, module.calcDepth());
	}

	public void testIsPresentSubModules() throws Exception {
		final SubModuleNode childOne = new SubModuleNode(new NavigationNodeId("nodeOne"));
		childOne.setVisible(false);
		final SubModuleNode childTwo = new SubModuleNode(new NavigationNodeId("nodeTwo"));

		final ModuleNode module = new ModuleNode();
		module.addChild(childOne);
		module.addChild(childTwo);

		assertFalse(module.isPresentSubModules());
	}

}
