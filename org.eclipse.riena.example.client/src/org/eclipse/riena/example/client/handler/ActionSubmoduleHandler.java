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

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;

/**
 * 
 */
public class ActionSubmoduleHandler extends DummyHandler {

	@Override
	protected String getTitle() {
		return "Active Sub-Module"; //$NON-NLS-1$
	}

	@Override
	protected String getMessage() {
		String msg = "This command is only enabled for one sub-module (Choice)!\n\n"; //$NON-NLS-1$
		msg += "Information:\n"; //$NON-NLS-1$
		msg += "\t active sub-application:\t" + getActiveSubApplication().getLabel() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		msg += "\t active module-group:\t" + getActiveModuleGroup().getLabel() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		msg += "\t active module:\t\t" + getActiveModule().getLabel() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		msg += "\t active sub-module:\t\t" + getActiveSubModule().getLabel(); //$NON-NLS-1$
		return msg;
	}

	private ISubApplicationNode getActiveSubApplication() {
		return ApplicationNodeManager.locateActiveSubApplicationNode();
	}

	private IModuleGroupNode getActiveModuleGroup() {
		return ApplicationNodeManager.locateActiveModuleGroupNode();
	}

	private IModuleNode getActiveModule() {
		return ApplicationNodeManager.locateActiveModuleNode();
	}

	private ISubModuleNode getActiveSubModule() {
		return ApplicationNodeManager.locateActiveSubModuleNode();
	}
}
