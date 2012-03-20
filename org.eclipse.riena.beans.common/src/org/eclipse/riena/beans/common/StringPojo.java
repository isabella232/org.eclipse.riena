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
 * Provides a string value for simple adapter UI-Binding. It does not fire any
 * property change events.
 * 
 * @since 2.0
 */
public class StringPojo {

	private String value;

	/**
	 * Creates a StringBean with the given value.
	 * 
	 * @param value
	 *            a String
	 */
	public StringPojo(final String value) {
		this.value = value;
	}

	/**
	 * Sets String stored by this bean.
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * Returns the String stored by this bean.
	 * 
	 * @param value
	 *            a String
	 */
	public String getValue() {
		return value;
	}
}
