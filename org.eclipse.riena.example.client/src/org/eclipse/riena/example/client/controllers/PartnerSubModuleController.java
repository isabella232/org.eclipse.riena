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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 *
 */
public class PartnerSubModuleController extends SubModuleController {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.ui.controllers.SubModuleController#
	 * configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		super.configureRidgets();

		final ITextRidget kundennummer = getRidget(ITextRidget.class, "customerNr"); //$NON-NLS-1$
		final IActionRidget setzen = getRidget(IActionRidget.class, "set"); //$NON-NLS-1$
		setzen.addListener(new IActionListener() {
			public void callback() {
				final String instanceId = kundennummer.getText();
				if (StringUtils.isGiven(instanceId)) {
					final ISubModuleNode sm = getNavigationNode();
					final IModuleNode m = sm.getParentOfType(IModuleNode.class);
					final IModuleGroupNode mg = sm.getParentOfType(IModuleGroupNode.class);
					setNewId(mg, instanceId);
					setNewId(m, instanceId);
					setNewId(sm, instanceId);
				}
			}

			private void setNewId(final INavigationNode<?> node, final String instanceId) {
				final NavigationNodeId oldId = node.getNodeId();
				final NavigationNodeId newId = new NavigationNodeId(oldId.getTypeId(), instanceId);
				node.setNodeId(newId);
			}
		});
	}

}
