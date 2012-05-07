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
package org.eclipse.riena.navigation.listener;

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;

/**
 * Default implementation for IApplicationNodeListener
 */
public class ApplicationNodeListener extends NavigationNodeListener<IApplicationNode, ISubApplicationNode> implements IApplicationNodeListener {

	/**
	 * @see org.eclipse.riena.navigation.listener.IApplicationNodeListener#logoChanged(org.eclipse.riena.navigation.IApplicationNode)
	 * @since 4.0
	 */
	public void logoChanged(final IApplicationNode node, final String newLogoPath) {
	}

}
