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

import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.ridgets.ILabelRidget;

/**
 *
 */
public class SvgSubModuleController extends SubModuleController {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		final ILabelRidget lblX = getRidget(ILabelRidget.class, "lblX");
		lblX.setIcon("cloud", IconSize.B22);

		final ILabelRidget lblY = getRidget(ILabelRidget.class, "lblY");
		lblY.setIcon("cloud", IconSize.E64);
	}
}
