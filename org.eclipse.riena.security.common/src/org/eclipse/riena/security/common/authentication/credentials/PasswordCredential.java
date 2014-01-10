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
package org.eclipse.riena.security.common.authentication.credentials;

/**
 * TODO JavaDoc
 */
public class PasswordCredential extends AbstractCredential {

	private char[] password;
	private final boolean echoOn;

	/**
	 * @param prompt
	 */
	public PasswordCredential(final String prompt, final boolean echoOn) {
		super(prompt);
		this.echoOn = echoOn;
	}

	public char[] getPassword() {
		if (password == null) {
			return null;
		}
		return password.clone();
	}

	public void setPassword(final char[] password) {
		if (password != null) {
			this.password = password.clone();
		} else {
			this.password = null;
		}
	}

	public boolean isEchoOn() {
		return echoOn;
	}

}
