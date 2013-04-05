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

import org.eclipse.riena.navigation.listener.IApplicationNodeListener;
import org.eclipse.riena.navigation.listener.INavigationNodeListenerable;

/**
 * Describes the riena mode of an application consisting of sub applications
 */
public interface IApplicationNode extends INavigationNode<ISubApplicationNode>,
		INavigationNodeListenerable<IApplicationNode, ISubApplicationNode, IApplicationNodeListener>, INavigationHistoryListenerable {

	/**
	 * The logo setting for an application.
	 * 
	 * @since 4.0
	 * 
	 */
	String PROPERTY_LOGO = "logo"; //$NON-NLS-1$

	/**
	 * Sets the logo of this node. This setting may also be <code>null</code>. In the non-<code>null</code> case this property overrides the LnF setting.
	 * Default is <code>null</code>.
	 * 
	 * @param logoPath
	 *            the path to the logo image ot <code>null</code> to use the LnF setting
	 * @since 4.0
	 */
	void setLogo(String logoPath);

	//	/**
	//	 * Returns the configured logo for this application. If this method returns <code>null</code> (no logo configured) then the logo setting from the active LnF
	//	 * must be considered.
	//	 * 
	//	 * @return the configured logo image or <code>null</code>
	//	 * @see IApplicationNode#setLogo(String)
	//	 * @since 4.0
	//	 */
	//	String getLogo();
}
