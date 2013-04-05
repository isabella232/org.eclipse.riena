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
package org.eclipse.riena.core.util;

/**
 * The <code>InvocationTargetFailure</code> signals errors with the usage of
 * reflection method calls that throw target exceptions.
 */
public class InvocationTargetFailure extends ReflectionFailure {

	private static final long serialVersionUID = -3653981097011011976L;

	/**
	 * Constructor. Create a failure with a message.
	 * 
	 * @param msg
	 *            the message.
	 */
	public InvocationTargetFailure(final String msg) {
		super(msg);
	}

	/**
	 * Constructor. Create a failure with a message and a target exception.
	 * 
	 * @param msg
	 *            the message.
	 * @param targetException
	 *            the cause.
	 */
	public InvocationTargetFailure(final String msg, final Throwable targetException) {
		super(msg, targetException);
	}

	/**
	 * Get the thrown target exception.
	 * 
	 * @return
	 */
	public Throwable getTargetException() {
		return super.getCause();
	}
}
