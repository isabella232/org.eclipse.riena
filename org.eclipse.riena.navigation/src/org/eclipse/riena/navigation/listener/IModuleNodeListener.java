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
package org.eclipse.riena.navigation.listener;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubModuleNode;

/**
 * Special listener for the module node
 */
public interface IModuleNodeListener extends INavigationNodeListener<IModuleNode, ISubModuleNode> {

	/**
	 * This method is called when the flag that decides, if a single sub module
	 * node is displayed in the module view, has changed.
	 * 
	 * @param source
	 *            the node whose flag that decides to display single nodes has
	 *            changed
	 */
	void presentSingleSubModuleChanged(IModuleNode source);

}
