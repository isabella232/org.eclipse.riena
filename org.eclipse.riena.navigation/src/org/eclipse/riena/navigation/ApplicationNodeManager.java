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
package org.eclipse.riena.navigation;

import org.eclipse.riena.core.singleton.SessionSingletonProvider;
import org.eclipse.riena.core.singleton.SingletonProvider;

/**
 * This class holds the ApplicationNode(s) of a Riena application in a static way. If you need more than one ApplicationNode, you must specify a unique
 * ApplicationNode names.
 */
public final class ApplicationNodeManager {

	private static final SingletonProvider<ApplicationNodeManagerInternal> ANMI = new SessionSingletonProvider<ApplicationNodeManagerInternal>(
			ApplicationNodeManagerInternal.class);

	private ApplicationNodeManager() {
		super();
	}

	/**
	 * Answer the default applicationModel
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 * @return the default application model or null if no default model is present. Usually only one (the default model) model is used.
	 */
	public static IApplicationNode getApplicationNode() {
		return ANMI.getInstance().getApplicationNode();
	}

	/**
	 * Answer the ApplicationNode with the given name
	 * 
	 * @param label
	 *            the name of the mApplicationModel
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 * @return the ApplicationNode with the matching name or null if no matching model is present.
	 */
	public static IApplicationNode getApplicationNode(final String name) {
		return ANMI.getInstance().getApplicationNode(name);
	}

	/**
	 * Clear the ApplicationNode map
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 */
	public static synchronized void clear() {
		ANMI.getInstance().clear();
	}

	/**
	 * Register the given ApplicationNode. If the ApplicationNode already exists, an ApplicationModelFailure is thrown
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 * @see org.eclipse.riena.navigation.ApplicationModelFailure
	 * @param node
	 *            the model to register
	 */
	public static synchronized void registerApplicationNode(final IApplicationNode node) {
		ANMI.getInstance().registerApplicationNode(node);
	}

	/**
	 * Returns the default navigation processor.
	 * 
	 * @return navigation processor
	 * @since 1.2
	 */
	public static INavigationProcessor getDefaultNavigationProcessor() {
		return ANMI.getInstance().getDefaultNavigationProcessor();
	}

	/**
	 * Returns the node of the active SubApplication
	 * 
	 * @since 1.2
	 */
	public static ISubApplicationNode locateActiveSubApplicationNode() {
		return ANMI.getInstance().locateActiveSubApplicationNode();
	}

	/**
	 * Returns the node of the active ModuleGroup
	 * 
	 * @since 1.2
	 */
	public static IModuleGroupNode locateActiveModuleGroupNode() {
		return ANMI.getInstance().locateActiveModuleGroupNode();
	}

	/**
	 * Returns the node of the active Module
	 * 
	 * @since 1.2
	 */
	public static IModuleNode locateActiveModuleNode() {
		return ANMI.getInstance().locateActiveModuleNode();
	}

	/**
	 * Returns the node of the active SubModule
	 * 
	 * @since 1.2
	 */
	public static ISubModuleNode locateActiveSubModuleNode() {
		return ANMI.getInstance().locateActiveSubModuleNode();
	}

	/**
	 * Returns the active child (sub-module) of the given module.
	 * 
	 * @param node
	 *            - module node
	 * @return active sub-module or {@code null} if no active child exists
	 */
	public static ISubModuleNode getActiveSubModule(final IModuleNode node) {

		for (final ISubModuleNode child : node.getChildren()) {
			final ISubModuleNode subModuleNode = getActiveSubModule(child);
			if (subModuleNode != null) {
				return getActiveSubModule(child);
			}
		}

		return null;

	}

	/**
	 * Returns the active sub-module.
	 * 
	 * @param node
	 *            parent sub-module
	 * @return active sub-module or {@code null} if no active sub-module exists
	 */
	public static ISubModuleNode getActiveSubModule(final ISubModuleNode node) {

		if (node.isActivated()) {
			return node;
		}
		for (final ISubModuleNode child : node.getChildren()) {
			final ISubModuleNode subModuleNode = getActiveSubModule(child);
			if (subModuleNode != null) {
				return subModuleNode;
			}
		}

		return null;

	}

}
