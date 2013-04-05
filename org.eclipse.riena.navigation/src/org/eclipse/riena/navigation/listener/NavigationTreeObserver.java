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
package org.eclipse.riena.navigation.listener;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode.State;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.filter.IUIFilter;

/**
 * listens on a sub tree of a passed node and fires the changes on all elements.
 * All added and remove children are automatically listened on
 */
public class NavigationTreeObserver {

	private final IApplicationNodeListener applicationNodeListener;
	private final Set<IApplicationNodeListener> applicationNodeListeners;
	private final ISubApplicationNodeListener subApplicationListener;
	private final Set<ISubApplicationNodeListener> subApplicationListeners;
	private final IModuleGroupNodeListener moduleGroupNodeListener;
	private final Set<IModuleGroupNodeListener> moduleGroupNodeListeners;
	private final IModuleNodeListener moduleNodeListener;
	private final Set<IModuleNodeListener> moduleNodeListeners;
	private final ISubModuleNodeListener subModuleNodeListener;
	private final Set<ISubModuleNodeListener> subModuleNodeListeners;

	/**
	 * creates the instance
	 */
	public NavigationTreeObserver() {
		super();
		applicationNodeListener = new MyApplicationNodeListener();
		applicationNodeListeners = new HashSet<IApplicationNodeListener>();
		subApplicationListener = new MySubApplicationNodeListener();
		subApplicationListeners = new HashSet<ISubApplicationNodeListener>();
		moduleGroupNodeListener = new MyModuleGroupNodeListener();
		moduleGroupNodeListeners = new HashSet<IModuleGroupNodeListener>();
		moduleNodeListener = new MyModuleNodeListener();
		moduleNodeListeners = new HashSet<IModuleNodeListener>();
		subModuleNodeListener = new MySubModuleNodeListener();
		subModuleNodeListeners = new HashSet<ISubModuleNodeListener>();
	}

	/**
	 * Add the passed listener
	 * 
	 * @param pListener
	 *            the listener to add
	 */
	public void addListener(final IApplicationNodeListener pListener) {
		applicationNodeListeners.add(pListener);
	}

	/**
	 * Remove the passed listener
	 * 
	 * @param pListener
	 *            the listener to remove
	 */
	public void removeListener(final IApplicationNodeListener pListener) {
		applicationNodeListeners.remove(pListener);
	}

	/**
	 * Add the passed listener
	 * 
	 * @param pListener
	 *            the listener to add
	 */
	public void addListener(final ISubApplicationNodeListener pListener) {
		subApplicationListeners.add(pListener);
	}

	/**
	 * Remove the passed listener
	 * 
	 * @param pListener
	 *            the listener to remove
	 */
	public void removeListener(final ISubApplicationNodeListener pListener) {
		subApplicationListeners.remove(pListener);
	}

	/**
	 * Add the passed listener
	 * 
	 * @param pListener
	 *            the listener to add
	 */
	public void addListener(final IModuleGroupNodeListener pListener) {
		moduleGroupNodeListeners.add(pListener);
	}

	/**
	 * Remove the passed listener
	 * 
	 * @param pListener
	 *            the listener to remove
	 */
	public void removeListener(final IModuleGroupNodeListener pListener) {
		moduleGroupNodeListeners.remove(pListener);
	}

	/**
	 * Add the passed listener
	 * 
	 * @param pListener
	 *            the listener to add
	 */
	public void addListener(final IModuleNodeListener pListener) {
		moduleNodeListeners.add(pListener);
	}

	/**
	 * Remove the passed listener
	 * 
	 * @param pListener
	 *            the listener to remove
	 */
	public void removeListener(final IModuleNodeListener pListener) {
		moduleNodeListeners.remove(pListener);
	}

	/**
	 * Add the passed listener
	 * 
	 * @param pListener
	 *            the listener to add
	 */
	public void addListener(final ISubModuleNodeListener pListener) {
		subModuleNodeListeners.add(pListener);
	}

	/**
	 * Remove the passed listener
	 * 
	 * @param pListener
	 *            the listener to remove
	 */
	public void removeListener(final ISubModuleNodeListener pListener) {
		subModuleNodeListeners.remove(pListener);
	}

	private Set<IApplicationNodeListener> getApplicationNodeListeners() {
		return applicationNodeListeners;
	}

	private Set<ISubApplicationNodeListener> getSubApplicationListeners() {
		return subApplicationListeners;
	}

	private Set<IModuleGroupNodeListener> getModuleGroupNodeListeners() {
		return moduleGroupNodeListeners;
	}

	private Set<IModuleNodeListener> getModuleNodeListeners() {
		return moduleNodeListeners;
	}

	private Set<ISubModuleNodeListener> getSubModuleNodeListeners() {
		return subModuleNodeListeners;
	}

	public void addListenerTo(final IApplicationNode pApplicationNode) {
		pApplicationNode.addListener(applicationNodeListener);
		for (final ISubApplicationNode next : pApplicationNode.getChildren()) {
			addListenerTo(next);
		}
	}

	public void addListenerTo(final ISubApplicationNode pSubApplication) {
		pSubApplication.addListener(subApplicationListener);
		for (final IModuleGroupNode next : pSubApplication.getChildren()) {
			addListenerTo(next);
		}
	}

	public void addListenerTo(final IModuleGroupNode pModuleGroupNode) {
		pModuleGroupNode.addListener(moduleGroupNodeListener);
		for (final IModuleNode next : pModuleGroupNode.getChildren()) {
			addListenerTo(next);
		}
	}

	public void addListenerTo(final IModuleNode pModuleNode) {
		pModuleNode.addListener(moduleNodeListener);
		for (final ISubModuleNode next : pModuleNode.getChildren()) {
			addListenerTo(next);
		}
	}

	public void addListenerTo(final ISubModuleNode pSubModuleNode) {
		pSubModuleNode.addListener(subModuleNodeListener);
		for (final ISubModuleNode next : pSubModuleNode.getChildren()) {
			addListenerTo(next);
		}
	}

	public void removeListenerFrom(final IApplicationNode pApplicationNode) {
		for (final ISubApplicationNode next : pApplicationNode.getChildren()) {
			removeListenerFrom(next);
		}
		pApplicationNode.removeListener(applicationNodeListener);
	}

	public void removeListenerFrom(final ISubApplicationNode pSubApplication) {
		for (final IModuleGroupNode next : pSubApplication.getChildren()) {
			removeListenerFrom(next);
		}
		pSubApplication.removeListener(subApplicationListener);
	}

	public void removeListenerFrom(final IModuleGroupNode pModuleGroupNode) {
		for (final IModuleNode next : pModuleGroupNode.getChildren()) {
			removeListenerFrom(next);
		}
		pModuleGroupNode.removeListener(moduleGroupNodeListener);
	}

	public void removeListenerFrom(final IModuleNode pModuleNode) {
		for (final ISubModuleNode next : pModuleNode.getChildren()) {
			removeListenerFrom(next);
		}
		pModuleNode.removeListener(moduleNodeListener);
	}

	public void removeListenerFrom(final ISubModuleNode pSubModuleNode) {
		for (final ISubModuleNode next : pSubModuleNode.getChildren()) {
			removeListenerFrom(next);
		}
		pSubModuleNode.removeListener(subModuleNodeListener);
	}

	private class MyApplicationNodeListener extends ApplicationNodeListener {

		@Override
		public void filterAdded(final IApplicationNode source, final IUIFilter filter) {
			super.filterAdded(source, filter);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.filterAdded(source, filter);
			}
		}

		@Override
		public void filterRemoved(final IApplicationNode source, final IUIFilter filter) {
			super.filterRemoved(source, filter);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.filterRemoved(source, filter);
			}
		}

		@Override
		public void activated(final IApplicationNode source) {
			super.activated(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.activated(source);
			}
		}

		@Override
		public void beforeActivated(final IApplicationNode source) {
			super.beforeActivated(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.beforeActivated(source);
			}
		}

		@Override
		public void block(final IApplicationNode source, final boolean block) {

			super.block(source, block);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.block(source, block);
			}
		}

		@Override
		public void afterActivated(final IApplicationNode source) {
			super.afterActivated(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.afterActivated(source);
			}
		}

		@Override
		public void deactivated(final IApplicationNode source) {
			super.deactivated(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.deactivated(source);
			}
		}

		@Override
		public void beforeDeactivated(final IApplicationNode source) {
			super.beforeDeactivated(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.beforeDeactivated(source);
			}
		}

		@Override
		public void afterDeactivated(final IApplicationNode source) {
			super.afterDeactivated(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.afterDeactivated(source);
			}
		}

		@Override
		public void disposed(final IApplicationNode source) {
			super.disposed(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.disposed(source);
			}
		}

		@Override
		public void beforeDisposed(final IApplicationNode source) {
			super.beforeDisposed(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.beforeDisposed(source);
			}
		}

		@Override
		public void afterDisposed(final IApplicationNode source) {
			super.afterDisposed(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.afterDisposed(source);
			}
		}

		@Override
		public void childAdded(final IApplicationNode source, final ISubApplicationNode childAdded) {
			super.childAdded(source, childAdded);
			addListenerTo(childAdded);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.childAdded(source, childAdded);
			}
		}

		@Override
		public void childRemoved(final IApplicationNode source, final ISubApplicationNode childRemoved) {
			super.childRemoved(source, childRemoved);
			removeListenerFrom(childRemoved);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.childRemoved(source, childRemoved);
			}
		}

		@Override
		public void expandedChanged(final IApplicationNode source) {
			super.expandedChanged(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.expandedChanged(source);
			}
		}

		@Override
		public void labelChanged(final IApplicationNode source) {
			super.labelChanged(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.labelChanged(source);
			}
		}

		@Override
		public void markerChanged(final IApplicationNode source, final IMarker marker) {
			super.markerChanged(source, marker);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.markerChanged(source, marker);
			}
		}

		@Override
		public void parentChanged(final IApplicationNode source) {
			super.parentChanged(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.parentChanged(source);
			}
		}

		@Override
		public void presentationChanged(final IApplicationNode source) {
			super.presentationChanged(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.presentationChanged(source);
			}
		}

		@Override
		public void selectedChanged(final IApplicationNode source) {
			super.selectedChanged(source);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.selectedChanged(source);
			}
		}

		@Override
		public void stateChanged(final IApplicationNode source, final State oldState, final State newState) {
			super.stateChanged(source, oldState, newState);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.stateChanged(source, oldState, newState);
			}
		}

		@Override
		public void nodeIdChange(final IApplicationNode source, final NavigationNodeId oldId,
				final NavigationNodeId newId) {
			super.nodeIdChange(source, oldId, newId);
			for (final IApplicationNodeListener next : getApplicationNodeListeners()) {
				next.nodeIdChange(source, oldId, newId);
			}
		}
	}

	private class MySubApplicationNodeListener extends SubApplicationNodeListener {

		@Override
		public void activated(final ISubApplicationNode source) {
			super.activated(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.activated(source);
			}
		}

		@Override
		public void filterAdded(final ISubApplicationNode source, final IUIFilter filter) {
			super.filterAdded(source, filter);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.filterAdded(source, filter);
			}
		}

		@Override
		public void filterRemoved(final ISubApplicationNode source, final IUIFilter filter) {
			super.filterRemoved(source, filter);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.filterRemoved(source, filter);
			}
		}

		@Override
		public void block(final ISubApplicationNode source, final boolean block) {
			super.block(source, block);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.block(source, block);
			}
		}

		@Override
		public void beforeActivated(final ISubApplicationNode source) {
			super.beforeActivated(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.beforeActivated(source);
			}
		}

		@Override
		public void afterActivated(final ISubApplicationNode source) {
			super.afterActivated(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.afterActivated(source);
			}
		}

		@Override
		public void deactivated(final ISubApplicationNode source) {
			super.deactivated(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.deactivated(source);
			}
		}

		@Override
		public void beforeDeactivated(final ISubApplicationNode source) {
			super.beforeDeactivated(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.beforeDeactivated(source);
			}
		}

		@Override
		public void afterDeactivated(final ISubApplicationNode source) {
			super.afterDeactivated(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.afterDeactivated(source);
			}
		}

		@Override
		public void disposed(final ISubApplicationNode source) {
			super.disposed(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.disposed(source);
			}
		}

		@Override
		public void beforeDisposed(final ISubApplicationNode source) {
			super.beforeDisposed(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.beforeDisposed(source);
			}
		}

		@Override
		public void afterDisposed(final ISubApplicationNode source) {
			super.afterDisposed(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.afterDisposed(source);
			}
		}

		@Override
		public void childAdded(final ISubApplicationNode source, final IModuleGroupNode childAdded) {
			super.childAdded(source, childAdded);
			addListenerTo(childAdded);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.childAdded(source, childAdded);
			}
		}

		@Override
		public void childRemoved(final ISubApplicationNode source, final IModuleGroupNode childRemoved) {
			super.childRemoved(source, childRemoved);
			removeListenerFrom(childRemoved);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.childRemoved(source, childRemoved);
			}
		}

		@Override
		public void expandedChanged(final ISubApplicationNode source) {
			super.expandedChanged(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.expandedChanged(source);
			}
		}

		@Override
		public void labelChanged(final ISubApplicationNode source) {
			super.labelChanged(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.labelChanged(source);
			}
		}

		@Override
		public void markerChanged(final ISubApplicationNode source, final IMarker marker) {
			super.markerChanged(source, marker);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.markerChanged(source, marker);
			}
		}

		@Override
		public void parentChanged(final ISubApplicationNode source) {
			super.parentChanged(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.parentChanged(source);
			}
		}

		@Override
		public void presentationChanged(final ISubApplicationNode source) {
			super.presentationChanged(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.presentationChanged(source);
			}
		}

		@Override
		public void selectedChanged(final ISubApplicationNode source) {
			super.selectedChanged(source);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.selectedChanged(source);
			}
		}

		@Override
		public void stateChanged(final ISubApplicationNode source, final State oldState, final State newState) {
			super.stateChanged(source, oldState, newState);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.stateChanged(source, oldState, newState);
			}
		}

		@Override
		public void nodeIdChange(final ISubApplicationNode source, final NavigationNodeId oldId,
				final NavigationNodeId newId) {
			super.nodeIdChange(source, oldId, newId);
			for (final ISubApplicationNodeListener next : getSubApplicationListeners()) {
				next.nodeIdChange(source, oldId, newId);
			}
		}
	}

	private class MyModuleGroupNodeListener extends ModuleGroupNodeListener {

		@Override
		public void filterAdded(final IModuleGroupNode source, final IUIFilter filter) {
			super.filterAdded(source, filter);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.filterAdded(source, filter);
			}
		}

		@Override
		public void filterRemoved(final IModuleGroupNode source, final IUIFilter filter) {
			super.filterRemoved(source, filter);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.filterRemoved(source, filter);
			}
		}

		@Override
		public void activated(final IModuleGroupNode source) {
			super.activated(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.activated(source);
			}
		}

		@Override
		public void block(final IModuleGroupNode source, final boolean block) {
			super.block(source, block);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.block(source, block);
			}
		}

		@Override
		public void beforeActivated(final IModuleGroupNode source) {
			super.beforeActivated(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.beforeActivated(source);
			}
		}

		@Override
		public void afterActivated(final IModuleGroupNode source) {
			super.afterActivated(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.afterActivated(source);
			}
		}

		@Override
		public void deactivated(final IModuleGroupNode source) {
			super.deactivated(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.deactivated(source);
			}
		}

		@Override
		public void beforeDeactivated(final IModuleGroupNode source) {
			super.beforeDeactivated(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.beforeDeactivated(source);
			}
		}

		@Override
		public void afterDeactivated(final IModuleGroupNode source) {
			super.afterDeactivated(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.afterDeactivated(source);
			}
		}

		@Override
		public void disposed(final IModuleGroupNode source) {
			super.disposed(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.disposed(source);
			}
		}

		@Override
		public void beforeDisposed(final IModuleGroupNode source) {
			super.beforeDisposed(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.beforeDisposed(source);
			}
		}

		@Override
		public void afterDisposed(final IModuleGroupNode source) {
			super.afterDisposed(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.afterDisposed(source);
			}
		}

		@Override
		public void childAdded(final IModuleGroupNode source, final IModuleNode childAdded) {
			super.childAdded(source, childAdded);
			addListenerTo(childAdded);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.childAdded(source, childAdded);
			}
		}

		@Override
		public void childRemoved(final IModuleGroupNode source, final IModuleNode childRemoved) {
			super.childRemoved(source, childRemoved);
			removeListenerFrom(childRemoved);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.childRemoved(source, childRemoved);
			}
		}

		@Override
		public void expandedChanged(final IModuleGroupNode source) {
			super.expandedChanged(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.expandedChanged(source);
			}
		}

		@Override
		public void labelChanged(final IModuleGroupNode source) {
			super.labelChanged(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.labelChanged(source);
			}
		}

		@Override
		public void markerChanged(final IModuleGroupNode source, final IMarker marker) {
			super.markerChanged(source, marker);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.markerChanged(source, marker);
			}
		}

		@Override
		public void parentChanged(final IModuleGroupNode source) {
			super.parentChanged(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.parentChanged(source);
			}
		}

		@Override
		public void presentationChanged(final IModuleGroupNode source) {
			super.presentationChanged(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.presentationChanged(source);
			}
		}

		@Override
		public void selectedChanged(final IModuleGroupNode source) {
			super.selectedChanged(source);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.selectedChanged(source);
			}
		}

		@Override
		public void stateChanged(final IModuleGroupNode source, final State oldState, final State newState) {
			super.stateChanged(source, oldState, newState);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.stateChanged(source, oldState, newState);
			}
		}

		@Override
		public void nodeIdChange(final IModuleGroupNode source, final NavigationNodeId oldId,
				final NavigationNodeId newId) {
			super.nodeIdChange(source, oldId, newId);
			for (final IModuleGroupNodeListener next : getModuleGroupNodeListeners()) {
				next.nodeIdChange(source, oldId, newId);
			}
		}
	}

	private class MyModuleNodeListener extends ModuleNodeListener {

		@Override
		public void filterAdded(final IModuleNode source, final IUIFilter filter) {
			super.filterAdded(source, filter);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.filterAdded(source, filter);
			}
		}

		@Override
		public void filterRemoved(final IModuleNode source, final IUIFilter filter) {
			super.filterRemoved(source, filter);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.filterRemoved(source, filter);
			}
		}

		@Override
		public void activated(final IModuleNode source) {
			super.activated(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.activated(source);
			}
		}

		@Override
		public void block(final IModuleNode source, final boolean block) {
			super.block(source, block);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.block(source, block);
			}
		}

		@Override
		public void beforeActivated(final IModuleNode source) {
			super.beforeActivated(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.beforeActivated(source);
			}
		}

		@Override
		public void afterActivated(final IModuleNode source) {
			super.afterActivated(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.afterActivated(source);
			}
		}

		@Override
		public void deactivated(final IModuleNode source) {
			super.deactivated(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.deactivated(source);
			}
		}

		@Override
		public void beforeDeactivated(final IModuleNode source) {
			super.beforeDeactivated(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.beforeDeactivated(source);
			}
		}

		@Override
		public void afterDeactivated(final IModuleNode source) {
			super.afterDeactivated(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.afterDeactivated(source);
			}
		}

		@Override
		public void disposed(final IModuleNode source) {
			super.disposed(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.disposed(source);
			}
		}

		@Override
		public void beforeDisposed(final IModuleNode source) {
			super.beforeDisposed(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.beforeDisposed(source);
			}
		}

		@Override
		public void afterDisposed(final IModuleNode source) {
			super.afterDisposed(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.afterDisposed(source);
			}
		}

		@Override
		public void childAdded(final IModuleNode source, final ISubModuleNode childAdded) {
			super.childAdded(source, childAdded);
			addListenerTo(childAdded);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.childAdded(source, childAdded);
			}
		}

		@Override
		public void childRemoved(final IModuleNode source, final ISubModuleNode childRemoved) {
			super.childRemoved(source, childRemoved);
			removeListenerFrom(childRemoved);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.childRemoved(source, childRemoved);
			}
		}

		@Override
		public void expandedChanged(final IModuleNode source) {
			super.expandedChanged(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.expandedChanged(source);
			}
		}

		@Override
		public void labelChanged(final IModuleNode source) {
			super.labelChanged(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.labelChanged(source);
			}
		}

		@Override
		public void markerChanged(final IModuleNode source, final IMarker marker) {
			super.markerChanged(source, marker);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.markerChanged(source, marker);
			}
		}

		@Override
		public void parentChanged(final IModuleNode source) {
			super.parentChanged(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.parentChanged(source);
			}
		}

		@Override
		public void presentationChanged(final IModuleNode source) {
			super.presentationChanged(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.presentationChanged(source);
			}
		}

		@Override
		public void selectedChanged(final IModuleNode source) {
			super.selectedChanged(source);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.selectedChanged(source);
			}
		}

		@Override
		public void stateChanged(final IModuleNode source, final State oldState, final State newState) {
			super.stateChanged(source, oldState, newState);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.stateChanged(source, oldState, newState);
			}
		}

		@Override
		public void nodeIdChange(final IModuleNode source, final NavigationNodeId oldId, final NavigationNodeId newId) {
			super.nodeIdChange(source, oldId, newId);
			for (final IModuleNodeListener next : getModuleNodeListeners()) {
				next.nodeIdChange(source, oldId, newId);
			}
		}
	}

	private class MySubModuleNodeListener extends SubModuleNodeListener {

		@Override
		public void filterAdded(final ISubModuleNode source, final IUIFilter filter) {
			super.filterAdded(source, filter);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.filterAdded(source, filter);
			}
		}

		@Override
		public void filterRemoved(final ISubModuleNode source, final IUIFilter filter) {
			super.filterRemoved(source, filter);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.filterRemoved(source, filter);
			}
		}

		@Override
		public void block(final ISubModuleNode source, final boolean block) {
			super.block(source, block);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.block(source, block);
			}
		}

		@Override
		public void activated(final ISubModuleNode source) {
			super.activated(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.activated(source);
			}
		}

		@Override
		public void beforeActivated(final ISubModuleNode source) {
			super.beforeActivated(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.beforeActivated(source);
			}
		}

		@Override
		public void afterActivated(final ISubModuleNode source) {
			super.afterActivated(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.afterActivated(source);
			}
		}

		@Override
		public void deactivated(final ISubModuleNode source) {
			super.deactivated(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.deactivated(source);
			}
		}

		@Override
		public void beforeDeactivated(final ISubModuleNode source) {
			super.beforeDeactivated(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.beforeDeactivated(source);
			}
		}

		@Override
		public void afterDeactivated(final ISubModuleNode source) {
			super.afterDeactivated(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.afterDeactivated(source);
			}
		}

		@Override
		public void disposed(final ISubModuleNode source) {
			super.disposed(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.disposed(source);
			}
		}

		@Override
		public void beforeDisposed(final ISubModuleNode source) {
			super.beforeDisposed(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.beforeDisposed(source);
			}
		}

		@Override
		public void afterDisposed(final ISubModuleNode source) {
			super.afterDisposed(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.afterDisposed(source);
			}
		}

		@Override
		public void childAdded(final ISubModuleNode source, final ISubModuleNode childAdded) {
			super.childAdded(source, childAdded);
			addListenerTo(childAdded);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.childAdded(source, childAdded);
			}
		}

		@Override
		public void childRemoved(final ISubModuleNode source, final ISubModuleNode childRemoved) {
			super.childRemoved(source, childRemoved);
			removeListenerFrom(childRemoved);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.childRemoved(source, childRemoved);
			}
		}

		@Override
		public void expandedChanged(final ISubModuleNode source) {
			super.expandedChanged(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.expandedChanged(source);
			}
		}

		@Override
		public void labelChanged(final ISubModuleNode source) {
			super.labelChanged(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.labelChanged(source);
			}
		}

		@Override
		public void markerChanged(final ISubModuleNode source, final IMarker marker) {
			super.markerChanged(source, marker);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.markerChanged(source, marker);
			}
		}

		@Override
		public void parentChanged(final ISubModuleNode source) {
			super.parentChanged(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.parentChanged(source);
			}
		}

		@Override
		public void presentationChanged(final ISubModuleNode source) {
			super.presentationChanged(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.presentationChanged(source);
			}
		}

		@Override
		public void selectedChanged(final ISubModuleNode source) {
			super.selectedChanged(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.selectedChanged(source);
			}
		}

		@Override
		public void stateChanged(final ISubModuleNode source, final State oldState, final State newState) {
			super.stateChanged(source, oldState, newState);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.stateChanged(source, oldState, newState);
			}
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * Every registered listener will be notified that this node is prepared
		 * now.
		 */
		@Override
		public void prepared(final ISubModuleNode source) {
			super.prepared(source);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.prepared(source);
			}
		}

		@Override
		public void nodeIdChange(final ISubModuleNode source, final NavigationNodeId oldId, final NavigationNodeId newId) {
			super.nodeIdChange(source, oldId, newId);
			for (final ISubModuleNodeListener next : getSubModuleNodeListeners()) {
				next.nodeIdChange(source, oldId, newId);
			}
		}
	}
}
