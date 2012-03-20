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

import java.beans.PropertyChangeListener;

import org.eclipse.riena.navigation.INavigationNode;

/**
 * Describes the ability of a navigation node to carry a listener. The ability
 * is not a part of the INavigationNode, because there are nodes which cannot be
 * listened to.
 * 
 * @param <S>
 *            the type of implemented node
 * @param <C>
 *            the type of the child nodes
 * @param <L>
 *            the type of the listener
 */
public interface INavigationNodeListenerable<S extends INavigationNode<C>, C extends INavigationNode<?>, L extends INavigationNodeListener<S, C>> {

	/**
	 * A change event with this id indicates a change in the list of children
	 * (value is "children").
	 * 
	 * @see INavigationNode#getChildren()
	 */
	String PROPERTY_CHILDREN = "children"; //$NON-NLS-1$

	/**
	 * Adds the given listener.
	 * 
	 * @param pListener
	 *            listener that will be added
	 */
	void addListener(L pListener);

	/**
	 * Removes the given listener.
	 * 
	 * @param pListener
	 *            listener that will be removed
	 */
	void removeListener(L pListener);

	/**
	 * Adds the given {@code PropertyChangeListener}.
	 * 
	 * @param propertyChangeListener
	 *            listener that will be added
	 */
	void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Removes the given {@code PropertyChangeListener}.
	 * 
	 * @param propertyChangeListener
	 *            listener that will be removes
	 */
	void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

}
