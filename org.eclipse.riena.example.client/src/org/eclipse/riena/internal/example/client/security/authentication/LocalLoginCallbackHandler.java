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
package org.eclipse.riena.internal.example.client.security.authentication;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;

/**
 * JAAS Callback handler for testing. The callback handler will always return
 * when asked the name and password that was set in the constructor. So it can
 * be used for a loopback kind of test.
 */
public class LocalLoginCallbackHandler implements CallbackHandler {

	private static String name;
	private static String password;

	public static void setSuppliedCredentials(final String nameParm, final String passwordParm) {
		name = nameParm;
		password = passwordParm;
	}

	/**
	 * 
	 */
	public LocalLoginCallbackHandler() {
		super();
	}

	public void handle(final Callback[] callbacks) {
		for (final Callback callback : callbacks) {
			if (callback instanceof NameCallback) {
				final NameCallback nc = (NameCallback) callback;
				nc.setName(name);
			} else {
				if (callback instanceof PasswordCallback) {
					final PasswordCallback pc = (PasswordCallback) callback;
					if (password == null) {
						pc.setPassword(null);
					} else {
						pc.setPassword(password.toCharArray());
					}
				} else {
					if (callback instanceof TextOutputCallback) {
						final TextOutputCallback toc = (TextOutputCallback) callback;
						String typeAsString;
						// detect text output message type and translate it to string
						switch (toc.getMessageType()) {
						case TextOutputCallback.INFORMATION:
							typeAsString = "information"; //$NON-NLS-1$
							break;
						case TextOutputCallback.ERROR:
							typeAsString = "error"; //$NON-NLS-1$
							break;
						case TextOutputCallback.WARNING:
							typeAsString = "warning"; //$NON-NLS-1$
							break;
						default:
							typeAsString = "unknown"; //$NON-NLS-1$
							break;
						}
						System.out.println(typeAsString + " " + toc.getMessage()); //$NON-NLS-1$
					}
				}
			}
		}
	}
}