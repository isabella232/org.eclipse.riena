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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.example.client.controllers.LoginDialogController;
import org.eclipse.riena.navigation.ui.login.ILoginDialogView;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.ridgets.swt.views.AbstractDialogView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * The view for the login dialog of the example.
 */
public class LoginDialogView extends AbstractDialogView implements ILoginDialogView {

	public LoginDialogView() {
		super(null);
	}

	@Override
	public boolean close() {
		((LoginDialogController) getController()).onClose();
		return super.close();
	}

	public int getResult() {
		return getReturnCode();
	}

	@Override
	protected AbstractWindowController createController() {
		return new LoginDialogController();
	}

	@Override
	protected Control buildView(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 20).applyTo(parent);

		final Composite content = createContentView(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(content);

		return content;
	}

	private Composite createContentView(final Composite parent) {

		final Composite content = UIControlsFactory.createComposite(parent);
		content.setLayout(new GridLayout()); // create a simple GridLayout so SWT-Designer can parse it
		GridLayoutFactory.fillDefaults().numColumns(4).spacing(6, 9).equalWidth(false).applyTo(content);

		// dummy to occupy the cell
		UIControlsFactory.createLabel(content, "                  "); //$NON-NLS-1$

		UIControlsFactory.createLabel(content, "User"); //$NON-NLS-1$
		final Text user = UIControlsFactory.createText(content);
		user.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		addUIControl(user, LoginDialogController.RIDGET_ID_USER);

		// dummy to occupy the cell
		UIControlsFactory.createLabel(content, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(content, "Password"); //$NON-NLS-1$
		final Text password = UIControlsFactory.createText(content);
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		addUIControl(password, LoginDialogController.RIDGET_ID_PASSWORD);

		// dummy to occupy the cell
		UIControlsFactory.createLabel(content, ""); //$NON-NLS-1$
		// dummy to occupy the cell
		UIControlsFactory.createLabel(content, ""); //$NON-NLS-1$

		final Button okButton = UIControlsFactory.createButton(content);
		okButton.setText("   Login   "); //$NON-NLS-1$
		okButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		addUIControl(okButton, LoginDialogController.RIDGET_ID_OK);

		final Button cancelButton = UIControlsFactory.createButton(content);
		cancelButton.setText("   Cancel   "); //$NON-NLS-1$
		cancelButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		addUIControl(cancelButton, LoginDialogController.RIDGET_ID_CANCEL);

		final Label info = UIControlsFactory
				.createLabel(
						content,
						"\nTo authenticate with running Sample App Server\n    type: user=john, password=john.\n    To omit it just press login."); //$NON-NLS-1$
		info.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));

		addUIControl(UIControlsFactory.createMessageBox(content),
				LoginDialogController.RIDGET_ID_MESSAGE_LOGIN_EXCEPTION);

		return content;
	}

}
