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
package org.eclipse.riena.monitor.common;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.ExtendedLogEntry;

import org.eclipse.riena.core.util.IOUtils;

/**
 *
 */
public class LogEntryTransferObject implements Serializable {

	private final String bundleName;
	private final String context;
	private final String exception;
	private final int level;
	private final String loggerName;
	private final String message;
	private final String threadName;
	private final long time;

	private static final long serialVersionUID = 189322808250966598L;

	@SuppressWarnings("unused")
	private LogEntryTransferObject() {
		// just4Hessian
		this.bundleName = null;
		this.context = null;
		this.exception = null;
		this.level = 0;
		this.loggerName = null;
		this.message = null;
		this.threadName = null;
		this.time = 0;
	}

	public LogEntryTransferObject(final ExtendedLogEntry logEntry) {
		Assert.isNotNull(logEntry, "logEntry must not be null"); //$NON-NLS-1$
		this.bundleName = logEntry.getBundle() != null ? logEntry.getBundle().getSymbolicName() : null;
		this.context = logEntry.getContext() != null ? logEntry.getContext().toString() : null;
		if (logEntry.getException() != null) {
			final StringWriter string = new StringWriter();
			final PrintWriter writer = new PrintWriter(string);
			logEntry.getException().printStackTrace(writer);
			IOUtils.close(writer);
			this.exception = string.toString();
		} else {
			this.exception = null;
		}
		this.level = logEntry.getLevel();
		this.loggerName = logEntry.getLoggerName();
		this.message = logEntry.getMessage();
		this.threadName = logEntry.getThreadName();
		this.time = logEntry.getTime();
	}

	/**
	 * @return the bundleName
	 */
	public String getBundleName() {
		return bundleName;
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the threadName
	 */
	public String getThreadName() {
		return threadName;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new Date(time) + " " + loggerName + " Level: " + level + " [" + threadName + "] " + message //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ (exception == null ? "" : "\n" + exception); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
