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
 * Close the currently active module.
 */
public class CloseModule extends AbstractNavigationHandler {

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		// assumes there is only one application node
		final IApplicationNode application = ApplicationNodeManager.getApplicationNode();
		final IModuleNode module = findModule(application);
		if (module != null && module.isClosable()) {
			final INavigationNode<?> previous = findPreviousModule(application);
			if (previous != null) {
				previous.activate();
			}
			module.dispose();
		}
		return null;
	}

	// helping methods
	//////////////////

	private INavigationNode<?> findPreviousModule(final IApplicationNode application) {
		final IModuleNode[] modules = collectModules(application);
		return findPreviousNode(modules, false);
	}

	/**
	 * Not API; public for testing only.
	 */
	@SuppressWarnings("unchecked")
	public final IModuleNode findModule(final IApplicationNode application) {
		IModuleNode result = null;
		final IModuleGroupNode moduleGroup = findModuleGroup(application);
		if (moduleGroup != null) {
			final INavigationNode<?> module = findActive((List) moduleGroup.getChildren());
			if (module instanceof IModuleNode) {
				result = (IModuleNode) module;
			}
		}
		return result;
	}

}
