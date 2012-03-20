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
package org.eclipse.riena.navigation.extension;

import org.eclipse.riena.navigation.ISubApplicationNodeExtension;

/**
 * Implementation of the interface {@code ISubApplicationNode2Extension}. This
 * is only used for conversion of the legacy extension
 * {@link ISubApplicationNodeExtension}.
 */
public class SubApplicationNode2Extension extends Node2Extension implements ISubApplicationNode2Extension {

	private String perspectiveId;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IModuleGroupNode2Extension[] getChildNodes() {
		return (IModuleGroupNode2Extension[]) super.getChildNodes();
	}

	/**
	 * {@inheritDoc}
	 */
	public IModuleGroupNode2Extension[] getModuleGroupNodes() {
		return getChildNodes();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPerspectiveId() {
		return perspectiveId;
	}

	/**
	 * Sets the ID of the perspective associated with the sub application. Must
	 * match an perspective elements id attribute of an
	 * "org.eclipse.ui.perspectives" extension.
	 * 
	 * @param perspectiveId
	 *            ID of perspective
	 */
	public void setPerspectiveId(final String perspectiveId) {
		this.perspectiveId = perspectiveId;
	}

}
