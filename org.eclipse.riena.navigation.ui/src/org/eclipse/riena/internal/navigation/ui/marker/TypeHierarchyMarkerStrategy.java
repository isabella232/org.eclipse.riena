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
package org.eclipse.riena.internal.navigation.ui.marker;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISimpleNavigationNodeListener;
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter;

/**
 * This implementation of {@link IUIProcessMarkupStrategy} adds an
 * {@link IMarker} per node hierarchy layer. (ISubModuleNode, IModuleNode,
 * IModuleGroupNode, ISubApplication)
 */
public class TypeHierarchyMarkerStrategy implements IUIProcessMarkupStrategy {

	// the uiprocess finished marker
	private IMarker marker;
	private final ISimpleNavigationNodeListener nodeObserver;

	public TypeHierarchyMarkerStrategy() {
		this.nodeObserver = new NodeObserver();
	}

	public void applyUIProcessMarker(final INavigationNode<?> baseNode, final IMarker marker) {
		// save marker for later reuse on activate of marked nodes
		this.marker = marker;
		INavigationNode<?> node = baseNode;
		final Set<Class<?>> markedTyped = new HashSet<Class<?>>();
		while (node != null && node.isDeactivated()) {
			if (!nodeTypeMarked(markedTyped, node.getClass())) {
				addNavigationNodeMarker(node);
				observeNodeActivity(node);
				markedTyped.add(node.getClass());
			}
			node = node.getParent();
		}
	}

	/*
	 * check if the hierarchy layer is already marked
	 */
	boolean nodeTypeMarked(final Set<Class<?>> markedTypes, final Class<?> nodeType) {
		for (final Class<?> markedType : markedTypes) {
			if (markedType.isAssignableFrom(nodeType) || nodeType.isAssignableFrom(markedType)) {
				return true;
			}
		}
		// not in hierarchy yet
		return false;
	}

	/*
	 * add the marker to the node
	 */
	void addNavigationNodeMarker(final INavigationNode<?> node) {
		node.addMarker(marker);
	}

	/*
	 * observer the node for later removal of the marker
	 */
	private void observeNodeActivity(final INavigationNode<?> node) {
		node.addSimpleListener(nodeObserver);
	}

	/*
	 * listener just forwarding to removeNavigationNodeMarker when node is
	 * activated
	 */
	class NodeObserver extends SimpleNavigationNodeAdapter {

		@SuppressWarnings("rawtypes")
		@Override
		public void activated(final INavigationNode source) {
			removeNavigationNodeMarker(source);
		}
	}

	@SuppressWarnings("rawtypes")
	private void removeNavigationNodeMarker(final INavigationNode source) {
		source.removeMarker(marker);
		// remove observer and let gc work
		source.removeSimpleListener(nodeObserver);
	}

}
