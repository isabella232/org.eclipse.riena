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
package org.eclipse.riena.navigation.ui.controllers;

import org.eclipse.riena.navigation.IModuleGroupNode;

/**
 * Default implementation for a ModuleGroupNodeController
 */
public class ModuleGroupController extends NavigationNodeController<IModuleGroupNode> {

	/**
	 * @param navigationNode
	 */
	public ModuleGroupController(final IModuleGroupNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
		// nothing to do
	}

}
