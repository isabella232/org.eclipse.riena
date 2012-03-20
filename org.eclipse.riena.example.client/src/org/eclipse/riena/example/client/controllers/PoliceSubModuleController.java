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
package org.eclipse.riena.example.client.controllers;

import java.util.List;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 *
 */
public class PoliceSubModuleController extends SubModuleController {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.ui.controllers.SubModuleController#
	 * configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		super.configureRidgets();
		final IActionRidget setzen = getRidget(IActionRidget.class, "move"); //$NON-NLS-1$
		setzen.addListener(new IActionListener() {
			public void callback() {
				final IModuleNode m = getNavigationNode().getParentOfType(IModuleNode.class);
				final INavigationNode<?> targetNode = getTarget("org.eclipse.riena.example.client.mgkundennummer"); //$NON-NLS-1$
				if (targetNode != null) {
					m.moveTo(targetNode.getNodeId());
				}
			}

			private INavigationNode<?> getTarget(final String typeId) {
				final ISubApplicationNode subApp = getNavigationNode().getParentOfType(ISubApplicationNode.class);
				final List<IModuleGroupNode> children = subApp.getChildren();
				for (final IModuleGroupNode child : children) {
					if (child.getNodeId() != null) {
						if (StringUtils.equals(child.getNodeId().getTypeId(), typeId)) {
							return child;
						}
					}
				}
				return null;
			}
		});
	}
}
