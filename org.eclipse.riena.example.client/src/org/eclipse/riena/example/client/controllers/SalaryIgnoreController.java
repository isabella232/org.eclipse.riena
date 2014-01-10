/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 * Dummy controller of the sub-module "Salary (Ignore)".
 */
public class SalaryIgnoreController extends SubModuleController implements IController {

	public SalaryIgnoreController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public SalaryIgnoreController() {
	}

}
