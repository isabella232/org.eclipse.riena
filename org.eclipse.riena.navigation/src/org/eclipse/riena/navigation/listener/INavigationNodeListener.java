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

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNode.State;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.filter.IUIFilter;

/**
 * Each call back includes as the first parameter always the source of the
 * change, even if the source is sometimes not needed
 * 
 * @param <S>
 *            the type of implemented node
 * @param <C>
 *            the type of child nodes
 */
public interface INavigationNodeListener<S extends INavigationNode<C>, C extends INavigationNode<?>> {

	/**
	 * This method is called when the label has changed.
	 * 
	 * @param source
	 *            the node whose label has changed
	 */
	void labelChanged(S source);

	/**
	 * This method is called when the icon has changed.
	 * 
	 * @param source
	 *            the node whose icon has changed
	 */
	void iconChanged(S source);

	/**
	 * This method is called when the selection has changed.
	 * 
	 * @param source
	 *            the node whose selection has changed
	 */
	void selectedChanged(S source);

	/**
	 * This method is called when a child has been added.
	 * 
	 * @param source
	 *            the node to which the child was added
	 * @param childAdded
	 *            child that has been added
	 */
	void childAdded(S source, C childAdded);

	/**
	 * This method is called when a child has been removed.
	 * 
	 * @param source
	 *            the node from which the child was removed
	 * @param childRemoved
	 *            child that has been removed
	 */
	void childRemoved(S source, C childRemoved);

	/**
	 * This method is called when the presentation (e.g. controller) of the node
	 * has changed.
	 * 
	 * @param source
	 *            the node whose presentation has changed
	 */
	void presentationChanged(S source);

	/**
	 * This method is called when the parent has changed.
	 * 
	 * @param source
	 *            the node whose parent has changed
	 */
	void parentChanged(S source);

	/**
	 * This method is called when a node (e.g. a sub module node) has been
	 * expanded or collapsed.
	 * 
	 * @param source
	 *            the node which has been expanded or collapsed
	 */
	void expandedChanged(S source);

	/**
	 * This method is called if a marker was added to or removed from a node.
	 * 
	 * @param source
	 *            node
	 * @param marker
	 *            marker that was removed or added
	 */
	void markerChanged(S source, IMarker marker);

	/**
	 * This method is called when the node has been activated.
	 * 
	 * @param source
	 *            the node which has been activated
	 */
	void activated(S source);

	/**
	 * This method is called before the node will be activated.
	 * 
	 * @param source
	 *            the node which will be activated
	 */
	void beforeActivated(S source);

	/**
	 * This method is called after the node has been activated.
	 * 
	 * @param source
	 *            the node which has been activated.
	 */
	void afterActivated(S source);

	/**
	 * This method is called when the node has been deactivated.
	 * 
	 * @param source
	 *            the node which has been deactivated
	 */
	void deactivated(S source);

	/**
	 * This method is called before the node will be deactivated.
	 * 
	 * @param source
	 *            the node which will be deactivated
	 */
	void beforeDeactivated(S source);

	/**
	 * This method is called after the node has been deactivated.
	 * 
	 * @param source
	 *            the node which has been deactivated.
	 */
	void afterDeactivated(S source);

	/**
	 * This method is called after the node has been disposed.
	 * 
	 * @param source
	 *            the node which has been disposed.
	 */
	void disposed(S source);

	/**
	 * This method is called after the node has been disposed.
	 * 
	 * @param source
	 *            the node which has been disposed.
	 */
	void beforeDisposed(S source);

	/**
	 * This method is called after the node has been disposed.
	 * 
	 * @param source
	 *            the node which has been disposed.
	 */
	void afterDisposed(S source);

	/**
	 * This method is called when the state of the node has changed.
	 * 
	 * @param source
	 *            the node whose state has changed
	 * @param oldState
	 *            old state of the node
	 * @param newState
	 *            new state of the node
	 */
	void stateChanged(S source, State oldState, State newState);

	/**
	 * This method is called when the node is blocked or unblocked.
	 * 
	 * @param source
	 *            the node which has been blocked or unblocked
	 * @param block
	 *            {@code true} if the node has been blocked; otherwise
	 *            {@code false}
	 */
	void block(S source, boolean block);

	/**
	 * This method is called when a filter was added to the given node.
	 * 
	 * @param source
	 *            the node to which a UI filter has been added
	 * @param filter
	 *            new added filter
	 */
	void filterAdded(S source, IUIFilter filter);

	/**
	 * This method is called when a filter was removed from the given node.
	 * 
	 * @param source
	 *            the node from which a UI filter has been removed
	 * @param filter
	 *            removed filter
	 */
	void filterRemoved(S source, IUIFilter filter);

	/**
	 * This method is called when the node has been prepared.
	 * 
	 * @param source
	 *            the node which has been prepared
	 * @since 2.0
	 */
	void prepared(S source);

	/**
	 * This method is called when the node id has changed.
	 * 
	 * @param source
	 *            the node
	 * @param newId
	 *            the new node id
	 * @since 3.0
	 */
	void nodeIdChange(S source, NavigationNodeId oldId, NavigationNodeId newId);
}
