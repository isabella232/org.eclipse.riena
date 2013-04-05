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
package org.eclipse.riena.ui.workarea;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 * Tests of the class {@link WorkareaDefinition}.
 */
@NonUITestCase
public class WorkareaDefinitionTest extends TestCase {

	/**
	 * Tests the method {@code createController()}.
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void testCreateController() throws IllegalAccessException, InstantiationException {

		WorkareaDefinition def = new WorkareaDefinition("viewOne");
		assertEquals("viewOne", def.getViewId());
		assertNull(def.getControllerClass());
		IController controller = def.createController();
		assertNull(controller);

		def = new WorkareaDefinition(SubModuleController.class, "viewTwo");
		assertEquals("viewTwo", def.getViewId());
		assertEquals(SubModuleController.class, def.getControllerClass());
		controller = def.createController();
		assertNotNull(controller);
		assertEquals(SubModuleController.class, controller.getClass());

	}

}
