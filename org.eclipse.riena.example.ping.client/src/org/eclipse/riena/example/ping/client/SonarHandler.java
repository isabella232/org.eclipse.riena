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
package org.eclipse.riena.example.ping.client;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.riena.example.ping.client.controllers.SonarController;
import org.eclipse.riena.example.ping.client.nls.Messages;
import org.eclipse.riena.example.ping.client.views.SonarView;
import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * This is a command handler that creates a Module for the
 * {@link SonarController} in the current application.
 */
public class SonarHandler extends AbstractHandler {

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISubApplicationNode subApplicationNode = ApplicationNodeManager.locateActiveSubApplicationNode();
		final NavigationNodeId navigationNodeId = new NavigationNodeId(
				"sonar.submodule", subApplicationNode.getNodeId().getInstanceId()); //$NON-NLS-1$
		INavigationNode<?> node = subApplicationNode.findNode(navigationNodeId);
		if (node == null) {
			node = createPingModuleGroup(subApplicationNode);
		}
		node.activate();
		return null;
	}

	/**
	 * Creates a ModuleGroup with the Sonar-SubModule.
	 * 
	 * @param subApplicationNode
	 *            the Node of the current sub-application.
	 * @return the Sonar SubModuleNode.
	 */
	private ISubModuleNode createPingModuleGroup(final ISubApplicationNode subApplicationNode) {
		final IModuleGroupNode group = new ModuleGroupNode(new NavigationNodeId(
				"sonar.module.group", subApplicationNode.getNodeId() //$NON-NLS-1$
						.getInstanceId()));
		subApplicationNode.addChild(group);
		final IModuleNode module = new ModuleNode(new NavigationNodeId(
				"sonar.module", subApplicationNode.getNodeId().getInstanceId()), Messages.sonar); //$NON-NLS-1$
		module.setClosable(true);
		// module.setPresentSingleSubModule(true);
		group.addChild(module);
		final ISubModuleNode submodule = new SubModuleNode(new NavigationNodeId(
				"sonar.submodule", subApplicationNode.getNodeId().getInstanceId()), //$NON-NLS-1$
				null);
		WorkareaManager.getInstance().registerDefinition(submodule, SonarController.class, SonarView.VIEW_ID, false);
		module.addChild(submodule);
		return submodule;
	}

}
