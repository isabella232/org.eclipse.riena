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

import org.eclipse.riena.example.client.views.HelloDialogView;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;

/**
 * Controller for the {@link DialogSubModuleController} example.
 */
public class DialogSubModuleController extends SubModuleController {

	public static final String RIDGET_ID_OPEN_DIALOG = "openDialog"; //$NON-NLS-1$

	public DialogSubModuleController() {
		super(null);
	}

	@Override
	public void configureRidgets() {

		final IActionRidget openShellAction = getRidget(IActionRidget.class, RIDGET_ID_OPEN_DIALOG);
		openShellAction.setText("&Open dialog"); //$NON-NLS-1$
		openShellAction.addListener(new IActionListener() {

			private HelloDialogView dialog;

			public void callback() {
				if (dialog == null) {
					dialog = new HelloDialogView();
				}
				// the dialog controller is now available
				dialog.getController().setContext("key", "value"); //$NON-NLS-1$//$NON-NLS-2$
				final int result = dialog.open();
				if (result == AbstractWindowController.OK) {
					System.out.println("OK pressed"); //$NON-NLS-1$
				} else {
					System.out.println("Cancel pressed"); //$NON-NLS-1$
				}
			}
		});
	}
}
