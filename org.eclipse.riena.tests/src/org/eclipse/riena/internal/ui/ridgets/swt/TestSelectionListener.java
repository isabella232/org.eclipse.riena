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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;

/**
 * SelectionListener for test purposes
 */
public class TestSelectionListener implements ISelectionListener {

	private int count;
	private SelectionEvent selectionEvent;

	public void setSelectionEvent(final SelectionEvent e) {
		this.selectionEvent = e;
	}

	public SelectionEvent getSelectionEvent() {
		return selectionEvent;
	}

	public int getCount() {
		return count;
	}

	public void ridgetSelected(final SelectionEvent e) {
		setSelectionEvent(e);
		count++;
	}

}
