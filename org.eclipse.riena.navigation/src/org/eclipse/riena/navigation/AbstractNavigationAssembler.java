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

import org.eclipse.riena.navigation.extension.INavigationAssembly2Extension;

/**
 * Abstract class that supplies some standard methods for INavigationAssembler
 */
public abstract class AbstractNavigationAssembler implements INavigationAssembler {

	private String id;
	private String parentNodeId;
	private int startOrder;
	private INavigationAssembly2Extension assembly;

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public String getParentNodeId() {
		return parentNodeId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public void setParentNodeId(final String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public int getStartOrder() {
		return startOrder;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public void setStartOrder(final int startOrder) {
		this.startOrder = startOrder;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public INavigationAssembly2Extension getAssembly() {
		return assembly;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public void setAssembly(final INavigationAssembly2Extension assembly) {
		this.assembly = assembly;
	}

}
