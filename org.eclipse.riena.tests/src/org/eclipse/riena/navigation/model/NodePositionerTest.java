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
import org.eclipse.riena.navigation.NodePositioner;

/**
 * Tests the {@link NodePositioner}
 */
@NonUITestCase
public class NodePositionerTest extends TestCase {

	public void testAddChildToParentFixed() {
		final SubModuleNode parent = new SubModuleNode(new NavigationNodeId("p"));
		final SubModuleNode childA = new SubModuleNode(new NavigationNodeId("ca"));
		final SubModuleNode childToAdd = new SubModuleNode(new NavigationNodeId("cta"));
		final SubModuleNode childC = new SubModuleNode(new NavigationNodeId("cc"));

		assertFalse(parent.getChildren().contains(childA));
		assertFalse(parent.getChildren().contains(childToAdd));

		parent.addChild(childA);
		NodePositioner.ADD_BEGINNING.addChildToParent(parent, childToAdd);

		assertEquals(childToAdd, parent.getChild(0));

		parent.removeChild(childToAdd);
		parent.addChild(childC);
		NodePositioner.ADD_END.addChildToParent(parent, childToAdd);

		assertEquals(childToAdd, parent.getChild(parent.getChildren().size() - 1));

		parent.removeChild(childToAdd);

		final int fixedIndex = 1;
		NodePositioner.indexed(fixedIndex).addChildToParent(parent, childToAdd);

		assertEquals(childToAdd, parent.getChild(fixedIndex));

		parent.removeChild(childToAdd);

		boolean failureOccured = false;
		try {
			NodePositioner.indexed(-1).addChildToParent(parent, childToAdd);
		} catch (final NavigationModelFailure e) {
			failureOccured = true;
		}
		assertTrue(failureOccured);

		failureOccured = false;

		try {
			NodePositioner.ordinal(10).addChildToParent(parent, new SubModuleNode(new NavigationNodeId("ordinal")));
		} catch (final NavigationModelFailure e) {
			failureOccured = true;
		}
		assertTrue(failureOccured);

	}

	public void testAddChildToParentOrdinal() {
		final SubModuleNode parent = new SubModuleNode(new NavigationNodeId("p"));
		final SubModuleNode childA = new SubModuleNode(new NavigationNodeId("A"));
		final SubModuleNode childB = new SubModuleNode(new NavigationNodeId("B"));
		final SubModuleNode childC = new SubModuleNode(new NavigationNodeId("C"));
		final SubModuleNode childD = new SubModuleNode(new NavigationNodeId("D"));

		assertFalse(parent.getChildren().contains(childA));
		assertFalse(parent.getChildren().contains(childB));

		NodePositioner.ordinal(10).addChildToParent(parent, childA);
		assertEquals(childA, parent.getChild(0));
		NodePositioner.ordinal(5).addChildToParent(parent, childB);
		assertEquals(childB, parent.getChild(0));
		assertEquals(childA, parent.getChild(1));
		NodePositioner.ordinal(15).addChildToParent(parent, childC);
		assertEquals(childB, parent.getChild(0));
		assertEquals(childA, parent.getChild(1));
		assertEquals(childC, parent.getChild(2));

		boolean failureOccured = false;
		try {
			NodePositioner.indexed(1).addChildToParent(parent, childD);
		} catch (final NavigationModelFailure e) {
			failureOccured = true;
		}
		assertTrue(failureOccured);

	}
}
