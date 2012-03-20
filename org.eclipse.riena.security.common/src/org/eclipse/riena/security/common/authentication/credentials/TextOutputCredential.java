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
public class TextOutputCredential extends AbstractCredential {

	private final int messageType;
	private final String message;

	public TextOutputCredential(final int messageType, final String message) {
		super(null);
		this.messageType = messageType;
		this.message = message;
	}

	public int getMessageType() {
		return messageType;
	}

	public String getMessage() {
		return message;
	}

}
