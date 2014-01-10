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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.equinox.log.LogFilter;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import org.eclipse.riena.internal.core.Activator;
import org.eclipse.riena.internal.core.ignore.IgnoreCheckStyle;
import org.eclipse.riena.internal.core.logging.LogLevelMapper;

/**
 * A {@code LogFilter} where the log level threshold can be set thru its command
 * provider interface.
 */
public class CommandProviderLogFilter implements LogFilter, CommandProvider, IExecutableExtension {

	private int threshold = LogService.LOG_DEBUG;

	public CommandProviderLogFilter() {
		Activator.getDefault().getContext().registerService(CommandProvider.class.getName(), this, null);
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

	/**
	 * Change log level.
	 * 
	 * @param ci
	 * @throws Exception
	 */
	@IgnoreCheckStyle("the _ is the pattern for console commands - do not change!!")
	public void _logLevel(final CommandInterpreter ci) throws Exception {
		final String level = ci.nextArgument();
		if (level != null) {
			threshold = LogLevelMapper.getValue(level);
		}
		System.out.println("LogLevel: " + LogLevelMapper.getValue(threshold)); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.osgi.framework.console.CommandProvider#getHelp()
	 */
	public String getHelp() {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter writer = new PrintWriter(stringWriter);
		writer.println("---Controlling Riena logging---"); //$NON-NLS-1$
		writer.println("\tlogLevel [ <level> ] - specify log level, e.g. debug, info, warn, error or none, or retrieve current level"); //$NON-NLS-1$
		writer.close();
		return stringWriter.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org
	 * .eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data)
			throws CoreException {
		if (data == null) {
			return;
		}
		if (!(data instanceof String)) {
			return;
		}
		threshold = LogLevelMapper.getValue((String) data);
	}

}
