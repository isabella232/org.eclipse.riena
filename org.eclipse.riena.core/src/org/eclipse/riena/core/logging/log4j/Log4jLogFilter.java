/*******************************************************************************
 * Copyright (c) 2007, 2013 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.logging.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.LogFilter;

/**
 * This {@code LogFilter} gets it level from the associated log4j logger.
 */
public class Log4jLogFilter implements LogFilter {

	public boolean isLoggable(final Bundle bundle, final String loggerName, final int logLevel) {
		final Logger logger = Logger.getLogger(loggerName == null ? "" : loggerName); //$NON-NLS-1$
		return logger != null && logger.isEnabledFor(getLevel(logLevel));
	}

	private Level getLevel(final int logLevel) {
		switch (logLevel) {
		case LogService.LOG_DEBUG:
			return Level.DEBUG;
		case LogService.LOG_INFO:
			return Level.INFO;
		case LogService.LOG_WARNING:
			return Level.WARN;
		case LogService.LOG_ERROR:
			return Level.ERROR;
		default:
			return logLevel < LogService.LOG_ERROR ? Level.OFF : Level.ALL;
		}
	}
}
