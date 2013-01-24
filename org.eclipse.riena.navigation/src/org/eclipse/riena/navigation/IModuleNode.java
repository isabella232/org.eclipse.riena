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
package org.eclipse.riena.navigation;

import org.eclipse.riena.navigation.listener.IModuleNodeListener;
import org.eclipse.riena.navigation.listener.INavigationNodeListenerable;

/**
 * This is a node containing sub module nodes.
 */
public interface IModuleNode extends INavigationNode<ISubModuleNode>,
		INavigationNodeListenerable<IModuleNode, ISubModuleNode, IModuleNodeListener> {

	/**
	 * Returns whether a single sub module should be shown.
	 * 
	 * @return {@code true} if a single sub module should be shown; otherwise
	 *         {@code false}
	 */
	boolean isPresentSingleSubModule();

	/**
	 * Sets whether a single sub module should be shown.
	 * 
	 * @param present
	 *            {@code true} if a single sub module should be shown; otherwise
	 *            {@code false}
	 */
	void setPresentSingleSubModule(boolean present);

	/**
	 * Returns {@code true}, if the module has more than one sub module or the
	 * flag present single submodule is {@code true}.
	 * 
	 * @return {@code true} if the submodule should be presented; otherwise
	 *         {@code false}
	 */
	boolean isPresentSubModules();

	/**
	 * Calculates the number of the visible and expanded children.
	 * 
	 * @return number of children
	 */
	int calcDepth();

	/**
	 * Indicates whether the user should have an opportunity to close the
	 * module.
	 * 
	 * @return {@code false} if the module should never be closed; by default is
	 *         {@code true}
	 */
	boolean isClosable();

	/**
	 * Sets whether the user should have an opportunity to close the module.
	 * 
	 * @param closable
	 *            {@code false} if the module should never be closed
	 */
	void setClosable(boolean closable);

}
