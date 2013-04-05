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
package org.eclipse.riena.internal.core.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * All properties a defered log event.
 */
public class DeferredLogEvent {

	private final String loggerName;
	private final long time;
	private final String threadName;
	private final Method log;
	private final Object[] args;

	public DeferredLogEvent(final String loggerName, final long time, final String threadName, final Method log,
			final Object[] args) {
		this.loggerName = loggerName;
		this.time = time;
		this.threadName = threadName;
		this.log = log;
		this.args = args;
	}

	/**
	 * @return the loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @return the threadName
	 */
	public String getThreadName() {
		return threadName;
	}

	/**
	 * @return the log
	 */
	public Method getLogMethod() {
		return log;
	}

	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * @return
	 */
	public int getLevel() {
		// This is a little bit brittle - we guess the log level from it's type !!
		for (final Object arg : args) {
			if (arg instanceof Integer) {
				return (Integer) arg;
			}
		}
		return LogService.LOG_DEBUG;
	}

	@Override
	public String toString() {
		final StringBuilder bob = new StringBuilder("DeferredLogEvent on "); //$NON-NLS-1$
		bob.append(new Date(getTime())).append(" (").append(getTime()); //$NON-NLS-1$
		bob.append(" ms) in thread [").append(getThreadName()).append("]:\n\t\t"); //$NON-NLS-1$ //$NON-NLS-2$
		appendArgs(bob, getArgs());
		return bob.toString();
	}

	private void appendArgs(final StringBuilder bob, final Object[] args) {
		if (args == null) {
			return;
		}
		for (final Object arg : args) {
			if (arg instanceof ServiceReference) {
				final ServiceReference ref = (ServiceReference) arg;
				final String bundleName = ref.getBundle() != null ? ref.getBundle().getSymbolicName() : null;
				bob.append("ServiceReference( bundle=").append(bundleName).append(", properties=("); //$NON-NLS-1$ //$NON-NLS-2$
				for (final String key : ref.getPropertyKeys()) {
					bob.append(key).append("=").append(ref.getProperty(key)).append(","); //$NON-NLS-1$ //$NON-NLS-2$
				}
				bob.append(")"); //$NON-NLS-1$
			} else if (arg instanceof String) {
				bob.append("Message(").append((String) arg).append(')'); //$NON-NLS-1$
			} else if (arg instanceof Integer) {
				bob.append("LogLevel(").append(LogLevelMapper.getValue((Integer) arg)).append(')'); //$NON-NLS-1$
			} else if (arg instanceof Throwable) {
				final Throwable throwable = (Throwable) arg;
				bob.append("Throwable("); //$NON-NLS-1$
				final StringWriter stringWriter = new StringWriter();
				final PrintWriter writer = new PrintWriter(stringWriter);
				throwable.printStackTrace(writer);
				writer.close();
				bob.append(stringWriter.toString()).append(')');
			} else {
				bob.append("Object(").append(arg).append(')'); //$NON-NLS-1$
			}
			bob.append(", "); //$NON-NLS-1$
		}
		bob.setLength(bob.length() - 2);
	}
}
