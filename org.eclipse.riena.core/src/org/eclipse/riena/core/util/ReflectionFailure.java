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
package org.eclipse.riena.core.util;

/**
 * The <code>ReflectionFailure</code> signals errors with the usage of
 * reflection.
 */
public class ReflectionFailure extends UtilFailure {

	private static final long serialVersionUID = 5597581412524466878L;

	/**
	 * Constructor. Create a failure with a message.
	 * 
	 * @param msg
	 *            the message.
	 */
	public ReflectionFailure(final String msg) {
		super(msg);
	}

	/**
	 * Constructor. Create a failure with a message and a cause.
	 * 
	 * @param msg
	 *            the message.
	 * @param cause
	 *            the cause.
	 */
	public ReflectionFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
