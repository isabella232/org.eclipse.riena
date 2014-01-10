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
package org.eclipse.riena.ui.tests.base;

import java.beans.PropertyChangeListener;

public class TestSingleSelectionBean {

	/** Key for the single selection property ("selection"). */
	public static final String PROPERTY_SELECTION = "selection";

	private Object selection;

	public Object getSelection() {
		return selection;
	}

	public void setSelection(final Object selection) {
		this.selection = selection;
	}

	/**
	 * unused
	 */
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		// unused
	}

	/**
	 * unused
	 */
	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		// unused
	}

}