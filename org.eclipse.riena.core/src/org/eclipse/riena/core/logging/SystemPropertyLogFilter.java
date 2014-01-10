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

import org.osgi.framework.Bundle;

import org.eclipse.equinox.log.LogFilter;

import org.eclipse.riena.internal.core.logging.LogLevelMapper;

/**
 * This {@code LogFilter} gets its threshold log level from a system property.
 */
public class SystemPropertyLogFilter implements LogFilter {

	private final int threshold;

	/**
	 * Supported log level strings are: "debug", "info", "warn", "error" and
	 * "none".
	 */
	public static final String RIENA_LOG_LEVEL_PROPERTY = "riena.loglevel"; //$NON-NLS-1$

	public SystemPropertyLogFilter() {
		this(RIENA_LOG_LEVEL_PROPERTY, "warn"); //$NON-NLS-1$
	}

	SystemPropertyLogFilter(final String systemPropertyName, final String defaultLevel) {
		final String logLevelString = System.getProperty(systemPropertyName, defaultLevel);
		threshold = LogLevelMapper.getValue(logLevelString);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.equinox.log.LogFilter#isLoggable(org.osgi.framework.Bundle,
	 * java.lang.String, int)
	 */
	public boolean isLoggable(final Bundle b, final String loggerName, final int logLevel) {
		return logLevel <= threshold;
	}

}
