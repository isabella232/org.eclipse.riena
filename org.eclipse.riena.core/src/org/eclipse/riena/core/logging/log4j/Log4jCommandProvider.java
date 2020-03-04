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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

/**
 * A {@code LogFilter} where the log level threshold can be set thru its command provider interface.
 */
public class Log4jCommandProvider implements CommandProvider {

	private static final String ROOT_LOGGER_NAME = "ROOT"; //$NON-NLS-1$

	/**
	 * log4jLevel command. See {@link #getHelp()} for details.
	 * 
	 * @param interpreter
	 */
	public void _log4jLevel(final CommandInterpreter interpreter) {

		final String category = interpreter.nextArgument();
		String loglevel = interpreter.nextArgument();

		final String effectiveCategory = category == null || category.equals(ROOT_LOGGER_NAME) //
				? null //
				: category;

		final String categoryForDisplay = effectiveCategory == null //
				? ROOT_LOGGER_NAME //
				: category;

		if (loglevel == null) {
			interpreter.println(String.format("LogLevel for %s is %s", categoryForDisplay, getLogLevelString(effectiveCategory))); //$NON-NLS-1$
		} else {
			loglevel = loglevel.toUpperCase();
			final Level newLevel = Level.toLevel(loglevel);
			if (newLevel.toString().equals(loglevel)) {
				final LoggerContext loggerContext = (LoggerContext) LogManager.getContext();

				final LoggerConfig loggerConfig = effectiveCategory == null // 
						? loggerContext.getConfiguration().getRootLogger() //
						: loggerContext.getConfiguration().getLoggerConfig(effectiveCategory);

				loggerConfig.setLevel(newLevel);
				loggerContext.updateLoggers();
				interpreter.println(String.format("LogLevel for %s set to %s", categoryForDisplay, getLogLevelString(effectiveCategory))); //$NON-NLS-1$
			} else {
				interpreter.println(String.format("LogLevel for %s is %s (not changed to unknown level %s)", //$NON-NLS-1$
						categoryForDisplay, getLogLevelString(effectiveCategory), loglevel));
			}
		}
	}

	private String getLogLevelString(final String effectiveCategory) {
		final Logger logger = effectiveCategory == null //
				? LogManager.getRootLogger() //
				: LogManager.getLogger(effectiveCategory);

		if (logger == null) {
			return "OFF"; //$NON-NLS-1$
		} else if (logger.isEnabled(Level.ALL)) {
			return "ALL"; //$NON-NLS-1$
		} else if (logger.isEnabled(Level.DEBUG)) {
			return "DEBUG"; //$NON-NLS-1$
		} else if (logger.isEnabled(Level.INFO)) {
			return "INFO"; //$NON-NLS-1$
		} else if (logger.isEnabled(Level.WARN)) {
			return "WARN"; //$NON-NLS-1$
		} else if (logger.isEnabled(Level.ERROR)) {
			return "ERROR"; //$NON-NLS-1$
		} else if (logger.isEnabled(Level.OFF)) {
			return "OFF"; //$NON-NLS-1$
		}
		return "UNKNOWN"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.osgi.framework.console.CommandProvider#getHelp()
	 */
	public String getHelp() {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter writer = new PrintWriter(stringWriter);
		writer.println("---Log4j configuration---"); //$NON-NLS-1$
		writer.println("\tlog4jLevel - display log level of root category (same as 'log4jLevel ROOT')"); //$NON-NLS-1$
		writer.println("\tlog4jLevel (ROOT|<category>) - display log level for specified category"); //$NON-NLS-1$
		writer.println("\tlog4jLevel (ROOT|<category>) (DEBUG|INFO|WARN|ERROR) - set log level for specified category"); //$NON-NLS-1$
		writer.close();
		return stringWriter.toString();
	}
}
