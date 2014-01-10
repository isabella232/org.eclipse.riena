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

import java.util.List;

/**
 * List bean that can hold a single selection.
 */
public class SingleSelectionListBean extends ListBean {

	/**
	 * Name of the selection property ({@value} ).
	 */
	public static final String PROPERTY_SELECTION = "selection"; //$NON-NLS-1$

	private Object selection = null;

	public SingleSelectionListBean(final List<?> values) {
		super(values);
	}

	public SingleSelectionListBean(final Object... values) {
		super(values);
	}

	/**
	 * Sets the selection (may be null).
	 * 
	 * @param selection
	 *            an object contained in the list or null
	 */
	public void setSelection(final Object selection) {
		final Object oldSelection = this.selection;
		if (selection != null && getValues().contains(selection)) {
			this.selection = selection;
		} else {
			this.selection = null;
		}
		firePropertyChanged(PROPERTY_SELECTION, oldSelection, this.selection);
	}

	/**
	 * @return the selection (may be null)
	 */
	public Object getSelection() {
		return selection;
	}
}
