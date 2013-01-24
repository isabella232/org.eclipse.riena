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
package org.eclipse.riena.beans.common;

/**
 * A runtime exception that describes that a Java Bean property could not be
 * found.
 */
public final class PropertyNotFoundFailure extends PropertyFailure {

	private static final long serialVersionUID = -3127926982719247061L;

	/**
	 * Constructs a new exception instance with the specified detail message.
	 * The cause is not initialized.
	 * 
	 * @param propertyName
	 *            the name of the property that could not be found
	 * @param bean
	 *            the Java Bean used to lookup the property
	 */
	public PropertyNotFoundFailure(final String propertyName, final Object bean) {
		this(propertyName, bean, null);
	}

	/**
	 * Constructs a new exception instance with the specified detail message and
	 * cause.
	 * 
	 * @param propertyName
	 *            the name of the property that could not be found
	 * @param bean
	 *            the Java Bean used to lookup the property
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()}method). (A<tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public PropertyNotFoundFailure(final String propertyName, final Object bean, final Throwable cause) {
		super("Property '" + propertyName + "' not found in bean " + bean, cause); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
