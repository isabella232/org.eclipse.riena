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

import java.net.URL;

import javax.security.auth.login.LoginException;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.example.client.application.ExampleIcons;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.internal.example.client.security.authentication.LocalLoginCallbackHandler;
import org.eclipse.riena.navigation.ui.application.AbstractApplication;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;

/**
 * The controller for the login dialog of the example.
 */
public class LoginDialogController extends AbstractWindowController {

	public static final String RIDGET_ID_USER = "user"; //$NON-NLS-1$
	public static final String RIDGET_ID_PASSWORD = "password"; //$NON-NLS-1$
	public static final String RIDGET_ID_OK = "okButton"; //$NON-NLS-1$
	public static final String RIDGET_ID_CANCEL = "cancelButton"; //$NON-NLS-1$
	public static final String RIDGET_ID_MESSAGE_LOGIN_EXCEPTION = "messageLoginException"; //$NON-NLS-1$
	public static final int EXIT_OK = IApplication.EXIT_OK;
	public static final int EXIT_ABORT = AbstractApplication.EXIT_ABORT;

	private static final String JAAS_CONFIG_FILE = "config/sample_jaas.config"; //$NON-NLS-1$
	private boolean canLogin;

	@Override
	public void afterBind() {
		super.afterBind();
		getRidget(ITextRidget.class, RIDGET_ID_USER).requestFocus();
	}

	@Override
	public void configureRidgets() {
		super.configureRidgets();

		getWindowRidget().setTitle("Riena login"); //$NON-NLS-1$
		getWindowRidget().setIcon(ExampleIcons.ICON_SAMPLE);

		final ITextRidget user = getRidget(ITextRidget.class, RIDGET_ID_USER);
		user.setMandatory(true);
		final ITextRidget password = getRidget(ITextRidget.class, RIDGET_ID_PASSWORD);
		password.setMandatory(true);

		final IActionRidget okAction = getRidget(IActionRidget.class, RIDGET_ID_OK);
		okAction.addListener(new IActionListener() {
			public void callback() {
				canLogin = checkLogin();
				if (canLogin) {
					dispose(EXIT_OK);
				} else {
					user.requestFocus();
				}
			}
		});
		addDefaultAction(getWindowRidget(), okAction);

		final IActionRidget cancelAction = getRidget(IActionRidget.class, RIDGET_ID_CANCEL);
		cancelAction.addListener(new IActionListener() {
			public void callback() {
				dispose(EXIT_ABORT);
			}
		});
	}

	public void onClose() {
		setReturnCode(canLogin ? EXIT_OK : EXIT_ABORT);
	}

	// helping methods
	//////////////////

	private boolean checkLogin() {
		// do not use server authentication in case user = "" and password=""
		final String userId = getRidget(ITextRidget.class, RIDGET_ID_USER).getText();
		final String password = getRidget(ITextRidget.class, RIDGET_ID_PASSWORD).getText();
		if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(password)) {
			return true;
		}

		// set the user, password that the authenticating callback handler will set (as user input)
		LocalLoginCallbackHandler.setSuppliedCredentials(getRidget(ITextRidget.class, RIDGET_ID_USER).getText(),
				getRidget(ITextRidget.class, RIDGET_ID_PASSWORD).getText());

		final URL configUrl = Activator.getDefault().getContext().getBundle().getEntry(JAAS_CONFIG_FILE);
		final ILoginContext secureContext = LoginContextFactory.createContext("Remote", configUrl); //$NON-NLS-1$

		try {
			secureContext.login();
			return true;
		} catch (final LoginException e) {
			showMessage(e);
			return false;
		}
	}

	private void dispose(final int result) {
		getWindowRidget().dispose();
		setReturnCode(result);
	}

	private void showMessage(final LoginException e) {
		final IMessageBoxRidget messageLoginException = getRidget(IMessageBoxRidget.class, RIDGET_ID_MESSAGE_LOGIN_EXCEPTION);
		messageLoginException.setType(IMessageBoxRidget.Type.ERROR);
		messageLoginException.setTitle("Login exception"); //$NON-NLS-1$
		messageLoginException.setText(e.getMessage()); //$NON-NLS-1
		messageLoginException.show();
	}
}
