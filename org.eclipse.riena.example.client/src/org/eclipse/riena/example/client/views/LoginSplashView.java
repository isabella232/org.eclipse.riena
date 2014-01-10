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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.example.client.controllers.LoginDialogController;
import org.eclipse.riena.example.client.nls.Messages;
import org.eclipse.riena.navigation.ui.swt.login.AbstractLoginSplashView;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * The view for the login splash dialog of the example.
 */
public class LoginSplashView extends AbstractLoginSplashView {

	private static final GridData GD11FILL = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
	private static final GridData GD21FILL = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
	private static final GridData GD11RIGHTBOTTOMFILL = new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 1, 1);
	private static final GridData GD11LEFTBOTTOMFILL = new GridData(SWT.LEFT, SWT.BOTTOM, true, true, 1, 1);

	@Override
	protected AbstractWindowController createController() {
		return new LoginDialogController();
	}

	@Override
	protected Control buildView(final Composite parent) {
		addUIControl(parent.getShell(), AbstractWindowController.RIDGET_ID_WINDOW);
		GridLayoutFactory.fillDefaults().numColumns(2).margins(5, 10).applyTo(parent);

		return createContentView(parent);
	}

	private Composite createContentView(final Composite parent) {

		final Label infoArea = new Label(parent, SWT.NONE);
		infoArea.setText(Messages.LoginSplashView_infoArea);
		infoArea.setLayoutData(GD11LEFTBOTTOMFILL);

		final Composite inputArea = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(4).spacing(6, 9).equalWidth(false).applyTo(inputArea);
		inputArea.setLayoutData(GD11RIGHTBOTTOMFILL);
		// Force composite to inherit the splash background
		inputArea.setBackgroundMode(SWT.INHERIT_DEFAULT);

		// dummy to occupy the cell
		new Label(inputArea, SWT.NONE).setText(""); //$NON-NLS-1$

		new Label(inputArea, SWT.NONE).setText(Messages.LoginSplashView_user);
		final Text user = UIControlsFactory.createText(inputArea);
		user.setLayoutData(GD21FILL);
		addUIControl(user, LoginDialogController.RIDGET_ID_USER);

		// dummy to occupy the cell
		new Label(inputArea, SWT.NONE);

		new Label(inputArea, SWT.NONE).setText(Messages.LoginSplashView_password);
		final Text password = UIControlsFactory.createText(inputArea);
		password.setLayoutData(GD21FILL);
		addUIControl(password, LoginDialogController.RIDGET_ID_PASSWORD);

		// dummy to occupy the cell
		new Label(inputArea, SWT.NONE);
		// dummy to occupy the cell
		new Label(inputArea, SWT.NONE);

		final Button okButton = UIControlsFactory.createButton(inputArea);
		okButton.setText(Messages.LoginSplashView_login);
		okButton.setLayoutData(GD11FILL);
		addUIControl(okButton, LoginDialogController.RIDGET_ID_OK);

		final Button cancelButton = UIControlsFactory.createButton(inputArea);
		cancelButton.setText(Messages.LoginSplashView_cancel);
		cancelButton.setLayoutData(GD11FILL);
		addUIControl(cancelButton, LoginDialogController.RIDGET_ID_CANCEL);

		addUIControl(UIControlsFactory.createMessageBox(inputArea),
				LoginDialogController.RIDGET_ID_MESSAGE_LOGIN_EXCEPTION);

		return inputArea;
	}
}
