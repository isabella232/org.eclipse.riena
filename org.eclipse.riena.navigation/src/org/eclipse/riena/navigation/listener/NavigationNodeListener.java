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
package org.eclipse.riena.navigation.listener;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNode.State;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.filter.IUIFilter;

/**
 * Default implementation for the INavigationNodeListener
 * 
 * @param <S>
 *            the type of the node to observe
 * @param <C>
 *            the type of the child nodes
 */
public class NavigationNodeListener<S extends INavigationNode<C>, C extends INavigationNode<?>> implements
		INavigationNodeListener<S, C> {

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#activated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void activated(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#beforeActivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void beforeActivated(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#afterActivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void afterActivated(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#disposed(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void disposed(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#beforeDisposed(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void beforeDisposed(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#afterDisposed(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void afterDisposed(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#childAdded(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNode)
	 */
	public void childAdded(final S source, final C childAdded) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#childRemoved(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNode)
	 */
	public void childRemoved(final S source, final C childRemoved) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#deactivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void deactivated(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#beforeDeactivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void beforeDeactivated(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#afterDeactivated(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void afterDeactivated(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#expandedChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void expandedChanged(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#labelChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void labelChanged(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#iconChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void iconChanged(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#markerChanged(org.eclipse.riena.navigation.INavigationNode,
	 *      IMarker)
	 */
	public void markerChanged(final S source, final IMarker marker) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#parentChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void parentChanged(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#presentationChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void presentationChanged(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#selectedChildChanged(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void selectedChanged(final S source) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#stateChanged(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNode.State,
	 *      org.eclipse.riena.navigation.INavigationNode.State)
	 */
	public void stateChanged(final S source, final State oldState, final State newState) {
	}

	/**
	 * @see org.eclipse.riena.navigation.listener.INavigationNodeListener#block(org.eclipse.riena.navigation.INavigationNode,
	 *      boolean)
	 */
	public void block(final S source, final boolean block) {

	}

	/**
	 * {@inheritDoc}
	 */
	public void filterAdded(final S source, final IUIFilter filter) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void filterRemoved(final S source, final IUIFilter filter) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void prepared(final S source) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.0
	 */
	public void nodeIdChange(final S source, final NavigationNodeId oldId, final NavigationNodeId newId) {
	}
}
