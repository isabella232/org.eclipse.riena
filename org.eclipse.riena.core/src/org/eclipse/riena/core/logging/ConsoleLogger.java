/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.logging;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.LogFilter;
import org.eclipse.equinox.log.Logger;

/**
 * The ConsoleLogger simply writes all logs to System.out/.err.<br>
 * Therefore it can be used when standard logging is not available or not
 * usable, e.g. within initializations of the Logger itself.
 * <p>
 * However, the <code>ConsoleLogger</code> pays attention to the
 * <code>SystemPropertyLogFilter</code> and because of that logging output can
 * be controlled be a system property.
 */
public class ConsoleLogger implements Logger {

	private final String name;
	private static String nameAndHost;
	private static DateFormat formatter;
	/**
	 * Supported log level strings are: "debug", "info", "warn", "error" and
	 * "none".
	 */
	public static final String RIENA_CONSOLE_LOG_LEVEL_PROPERTY = "riena.console.loglevel"; //$NON-NLS-1$

	private static final LogFilter LOG_FILTER = new SystemPropertyLogFilter(RIENA_CONSOLE_LOG_LEVEL_PROPERTY, "debug"); //$NON-NLS-1$

	static {
		final String user = System.getProperty("user.name", "?"); //$NON-NLS-1$ //$NON-NLS-2$
		String host;
		try {
			host = Inet4Address.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
			host = "?"; //$NON-NLS-1$
		}
		final StringBuilder buffer = new StringBuilder();
		buffer.append(user).append('@').append(host);
		nameAndHost = buffer.toString();

		formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z"); //$NON-NLS-1$
	}

	public ConsoleLogger(final String name) {
		this.name = name;
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#isLoggable(int)
	 */
	public boolean isLoggable(final int level) {
		return LOG_FILTER.isLoggable(null, name, level);
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#log(int, java.lang.String)
	 */
	public void log(final int level, final String message) {
		log(level, null, null, message, null);
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#log(int, java.lang.String,
	 * java.lang.Throwable)
	 */
	public void log(final int level, final String message, final Throwable exception) {
		log(level, null, null, message, exception);
	}

	/*
	 * @see
	 * org.eclipse.equinox.log.Logger#log(org.osgi.framework.ServiceReference,
	 * int, java.lang.String)
	 */
	public void log(final ServiceReference sr, final int level, final String message) {
		log(level, null, sr, message, null);
	}

	/*
	 * @see
	 * org.eclipse.equinox.log.Logger#log(org.osgi.framework.ServiceReference,
	 * int, java.lang.String, java.lang.Throwable)
	 */
	public void log(final ServiceReference sr, final int level, final String message, final Throwable exception) {
		log(level, null, null, message, exception);
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#log(java.lang.Object, int,
	 * java.lang.String)
	 */
	public void log(final Object context, final int level, final String message) {
		log(level, context, null, message, null);
	}

	/*
	 * @see org.eclipse.equinox.log.Logger#log(java.lang.Object, int,
	 * java.lang.String, java.lang.Throwable)
	 */
	public void log(final Object context, final int level, final String message, final Throwable exception) {
		log(level, context, null, message, exception);
	}

	private void log(final int level, final Object context, final ServiceReference sr, final String message,
			final Throwable throwable) {
		if (!isLoggable(level)) {
			return;
		}
		final StringBuilder bob = new StringBuilder();
		synchronized (formatter) {
			bob.append(formatter.format(new Date()));
		}
		bob.append(' ');
		bob.append(nameAndHost);
		bob.append(' ');
		bob.append(getLevel(level));
		bob.append(' ');
		bob.append('[');
		bob.append(Thread.currentThread().getName());
		if (context != null) {
			bob.append(", CTX: "); //$NON-NLS-1$
			bob.append(context);
		}
		if (sr != null) {
			bob.append(", SR: "); //$NON-NLS-1$
			bob.append(sr);
		}
		bob.append("] "); //$NON-NLS-1$
		bob.append(name);
		bob.append(' ');
		bob.append(message);
		if (throwable != null) {
			final StringWriter stringWriter = new StringWriter();
			final PrintWriter writer = new PrintWriter(stringWriter);
			throwable.printStackTrace(writer);
			writer.close();
			bob.append('\n').append(stringWriter.toString());
		}
		final PrintStream printStream = getPrintStream(level);
		printStream.println(bob.toString());
	}

	/**
	 * @param level
	 * @return
	 */
	private String getLevel(final int level) {
		switch (level) {
		case LogService.LOG_DEBUG:
			return "DEBUG"; //$NON-NLS-1$
		case LogService.LOG_INFO:
			return "INFO"; //$NON-NLS-1$
		case LogService.LOG_WARNING:
			return "WARN"; //$NON-NLS-1$
		case LogService.LOG_ERROR:
			return "ERROR"; //$NON-NLS-1$
		default:
			return "CUSTOM(" + level + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private PrintStream getPrintStream(final int level) {
		return LogService.LOG_WARNING == level || LogService.LOG_ERROR == level ? System.err : System.out;
	}

}
