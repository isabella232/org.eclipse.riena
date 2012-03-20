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
package org.eclipse.riena.navigation;

import java.util.List;

/**
 * Describes a Context in which a node is activated, deactivated or disposed.
 * The activation context knows all nodes which have to be activated and
 * deactivated while a selected node has to be activated. The INavigationContext
 * is created by a NavigationProcessor, which decides which nodes are activated
 * an which.
 */
public interface INavigationContext {

	/**
	 * Returns a list of nodes which have to be deactivated.
	 * 
	 * @return ordered list of nodes to deactivated
	 */
	List<INavigationNode<?>> getToDeactivate();

	/**
	 * Returns a list of nodes which have to be activated.
	 * 
	 * @return ordered list of nodes to activated
	 */
	List<INavigationNode<?>> getToActivate();

	/**
	 * Returns a list of nodes which have to prepared.
	 * 
	 * @return ordered list of nodes to prepared
	 */
	List<INavigationNode<?>> getToPrepare();

}
