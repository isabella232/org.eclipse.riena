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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNode.State;
import org.eclipse.riena.navigation.ISimpleNavigationNodeListener;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.filter.IUIFilter;

/**
 * This adapter provides an empty implemenentation of all methods declared in
 * {@link ISimpleNavigationNodeListener}.
 * <p>
 * Implementors wishing to implement this interface should extend this class and
 * override only the methods they need.
 */
public class SimpleNavigationNodeAdapter implements ISimpleNavigationNodeListener {

	public void activated(final INavigationNode<?> source) {
	}

	public void afterActivated(final INavigationNode<?> source) {
	}

	public void afterDeactivated(final INavigationNode<?> source) {
	}

	public void afterDisposed(final INavigationNode<?> source) {
	}

	public void beforeActivated(final INavigationNode<?> source) {
	}

	public void beforeDeactivated(final INavigationNode<?> source) {
	}

	public void beforeDisposed(final INavigationNode<?> source) {
	}

	public void block(final INavigationNode<?> source, final boolean block) {
	}

	public void childAdded(final INavigationNode<?> source, final INavigationNode<?> childAdded) {
	}

	public void childRemoved(final INavigationNode<?> source, final INavigationNode<?> childRemoved) {
	}

	public void deactivated(final INavigationNode<?> source) {
	}

	public void disposed(final INavigationNode<?> source) {
	}

	public void expandedChanged(final INavigationNode<?> source) {
	}

	public void iconChanged(final INavigationNode<?> source) {
	}

	public void labelChanged(final INavigationNode<?> source) {
	}

	public void markerChanged(final INavigationNode<?> source, final IMarker marker) {
	}

	public void parentChanged(final INavigationNode<?> source) {
	}

	public void presentationChanged(final INavigationNode<?> source) {
	}

	public void selectedChanged(final INavigationNode<?> source) {
	}

	public void stateChanged(final INavigationNode<?> source, final State oldState, final State newState) {
	}

	public void filterAdded(final INavigationNode<?> source, final IUIFilter filter) {
	}

	public void filterRemoved(final INavigationNode<?> source, final IUIFilter filter) {
	}

	public void prepared(final INavigationNode<?> source) {
	}

	/**
	 * @since 3.0
	 */
	public void nodeIdChange(final INavigationNode<?> source, final NavigationNodeId oldId, final NavigationNodeId newId) {
	}
}
