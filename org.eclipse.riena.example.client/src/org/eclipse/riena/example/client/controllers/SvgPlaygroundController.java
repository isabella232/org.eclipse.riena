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
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;

/**
 *
 */
public class SvgPlaygroundController extends SubModuleController {
	@Override
	public void configureRidgets() {
		final IActionRidget button1 = getRidget(IActionRidget.class, "button1");
		final IActionRidget button2 = getRidget(IActionRidget.class, "button2");
		final IActionRidget button3 = getRidget(IActionRidget.class, "button3");
		final ILabelRidget lbl = getRidget(ILabelRidget.class, "lbl");
		button1.setIcon("abba", IconSize.A16);
		button2.setIcon("cloud");

	}
}
