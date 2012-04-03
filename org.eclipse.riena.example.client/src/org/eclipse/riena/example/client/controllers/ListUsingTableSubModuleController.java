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

import org.eclipse.riena.example.client.views.TextSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.ui.ridgets.ITableRidget;

/**
 * Controller for the {@link TextSubModuleView} example.
 */
public class ListUsingTableSubModuleController extends ListSubModuleController {

	public ListUsingTableSubModuleController() {
		this(null);
	}

	public ListUsingTableSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	@Override
	protected ITableRidget getListRidget() {
		return getRidget(ITableRidget.class, "listPersons"); //$NON-NLS-1$
	}

}
