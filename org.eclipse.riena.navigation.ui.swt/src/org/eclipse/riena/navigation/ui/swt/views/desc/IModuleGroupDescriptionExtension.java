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
package org.eclipse.riena.navigation.ui.swt.views.desc;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.navigation.ui.controllers.ModuleGroupController;
import org.eclipse.riena.navigation.ui.swt.views.ModuleGroupView;

/**
 * interfaces for injecting org.eclipse.riena.navigation.ui.swt.moduleGroupView
 */
@ExtensionInterface(id = "moduleGroupView")
public interface IModuleGroupDescriptionExtension {
	/**
	 * Returns the view class.
	 * 
	 * @return the view class.
	 */
	Class<ModuleGroupView> getView();

	/**
	 * Returns the controller class.
	 * 
	 * @return the controller class.
	 */
	Class<ModuleGroupController> getController();

	/**
	 * Returns the name of the module.
	 * 
	 * @return the name.
	 */
	String getName();

}
