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
package org.eclipse.riena.communication.sample.pingpong.common;

/**
 * Simple abstract Message class with text
 */
public abstract class Message {
	private String text;

	/**
	 * @return the text message
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the given ping text
	 * 
	 * @param text
	 */
	public void setText(final String text) {
		this.text = text;
	}

	/**
	 * @return this message text
	 */
	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] says = " + getText(); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
