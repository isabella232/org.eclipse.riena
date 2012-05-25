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
package org.eclipse.riena.ui.core.uiprocess;

import java.util.HashMap;

import org.eclipse.core.runtime.jobs.IJobChangeListener;

/**
 * Holds a map of {@link IUIProcessChangeListener}s and their wrappers of type {@link IUIMonitor}.
 */
class ListenerMapper {
	private final HashMap<IUIProcessChangeListener, IUIMonitor> map = new HashMap<IUIProcessChangeListener, IUIMonitor>();

	/**
	 * Retrieves a wrapper for the given listener. If no such wrapper exists, an instance is created and stored in the map for further requests. This ensures
	 * that always the same wrapper is returned for a given listener.
	 * 
	 * @param listener
	 *            the {@link IUIProcessChangeListener} instance which should be wrapped
	 * @return a wrapper of type {@link IJobChangeListener}, never <code>null</code>
	 */
	IUIMonitor getWrapperFor(final IUIProcessChangeListener listener) {
		IUIMonitor result = map.get(listener);
		if (result == null) {
			result = createWrapperFor(listener);
			map.put(listener, result);
		}
		return result;
	}

	/**
	 * Creates an {@link IUIMonitor} implementation that wraps all calls to the given {@link IUIProcessChangeListener}.
	 */
	private IUIMonitor createWrapperFor(final IUIProcessChangeListener listener) {
		return new IUIMonitor() {
			public Object getAdapter(final Class adapter) {
				if (adapter == null) {
					return null;
				}
				if (adapter.isInstance(this)) {
					return this;
				}
				if (adapter.isInstance(listener)) {
					return listener;
				}
				return null;
			}

			public void updateProgress(final int progress) {
				// do nothing
			}

			public void initialUpdateUI(final int totalWork) {
				listener.onInitialUpdateUI(totalWork);
			}

			public void finalUpdateUI() {
				listener.onFinalUpdateUI();
			}
		};
	}
}
