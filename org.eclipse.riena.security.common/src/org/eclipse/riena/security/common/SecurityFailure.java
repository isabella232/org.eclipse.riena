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
package org.eclipse.riena.security.common;

import org.eclipse.riena.core.exception.Failure;

/**
 * The mother of all security related failures
 * 
 */
public class SecurityFailure extends Failure {

	private static final long serialVersionUID = -4828670387343650064L;

	/**
	 * Creates a new instance of <code>SecurityFailure</code>
	 * 
	 * @param msg
	 *            message text or message code
	 */
	public SecurityFailure(final String msg) {
		super(msg);
	}

	/**
	 * Creates a new instance of <code>SecurityFailure</code>
	 * 
	 * @param msg
	 *            message text or message code
	 * @param cause
	 *            exception which has caused this Failure
	 */
	public SecurityFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
