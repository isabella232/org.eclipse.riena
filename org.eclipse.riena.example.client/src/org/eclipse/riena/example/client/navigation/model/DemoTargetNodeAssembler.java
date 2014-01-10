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
package org.eclipse.riena.example.client.navigation.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.example.client.application.ExampleIcons;
import org.eclipse.riena.example.client.controllers.DemoTargetSubModuleController;
import org.eclipse.riena.example.client.views.DemoTargetSubModuleView;
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
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 *
 */
public class DemoTargetNodeAssembler extends AbstractNavigationAssembler {

	public final static String ID_MODULE_GROUP = "org.eclipse.riena.example.navigate.target.moduleGroup"; //$NON-NLS-1$
	public final static String ID_MODULE = "org.eclipse.riena.example.navigate.demotarget"; //$NON-NLS-1$
	public final static String ID_FIRST_SUBMODULE = "org.eclipse.riena.example.navigate.target.submodule"; //$NON-NLS-1$

	private Set<String> knownTargetIds = null;

	public boolean acceptsToBuildNode(final NavigationNodeId nodeId, final NavigationArgument argument) {
		if (knownTargetIds == null) {
			knownTargetIds = new HashSet<String>(Arrays.asList(ID_MODULE, ID_FIRST_SUBMODULE + "4")); //$NON-NLS-1$
			knownTargetIds = Collections.unmodifiableSet(knownTargetIds);
		}

		return knownTargetIds.contains(nodeId.getTypeId());
	}

	public INavigationNode<?>[] buildNode(final NavigationNodeId nodeId, final NavigationArgument navigationArgument) {
		final IModuleGroupNode moduleGroup = new ModuleGroupNode(new NavigationNodeId(ID_MODULE_GROUP));
		moduleGroup.setPresentWithSingleModule(false);

		IModuleNode module = new ModuleNode(new NavigationNodeId(ID_MODULE), "Target Module1"); //$NON-NLS-1$ 
		module.setIcon(ExampleIcons.ICON_GREEN_LED);
		moduleGroup.addChild(module);

		ISubModuleNode subModule = new SubModuleNode(new NavigationNodeId(ID_FIRST_SUBMODULE), "Target"); //$NON-NLS-1$ 
		IWorkareaDefinition def = WorkareaManager.getInstance().registerDefinition(subModule,
				DemoTargetSubModuleController.class, DemoTargetSubModuleView.ID);
		def.setRequiredPreparation(true);
		module.addChild(subModule);

		subModule = new SubModuleNode(new NavigationNodeId(ID_FIRST_SUBMODULE + "2"), "Target2"); //$NON-NLS-1$ //$NON-NLS-2$
		def = WorkareaManager.getInstance().registerDefinition(subModule, DemoTargetSubModuleController.class,
				DemoTargetSubModuleView.ID);
		def.setRequiredPreparation(true);
		module.addChild(subModule);

		module = new ModuleNode(new NavigationNodeId(ID_MODULE + "2"), "Target Module2"); //$NON-NLS-1$ //$NON-NLS-2$ 
		module.setIcon(ExampleIcons.ICON_GREEN_LED);
		moduleGroup.addChild(module);

		subModule = new SubModuleNode(new NavigationNodeId(ID_FIRST_SUBMODULE + "3"), "Target3"); //$NON-NLS-1$ //$NON-NLS-2$ 
		def = WorkareaManager.getInstance().registerDefinition(subModule, DemoTargetSubModuleController.class,
				DemoTargetSubModuleView.ID);
		def.setRequiredPreparation(true);
		module.addChild(subModule);

		subModule = new SubModuleNode(new NavigationNodeId(ID_FIRST_SUBMODULE + "4"), "Target4"); //$NON-NLS-1$ //$NON-NLS-2$
		def = WorkareaManager.getInstance().registerDefinition(subModule, DemoTargetSubModuleController.class,
				DemoTargetSubModuleView.ID);
		def.setRequiredPreparation(true);
		module.addChild(subModule);

		return new INavigationNode<?>[] { moduleGroup };
	}

}
