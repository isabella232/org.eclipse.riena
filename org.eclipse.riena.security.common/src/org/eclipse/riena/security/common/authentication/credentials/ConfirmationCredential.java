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
package org.eclipse.riena.security.common.authentication.credentials;

/**
 * 
 */
public class ConfirmationCredential extends AbstractCredential {

	private final int messageType;
	private final int optionType;
	private final int defaultOption;
	private int selectedIndex;

	/**
	 * @param prompt
	 */
	public ConfirmationCredential(final int messageType, final int optionType, final int defaultOption) {
		super(null);
		this.messageType = messageType;
		this.optionType = optionType;
		this.defaultOption = defaultOption;
	}

	public int getMessageType() {
		return messageType;
	}

	public int getOptionType() {
		return optionType;
	}

	public int getDefaultOption() {
		return defaultOption;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(final int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

}
