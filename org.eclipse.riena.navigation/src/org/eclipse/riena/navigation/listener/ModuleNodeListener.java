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
package org.eclipse.riena.navigation.listener;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubModuleNode;

/**
 * Default implementation for the IModuleGroupNodeListener
 */
public class ModuleNodeListener extends NavigationNodeListener<IModuleNode, ISubModuleNode> implements
		IModuleNodeListener {

	public void presentSingleSubModuleChanged(final IModuleNode source) {

	}

}
