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
package org.eclipse.riena.navigation;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 * Tests of the class {@link NavigationNodeUtility}.
 */
@NonUITestCase
public class NavigationNodeUtilityTest extends TestCase {

	/**
	 * Tests the method {@code getLongNodeId}.
	 */
	public void testGetLongNodeId() {

		final ISubModuleNode sm = new SubModuleNode(new NavigationNodeId("sm"));
		assertEquals("/sm", NavigationNodeUtility.getNodeLongId(sm));

		final ISubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("sm2"));
		sm.setParent(sm2);
		assertEquals("/sm2/sm", NavigationNodeUtility.getNodeLongId(sm));

		final IModuleNode m = new ModuleNode();
		sm2.setParent(m);
		assertEquals("//sm2/sm", NavigationNodeUtility.getNodeLongId(sm));

		final IModuleGroupNode mg = new ModuleGroupNode(new NavigationNodeId("group"));
		m.setParent(mg);
		assertEquals("/group//sm2/sm", NavigationNodeUtility.getNodeLongId(sm));

	}

	/**
	 * Tests the method {@code findNode(String, INavigationNode<?>)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testFindNode() throws Exception {

		final ISubModuleNode sm = new SubModuleNode(new NavigationNodeId("sm"));
		assertEquals(0, NavigationNodeUtility.findNode("sm2", sm).size());

		final ISubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("sm2"));
		sm.addChild(sm2);
		assertSame(sm2, NavigationNodeUtility.findNode("sm2", sm).get(0));

		final ISubModuleNode sm22 = new SubModuleNode(new NavigationNodeId("sm22"));
		sm.addChild(sm22);
		assertSame(sm, NavigationNodeUtility.findNode("sm*", sm).get(0));
		assertSame(sm2, NavigationNodeUtility.findNode("sm2", sm).get(0));
		assertSame(sm22, NavigationNodeUtility.findNode("sm22", sm22).get(0));

		final ISubModuleNode sm222 = new SubModuleNode(new NavigationNodeId("sm222"));
		sm2.addChild(sm222);
		final ISubModuleNode sm21 = new SubModuleNode(new NavigationNodeId("sm21"));
		sm2.addChild(sm21);
		assertSame(sm21, NavigationNodeUtility.findNode("sm21", sm).get(0));
	}

	/**
	 * Tests the method {@code findNodeLongId(String, INavigationNode<?>)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testFindNodeLongId() throws Exception {

		final ISubModuleNode sm = new SubModuleNode(new NavigationNodeId("sm"));
		assertEquals(0, NavigationNodeUtility.findNodesByLongId("sm2", sm).size());

		final ISubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("sm2"));
		sm.addChild(sm2);
		assertEquals(0, NavigationNodeUtility.findNodesByLongId("sm2", sm).size());
		assertSame(sm2, NavigationNodeUtility.findNodesByLongId("/sm/sm2", sm).get(0));

		final ISubModuleNode sm22 = new SubModuleNode(new NavigationNodeId("sm22"));
		sm.addChild(sm22);
		assertSame(sm2, NavigationNodeUtility.findNodesByLongId("/sm/sm2", sm).get(0));
		assertSame(sm22, NavigationNodeUtility.findNodesByLongId("/sm/sm22", sm22).get(0));

	}

}
