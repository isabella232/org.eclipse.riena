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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.INavigationNode;

/**
 * View (UI representation) of a navigation node.
 * 
 * @param <N>
 *            implementation the navigation node
 */
public interface INavigationNodeView<N extends INavigationNode<?>> {

	/**
	 * Binds the navigation node to the view. Creates the widgets and the
	 * controller if necessary.
	 * 
	 * @param node
	 *            the node to bind.
	 */
	void bind(N node);

	/**
	 * Unbinds the navigation node from the view.
	 */
	void unbind();

	/**
	 * Returns the navigation node of this view.
	 * 
	 * @return navigation node
	 */
	N getNavigationNode();

	/**
	 * Adds the given listener.
	 * 
	 * @param listener
	 *            listener that will be added
	 */
	void addUpdateListener(IComponentUpdateListener listener);

}
