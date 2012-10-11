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
package org.eclipse.riena.example.client.controller;

import java.util.UUID;

import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 *
 */
public class UsersController extends SubModuleController {

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		final IActionRidget createAction = getRidget(IActionRidget.class, "btnCreate"); //$NON-NLS-1$
		createAction.addListener(new IActionListener() {

			public void callback() {
				final String typeId = "org.eclipse.riena.example.client.User.submodule"; //$NON-NLS-1$
				final String instanceId = UUID.randomUUID().toString();
				getNavigationNode().navigate(new NavigationNodeId(typeId, instanceId));
			}
		});

	}

}