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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.AbstractNavigationAssembler;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;

public class TestSecondModuleGroupNodeAssembler extends AbstractNavigationAssembler {

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#buildNode(org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?>[] buildNode(final NavigationNodeId navigationNodeId,
			final NavigationArgument navigationArgument) {
		final IModuleGroupNode moduleGroup = new ModuleGroupNode(navigationNodeId);
		final IModuleNode module = new ModuleNode(new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.secondModule"));
		moduleGroup.addChild(module);
		final ISubModuleNode subModule = new SubModuleNode(new NavigationNodeId(
				"org.eclipse.riena.navigation.model.test.secondSubModule"));
		module.addChild(subModule);
		return new IModuleGroupNode[] { moduleGroup };
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationAssembler#acceptsTargetId(String)
	 */
	public boolean acceptsToBuildNode(final NavigationNodeId nodeId, final NavigationArgument argument) {

		return nodeId.getTypeId().equals("org.eclipse.riena.navigation.model.test.secondModuleGroup")
				|| nodeId.getTypeId().equals("org.eclipse.riena.navigation.model.test.secondModule")
				|| nodeId.getTypeId().equals("org.eclipse.riena.navigation.model.test.secondSubModule");
	}

}
