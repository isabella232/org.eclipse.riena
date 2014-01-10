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
package org.eclipse.riena.internal.core.logging;

import org.osgi.service.log.LogService;

/**
 * Map {@code LogService} log level into a string representation and vice versa.
 */
public final class LogLevelMapper {

	// Do not change the case!!! The code depends on upper case!
	private static final String CUSTOM = "CUSTOM"; //$NON-NLS-1$
	private static final String NONE = "NONE"; //$NON-NLS-1$
	private static final String ERROR = "ERROR"; //$NON-NLS-1$
	private static final String WARN = "WARNING"; //$NON-NLS-1$
	private static final String INFO = "INFO"; //$NON-NLS-1$
	private static final String DEBUG = "DEBUG"; //$NON-NLS-1$

	private LogLevelMapper() {
		// utility
	}

	/**
	 * Return the log level value.
	 * 
	 * @param logLevelString
	 * @return the log level value as defined by <code>LogService</code>
	 */
	public static int getValue(String logLevelString) {
		logLevelString = logLevelString.toUpperCase();
		if (DEBUG.equals(logLevelString)) {
			return LogService.LOG_DEBUG;
		} else if (INFO.equals(logLevelString)) {
			return LogService.LOG_INFO;
		} else if (WARN.equals(logLevelString)) {
			return LogService.LOG_WARNING;
		} else if (ERROR.equals(logLevelString)) {
			return LogService.LOG_ERROR;
		} else if (NONE.equals(logLevelString)) {
			return LogService.LOG_ERROR - 1;
		} else if (logLevelString.startsWith(CUSTOM)) {
			final int open = logLevelString.indexOf('(');
			final int close = logLevelString.indexOf(')');
			if (open != -1 && close != -1 && open < close) {
				final String level = logLevelString.substring(open + 1, close).trim();
				try {
					return Integer.parseInt(level);
				} catch (final NumberFormatException e) {
					return LogService.LOG_WARNING;
				}
			}
		}
		return LogService.LOG_WARNING;
	}

	/**
	 * Return the log level value.
	 * 
	 * @param logLevelString
	 * @return the log level value as defined by <code>LogService</code>
	 */
	public static String getValue(final int logLevel) {
		switch (logLevel) {
		case LogService.LOG_DEBUG:
			return DEBUG;
		case LogService.LOG_INFO:
			return INFO;
		case LogService.LOG_WARNING:
			return WARN;
		case LogService.LOG_ERROR:
			return ERROR;
		case LogService.LOG_ERROR - 1:
			return NONE;
		default:
			return CUSTOM + '(' + logLevel + ')';
		}
	}

}
