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
package org.eclipse.riena.core.logging;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

import org.eclipse.equinox.log.SynchronousLogListener;

/**
 * Wraps another {@code LogListener} (which defines an asynchronous
 * {@code LogListener}!) so that it used as synchronous {@code LogListener}.
 * 
 * @since 3.0
 */
public class SynchronousLogListenerAdapter implements SynchronousLogListener {

	private final LogListener logListener;

	public SynchronousLogListenerAdapter(final LogListener logListener) {
		this.logListener = logListener;
	}

	public void logged(final LogEntry entry) {
		logListener.logged(entry);
	}

}
