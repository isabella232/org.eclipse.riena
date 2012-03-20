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
package org.eclipse.riena.demo.client.application;

import org.eclipse.riena.internal.demo.client.Activator;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.application.SwtApplication;

/**
 * Define the model of the application
 */
public class SwtDemoApplication extends SwtApplication {

	@Override
	protected ApplicationController createApplicationController(final IApplicationNode node) {
		final ApplicationController controller = super.createApplicationController(node);
		controller.setMenubarVisible(true);
		return controller;
	}

	@Override
	protected IApplicationNode createModel() {
		final String bundleVersion = Activator.getDefault().getBundle().getHeaders().get("Bundle-Version"); //$NON-NLS-1$
		final IApplicationNode applicationNode = new ApplicationNode(
				new NavigationNodeId("application"), "Riena Demo - " + bundleVersion); //$NON-NLS-1$ //$NON-NLS-2$
		return applicationNode;
	}

}
