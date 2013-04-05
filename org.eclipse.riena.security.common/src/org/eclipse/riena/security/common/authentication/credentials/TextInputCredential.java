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
package org.eclipse.riena.security.common.authentication.credentials;

/**
 * 
 */
public class TextInputCredential extends AbstractCredential {

	private final String defaultText;
	private String inputText;

	/**
	 * @param prompt
	 */
	public TextInputCredential(final String prompt, final String defaultText) {
		super(prompt);
		this.defaultText = defaultText;
	}

	public String getText() {
		return inputText;
	}

	public void setText(final String inputText) {
		this.inputText = inputText;
	}

	public String getDefaultText() {
		return defaultText;
	}

}
