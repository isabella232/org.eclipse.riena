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
package org.eclipse.riena.beans.common;

/**
 * Integer bean provides a boolean value for simple adapter UI-Binding
 */
public class IntegerBean extends AbstractBean {

	/**
	 * Property key of the value propert ("value").
	 */
	public static final String PROP_VALUE = "value"; //$NON-NLS-1$

	private Integer value;

	/**
	 * Creates an integer bean with a default 0
	 * 
	 * @param value
	 */
	public IntegerBean() {
		value = 0;
	}

	/**
	 * Creates a integer bean with the given value
	 * 
	 * @param value
	 */
	public IntegerBean(final Integer value) {
		this.value = value;
	}

	/**
	 * Creates a integer bean with the given value
	 * 
	 * @param value
	 */
	public IntegerBean(final int value) {
		this.value = value;
	}

	/**
	 * Sets the value of this bean
	 * 
	 * @param value
	 *            value to set
	 */
	public void setValue(final Integer value) {
		final Object old = this.value;
		this.value = value;
		firePropertyChanged(PROP_VALUE, old, this.value);

	}

	/**
	 * Returns the value of this bean
	 * 
	 * @return value
	 */
	public Integer getValue() {
		return this.value;
	}
}
