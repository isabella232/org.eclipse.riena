package org.eclipse.riena.navigation.ui.swt.facades;

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

import org.eclipse.riena.internal.navigation.ui.swt.IAdvisorHelper;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.ApplicationUtility;
import org.eclipse.riena.navigation.ui.swt.IApplicationUtility;
import org.eclipse.riena.navigation.ui.swt.views.ApplicationAdvisor;
import org.eclipse.riena.navigation.ui.swt.views.ModuleNavigationListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class NavigationFacadeImpl extends NavigationFacade {

	@Override
	public void attachModuleNavigationListener(Tree tree) {
		new ModuleNavigationListener(tree);
	}

	@Override
	public WorkbenchAdvisor createWorkbenchAdvisor(
			ApplicationController applicationController,
			IAdvisorHelper advisorHelper) {
		return new ApplicationAdvisor(applicationController, advisorHelper);
	}

	@Override
	public IApplicationUtility getApplicationUtility() {
		return new ApplicationUtility();
	}
	
}
