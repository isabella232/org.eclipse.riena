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

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.AbstractObserver;

/**
 * This class notifies a collection of listeners of the type T when a widget is selected.
 */
abstract class AbstractSelectionObserver<T> extends AbstractObserver<T> implements SelectionListener {

	/**
	 * Create a new instance.
	 * 
	 * @param source
	 *            the ridget where the selection occured; never null
	 */
	public AbstractSelectionObserver(final IRidget source) {
		super(source);
	}

	public final void widgetSelected(final SelectionEvent evt) {
		fireAction(evt);
	}

	public void widgetDefaultSelected(final SelectionEvent e) {
	}

	// protected methods
	////////////////////

	/**
	 * This method forwards the given SelectionEvent to the collection of listeners.
	 * <p>
	 * Must be implemented by subclasses. Subclasses are free to create an entirely new event and forward that instead of the original one, if necessary.
	 */
	protected abstract void fireAction(SelectionEvent evt);

}
