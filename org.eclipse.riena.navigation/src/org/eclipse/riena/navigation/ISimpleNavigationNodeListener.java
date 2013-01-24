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

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode.State;
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter;
import org.eclipse.riena.ui.filter.IUIFilter;

/**
 * Listener for various events related to a navigation node and it's lifecycle.
 * <p>
 * Because this interface has many methods, but implementors are typically only
 * interested in a few of them, it is is recommended to extend the
 * {@link SimpleNavigationNodeAdapter}.
 * 
 * @see SimpleNavigationNodeAdapter
 */
public interface ISimpleNavigationNodeListener {

	/**
	 * This method is called when the label has changed.
	 * 
	 * @param source
	 *            the node whose label has changed
	 */
	void labelChanged(INavigationNode<?> source);

	/**
	 * This method is called when the icon has changed.
	 * 
	 * @param source
	 *            the node whose icon has changed
	 */
	void iconChanged(INavigationNode<?> source);

	/**
	 * This method is called when the selection has changed.
	 * 
	 * @param source
	 *            the node whose selection has changed
	 */
	void selectedChanged(INavigationNode<?> source);

	/**
	 * This method is called when a child has been added.
	 * 
	 * @param source
	 *            the node to which the child was added
	 * @param childAdded
	 *            child that has been added
	 */
	void childAdded(INavigationNode<?> source, INavigationNode<?> childAdded);

	/**
	 * This method is called when a child has been removed.
	 * 
	 * @param source
	 *            the node from which the child was removed
	 * @param childRemoved
	 *            child that has been removed
	 */
	void childRemoved(INavigationNode<?> source, INavigationNode<?> childRemoved);

	/**
	 * This method is called when the presentation (e.g. controller) of the node
	 * has changed.
	 * 
	 * @param source
	 *            the node whose presentation has changed
	 */
	void presentationChanged(INavigationNode<?> source);

	/**
	 * This method is called when the parent has changed.
	 * 
	 * @param source
	 *            the node whose parent has changed
	 */
	void parentChanged(INavigationNode<?> source);

	/**
	 * This method is called when a node (e.g. a sub module node) has been
	 * expanded or collapsed.
	 * 
	 * @param source
	 *            the node which has been expanded or collapsed
	 */
	void expandedChanged(INavigationNode<?> source);

	/**
	 * This method is called when a marker was added to or removed from a node.
	 * 
	 * @param source
	 *            node
	 * @param marker
	 *            marker that was removed or added
	 */
	void markerChanged(INavigationNode<?> source, IMarker marker);

	/**
	 * This method is called when the node has been activated.
	 * 
	 * @param source
	 *            the node which has been activated
	 */
	void activated(INavigationNode<?> source);

	/**
	 * This method is called before the node will be activated.
	 * 
	 * @param source
	 *            the node which will be activated
	 */
	void beforeActivated(INavigationNode<?> source);

	/**
	 * This method is called after the node has been activated.
	 * 
	 * @param source
	 *            the node which has been activated.
	 */
	void afterActivated(INavigationNode<?> source);

	/**
	 * This method is called when the node has been deactivated.
	 * 
	 * @param source
	 *            the node which has been deactivated
	 */
	void deactivated(INavigationNode<?> source);

	/**
	 * This method is called before the node will be deactivated.
	 * 
	 * @param source
	 *            the node which will be deactivated
	 */
	void beforeDeactivated(INavigationNode<?> source);

	/**
	 * This method is called after the node has been deactivated.
	 * 
	 * @param source
	 *            the node which has been deactivated.
	 */
	void afterDeactivated(INavigationNode<?> source);

	/**
	 * This method is called after the node has been disposed.
	 * 
	 * @param source
	 *            the node which has been disposed.
	 */
	void disposed(INavigationNode<?> source);

	/**
	 * This method is called after the node has been disposed.
	 * 
	 * @param source
	 *            the node which has been disposed.
	 */
	void beforeDisposed(INavigationNode<?> source);

	/**
	 * This method is called after the node has been disposed.
	 * 
	 * @param source
	 *            the node which has been disposed.
	 */
	void afterDisposed(INavigationNode<?> source);

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
	void stateChanged(INavigationNode<?> source, State oldState, State newState);

	/**
	 * This method is called when the node is blocked or unblocked.
	 * 
	 * @param source
	 *            the node which has been blocked or unblocked
	 * @param block
	 *            {@code true} if the node has been blocked; otherwise
	 *            {@code false}
	 */
	void block(INavigationNode<?> source, boolean block);

	/**
	 * This method is called when a UI filter has been added.
	 * 
	 * @param source
	 *            the node to which a UI filter has been added
	 * @param filter
	 *            the UI filter that has been added
	 */
	void filterAdded(INavigationNode<?> source, IUIFilter filter);

	/**
	 * This method is called when a UI filter has been removed.
	 * 
	 * @param source
	 *            the node from which a UI filter has been removed
	 * @param filter
	 *            the UI filter that has been removed
	 */
	void filterRemoved(INavigationNode<?> source, IUIFilter filter);

	/**
	 * This method is called when the node has been prepared.
	 * 
	 * @param source
	 *            the node which has been prepared
	 * @since 2.0
	 */
	void prepared(INavigationNode<?> source);

	/**
	 * This method is called when the node id has changed.
	 * 
	 * @param source
	 *            the node
	 * @param newId
	 *            the new node id
	 * @since 3.0
	 */
	void nodeIdChange(INavigationNode<?> source, NavigationNodeId oldId, NavigationNodeId newId);
}
