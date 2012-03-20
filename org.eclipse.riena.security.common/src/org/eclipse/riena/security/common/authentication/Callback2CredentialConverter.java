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
package org.eclipse.riena.security.common.authentication;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.ChoiceCallback;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.LanguageCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.TextOutputCallback;

import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;
import org.eclipse.riena.security.common.authentication.credentials.ChoiceCredential;
import org.eclipse.riena.security.common.authentication.credentials.ConfirmationCredential;
import org.eclipse.riena.security.common.authentication.credentials.CustomCredential;
import org.eclipse.riena.security.common.authentication.credentials.LanguageCredential;
import org.eclipse.riena.security.common.authentication.credentials.NameCredential;
import org.eclipse.riena.security.common.authentication.credentials.PasswordCredential;
import org.eclipse.riena.security.common.authentication.credentials.TextInputCredential;
import org.eclipse.riena.security.common.authentication.credentials.TextOutputCredential;

/**
 * this class converts between JAAS callbacks and credential object so that they
 * can be easily transported to a remote service
 */
public final class Callback2CredentialConverter {

	private Callback2CredentialConverter() {
		// utility
	}

	/**
	 * Convert Callback objects to AbstractCredential objects
	 * 
	 * @param callbacks
	 * @return
	 */
	public static AbstractCredential[] callbacks2Credentials(final Callback[] callbacks) {
		final AbstractCredential[] creds = new AbstractCredential[callbacks.length];
		int i = 0;
		for (final Callback cb : callbacks) {
			if (cb instanceof NameCallback) {
				final NameCallback ncb = (NameCallback) cb;
				final NameCredential nc = new NameCredential(ncb.getPrompt(), ncb.getDefaultName());
				nc.setName(ncb.getName());
				creds[i++] = nc;
			} else if (cb instanceof PasswordCallback) {
				final PasswordCallback pcb = (PasswordCallback) cb;
				final PasswordCredential pc = new PasswordCredential(pcb.getPrompt(), pcb.isEchoOn());
				pc.setPassword(pcb.getPassword());
				creds[i++] = pc;
			} else if (cb instanceof ConfirmationCallback) {
				final ConfirmationCallback ccb = (ConfirmationCallback) cb;
				final ConfirmationCredential ccc = new ConfirmationCredential(ccb.getMessageType(),
						ccb.getOptionType(), ccb.getDefaultOption());
				ccc.setSelectedIndex(ccb.getSelectedIndex());
				creds[i++] = ccc;
			} else if (cb instanceof TextInputCallback) {
				final TextInputCallback ticb = (TextInputCallback) cb;
				final TextInputCredential tic = new TextInputCredential(ticb.getPrompt(), ticb.getDefaultText());
				tic.setText(ticb.getText());
				creds[i++] = tic;
			} else if (cb instanceof TextOutputCallback) {
				final TextOutputCallback tocb = (TextOutputCallback) cb;
				final TextOutputCredential toc = new TextOutputCredential(tocb.getMessageType(), tocb.getMessage());
				creds[i++] = toc;
			} else if (cb instanceof LanguageCallback) {
				final LanguageCallback lcb = (LanguageCallback) cb;
				final LanguageCredential lc = new LanguageCredential(lcb.getLocale());
				creds[i++] = lc;
			} else if (cb instanceof ChoiceCallback) {
				final ChoiceCallback ccb = (ChoiceCallback) cb;
				final ChoiceCredential cc = new ChoiceCredential(ccb.getPrompt(), ccb.getChoices(),
						ccb.getDefaultChoice(), ccb.allowMultipleSelections());
				cc.setSelections(ccb.getSelectedIndexes());
				creds[i++] = cc;
			} else {
				final CustomCredential cc = new CustomCredential(cb);
				creds[i++] = cc;
			}

		}

		return creds;
	}

	public static Callback[] credentials2Callbacks(final AbstractCredential[] credentials) {
		final Callback[] callbacks = new Callback[credentials.length];
		int i = 0;
		for (final AbstractCredential cred : credentials) {
			if (cred instanceof NameCredential) {
				final NameCredential nc = (NameCredential) cred;
				NameCallback ncb;
				if (nc.getDefaultName() == null) {
					ncb = new NameCallback(nc.getPrompt());
				} else {
					ncb = new NameCallback(nc.getPrompt(), nc.getDefaultName());
				}
				ncb.setName(nc.getName());
				callbacks[i++] = ncb;
			} else if (cred instanceof PasswordCredential) {
				final PasswordCredential pc = (PasswordCredential) cred;
				final PasswordCallback pcb = new PasswordCallback(pc.getPrompt(), pc.isEchoOn());
				pcb.setPassword(pc.getPassword());
				callbacks[i++] = pcb;
			} else if (cred instanceof ConfirmationCredential) {
				final ConfirmationCredential cc = (ConfirmationCredential) cred;
				final ConfirmationCallback ccb = new ConfirmationCallback(cc.getMessageType(), cc.getOptionType(),
						cc.getDefaultOption());
				ccb.setSelectedIndex(cc.getSelectedIndex());
				callbacks[i++] = ccb;
			} else if (cred instanceof TextInputCredential) {
				final TextInputCredential tic = (TextInputCredential) cred;
				final TextInputCallback ticb = new TextInputCallback(tic.getPrompt(), tic.getDefaultText());
				ticb.setText(tic.getText());
				callbacks[i++] = ticb;
			} else if (cred instanceof TextOutputCredential) {
				final TextOutputCredential toc = (TextOutputCredential) cred;
				final TextOutputCallback tocb = new TextOutputCallback(toc.getMessageType(), toc.getMessage());
				callbacks[i++] = tocb;
			} else if (cred instanceof LanguageCredential) {
				final LanguageCredential lc = (LanguageCredential) cred;
				final LanguageCallback lcb = new LanguageCallback();
				lcb.setLocale(lc.getLocale());
				callbacks[i++] = lcb;
			} else if (cred instanceof ChoiceCredential) {
				final ChoiceCredential cc = (ChoiceCredential) cred;
				final ChoiceCallback ccb = new ChoiceCallback(cc.getPrompt(), cc.getChoices(), cc.getDefaultChoice(),
						cc.isMultipleSelectionsAllowed());
				final int[] selections = cc.getSelections();
				if (selections.length == 1) {
					ccb.setSelectedIndex(selections[0]);
				} else {
					ccb.setSelectedIndexes(cc.getSelections());
				}
				callbacks[i++] = ccb;
			} else if (cred instanceof CustomCredential) {
				callbacks[i++] = ((CustomCredential) cred).getCallback();
			}
		}

		return callbacks;
	}
}
