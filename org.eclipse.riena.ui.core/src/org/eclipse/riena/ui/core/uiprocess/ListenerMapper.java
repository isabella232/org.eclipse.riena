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

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;

/**
 * Holds a map of {@link IUIProcessChangeListener}s and their wrappers of type {@link IJobChangeListener}.
 */
class ListenerMapper {
	private final HashMap<IUIProcessChangeListener, IJobChangeListener> map = new HashMap<IUIProcessChangeListener, IJobChangeListener>();

	/**
	 * Retrieves a wrapper for the given listener. If no such wrapper exists, an instance is created and stored in the map for further requests. This ensures
	 * that always the same wrapper is returned for a given listener.
	 * 
	 * @param listener
	 *            the {@link IUIProcessChangeListener} instance which should be wrapped
	 * @return a wrapper of type {@link IJobChangeListener}, never <code>null</code>
	 */
	IJobChangeListener getWrapperFor(final IUIProcessChangeListener listener) {
		IJobChangeListener result = map.get(listener);
		if (result == null) {
			result = createWrapperFor(listener);
			map.put(listener, result);
		}
		return result;
	}

	/**
	 * Creates an {@link IJobChangeListener} implementation that wraps all calls to the given {@link IUIProcessChangeListener}.
	 */
	private IJobChangeListener createWrapperFor(final IUIProcessChangeListener listener) {
		return new IJobChangeListener() {
			public void sleeping(final IJobChangeEvent event) {
				listener.sleeping();
			}

			public void scheduled(final IJobChangeEvent event) {
				listener.scheduled(event.getDelay());

			}

			public void running(final IJobChangeEvent event) {
				listener.running();
			}

			public void done(final IJobChangeEvent event) {
				listener.done(event.getResult());
			}

			public void awake(final IJobChangeEvent event) {
				listener.awake();
			}

			public void aboutToRun(final IJobChangeEvent event) {
				listener.aboutToRun();
			}
		};
	}

}
