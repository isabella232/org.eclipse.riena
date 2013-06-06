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
package org.eclipse.riena.core.logging;

import java.text.MessageFormat;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.osgi.service.log.LogService;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.internal.core.Activator;

/**
 * The {@code JavaLogCatcher} can attach to the Java logging and route the log events into Riena's logging.
 */
public class JavaLogCatcher implements ILogCatcher {

	private Handler javaLogginghandler;
	/**
	 * @since 5.0
	 */
	protected Logger rootLogger;
	/**
	 * @since 5.0
	 */
	protected Level rootLevel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.core.logging.ILogCatcher#attach()
	 */
	public void attach() {
		prepareJulLogging();
		javaLogginghandler = new JavaLoggingHandler();
		rootLogger.addHandler(javaLogginghandler);
	}

	/**
	 * Allow customization for Java Util Logging.
	 * 
	 * @since 5.0
	 */
	protected void prepareJulLogging() {
		LogManager.getLogManager().reset();
		rootLogger = Logger.getLogger(""); //$NON-NLS-1$
		rootLevel = rootLogger.getLevel();
		// we want it all, we want it all, we want it now
		// filtering is done by the equinox logger
		rootLogger.setLevel(Level.ALL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.core.logging.ILogCatcher#detach()
	 */
	public void detach() {
		if (rootLogger == null || javaLogginghandler == null) {
			return;
		}
		rootLogger.removeHandler(javaLogginghandler);
		cleanupJulLogging();
	}

	/**
	 * Allow customization of cleaning up Java Util Logging.
	 * 
	 * @since 5.0
	 */
	protected void cleanupJulLogging() {
		rootLogger.setLevel(rootLevel);
	}

	/**
	 * Java logging handler that routes to the equinox logging.
	 */
	private static class JavaLoggingHandler extends Handler {

		@Override
		public void close() {
			Nop.reason("nothing to close here"); //$NON-NLS-1$
		}

		@Override
		public void flush() {
			Nop.reason("nothing to flush here"); //$NON-NLS-1$
		}

		@Override
		public void publish(final LogRecord record) {

			// get equinox log level
			final int equinoxLoglevel;
			final int javaLoglevel = record.getLevel().intValue();
			if (javaLoglevel == Level.SEVERE.intValue()) {
				equinoxLoglevel = LogService.LOG_ERROR;
			} else if (javaLoglevel == Level.WARNING.intValue()) {
				equinoxLoglevel = LogService.LOG_WARNING;
			} else if (javaLoglevel == Level.INFO.intValue() || javaLoglevel == Level.CONFIG.intValue()) {
				equinoxLoglevel = LogService.LOG_INFO;
			} else {
				equinoxLoglevel = LogService.LOG_DEBUG;
			}

			// find corresponding equinox logger
			final org.eclipse.equinox.log.Logger logger = Log4r.getLogger(Activator.getDefault(), record.getLoggerName());

			if (!logger.isLoggable(equinoxLoglevel)) {
				return;
			}
			String message = record.getMessage();
			final Object[] params = record.getParameters();
			if (params != null) {
				message = MessageFormat.format(message, params);
			}

			logger.log(equinoxLoglevel, message, record.getThrown());
		}

	}

}
