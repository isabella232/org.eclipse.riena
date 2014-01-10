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

import org.eclipse.riena.navigation.listener.IModuleGroupNodeListener;
import org.eclipse.riena.navigation.listener.INavigationNodeListenerable;

/**
 * This is a node containing module nodes.
 */
public interface IModuleGroupNode extends INavigationNode<IModuleNode>,
		INavigationNodeListenerable<IModuleGroupNode, IModuleNode, IModuleGroupNodeListener> {

	/**
	 * Returns whether a group with a single module should be present.
	 * <p>
	 * <i>At the moment no implementation makes a different between a group with
	 * a single module or a couple of modules. All module groups have always a
	 * border. (The default value is {@code true})</i>
	 * <p>
	 * <i>A possible alternative implementation can be that a group with only
	 * one child module has no border and a group with a couple of modules has a
	 * border. </i>
	 * 
	 * @return {@code true}, if the group should be presented with a single
	 *         module contained; otherwise {@code false}
	 */
	boolean isPresentWithSingleModule();

	/**
	 * Sets whether a group with a single module should be present.
	 * 
	 * @param pPresentWithSingleModule
	 *            {@code true}, if the group should be presented with a single
	 *            module contained; otherwise {@code false}
	 */
	void setPresentWithSingleModule(boolean pPresentWithSingleModule);

	/**
	 * The method answers {@code true} if more than one module is contained or
	 * the flag PresentWithSingleModule is set to {@code true}
	 * 
	 * @return {@code true}, if this group node should be explicitly presented
	 *         to the user; otherwise {@code false}
	 */
	boolean isPresentGroupNode();

}
