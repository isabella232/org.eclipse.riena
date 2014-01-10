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

import org.eclipse.riena.example.client.views.DetachedSubModuleView;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;

/**
 * Controller for the {@link DetachedSubModuleView} example.
 */
public class DetachedSubModuleController extends SubModuleController {

	public DetachedSubModuleController() {
		super(null);
	}

	@Override
	public void configureRidgets() {
		// unused
	}

}
