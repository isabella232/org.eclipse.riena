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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

/**
 * A {@code LogFilter} where the log level threshold can be set thru its command
 * provider interface.
 */
public class Log4jCommandProvider implements CommandProvider {

	private static final String ROOT_LOGGER_NAME = "ROOT"; //$NON-NLS-1$

	/**
	 * log4jLevel command. See {@link #getHelp()} for details.
	 * 
	 * @param intp
	 */
	public void _log4jLevel(final CommandInterpreter intp) {

		final String category = intp.nextArgument();
		String loglevel = intp.nextArgument();

		final String categoryForDisplay = category == null ? ROOT_LOGGER_NAME : category;
		final Logger logger = category == null || category.equals(ROOT_LOGGER_NAME) ? Logger.getRootLogger() : Logger
				.getLogger(category);

		if (loglevel == null) {
			intp.println(String.format("LogLevel for %s is %s", categoryForDisplay, getLogLevelString(logger))); //$NON-NLS-1$
		} else {
			loglevel = loglevel.toUpperCase();
			final Level newLevel = Level.toLevel(loglevel);
			if (newLevel.toString().equals(loglevel)) {
				logger.setLevel(newLevel);
				intp.println(String.format("LogLevel for %s set to %s", categoryForDisplay, getLogLevelString(logger))); //$NON-NLS-1$
			} else {
				intp.println(String.format("LogLevel for %s is %s (not changed to unknown level %s)", //$NON-NLS-1$
						categoryForDisplay, getLogLevelString(logger), loglevel));
			}
		}
	}

	private String getLogLevelString(final Logger logger) {
		if (logger == null) {
			return "OFF"; //$NON-NLS-1$
		} else if (logger.isEnabledFor(Level.ALL)) {
			return "ALL"; //$NON-NLS-1$
		} else if (logger.isEnabledFor(Level.DEBUG)) {
			return "DEBUG"; //$NON-NLS-1$
		} else if (logger.isEnabledFor(Level.INFO)) {
			return "INFO"; //$NON-NLS-1$
		} else if (logger.isEnabledFor(Level.WARN)) {
			return "WARN"; //$NON-NLS-1$
		} else if (logger.isEnabledFor(Level.ERROR)) {
			return "ERROR"; //$NON-NLS-1$
		} else if (logger.isEnabledFor(Level.OFF)) {
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
