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
package org.eclipse.riena.navigation;

import org.eclipse.riena.navigation.listener.INavigationNodeListenerable;
import org.eclipse.riena.navigation.listener.ISubApplicationNodeListener;
import org.eclipse.riena.navigation.model.SubApplicationNode;

/**
 * Represents a business area within the desktop.
 * 
 * @see SubApplicationNode
 */
public interface ISubApplicationNode extends INavigationNode<IModuleGroupNode>,
		INavigationNodeListenerable<ISubApplicationNode, IModuleGroupNode, ISubApplicationNodeListener> {

}
