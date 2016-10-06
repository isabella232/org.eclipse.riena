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
import org.eclipse.riena.ui.ridgets.IImageButtonRidget;

/**
 *
 */
public class SvgButtonExampleController extends SubModuleController {

	@Override
	public void configureRidgets() {
		//Button

		final IActionRidget btnDImage = getRidget(IActionRidget.class, "btnDImage"); //$NON-NLS-1$
		btnDImage.setIcon("cloud", IconSize.D48); //$NON-NLS-1$

		final IActionRidget btnCImage = getRidget(IActionRidget.class, "btnCImage"); //$NON-NLS-1$
		btnCImage.setIcon("cloud", IconSize.C32); //$NON-NLS-1$

		final IActionRidget btnBImage = getRidget(IActionRidget.class, "btnBImage"); //$NON-NLS-1$
		btnBImage.setIcon("cloud", IconSize.B22); //$NON-NLS-1$

		final IActionRidget btnAImage = getRidget(IActionRidget.class, "btnAImage"); //$NON-NLS-1$
		btnAImage.setIcon("cloud", IconSize.A16); //$NON-NLS-1$

		final IActionRidget btnA = getRidget(IActionRidget.class, "btn1ParamA"); //$NON-NLS-1$
		btnA.setIcon("clouda"); //$NON-NLS-1$

		final IActionRidget btnB = getRidget(IActionRidget.class, "btn1ParamB"); //$NON-NLS-1$
		btnB.setIcon("cloudb"); //$NON-NLS-1$

		final IActionRidget btnC = getRidget(IActionRidget.class, "btn1ParamC"); //$NON-NLS-1$
		btnC.setIcon("cloudc"); //$NON-NLS-1$

		final IActionRidget btnD = getRidget(IActionRidget.class, "btn1ParamD"); //$NON-NLS-1$
		btnD.setIcon("cloudd"); //$NON-NLS-1$

		//ImageButton
		final IImageButtonRidget imageButtonRidget = getRidget(IImageButtonRidget.class, "imageButton"); //$NON-NLS-1$
		imageButtonRidget.setIcon("cloud", IconSize.A16); //$NON-NLS-1$

		final IImageButtonRidget imageButtonRidgetNoIconSize = getRidget(IImageButtonRidget.class, "imageButtonNoIconSize"); //$NON-NLS-1$
		imageButtonRidgetNoIconSize.setIcon("test"); //$NON-NLS-1$

		final IImageButtonRidget imageButtonRidgetB22 = getRidget(IImageButtonRidget.class, "imageButtonB22"); //$NON-NLS-1$
		imageButtonRidgetB22.setIcon("test", IconSize.B22); //$NON-NLS-1$

	}

}
