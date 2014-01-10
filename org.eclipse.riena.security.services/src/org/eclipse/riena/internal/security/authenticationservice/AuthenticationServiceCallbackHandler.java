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
package org.eclipse.riena.internal.security.authenticationservice;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.ChoiceCallback;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.LanguageCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 *
 */
public class AuthenticationServiceCallbackHandler implements CallbackHandler {

	private static ThreadLocal<Callback[]> remoteCallbacks = new ThreadLocal<Callback[]>();

	public static void setCallbacks(final Callback[] parmRemoteCallbacks) {
		remoteCallbacks.set(parmRemoteCallbacks);
	}

	public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (final Callback cb : callbacks) {
			for (final Callback rcb : remoteCallbacks.get()) {
				if (cb.getClass() != rcb.getClass()) {
					continue;
				}
				if (cb instanceof NameCallback) {
					if (((NameCallback) cb).getPrompt().equals(((NameCallback) rcb).getPrompt())) {
						((NameCallback) cb).setName(((NameCallback) rcb).getName());
						break;
					}
				} else if (cb instanceof PasswordCallback) {
					if (((PasswordCallback) cb).getPrompt().equals(((PasswordCallback) rcb).getPrompt())) {
						((PasswordCallback) cb).setPassword(((PasswordCallback) rcb).getPassword());
						break;
					}
				} else if (cb instanceof ConfirmationCallback) {
					if (((ConfirmationCallback) cb).getPrompt().equals(((ConfirmationCallback) rcb).getPrompt())) {
						((ConfirmationCallback) cb).setSelectedIndex(((ConfirmationCallback) rcb).getSelectedIndex());
						break;
					}
				} else if (cb instanceof TextInputCallback) {
					if (((TextInputCallback) cb).getPrompt().equals(((TextInputCallback) rcb).getPrompt())) {
						((TextInputCallback) cb).setText(((TextInputCallback) rcb).getText());
						break;
					}
				} else if (cb instanceof TextOutputCallback) {
					// do nothing for now
					break;
				} else if (cb instanceof LanguageCallback) {
					// do nothing for now
					break;
				} else if (cb instanceof ChoiceCallback) {
					if (((ChoiceCallback) cb).getPrompt().equals(((ChoiceCallback) rcb).getPrompt())) {
						((ChoiceCallback) cb).setSelectedIndexes(((ChoiceCallback) rcb).getSelectedIndexes());
						break;
					}
				} else {
					throw new UnsupportedOperationException("unsupported authentication callback type"); //$NON-NLS-1$
				}
			}
		}
	}
}
