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
import org.eclipse.riena.example.client.controllers.SharedViewDemoSecondSubModuleController;
import org.eclipse.riena.example.client.controllers.SharedViewDemoSubModuleController;
import org.eclipse.riena.example.client.views.SharedViewDemoSubModuleView;
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

public class SharedViewsNodeAssembler extends AbstractNavigationAssembler {

	private static final String TYPE_ID_SHARED_VIEW = "org.eclipse.riena.example.sharedView"; //$NON-NLS-1$
	private static final String TYPE_ID_SHARED_VIEWS = "org.eclipse.riena.example.sharedViews"; //$NON-NLS-1$

	private Set<String> knownTargetIds = null;

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#buildNode(org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?>[] buildNode(final NavigationNodeId navigationNodeId,
			final NavigationArgument navigationArgument) {
		final IModuleGroupNode moduleGroup = new ModuleGroupNode(new NavigationNodeId(TYPE_ID_SHARED_VIEWS));

		final IModuleNode sharedViewModule = new ModuleNode(null, "Shared View Demo"); //$NON-NLS-1$
		sharedViewModule.setIcon(ExampleIcons.ICON_SAMPLE);
		moduleGroup.addChild(sharedViewModule);

		final ISubModuleNode sharedViewSm1 = new SubModuleNode(
				new NavigationNodeId(TYPE_ID_SHARED_VIEW, "1"), "Node 1 (shared)"); //$NON-NLS-1$ //$NON-NLS-2$ 
		WorkareaManager.getInstance().registerDefinition(sharedViewSm1, SharedViewDemoSubModuleController.class,
				SharedViewDemoSubModuleView.ID, true);
		sharedViewModule.addChild(sharedViewSm1);

		final ISubModuleNode sharedViewSm2 = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.sharedViewNotShared", "2"), "Node 2 (not shared)"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		WorkareaManager.getInstance().registerDefinition(sharedViewSm2, SharedViewDemoSubModuleController.class,
				SharedViewDemoSubModuleView.ID + "NotShared", false); //$NON-NLS-1$
		sharedViewModule.addChild(sharedViewSm2);

		final ISubModuleNode sharedViewSm3 = new SubModuleNode(
				new NavigationNodeId(TYPE_ID_SHARED_VIEW, "3"), "Node 3 (shared)"); //$NON-NLS-1$ //$NON-NLS-2$ 
		WorkareaManager.getInstance().registerDefinition(sharedViewSm3, SharedViewDemoSubModuleController.class,
				SharedViewDemoSubModuleView.ID, true);
		sharedViewModule.addChild(sharedViewSm3);
		
		final ISubModuleNode sharedViewSm4 = new SubModuleNode(
				new NavigationNodeId(TYPE_ID_SHARED_VIEW, "4"), "Node 4 (shared with other controller)"); //$NON-NLS-1$ //$NON-NLS-2$ 
		WorkareaManager.getInstance().registerDefinition(sharedViewSm4, SharedViewDemoSecondSubModuleController.class,
				SharedViewDemoSubModuleView.ID, true);
		sharedViewModule.addChild(sharedViewSm4);

		return new IModuleGroupNode[] { moduleGroup };
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#acceptsTargetId(String)
	 */
	public boolean acceptsToBuildNode(final NavigationNodeId nodeId, final NavigationArgument argument) {

		if (knownTargetIds == null) {
			knownTargetIds = new HashSet<String>(Arrays.asList(TYPE_ID_SHARED_VIEWS, TYPE_ID_SHARED_VIEW));
			knownTargetIds = Collections.unmodifiableSet(knownTargetIds);
		}

		return knownTargetIds.contains(nodeId.getTypeId());
	}
}
