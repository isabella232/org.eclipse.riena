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

import org.eclipse.riena.core.exception.Failure;

/**
 * Failure class for util specific failures.
 */
public class UtilFailure extends Failure {

	private static final long serialVersionUID = -5852396325117403473L;

	/**
	 * Constructor. Failure with a simple message.
	 * 
	 * @param msg
	 *            the message.
	 */
	public UtilFailure(final String msg) {
		super(msg);
	}

	/**
	 * Constructor. Failure with a simple message and a cause.
	 * 
	 * @param msg
	 *            the message.
	 * @param cause
	 *            the cause.
	 */
	public UtilFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
