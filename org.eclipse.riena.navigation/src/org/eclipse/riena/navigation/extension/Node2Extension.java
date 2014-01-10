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
package org.eclipse.riena.navigation.extension;

/**
 * @since 2.0
 * 
 */
public abstract class Node2Extension implements INode2Extension {

	private String name;
	private String nodeId;
	private String icon;
	private INode2Extension[] childNodes;

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * {@inheritDoc}
	 */
	public INode2Extension[] getChildNodes() {
		return childNodes;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setNodeId(final String nodeId) {
		this.nodeId = nodeId;
	}

	public void setIcon(final String icon) {
		this.icon = icon;
	}

	public void setChildNodes(final INode2Extension[] childNodes) {
		this.childNodes = childNodes;
	}

}
