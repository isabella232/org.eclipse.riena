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
package org.eclipse.riena.navigation.ui.controllers;

import junit.framework.TestCase;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.ActionRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Tests of the class {@link SubApplicationController}.
 */
@NonUITestCase
public class SubApplicationControllerTest extends TestCase {

	private SubApplicationController controller;
	private SubApplicationNode node;

	@Override
	protected void setUp() throws Exception {

		final Display display = Display.getDefault();
		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm); //$NON-NLS-1$

		node = new SubApplicationNode();
		controller = new SubApplicationController(node);

	}

	@Override
	protected void tearDown() throws Exception {
		controller = null;
		node = null;
	}

	/**
	 * Tests the method {@code getMenuActionRidget(String)}.
	 */
	public void testGetMenuActionRidget() {

		controller.addRidget(IActionRidget.BASE_ID_MENUACTION + "id1", new LabelRidget()); //$NON-NLS-1$
		final ActionRidget menuAction = new ActionRidget();
		controller.addRidget("id2", menuAction); //$NON-NLS-1$
		controller.addRidget(IActionRidget.BASE_ID_MENUACTION + "id3", menuAction); //$NON-NLS-1$
		controller.addRidget(IActionRidget.BASE_ID_TOOLBARACTION + "id4", menuAction); //$NON-NLS-1$

		assertNull(controller.getMenuActionRidget("id1")); //$NON-NLS-1$
		assertNull(controller.getMenuActionRidget("id2")); //$NON-NLS-1$
		assertSame(menuAction, controller.getMenuActionRidget("id3")); //$NON-NLS-1$
		assertNull(controller.getMenuActionRidget("id4")); //$NON-NLS-1$

	}

	/**
	 * Tests the method {@code getToolbarActionRidget(String)}.
	 */
	public void testGetToolbarActionRidget() {

		controller.addRidget(IActionRidget.BASE_ID_TOOLBARACTION + "id1", new LabelRidget()); //$NON-NLS-1$
		final ActionRidget menuAction = new ActionRidget();
		controller.addRidget("id2", menuAction); //$NON-NLS-1$
		controller.addRidget(IActionRidget.BASE_ID_TOOLBARACTION + "id3", menuAction); //$NON-NLS-1$
		controller.addRidget(IActionRidget.BASE_ID_MENUACTION + "id4", menuAction); //$NON-NLS-1$

		assertNull(controller.getToolbarActionRidget("id1")); //$NON-NLS-1$
		assertNull(controller.getToolbarActionRidget("id2")); //$NON-NLS-1$
		assertSame(menuAction, controller.getToolbarActionRidget("id3")); //$NON-NLS-1$
		assertNull(controller.getToolbarActionRidget("id4")); //$NON-NLS-1$

	}

	/**
	 * Test the method {@code prepareController}.
	 */
	public void testPrepareController() {

		final ISubModuleNode subModuleNode = new SubModuleNode(new NavigationNodeId("typeId1", "instanceId1")); //$NON-NLS-1$ //$NON-NLS-2$
		ReflectionUtils.invokeHidden(controller, "prepareController", subModuleNode); //$NON-NLS-1$
		assertNull(subModuleNode.getNavigationNodeController());

		WorkareaManager.getInstance().registerDefinition(subModuleNode, MySubModuleController.class, "whatever"); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(controller, "prepareController", subModuleNode); //$NON-NLS-1$
		assertNotNull(subModuleNode.getNavigationNodeController());
		assertTrue(subModuleNode.getNavigationNodeController() instanceof MySubModuleController);
		final MySubModuleController myController = (MySubModuleController) subModuleNode.getNavigationNodeController();
		assertTrue(myController.isConfigureRidgetsCalled());
		assertSame(subModuleNode, myController.getNavigationNode());

	}

}
