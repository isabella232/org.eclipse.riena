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
package org.eclipse.riena.navigation;

import org.eclipse.riena.core.util.StringUtils;

/**
 * An ID that identifies a node in the application model tree. The ID is used to
 * find navigate targets and to associated sub module nodes with their views.<br>
 * The following characters are not allowed in an ID: * (asterisk), ? (question
 * mark) and / (slash)
 */
public class NavigationNodeId {

	private final String instanceId;
	private final String typeId;
	private int hash = 0;

	public NavigationNodeId(final String typeId, final String instanceId) {
		if (!checkId(typeId)) {
			throw new IllegalArgumentException("ID with illegal characters: " + typeId); //$NON-NLS-1$
		}
		this.typeId = typeId;
		this.instanceId = instanceId;
	}

	public NavigationNodeId(final String typeId) {
		this(typeId, null);
	}

	/**
	 * Returns the type of a node. Nodes of the same type are created using the
	 * same node assembler. Sub module nodes of the same type use the same type
	 * of view. Both is configured using extensions (NavigationNodeType and
	 * SubModuleType). This typeId is used to find the right extension.
	 * 
	 * @see INavigationAssembler
	 * @return The type ID of a navigation node.
	 */
	public String getTypeId() {
		return typeId;
	}

	/**
	 * The optional instance ID is used to differentiate between nodes of the
	 * same type. E.g. two nodes representing employees that have the same type
	 * ID could use the social security number as instance ID.
	 * 
	 * @return The instance ID of a navigation node.
	 */
	public String getInstanceId() {
		return instanceId;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (getClass() != other.getClass()) {
			return false;
		}
		final NavigationNodeId otherId = (NavigationNodeId) other;
		return StringUtils.equals(typeId, otherId.typeId) && StringUtils.equals(instanceId, otherId.instanceId);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("NavigationNodeId [typeId=").append(typeId).append(", instanceId=").append(instanceId) //$NON-NLS-1$ //$NON-NLS-2$
				.append("]"); //$NON-NLS-1$
		return builder.toString();
	}

	@Override
	public int hashCode() {
		if (hash == 0) {
			if (typeId != null) {
				hash += typeId.hashCode();
			}
			if (instanceId != null) {
				hash += instanceId.hashCode();
			}
		}
		return hash;
	}

	/**
	 * Checks if the given ID contains illegal characters.
	 * 
	 * @param id
	 *            ID
	 * @return <code>true</code> if the ID is OK; otherwise <code>false</code>
	 */
	private boolean checkId(final String id) {

		if (id == null) {
			return true;
		}

		if (id.indexOf('*') != -1) {
			return false;
		}
		if (id.indexOf('?') != -1) {
			return false;
		}
		if (id.indexOf('/') != -1) {
			return false;
		}

		return true;

	}

}
