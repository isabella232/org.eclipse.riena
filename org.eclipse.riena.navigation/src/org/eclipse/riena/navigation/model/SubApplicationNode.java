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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.ISubApplicationNodeListener;

/**
 * Default implementation for the sub application
 */
public class SubApplicationNode extends
		NavigationNode<ISubApplicationNode, IModuleGroupNode, ISubApplicationNodeListener> implements
		ISubApplicationNode {

	/**
	 * Creates a SubApplicationNode
	 * 
	 */
	public SubApplicationNode() {
		super(null);
	}

	public Class<IModuleGroupNode> getValidChildType() {
		return IModuleGroupNode.class;
	}

	/**
	 * Creates a SubApplicationNode
	 * 
	 * @param nodeId
	 *            Identifies the node in the application model tree.
	 */
	public SubApplicationNode(final NavigationNodeId nodeId) {
		super(nodeId);
	}

	/**
	 * Creates a SubApplicationNode.
	 * 
	 * @param nodeId
	 *            Identifies the node in the application model tree.
	 * @param label
	 *            Label of the sub application displayed on the sub applications
	 *            tab.
	 */
	public SubApplicationNode(final NavigationNodeId nodeId, final String label) {
		super(nodeId, label);
	}

	/**
	 * Creates a SubApplicationNode.
	 * 
	 * @param label
	 *            Label of the sub application displayed on the sub applications
	 *            tab.
	 */
	public SubApplicationNode(final String label) {
		super(null, label);
	}

	@Override
	protected void addChildParent(final IModuleGroupNode child) {
		child.setParent(this);
	}

}
