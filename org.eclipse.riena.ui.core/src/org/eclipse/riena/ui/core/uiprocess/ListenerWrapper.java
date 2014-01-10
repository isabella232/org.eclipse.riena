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
package org.eclipse.riena.ui.core.uiprocess;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps a list of {@link IUIProcessChangeListener}s as a {@link IUIMonitor} so that it can be passed to the {@link UICallbackDispatcher}.
 */
class ListenerWrapper implements IUIMonitor {
	private final List<IUIProcessChangeListener> listeners = new ArrayList<IUIProcessChangeListener>();

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(final Class adapter) {
		return null;
	}

	/**
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIMonitor#updateProgress(int)
	 */
	public void updateProgress(final int progress) {
	}

	/**
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIMonitor#initialUpdateUI(int)
	 */
	public void initialUpdateUI(final int totalWork) {
		for (final IUIProcessChangeListener listener : new ArrayList<IUIProcessChangeListener>(listeners)) {
			listener.afterInitialUpdateUI(totalWork);
		}
	}

	/**
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIMonitor#finalUpdateUI()
	 */
	public void finalUpdateUI() {
		for (final IUIProcessChangeListener listener : new ArrayList<IUIProcessChangeListener>(listeners)) {
			listener.afterFinalUpdateUI();
		}
	}

	/**
	 * Adds a listener to the list.
	 * 
	 * @param listener
	 *            The listener to add. Must be not <code>null</code>
	 */
	void add(final IUIProcessChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a listener. This method has no effect if this listener is not in the list.
	 * 
	 * @param listener
	 *            The listener to remove
	 */
	void remove(final IUIProcessChangeListener listener) {
		listeners.remove(listener);
	}
}
