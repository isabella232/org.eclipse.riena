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
package org.eclipse.riena.example.client.navigation.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.example.client.application.ExampleIcons;
import org.eclipse.riena.example.client.controllers.RemoteServiceProgressSubModuleController;
import org.eclipse.riena.example.client.controllers.UIProcessDemoSubModuleController;
import org.eclipse.riena.example.client.controllers.UIProcessesModuleController;
import org.eclipse.riena.example.client.views.RemoteServiceProgressSubModuleView;
import org.eclipse.riena.example.client.views.UIProcessDemoSubModuleView;
import org.eclipse.riena.navigation.AbstractNavigationAssembler;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Creates the module group, modules and sub modules for UI Process examples.
 */
public class UIProcessNodeAssembler extends AbstractNavigationAssembler {

	private static final String TYPE_ID_MODULE_GROUP_UI_PROCESSES = "org.eclipse.riena.example.uiProcesses"; //$NON-NLS-1$
	private static final String TYPE_ID_MODULE_UI_PROCESSES = "uiProcesses"; //$NON-NLS-1$
	private static final String TYPE_ID_MODULE_REMOTE_PROGRESS = "remoteProgress"; //$NON-NLS-1$

	private Set<String> knownTargetIds = null;

	/**
	 * {@inheritDoc}
	 * <p>
	 * Creates the module group, modules and sub modules for UI Process
	 * examples.
	 */
	public INavigationNode<?>[] buildNode(final NavigationNodeId nodeId, final NavigationArgument navigationArgument) {

		final IModuleGroupNode moduleGroup = new ModuleGroupNode(
				new NavigationNodeId(TYPE_ID_MODULE_GROUP_UI_PROCESSES));

		final IModuleNode uiProcessesModule = new ModuleNode(new NavigationNodeId(TYPE_ID_MODULE_UI_PROCESSES),
				"UIProcess"); //$NON-NLS-1$ 
		final UIProcessesModuleController moduleController = new UIProcessesModuleController(uiProcessesModule);
		uiProcessesModule.setNavigationNodeController(moduleController);
		uiProcessesModule.setIcon(ExampleIcons.ICON_RED_LED);
		moduleGroup.addChild(uiProcessesModule);

		final ISubModuleNode uiProcessSm1 = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.uiProcess_1"), "Demo1"); //$NON-NLS-1$ //$NON-NLS-2$ 
		WorkareaManager.getInstance().registerDefinition(uiProcessSm1, UIProcessDemoSubModuleController.class,
				UIProcessDemoSubModuleView.ID, true);
		uiProcessesModule.setIcon(ExampleIcons.ICON_GREEN_LED);
		uiProcessesModule.addChild(uiProcessSm1);

		final ISubModuleNode uiProcessSm2 = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.uiProcess_2"), "uiProcess_2"); //$NON-NLS-1$ //$NON-NLS-2$ 
		WorkareaManager.getInstance().registerDefinition(uiProcessSm2, UIProcessDemoSubModuleController.class,
				UIProcessDemoSubModuleView.ID, true);
		uiProcessesModule.addChild(uiProcessSm2);

		final IModuleNode remoteProgressModule = new ModuleNode(new NavigationNodeId(TYPE_ID_MODULE_REMOTE_PROGRESS),
				"service call progress"); //$NON-NLS-1$ 
		remoteProgressModule.setIcon(ExampleIcons.ICON_RED_LED);
		moduleGroup.addChild(remoteProgressModule);

		final ISubModuleNode remoteSm = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.remoteProgress"), "service call progress"); //$NON-NLS-1$ //$NON-NLS-2$ 
		WorkareaManager.getInstance().registerDefinition(remoteSm, RemoteServiceProgressSubModuleController.class,
				RemoteServiceProgressSubModuleView.ID, true);
		remoteSm.setIcon(ExampleIcons.ICON_GREEN_LED);
		remoteProgressModule.addChild(remoteSm);

		return new IModuleGroupNode[] { moduleGroup };

	}

	/**
	 * {@inheritDoc}
	 */
	public boolean acceptsToBuildNode(final NavigationNodeId nodeId, final NavigationArgument argument) {
		if (knownTargetIds == null) {
			knownTargetIds = new HashSet<String>(Arrays.asList(TYPE_ID_MODULE_GROUP_UI_PROCESSES,
					TYPE_ID_MODULE_UI_PROCESSES, TYPE_ID_MODULE_REMOTE_PROGRESS));
			knownTargetIds = Collections.unmodifiableSet(knownTargetIds);
		}

		return knownTargetIds.contains(nodeId.getTypeId());
	}

}
