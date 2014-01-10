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
package org.eclipse.riena.security.authentication.callbackhandler;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;

/**
 * JAAS Callbackhandler for testing. The callbackhandler will always return when
 * asked the name and password that was set in the constructor. So it can be
 * used for a loopback kind of test.
 */
public class TestLocalCallbackHandler implements CallbackHandler {

	private static String name;
	private static String password;

	public static void setSuppliedCredentials(final String nameParm, final String passwordParm) {
		name = nameParm;
		password = passwordParm;
	}

	/**
	 * 
	 */
	public TestLocalCallbackHandler() {
		super();
	}

	//	public TestLocalCallbackHandler(String name, String password) {
	//		this.name = name;
	//		this.password = password;
	//	}

	public void handle(final Callback[] callbacks) {
		for (final Callback callback : callbacks) {
			if (callback instanceof NameCallback) {
				final NameCallback nc = (NameCallback) callback;
				nc.setName(name);
			} else {
				if (callback instanceof PasswordCallback) {
					final PasswordCallback pc = (PasswordCallback) callback;
					pc.setPassword(password.toCharArray());
				} else {
					if (callback instanceof TextOutputCallback) {
						final TextOutputCallback toc = (TextOutputCallback) callback;
						String typeAsString;
						// detect text output message type and translate it
						// to string
						switch (toc.getMessageType()) {
						case TextOutputCallback.INFORMATION:
							typeAsString = "information";
							break;
						case TextOutputCallback.ERROR:
							typeAsString = "error";
							break;
						case TextOutputCallback.WARNING:
							typeAsString = "warning";
							break;
						default:
							typeAsString = "unknown";
							break;
						}
						System.out.println(typeAsString + " " + toc.getMessage());
					}
				}
			}
		}
	}
}