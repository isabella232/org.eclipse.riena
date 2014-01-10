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
package org.eclipse.riena.objecttransaction.context;

import org.eclipse.riena.core.exception.Failure;

/**
 * This failure is thrown if a fatal error on the management of the context
 * happens.
 */

public class ContextFailure extends Failure {

	private static final long serialVersionUID = 2796815564631629552L;

	/**
	 * Creates a new instance of this failure.
	 * 
	 * @param msg
	 *            The failure message text.
	 * @param cause
	 *            the cause if any
	 */
	public ContextFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Creates a new instance of this failure.
	 * 
	 * @param msg
	 *            The failure message text.
	 */
	public ContextFailure(final String msg) {
		super(msg);
	}

}
