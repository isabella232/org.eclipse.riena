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
package org.eclipse.riena.core.logging.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogLevel;

import org.eclipse.equinox.log.LogFilter;

/**
 * This {@code LogFilter} gets it level from the associated log4j2 logger.
 */
public class Log4jLogFilter implements LogFilter {

	private final static Logger LOGGER = LogManager.getLogger(Log4jLogFilter.class);
	private final static LogLevel LOG_LEVELS[] = LogLevel.values();

	public boolean isLoggable(final Bundle bundle, final String loggerName, final int logLevel) {

		try {
			final Logger logger = loggerName == null //
					? LogManager.getRootLogger() //
					: LogManager.getLogger(loggerName);

			return logger != null && logger.isEnabled(getLevel(logLevel));
		} catch (final UnsupportedOperationException e) {
			LOGGER.error("LogManager could not create a logger for category " + loggerName + "."); //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		}
	}

	private Level getLevel(final int logLevel) {
		switch (getLogLevel(logLevel)) {
		case DEBUG:
			return Level.DEBUG;
		case INFO:
			return Level.INFO;
		case WARN:
			return Level.WARN;
		case ERROR:
			return Level.ERROR;
		case TRACE:
			return Level.TRACE;
		case AUDIT:
			return Level.ALL;
		default:
			return Level.OFF;
		}
	}

	private LogLevel getLogLevel(final int ordinal) {
		if (ordinal < 0 || ordinal > LOG_LEVELS.length - 1) {
			return LogLevel.TRACE;
		} else {
			return LOG_LEVELS[ordinal];
		}
	}
}
