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

import org.eclipse.riena.navigation.INavigationContext;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.MessageBoxOption;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

public class AllowsDisposeSubModuleController extends SubModuleController {

	private boolean allowsDispose;
	private IMessageBoxRidget messageBoxRidget;
	private static final MessageBoxOption NAVIGATE_TO_CONTROLLER = new IMessageBoxRidget.MessageBoxOption("Navigate"); //$NON-NLS-1$
	private static final MessageBoxOption CLOSE_NEVERTHELESS = new IMessageBoxRidget.MessageBoxOption("Close"); //$NON-NLS-1$

	@Override
	public void configureRidgets() {
		final IToggleButtonRidget checkBoxRidget = getRidget(IToggleButtonRidget.class, "allowsDispose"); //$NON-NLS-1$
		checkBoxRidget.addListener(new IActionListener() {
			public void callback() {
				if (checkBoxRidget.isSelected()) {
					allowsDispose = true;
				} else {
					allowsDispose = false;
				}
			}
		});
		checkBoxRidget.setSelected(true);

		messageBoxRidget = getRidget(IMessageBoxRidget.class, "messageBox"); //$NON-NLS-1$
		messageBoxRidget
				.setText("The Controller \"" + getNavigationNode().getLabel() + "\" does not allow disposing.\n Do you want to close the application nevertheless or navigate to the controller?"); //$NON-NLS-1$ //$NON-NLS-2$
		final MessageBoxOption[] options = new MessageBoxOption[] { CLOSE_NEVERTHELESS, NAVIGATE_TO_CONTROLLER };
		messageBoxRidget.setOptions(options);
	}

	@Override
	public boolean allowsDispose(final INavigationNode<?> node, final INavigationContext context) {
		if (!allowsDispose) {
			final IMessageBoxRidget.MessageBoxOption selectedOption = messageBoxRidget.show();
			if (selectedOption.equals(NAVIGATE_TO_CONTROLLER)) {
				getNavigationNode().navigate(new NavigationNodeId("org.eclipse.riena.example.client.subModule.allowsDispose")); //$NON-NLS-1$
			} else if (selectedOption.equals(CLOSE_NEVERTHELESS)) {
				return true;
			}
		}
		return allowsDispose && super.allowsDispose(node, context);
	}
}
