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
package org.eclipse.riena.security.simpleservices.authentication.loginmodule;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.security.simpleservices.Activator;
import org.eclipse.riena.security.common.SecurityFailure;
import org.eclipse.riena.security.common.authentication.SimplePrincipal;

/**
 * Test module that implements the JAAS LoginModule interface
 * 
 */
public class SampleLoginModule implements LoginModule {

	private Subject subject;
	private CallbackHandler callbackHandler;

	private String username;
	private String password;

	private Properties accounts;

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SampleLoginModule.class);

	public boolean abort() throws LoginException {
		return false;
	}

	public boolean commit() throws LoginException {
		subject.getPrincipals().add(new SimplePrincipal(username));
		return true;
	}

	public void initialize(final Subject subject, final CallbackHandler callbackHandler,
			final Map<String, ?> sharedState, final Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;

		try {
			accounts = loadProperties((String) options.get("accounts.file")); //$NON-NLS-1$
		} catch (final IOException e) {
			throw new SecurityFailure("Reading account properties failed", e); //$NON-NLS-1$
		}
	}

	private Properties loadProperties(final String path) throws IOException {
		final URL url = Activator.getDefault().getContext().getBundle().getEntry(path);
		final Properties properties = new Properties();
		properties.load(url.openStream());
		return properties;
	}

	public boolean login() throws LoginException {
		final Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("username: "); //$NON-NLS-1$
		callbacks[1] = new PasswordCallback("password: ", false); //$NON-NLS-1$
		if (callbackHandler == null) {
			LOGGER.log(LogService.LOG_ERROR, "callbackhandler cant be null"); //$NON-NLS-1$
			return false;
		}
		try {
			callbackHandler.handle(callbacks);
			username = ((NameCallback) callbacks[0]).getName();
			final char[] password2 = ((PasswordCallback) callbacks[1]).getPassword();
			if (password2 == null) {
				password = null;
			} else {
				password = new String(password2);
			}
			if (username == null) {
				return false;
			}
			final String psw = (String) accounts.get(username);
			return psw != null && psw.equals(password);
			//			if (username != null && password != null) {
			//				if (username.equals("testuser") && password.equals("testpass")) { //$NON-NLS-1$ //$NON-NLS-2$
			//					return true;
			//				} else {
			//					if (username.equals("testuser2") && password.equals("testpass2")) { //$NON-NLS-1$ //$NON-NLS-2$
			//						return true;
			//					} else {
			//						if (username.equals("cca") && password.equals("christian")) { //$NON-NLS-1$ //$NON-NLS-2$
			//							return true;
			//						}
			//					}
			//				}
			//			}
			//
			//			return false;
		} catch (final IOException e) {
			LOGGER.log(LogService.LOG_ERROR, "Login failed", e); //$NON-NLS-1$
			throw new LoginException("Login failed because of: " + e.getMessage()); //$NON-NLS-1$
		} catch (final UnsupportedCallbackException e) {
			LOGGER.log(LogService.LOG_ERROR, "Login failed", e); //$NON-NLS-1$
			throw new LoginException("Login failed because of: " + e.getMessage()); //$NON-NLS-1$
		}
	}

	public boolean logout() throws LoginException {
		return false;
	}

}
