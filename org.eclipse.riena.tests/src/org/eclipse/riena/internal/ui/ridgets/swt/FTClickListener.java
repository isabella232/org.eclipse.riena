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

import org.eclipse.riena.ui.ridgets.listener.ClickEvent;
import org.eclipse.riena.ui.ridgets.listener.IClickListener;

/**
 * {@link IClickListener} test fixture, that counts how many times the
 * {@link #callback()} method has been invoked.
 */
public final class FTClickListener implements IClickListener {

	private int count;
	private ClickEvent event;

	public int getCount() {
		return count;
	}

	/**
	 * @return the event
	 */
	public ClickEvent getEvent() {
		return event;
	}

	public void callback(final ClickEvent event) {
		count++;
		this.event = event;
	}
}
