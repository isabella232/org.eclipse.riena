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
package org.eclipse.riena.internal.ui.workarea.registry;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaDefinition;

/**
 * Tests of the class {@link WorkareaDefinitionRegistryFacade}.
 */
@NonUITestCase
public class WorkareaDefinitionRegistryFacadeTest extends TestCase {

	/**
	 * Tests the method {@code getInstance()}.
	 */
	public void testGetInstance() {

		final WorkareaDefinitionRegistryFacade facade1 = WorkareaDefinitionRegistryFacade.getInstance();
		assertNotNull(facade1);
		final WorkareaDefinitionRegistryFacade facade2 = WorkareaDefinitionRegistryFacade.getInstance();
		assertNotNull(facade2);
		assertSame(facade1, facade2);

	}

	/**
	 * Tests the method {@code registerDefinition}.
	 */
	public void testRegisterDefinition() {

		final WorkareaDefinitionRegistryFacade facade = WorkareaDefinitionRegistryFacade.getInstance();
		final IWorkareaDefinition def = facade
				.registerDefinition("id0101", SubModuleController.class, "secondId", true);
		assertEquals(SubModuleController.class, def.getControllerClass());
		assertEquals("secondId", def.getViewId());
		assertTrue(def.isViewShared());
		assertSame(def, facade.getDefinition("id0101"));

	}

	/**
	 * Tests the method {@code register(Object,IWorkareaDefinition)}.
	 */
	public void testRegister() {

		final WorkareaDefinitionRegistryFacade facade = WorkareaDefinitionRegistryFacade.getInstance();
		final IWorkareaDefinition def1 = new WorkareaDefinition("IdOfView");
		final IWorkareaDefinition def2 = facade.register("id0201", def1);
		assertSame(def1, def2);
		final IWorkareaDefinition def3 = facade.getDefinition("id0201");
		assertSame(def1, def3);

	}

	/**
	 * Tests the method {@code getDefinition(Object)}.
	 */
	public void testGetDefinition() {

		final WorkareaDefinitionRegistryFacade facade = WorkareaDefinitionRegistryFacade.getInstance();
		final IWorkareaDefinition def1 = new WorkareaDefinition("IdOfView01");
		facade.register("id0301", def1);
		IWorkareaDefinition retdef = facade.getDefinition("id0301");
		assertSame(def1, retdef);

		final Object id = new Object();
		final IWorkareaDefinition def2 = new WorkareaDefinition("view2");
		facade.register(id, def2);
		retdef = facade.getDefinition(id);
		assertSame(def2, retdef);

		final ISubModuleNode node1 = new SubModuleNode(new NavigationNodeId("id0302"));
		final IWorkareaDefinition def3 = new WorkareaDefinition("IdOfView02");
		facade.register(node1, def3);
		retdef = facade.getDefinition(node1);
		assertSame(def3, retdef);

		final ISubModuleNode node2 = new SubModuleNode(new NavigationNodeId("id0303"));
		final IWorkareaDefinition def4 = new WorkareaDefinition("IdOfView03");
		facade.register("id0303", def4);
		retdef = facade.getDefinition(node2);
		assertSame(def4, retdef);

		final ISubModuleNode node3 = new SubModuleNode(new NavigationNodeId("id0304"));
		final IWorkareaDefinition def5 = new WorkareaDefinition("IdOfView04");
		facade.register(node3, def5);
		retdef = facade.getDefinition("id0304");
		assertSame(def5, retdef);

	}

}
