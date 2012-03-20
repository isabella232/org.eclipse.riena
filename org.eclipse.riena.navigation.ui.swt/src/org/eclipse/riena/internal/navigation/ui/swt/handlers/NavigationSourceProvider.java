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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.NavigationModelFailure;

/**
 * This class provides information about the active navigation node for updating
 * the menus.
 */
public class NavigationSourceProvider extends AbstractSourceProvider {

	private boolean isDisposed;

	private static final String ACTIVE_SUB_APPLICATION_NODE = "activeSubApplicationNode"; //$NON-NLS-1$
	private static final String ACTIVE_SUB_APPLICATION_NODE_ID = ACTIVE_SUB_APPLICATION_NODE + "Id"; //$NON-NLS-1$
	private static final String ACTIVE_MODULE_GROUP_NODE = "activeModuleGroupNode"; //$NON-NLS-1$
	private static final String ACTIVE_MODULE_GROUP_NODE_ID = ACTIVE_MODULE_GROUP_NODE + "Id"; //$NON-NLS-1$
	private static final String ACTIVE_MODULE_NODE = "activeModuleNode"; //$NON-NLS-1$
	private static final String ACTIVE_MODULE_NODE_ID = ACTIVE_MODULE_NODE + "Id"; //$NON-NLS-1$
	private static final String ACTIVE_SUB_MODULE_NODE = "activeSubModuleNode"; //$NON-NLS-1$
	private static final String ACTIVE_SUB_MODULE_NODE_ID = ACTIVE_SUB_MODULE_NODE + "Id"; //$NON-NLS-1$
	private static final int EVENT_PRIORITY = 1 << 28;

	private static final String[] PROVIDED_SOURCE_NAMES = new String[] { ACTIVE_SUB_APPLICATION_NODE_ID,
			ACTIVE_MODULE_GROUP_NODE_ID, ACTIVE_MODULE_NODE_ID, ACTIVE_SUB_MODULE_NODE_ID, ACTIVE_MODULE_NODE };

	public void dispose() {
		isDisposed = true;
	}

	public boolean isDisposed() {
		return isDisposed;
	}

	public final Map<String, Object> getCurrentState() {

		final Map<String, Object> state = new HashMap<String, Object>();

		final ISubApplicationNode subAppNode = ApplicationNodeManager.locateActiveSubApplicationNode();
		final IModuleGroupNode moduleGroupNode = ApplicationNodeManager.locateActiveModuleGroupNode();
		final IModuleNode moduleNode = ApplicationNodeManager.locateActiveModuleNode();
		final ISubModuleNode subModuleNode = ApplicationNodeManager.locateActiveSubModuleNode();

		state.put(ACTIVE_SUB_APPLICATION_NODE_ID, getTypeNodeId(subAppNode));
		state.put(ACTIVE_MODULE_GROUP_NODE_ID, getTypeNodeId(moduleGroupNode));
		state.put(ACTIVE_MODULE_NODE_ID, getTypeNodeId(moduleNode));
		state.put(ACTIVE_SUB_MODULE_NODE_ID, getTypeNodeId(subModuleNode));

		state.put(ACTIVE_SUB_APPLICATION_NODE, subAppNode);
		state.put(ACTIVE_MODULE_GROUP_NODE, moduleGroupNode);
		state.put(ACTIVE_MODULE_NODE, moduleNode);
		state.put(ACTIVE_SUB_MODULE_NODE, subModuleNode);

		return state;

	}

	public String[] getProvidedSourceNames() {
		return PROVIDED_SOURCE_NAMES;
	}

	void fireSourceChange(final INavigationNode<?> node) {
		final String variable = getVariableNameForNode(node);
		fireSourceChanged(EVENT_PRIORITY, variable + "Id", getTypeNodeId(node)); //$NON-NLS-1$
		fireSourceChanged(EVENT_PRIORITY, variable, node);
	}

	/**
	 * Returns the type ID of the given node. An empty string is returned if the
	 * node has no ID.
	 * 
	 * @param node
	 *            navigation node
	 * @return type ID of the given node
	 */
	private String getTypeNodeId(final INavigationNode<?> node) {
		if (node != null && node.getNodeId() != null) {
			return node.getNodeId().getTypeId();
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	private String getVariableNameForNode(final INavigationNode<?> node) {
		if (node == null) {
			return null;
		}

		if (node instanceof ISubApplicationNode) {
			return ACTIVE_SUB_APPLICATION_NODE;
		} else if (node instanceof IModuleGroupNode) {
			return ACTIVE_MODULE_GROUP_NODE;
		} else if (node instanceof IModuleNode) {
			return ACTIVE_MODULE_NODE;
		} else if (node instanceof ISubModuleNode) {
			return ACTIVE_SUB_MODULE_NODE;
		}

		throw new NavigationModelFailure("Unsupported instance of INavigationNode: " + node); //$NON-NLS-1$
	}

	/**
	 * This method should be called if a node was activated so that the value of
	 * the source can be updated.
	 * <p>
	 * Also the sources of all parent nodes are updated.
	 * 
	 * @param node
	 *            node that was activated right now
	 */
	public void activeNodeChanged(final INavigationNode<?> node) {
		fireSourceChange(node);
		if (node.getParent() != null && !(node.getParent() instanceof IApplicationNode)) {
			activeNodeChanged(node.getParent());
		}
	}

}
