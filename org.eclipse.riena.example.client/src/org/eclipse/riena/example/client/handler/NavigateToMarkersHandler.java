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
package org.eclipse.riena.example.client.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;

public class NavigateToMarkersHandler extends AbstractHandler {

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISubModuleNode activeSubModuleNode = ApplicationNodeManager.locateActiveSubModuleNode();
		ApplicationNodeManager.getDefaultNavigationProcessor().navigate(activeSubModuleNode,
				new NavigationNodeId("org.eclipse.riena.example.marker"), new NavigationArgument(null, "textAmount")); //$NON-NLS-1$ //$NON-NLS-2$
		return null;
	}

}
