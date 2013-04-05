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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.navigation.INavigationContext;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;

/**
 * This controller shows a message box if it's not allowed to activate this
 * sub-module.
 */
public class AllowActivateSubModuleController extends SubModuleController {

	private IMessageBoxRidget messageBox;

	@Override
	public void configureRidgets() {

		super.configureRidgets();

		messageBox = getRidget(IMessageBoxRidget.class, "messageBox"); //$NON-NLS-1$
		messageBox.setTitle(getNavigationNode().getLabel());
		messageBox.setText("Change value in the previous sub-module and \ntry it again."); //$NON-NLS-1$
		messageBox.setOptions(IMessageBoxRidget.OPTIONS_OK);
		messageBox.setType(IMessageBoxRidget.Type.INFORMATION);

	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Shows a message box if it's not allowed to activate this sub-module.
	 */
	@Override
	public boolean allowsActivate(final INavigationNode<?> pNode, final INavigationContext context) {

		final Object contextValue = getNavigationNode().getContext("allow"); //$NON-NLS-1$
		if (contextValue instanceof Boolean) {
			if ((Boolean) contextValue) {
				return true;
			}
		}

		messageBox.show();
		return false;

	}

}
