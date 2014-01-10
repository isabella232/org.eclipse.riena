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

import org.eclipse.riena.example.client.controllers.ListSubModuleController;
import org.eclipse.riena.example.client.views.ListSubModuleView;
import org.eclipse.riena.navigation.AbstractNavigationAssembler;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 *
 */
public class OrdinalIndex10NodeAssembler extends AbstractNavigationAssembler {

	private Set<String> knownTargetIds;

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#acceptsTargetId(String)
	 */
	public boolean acceptsToBuildNode(final NavigationNodeId nodeId, final NavigationArgument argument) {

		if (knownTargetIds == null) {
			knownTargetIds = new HashSet<String>(Arrays.asList("org.eclipse.riena.example.navigate.ordinal10SubModule" //$NON-NLS-1$
					));
			knownTargetIds = Collections.unmodifiableSet(knownTargetIds);
		}

		return knownTargetIds.contains(nodeId.getTypeId());
	}

	public INavigationNode<?>[] buildNode(final NavigationNodeId nodeId, final NavigationArgument navigationArgument) {
		final SubModuleNode subModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.example.navigate.ordinal10SubModule"), "Ordinal 10"); //$NON-NLS-1$ //$NON-NLS-2$
		WorkareaManager.getInstance().registerDefinition(subModule, ListSubModuleController.class,
				ListSubModuleView.ID, false);
		return new INavigationNode<?>[] { subModule };
	}

}
