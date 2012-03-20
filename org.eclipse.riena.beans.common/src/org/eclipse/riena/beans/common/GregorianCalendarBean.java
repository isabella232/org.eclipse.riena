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

import java.util.GregorianCalendar;

/**
 * GregorianCalendarBean provides a GregorianCalendar value for simple adapter
 * UI-Binding
 */
public class GregorianCalendarBean extends AbstractBean {

	private GregorianCalendar value;

	/**
	 * Creates a GregorianCalendar bean with a default null
	 * 
	 * @param value
	 */
	public GregorianCalendarBean() {
		value = null;
	}

	/**
	 * Creates a GregorianCalendar bean with the given value
	 * 
	 * @param value
	 */
	public GregorianCalendarBean(final GregorianCalendar value) {
		this.value = value;
	}

	/**
	 * Sets the value of this bean
	 * 
	 * @param value
	 *            value to set
	 */
	public void setValue(final GregorianCalendar value) {
		// if (value != null) {
		// System.out.println( "Greg bean setValue: " + value.toString() );
		// } else {
		// System.out.println( "Greg bean setValue: " + null );
		// }
		final Object old = this.value;
		this.value = value;
		firePropertyChanged("value", old, this.value); //$NON-NLS-1$
	}

	/**
	 * Returns the value of this bean
	 * 
	 * @return value
	 */
	public GregorianCalendar getValue() {
		// System.out.println( "Greg bean getValue: " + value.toString() );
		return value;
	}
}
