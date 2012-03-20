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
package org.eclipse.riena.client.controller.test;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.swt.controllers.AbstractSubModuleControllerTest;

/**
 * Tests for the NavigationSubModuleController.
 */
@NonUITestCase
public class NavigationSubModuleControllerTest extends AbstractSubModuleControllerTest<NavigationSubModuleController> {

	@Override
	protected NavigationSubModuleController createController(final ISubModuleNode node) {
		final NavigationSubModuleController newInst = new NavigationSubModuleController();
		node.setNodeId(new NavigationNodeId("org.eclipse.riena.example.navigation")); //$NON-NLS-1$
		newInst.setNavigationNode(node);

		return newInst;
	}

	/**
	 * Tests whether a SubModule is added to itself properly.
	 * 
	 * @throws Exception
	 */
	public void testAddSubModules() throws Exception {
		// TODO: not working anymore because of changes in equal(..) method of NavigationNode
		//		final ISubModuleNode newSubModuleNode = new SubModuleNode(new NavigationNodeId(
		//				"org.eclipse.riena.example.navigation", Integer.toString(1)), "Node 0"); //$NON-NLS-1$
		//		getMockNavigationProcessor().activate(EasyMock.eq(newSubModuleNode));
		//
		//		EasyMock.replay(getMockNavigationProcessor());
		//
		//		final IActionRidget addSubModuleToSelf = getController()
		//				.getRidget(IActionRidget.class, "addSubModuleToSelfBtn");
		//		addSubModuleToSelf.fireAction();
		//
		//		EasyMock.verify(getMockNavigationProcessor());
	}
}
