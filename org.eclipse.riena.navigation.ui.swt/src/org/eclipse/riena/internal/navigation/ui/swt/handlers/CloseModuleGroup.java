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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;

/**
 * Close the currently active module-group.
 */
public class CloseModuleGroup extends AbstractNavigationHandler {

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		// assumes there is only one application node
		final IApplicationNode application = ApplicationNodeManager.getApplicationNode();
		final IModuleGroupNode moduleGroup = findModuleGroup(application);
		if (moduleGroup != null && isCloseable(moduleGroup)) {
			final INavigationNode<?> previous = findPreviousModuleGroup(moduleGroup);
			if (previous != null) {
				previous.activate();
			}
			moduleGroup.dispose();
		}
		return null;
	}

	// helping methods
	//////////////////

	private INavigationNode<?> findPreviousModuleGroup(final IModuleGroupNode moduleGroup) {
		final List<?> moduleGroups = moduleGroup.getParent().getChildren();
		final INavigationNode<?>[] nodes = moduleGroups.toArray(new INavigationNode<?>[moduleGroups.size()]);
		return findPreviousNode(nodes, false);
	}

	private boolean isCloseable(final IModuleGroupNode moduleGroup) {
		boolean result = true;
		for (final IModuleNode module : moduleGroup.getChildren()) {
			result = result && module.isClosable();
		}
		return result;
	}

}
