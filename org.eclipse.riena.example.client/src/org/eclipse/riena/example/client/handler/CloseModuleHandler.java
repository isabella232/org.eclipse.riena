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
package org.eclipse.riena.example.client.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IModuleNode;

/**
 * Handler used to close the active Module.
 */
public class CloseModuleHandler extends AbstractHandler {
	/**
	 * {@inheritDoc}
	 * <p>
	 * Closes the active Module if it is closable.
	 */
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IModuleNode module = getActiveModuleNode();
		if (module != null && module.isClosable()) {
			module.dispose();
		}
		return null;
	}

	private IModuleNode getActiveModuleNode() {
		return ApplicationNodeManager.locateActiveModuleNode();
	}

}
