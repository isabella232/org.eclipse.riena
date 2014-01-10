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
 * String bean provides a string value for simple adapter UI-Binding
 */
public class StringBean extends AbstractBean {

	/**
	 * Key for the value property (PROP_VALUE = "value").
	 */
	public static final String PROP_VALUE = "value"; //$NON-NLS-1$

	private String value;

	/**
	 * Creates an String bean with a default 0
	 * 
	 * @param value
	 */
	public StringBean() {
		value = ""; //$NON-NLS-1$
	}

	/**
	 * Creates a String bean with the given value
	 * 
	 * @param value
	 */
	public StringBean(final String value) {
		this.value = value;
	}

	/**
	 * Sets the value of this bean
	 * 
	 * @param value
	 *            to set
	 */
	public void setValue(final String value) {
		final Object old = this.value;
		this.value = value;
		firePropertyChanged(PROP_VALUE, old, this.value);
	}

	/**
	 * Returns the value of this bean
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}
}
