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
 * Double bean provides a boolean value for simple adapter UI-Binding.
 */
public class DoubleBean extends AbstractBean {

	/**
	 * Property key of the value propert ("value").
	 */
	public static final String PROP_VALUE = "value"; //$NON-NLS-1$

	private Double value;

	/**
	 * Creates a double bean with a default 0.0
	 * 
	 * @param value
	 */
	public DoubleBean() {
		value = null;
	}

	/**
	 * Creates a double bean with the given value
	 * 
	 * @param value
	 */
	public DoubleBean(final Double value) {
		this.value = value;
	}

	/**
	 * Creates a double bean with the given value
	 * 
	 * @param value
	 */
	public DoubleBean(final double value) {
		this.value = value;
	}

	/**
	 * Sets the value of this bean
	 * 
	 * @param value
	 *            value to set
	 */
	public void setValue(final Double value) {
		// System.out.println( "Double bean setValue: " + value.toString() );
		final Object old = this.value;
		this.value = value;
		firePropertyChanged(PROP_VALUE, old, this.value);
	}

	/**
	 * Returns the value of this bean
	 * 
	 * @return value
	 */
	public Double getValue() {
		// System.out.println( "Double bean getValue: " + value.toString() );
		return value;
	}
}
