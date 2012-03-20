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
package org.eclipse.riena.security.ui.filter;

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISimpleNavigationNodeListener;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.ModuleGroupNodeListener;
import org.eclipse.riena.navigation.listener.ModuleNodeListener;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubApplicationNodeListener;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;

/**
 * Convenience class observing the application model and delegating events to
 * the given {@link ISimpleNavigationNodeListener}
 * 
 */
public class NodeStructureObserver {

	private final ISimpleNavigationNodeListener simpleListener;
	private NavigationTreeObserver treeObserver;
	private final IApplicationNode applicationNode;

	public NodeStructureObserver(final IApplicationNode applicationNode,
			final ISimpleNavigationNodeListener simpleListener) {
		this.applicationNode = applicationNode;
		this.simpleListener = simpleListener;
	}

	/**
	 * Starts the observation of the application model
	 */
	public void start() {
		treeObserver = new NavigationTreeObserver();
		treeObserver.addListener(new SubModuleListener());
		treeObserver.addListener(new ModuleListener());
		treeObserver.addListener(new ModuleGroupListener());
		treeObserver.addListener(new SubApplicationListener());
		treeObserver.addListenerTo(applicationNode);
	}

	/**
	 * Stops the observation of the application model
	 */
	public void stop() {
		treeObserver.removeListenerFrom(applicationNode);
	}

	private void notifyAdded(final INavigationNode<?> source, final INavigationNode<?> node) {
		simpleListener.childAdded(source, node);
	}

	private void notifyRemoved(final INavigationNode<?> source, final INavigationNode<?> node) {
		simpleListener.childRemoved(source, node);
	}

	////Delegating listeners

	private class SubModuleListener extends SubModuleNodeListener {

		@Override
		public void childAdded(final ISubModuleNode source, final ISubModuleNode childAdded) {
			notifyAdded(source, childAdded);
		}

		@Override
		public void childRemoved(final ISubModuleNode source, final ISubModuleNode childRemoved) {
			notifyRemoved(source, childRemoved);
		}

	}

	private class ModuleListener extends ModuleNodeListener {

		@Override
		public void childAdded(final IModuleNode source, final ISubModuleNode childAdded) {
			notifyAdded(source, childAdded);
		}

		@Override
		public void childRemoved(final IModuleNode source, final ISubModuleNode childRemoved) {
			notifyRemoved(source, childRemoved);
		};

	}

	private class ModuleGroupListener extends ModuleGroupNodeListener {

		@Override
		public void childAdded(final org.eclipse.riena.navigation.IModuleGroupNode source, final IModuleNode childAdded) {
			notifyAdded(source, childAdded);
		};

		@Override
		public void childRemoved(final IModuleGroupNode source, final IModuleNode childRemoved) {
			notifyRemoved(source, childRemoved);
		}
	}

	private class SubApplicationListener extends SubApplicationNodeListener {

		@Override
		public void childAdded(final org.eclipse.riena.navigation.ISubApplicationNode source,
				final IModuleGroupNode childAdded) {
			notifyAdded(source, childAdded);
		};

		@Override
		public void childRemoved(final org.eclipse.riena.navigation.ISubApplicationNode source,
				final IModuleGroupNode childRemoved) {
			notifyRemoved(source, childRemoved);
		};

	}

}
