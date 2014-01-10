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
package org.eclipse.riena.internal.ui.ridgets.databinding;

import org.osgi.service.log.LogService;

import org.eclipse.core.databinding.util.ILogger;
import org.eclipse.core.databinding.util.Policy;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.logging.ILogCatcher;

/**
 * Attach to the {@code org.eclipse.core.databinding.Policy} logging to riena's
 * logging.<br>
 * Unfortunately databinding "logs" into its own logging facility. Even more
 * unfortunately some of the their log events should be exceptions! However,
 * there is no clean way to detect whether such a log event is just a "logging"
 * or an exception.
 * 
 * @since 4.0
 */
public class DatabindingLogCatcher implements ILogCatcher {

	public void attach() {
		Policy.setLog(new DatabindingLoggingDelegate());
	}

	public void detach() {
		Policy.setLog(null);
	}

	private static class DatabindingLoggingDelegate implements ILogger {

		private final static Logger LOGGER = Log4r.getLogger(Policy.class);

		public void log(final IStatus status) {
			final StringBuilder bob = new StringBuilder("Message: "); //$NON-NLS-1$
			bob.append(status.getMessage()).append(", Code: "); //$NON-NLS-1$
			bob.append(status.getCode()).append(", Plugin: "); //$NON-NLS-1$
			bob.append(status.getPlugin());
			LOGGER.log(getLogLevel(status), bob.toString(), status.getException());
		}

		private int getLogLevel(final IStatus status) {
			switch (status.getSeverity()) {
			case IStatus.ERROR:
				return LogService.LOG_ERROR;
			case IStatus.WARNING:
				return LogService.LOG_WARNING;
			case IStatus.INFO:
				return LogService.LOG_INFO;
			case IStatus.OK:
				return LogService.LOG_INFO;
			default:
				return LogService.LOG_DEBUG;
			}
		}

	}

}
