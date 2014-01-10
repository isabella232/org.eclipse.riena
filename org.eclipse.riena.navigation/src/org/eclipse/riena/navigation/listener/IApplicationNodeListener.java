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
package org.eclipse.riena.navigation.listener;

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;

/**
 * Special listener for the application model
 */
public interface IApplicationNodeListener extends INavigationNodeListener<IApplicationNode, ISubApplicationNode> {

	/**
	 * This method is called when the logo has changed.
	 * 
	 * @param source
	 *            the application node whose logo has changed
	 * @param newLogoPath
	 *            the new logo path
	 * 
	 * @since 4.0
	 */
	void logoChanged(IApplicationNode source, String newLogoPath);
}
