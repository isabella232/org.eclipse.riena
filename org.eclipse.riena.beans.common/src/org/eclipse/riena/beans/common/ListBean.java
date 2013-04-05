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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * List bean provides a list value for simple adapter UI-Binding
 */
public class ListBean extends AbstractBean {

	/**
	 * Name of the values property ("values").
	 */
	public static final String PROPERTY_VALUES = "values"; //$NON-NLS-1$

	private List<?> values;

	/**
	 * Creates a list bean with an empty list
	 */
	public ListBean() {
		values = new ArrayList<Object>();
	}

	/**
	 * Creates a list bean with the given value;
	 * 
	 * @param values
	 *            The values.
	 * @pre values != null
	 */
	public ListBean(final List<?> values) {
		Assert.isNotNull(values, "Expected a list"); //$NON-NLS-1$
		this.values = values;
	}

	/**
	 * Creates a list bean with the given value;
	 * 
	 * @param values
	 *            The values.
	 * @pre values != null
	 */
	public ListBean(final Object... values) {
		Assert.isNotNull(values, "Expected a list"); //$NON-NLS-1$
		this.values = Arrays.asList(values);
	}

	/**
	 * @return the value list
	 */
	public List<?> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            The values list to set.
	 */
	public void setValues(final List<?> values) {
		final Object old = this.values;
		this.values = values;
		firePropertyChanged(PROPERTY_VALUES, old, this.values);
	}
}
