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
import org.eclipse.riena.example.client.controllers.CustomerDetailSubModuleController;
import org.eclipse.riena.example.client.controllers.NavigationSubModuleController;
import org.eclipse.riena.example.client.views.CustomerDetailSubModuleView;
import org.eclipse.riena.example.client.views.NavigationSubModuleView;
import org.eclipse.riena.navigation.AbstractNavigationAssembler;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.workarea.WorkareaManager;

public class NavigationSubApplicationNodeAssembler extends AbstractNavigationAssembler {

	private Set<String> knownTargetIds = null;

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#buildNode(org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?>[] buildNode(final NavigationNodeId navigationNodeId,
			final NavigationArgument navigationArgument) {

		final ISubApplicationNode subApplication = new SubApplicationNode(new NavigationNodeId(
				"org.eclipse.riena.example.navigation.subapplication"), "Na&vigation"); //$NON-NLS-1$ //$NON-NLS-2$
		subApplication.setIcon(ExampleIcons.ICON_APPLICATION);
		WorkareaManager.getInstance().registerDefinition(subApplication, "subapplication.1", false); //$NON-NLS-1$
		subApplication.setSelected(true);

		IModuleGroupNode moduleGroup = new ModuleGroupNode(new NavigationNodeId(
				"org.eclipse.riena.example.moduleGroup1.1.1")); //$NON-NLS-1$ 
		moduleGroup.setLabel("ModuleGroup 1.1"); //$NON-NLS-1$
		subApplication.addChild(moduleGroup);
		IModuleNode module = new ModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.module.1.1.1"), "Module 1.1.1"); //$NON-NLS-1$ //$NON-NLS-2$ 
		module.setIcon(ExampleIcons.ICON_APPLICATION);
		moduleGroup.addChild(module);
		ISubModuleNode subModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.customerDetail", "1.1.1.1"), "SubModule 1.1.1.1"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		subModule.setIcon(ExampleIcons.ICON_FILE);
		WorkareaManager.getInstance().registerDefinition(subModule, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		module.addChild(subModule);

		ISubModuleNode subModule2 = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.customerDetail", "1.1.1.1.1"), "SubModule 1.1.1.1.1"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		WorkareaManager.getInstance().registerDefinition(subModule2, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		subModule.addChild(subModule2);

		subModule2 = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail", "1.1.1.1.2"), "SubModule 1.1.1.1.2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		WorkareaManager.getInstance().registerDefinition(subModule2, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		subModule.addChild(subModule2);
		// expand
		subModule.setExpanded(true);

		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail", "1.1.1.2"), "SubModule 1.1.1.2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		WorkareaManager.getInstance().registerDefinition(subModule, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		module.addChild(subModule);
		module = new ModuleNode(new NavigationNodeId("module", "1.1.2"), "Module 1.1.2 (closeable)"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		module.setIcon(ExampleIcons.ICON_HOMEFOLDER);
		moduleGroup.addChild(module);
		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail", "1.1.2.1"), "SubModule 1.1.2.1"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		WorkareaManager.getInstance().registerDefinition(subModule, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		module.addChild(subModule);
		/* NEW */
		subModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.navigation"), "Navigation"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(subModule, NavigationSubModuleController.class,
				NavigationSubModuleView.ID, false);
		module.addChild(subModule);

		moduleGroup = new ModuleGroupNode(new NavigationNodeId("moduleGroup", "1.2")); //$NON-NLS-1$ //$NON-NLS-2$
		moduleGroup.setLabel("ModuleGroup 1.2"); //$NON-NLS-1$
		moduleGroup.setPresentWithSingleModule(false);
		subApplication.addChild(moduleGroup);
		module = new ModuleNode(new NavigationNodeId("module121"), "Module 1.2.1 (not closeable)"); //$NON-NLS-1$ //$NON-NLS-2$
		module.setClosable(false);
		module.setIcon(ExampleIcons.ICON_RED_LED);
		module.setToolTipText("You can't close this module!"); //$NON-NLS-1$
		moduleGroup.addChild(module);
		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail", "1.2.1.1"), "SubModule 1.2.1.1"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		WorkareaManager.getInstance().registerDefinition(subModule, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		module.addChild(subModule);
		subModule = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.customerDetail", "1.2.1.2"), "SubModule 1.2.1.2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		WorkareaManager.getInstance().registerDefinition(subModule, CustomerDetailSubModuleController.class,
				CustomerDetailSubModuleView.ID, false);
		module.addChild(subModule);

		return new ISubApplicationNode[] { subApplication };
	}

	@Override
	public String getParentNodeId() {
		return "application"; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#acceptsTargetId(String)
	 */
	public boolean acceptsToBuildNode(final NavigationNodeId nodeId, final NavigationArgument argument) {

		if (knownTargetIds == null) {
			knownTargetIds = new HashSet<String>(Arrays.asList("org.eclipse.riena.example.navigation.subapplication", //$NON-NLS-1$
					"org.eclipse.riena.example.customerDetail", //$NON-NLS-1$
					"org.eclipse.riena.example.navigation", //$NON-NLS-1$
					"org.eclipse.riena.example.module.1.1.1" //$NON-NLS-1$
			));
			knownTargetIds = Collections.unmodifiableSet(knownTargetIds);
		}

		return knownTargetIds.contains(nodeId.getTypeId());
	}
}
