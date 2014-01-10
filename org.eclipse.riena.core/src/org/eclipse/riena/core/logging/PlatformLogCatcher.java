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

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.core.Activator;

/**
 * The <code>PlatformLogCatcher</code> can attach to the <code>Platform</code>
 * log and route platform log events into Riena's logging.
 */
public class PlatformLogCatcher implements ILogListener, ILogCatcher {

	/**
	 * Attach to the <code>Platform</code> log.
	 */
	public void attach() {
		Platform.addLogListener(this);
	}

	/**
	 * Detach from the <code>Platform</code> log.
	 */
	public void detach() {
		Platform.removeLogListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.ILogListener#logging(org.eclipse.core.runtime
	 * .IStatus, java.lang.String)
	 */
	public void logging(final IStatus status, final String plugin) {
		if (Activator.getDefault() == null) {
			return;
		}
		int logLevel;
		final int severity = status.getSeverity();
		if (IStatus.ERROR == severity) {
			logLevel = LogService.LOG_ERROR;
		} else if (IStatus.WARNING == severity) {
			logLevel = LogService.LOG_WARNING;
		} else if (IStatus.INFO == severity || IStatus.OK == severity) {
			logLevel = LogService.LOG_INFO;
		} else {
			logLevel = LogService.LOG_DEBUG;
		}
		final StringBuilder bob = new StringBuilder("Message: "); //$NON-NLS-1$
		bob.append(status.getMessage()).append(", Code: "); //$NON-NLS-1$
		bob.append(status.getCode()).append(", Plugin: "); //$NON-NLS-1$
		bob.append(status.getPlugin());
		Log4r.getLogger(Activator.getDefault(), "Bundle " + plugin).log(logLevel, bob.toString(), status.getException()); //$NON-NLS-1$
	}

}
