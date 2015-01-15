/*******************************************************************************
 * Copyright (c) 2007, 2015 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.listener;

/**
 * Event sent to {@link IProgressListener}s when the Browser update his progress.
 * 
 * @since 6.1
 */
public class ProgressEvent {

	private final int current;
	private final int total;

	/**
	 * Creates a new ProgressEvent.
	 * 
	 * @param current
	 *            The current value of the progress
	 * @param total
	 *            The maximum value of the progress
	 */
	public ProgressEvent(final int current, final int total) {
		this.current = current;
		this.total = total;
	}

	/**
	 * Returns the current value of the progress.
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * Return the maximum value of the progress.
	 */
	public int getTotal() {
		return total;
	}

	@Override
	public String toString() {
		return String.format("ProgressEvent [current=%d, total=%d]", current, total); //$NON-NLS-1$
	}

}
