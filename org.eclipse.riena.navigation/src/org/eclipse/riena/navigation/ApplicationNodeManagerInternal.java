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
package org.eclipse.riena.navigation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.model.NavigationProcessor;

/**
 * This class holds the ApplicationNode(s) of a Riena application in a static way. If you need more than one ApplicationNode, you must specify a unique
 * ApplicationNode names.
 * 
 * @since 3.0
 */
final class ApplicationNodeManagerInternal {

	private Map<String, IApplicationNode> nodeMap = new HashMap<String, IApplicationNode>();
	private INavigationProcessor defaultNavigationProcessor;
	private final static String DEFAULT_NODE_NAME = "default"; //$NON-NLS-1$

	/**
	 * Answer the default applicationModel
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 * @return the default application model or null if no defaultmodel is present. Usually only one (the default model) model is used.
	 */
	public IApplicationNode getApplicationNode() {
		final IApplicationNode model = getApplicationNode(DEFAULT_NODE_NAME);
		if (model == null && nodeMap.size() == 1) {
			// fallback strategy
			return nodeMap.values().iterator().next();
		}
		return model;
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
	public IApplicationNode getApplicationNode(final String name) {
		String modelName = name;
		if (modelName == null) {
			modelName = DEFAULT_NODE_NAME;
		}
		return nodeMap.get(modelName);
	}

	/**
	 * Clear the ApplicationNode map
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 */
	public synchronized void clear() {
		nodeMap = new HashMap<String, IApplicationNode>();
	}

	/**
	 * Register the given ApplicationNode. If the ApplicationNode already exists, an ApplicationModelFailure is thrown
	 * 
	 * @see org.eclipse.riena.navigation.IApplicationNode
	 * @see org.eclipse.riena.navigation.ApplicationModelFailure
	 * @param node
	 *            the model to register
	 */
	public synchronized void registerApplicationNode(final IApplicationNode node) {
		String nodeName = node.getLabel();
		if (nodeName == null) {
			nodeName = DEFAULT_NODE_NAME;
		}
		if (nodeMap.containsKey(nodeName)) {
			throw new ApplicationModelFailure("ApplicationNode '" + nodeName + "' already registered"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		nodeMap.put(nodeName, node);
		return;
	}

	/**
	 * Returns the default navigation processor.
	 * 
	 * @return navigation processor
	 * @since 1.2
	 */
	public INavigationProcessor getDefaultNavigationProcessor() {
		if (defaultNavigationProcessor == null) {
			defaultNavigationProcessor = new NavigationProcessor();
		}
		return defaultNavigationProcessor;
	}

	/**
	 * Returns the node of the active SubApplication
	 * 
	 * @since 1.2
	 */
	public ISubApplicationNode locateActiveSubApplicationNode() {
		final IApplicationNode applicationNode = getApplicationNode();
		if (applicationNode == null) {
			return null;
		}
		for (final ISubApplicationNode child : applicationNode.getChildren()) {
			if (child.isActivated()) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Returns the node of the active ModuleGroup
	 * 
	 * @since 1.2
	 */
	public IModuleGroupNode locateActiveModuleGroupNode() {
		final ISubApplicationNode subApplicationNode = locateActiveSubApplicationNode();
		if (subApplicationNode == null) {
			return null;
		}
		for (final IModuleGroupNode child : subApplicationNode.getChildren()) {
			if (child.isActivated()) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Returns the node of the active Module
	 * 
	 * @since 1.2
	 */
	public IModuleNode locateActiveModuleNode() {
		final IModuleGroupNode moduleGroupNode = locateActiveModuleGroupNode();
		if (moduleGroupNode == null) {
			return null;
		}
		for (final IModuleNode child : moduleGroupNode.getChildren()) {
			if (child.isActivated()) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Returns the node of the active SubModule
	 * 
	 * @since 1.2
	 */
	public ISubModuleNode locateActiveSubModuleNode() {
		final IModuleNode moduleNode = locateActiveModuleNode();
		if (moduleNode == null) {
			return null;
		}
		return ApplicationNodeManager.getActiveSubModule(moduleNode);
	}

}
