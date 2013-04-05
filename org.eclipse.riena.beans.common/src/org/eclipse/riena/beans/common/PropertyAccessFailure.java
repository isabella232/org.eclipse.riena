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
 * A runtime exception that is thrown if access to bean properties fails
 */
public class PropertyAccessFailure extends PropertyFailure {

	private static final long serialVersionUID = 3430407290999660809L;

	/**
	 * Constructor for exception if accessing a bean property fails
	 * 
	 * @param message
	 * @param cause
	 */
	public PropertyAccessFailure(final String message, final Throwable cause) {
		super(message, cause);
	}
}
