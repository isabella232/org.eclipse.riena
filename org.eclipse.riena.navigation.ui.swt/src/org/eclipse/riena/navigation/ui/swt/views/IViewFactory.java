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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ModuleController;
import org.eclipse.riena.navigation.ui.controllers.ModuleGroupController;

/**
 * Factory to create (sub-)views of the navigation view.
 */
public interface IViewFactory {

	/**
	 * Creates a view for a module.
	 * 
	 * @param parent
	 *            a composite which will be the parent
	 * @return view of module
	 */
	ModuleView createModuleView(Composite parent);

	/**
	 * Creates a controller for a module in the navigation.
	 * 
	 * @param moduleNode
	 *            node of a module
	 * 
	 * @return created module controller
	 */
	ModuleController createModuleController(IModuleNode moduleNode);

	/**
	 * Creates a view for a module group.
	 * 
	 * @param parent
	 *            a composite which will be the parent
	 * @return view of module group
	 */
	ModuleGroupView createModuleGroupView(Composite parent);

	/**
	 * Creates a controller for the module group in the navigation.
	 * 
	 * @param moduleGroupNode
	 *            node of a module group
	 * @return created module group controller
	 */
	ModuleGroupController createModuleGroupController(IModuleGroupNode moduleGroupNode);

}
