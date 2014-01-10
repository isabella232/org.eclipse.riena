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
package org.eclipse.riena.beans.common;

/**
 * A runtime exception that describes that a Java Bean does not support bound
 * properties. The conditions for bound properties are specified in section 7.4
 * of the <a href="http://java.sun.com/products/javabeans/docs/spec.html">Java
 * Bean Secification </a>. Basically you must provide the following two methods:
 * 
 * <pre>
 * public void addPropertyChangeHandler(PropertyChangeListener x);
 * 
 * public void removePropertyChangeHandler(PropertyChangeListener x);
 * </pre>
 */
public final class PropertyUnboundFailure extends PropertyFailure {

	private static final long serialVersionUID = -857499942518059862L;

	/**
	 * Constructs a new exception instance with the specified detail message.
	 * The cause is not initialized.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()}method.
	 */
	public PropertyUnboundFailure(final String message) {
		super(message);
	}

	/**
	 * Constructs a new exception instance with the specified detail message and
	 * cause.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()}method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()}method). (A<tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public PropertyUnboundFailure(final String message, final Throwable cause) {
		super(message, cause);
	}

}
