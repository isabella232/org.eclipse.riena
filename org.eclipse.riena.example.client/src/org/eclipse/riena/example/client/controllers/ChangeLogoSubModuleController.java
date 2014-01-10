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

import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.annotation.OnActionCallback;
import org.eclipse.riena.ui.ridgets.holder.SelectableListHolder;

public class ChangeLogoSubModuleController extends SubModuleController {
	private static final String NULL = "null (from LnF)";

	private SelectableListHolder<String> listHolder;

	public ChangeLogoSubModuleController() {
		this(null);
	}

	public ChangeLogoSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		final IComboRidget r = getRidget(IComboRidget.class, "logoChoice");
		listHolder = new SelectableListHolder<String>("RIENA_Logo_RGB", "RIENA_Logo_RGB_red", NULL);
		// select the first element
		listHolder.setSelection(listHolder.getItems().get(0));
		r.bindToModel(listHolder, "toString");

		updateAllRidgetsFromModel();
	}

	@OnActionCallback(ridgetId = "btnChangeLogo")
	public void changeLogo() {
		final IApplicationNode app = getNavigationNode().getParentOfType(IApplicationNode.class);
		final String selection = listHolder.getSelection();
		app.setLogo(NULL.equals(selection) ? null : selection);
	}

}
