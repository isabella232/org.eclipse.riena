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
import org.eclipse.riena.navigation.listener.ISubModuleNodeListener;

/**
 * A Node containing other sub sub module nodes.
 */
public interface ISubModuleNode extends INavigationNode<ISubModuleNode>,
		INavigationNodeListenerable<ISubModuleNode, ISubModuleNode, ISubModuleNodeListener> {

	/**
	 * Indicates whether this Node is selectable or not. If this has children
	 * and is not selectable, then the first child will be selected in
	 * navigation.
	 * 
	 * @return {@code true} if selectable, otherwise {@code false}
	 * @since 1.2
	 */
	boolean isSelectable();

	/**
	 * Sets the selectable-state of this Node.
	 * 
	 * @param selectable
	 *            {@code true} if selectable, otherwise {@code false}
	 * @since 1.2
	 */
	void setSelectable(boolean selectable);

	/**
	 * Returns whether the tree should be closed on a navigation event. This
	 * only works if
	 * <code>LnfKeyConstants.SUB_MODULE_TREE_SHOW_ONE_SUB_TREE</code> is set to
	 * true.
	 * 
	 * @since 3.0
	 */
	boolean isCloseSubTree();

	/**
	 * Sets whether the tree should be closed on a navigation event. This only
	 * works if <code>LnfKeyConstants.SUB_MODULE_TREE_SHOW_ONE_SUB_TREE</code>
	 * is set to true.
	 * 
	 * @since 3.0
	 */
	void setCloseSubTree(boolean close);

	/**
	 * Indicates whether the user should have an opportunity to close the
	 * subModule.
	 * 
	 * @return {@code true} if the subModule should never be closed; by default
	 *         is {@code false}
	 * @since 3.0
	 */
	boolean isClosable();

	/**
	 * Sets whether the user should have an opportunity to close the subModule.
	 * 
	 * @param closable
	 *            {@code false} if the subModule should never be closed
	 * @since 3.0
	 */
	void setClosable(boolean closable);

}
