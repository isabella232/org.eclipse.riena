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

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;

/**
 * Special listener for the module group node
 */
public interface IModuleGroupNodeListener extends INavigationNodeListener<IModuleGroupNode, IModuleNode> {

	/**
	 * This method is called when the flag that decides, if a module group with
	 * a single module node is displayed, has changed.
	 * 
	 * @param source
	 *            the node whose flag that decides to module group with single
	 *            module has changed
	 */
	void presentWithSingleModuleChanged(IModuleGroupNode source);

}
