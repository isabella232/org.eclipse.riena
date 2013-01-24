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

import java.util.Date;

/**
 * DateBean provides a Date value for simple adapter UI-Binding
 */

public class DateBean extends AbstractBean {

	/** Name of the date property (="value") */
	public static final String DATE_PROPERTY = "value"; //$NON-NLS-1$

	private Date date;

	/**
	 * Creates a date bean with a default null
	 */
	public DateBean() {
		date = null;
	}

	/**
	 * Creates a date bean with the given value
	 * 
	 * @param value
	 */
	public DateBean(final Date value) {
		if (value != null) {
			this.date = (Date) value.clone();
		} else {
			this.date = null;
		}

	}

	/**
	 * Sets the value of this bean
	 * 
	 * @param value
	 *            - value to set
	 */
	public void setValue(final Date value) {
		final Object old = this.date;
		if (value != null) {
			this.date = (Date) value.clone();
		} else {
			this.date = null;
		}

		firePropertyChanged(DATE_PROPERTY, old, this.date);
	}

	/**
	 * Returns the value of this bean
	 * 
	 * @return value
	 */
	public Date getValue() {
		if (date != null) {
			return (Date) date.clone();
		}
		return null;

	}
}
