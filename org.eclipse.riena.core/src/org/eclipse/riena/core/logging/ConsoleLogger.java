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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.naming.NoPermissionException;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogLevel;
import org.osgi.service.log.LoggerConsumer;

import org.eclipse.equinox.log.LogFilter;
import org.eclipse.equinox.log.Logger;

/**
 * The ConsoleLogger simply writes all logs to System.out/.err.<br>
 * Therefore it can be used when standard logging is not available or not usable, e.g. within initializations of the Logger itself.
 * <p>
 * However, the <code>ConsoleLogger</code> pays attention to the <code>SystemPropertyLogFilter</code> and because of that logging output can be controlled be a
 * system property.
 */
public class ConsoleLogger implements Logger {

	private final String name;
	private static String nameAndHost;
	private static DateFormat formatter;
	/**
	 * Supported log level strings are: "debug", "info", "warn", "error" and "none".
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

	public String getName() {
		return name;
	}

	public boolean isLoggable(final int level) {
		return LOG_FILTER.isLoggable(null, name, level);
	}

	public void log(final int level, final String message) {
		log(level, null, null, message, null);
	}

	public void log(final int level, final String message, final Throwable exception) {
		log(level, null, null, message, exception);
	}

	public void log(final ServiceReference<?> sr, final int level, final String message) {
		log(level, null, sr, message, null);
	}

	public void log(final ServiceReference<?> sr, final int level, final String message, final Throwable exception) {
		log(level, null, null, message, exception);
	}

	public void log(final Object context, final int level, final String message) {
		log(level, context, null, message, null);
	}

	public void log(final Object context, final int level, final String message, final Throwable exception) {
		log(level, context, null, message, exception);
	}

	private static LogLevel getLogLevel(final int level) {
		if (level < 0 || level > LogLevel.values().length - 1) {
			return LogLevel.TRACE;
		} else {
			return LogLevel.values()[level];
		}
	}

	private void log(final int level, final Object context, final ServiceReference<?> sr, final String message, final Throwable throwable) {
		log(getLogLevel(level), context, sr, message, throwable);
	}

	private void log(final LogLevel logLevel, final Object context, final ServiceReference<?> sr, final String message, final Throwable throwable) {
		if (!isLoggable(logLevel)) {
			return;
		}
		final StringBuilder bob = new StringBuilder();
		synchronized (formatter) {
			bob.append(formatter.format(new Date()));
		}
		bob.append(' ');
		bob.append(nameAndHost);
		bob.append(' ');
		bob.append(logLevel);
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
		final PrintStream printStream = getPrintStream(logLevel);
		printStream.println(bob.toString());
	}

	private PrintStream getPrintStream(final LogLevel logLevel) {
		return logLevel == LogLevel.WARN || logLevel == LogLevel.ERROR ? System.err : System.out;
	}

	private boolean isLoggable(final LogLevel logLevel) {
		return isLoggable(logLevel.ordinal());
	}

	private void log(final LogLevel logLevel, final String format, final Object... arguments) {
		if (arguments != null && arguments.length > 0 && arguments[arguments.length - 1] instanceof Throwable) {
			final Object[] newArguments = Arrays.copyOf(arguments, arguments.length - 1);
			log(logLevel, null, null, String.format(format, newArguments), (Throwable) arguments[arguments.length - 1]);
		} else {
			log(logLevel, null, null, String.format(format, arguments), null);
		}
	}

	public boolean isTraceEnabled() {
		return isLoggable(LogLevel.TRACE);
	}

	public void trace(final String message) {
		log(LogLevel.TRACE, message, new Object[] {});
	}

	public void trace(final String format, final Object arg) {
		log(LogLevel.TRACE, format, new Object[] { arg });
	}

	public void trace(final String format, final Object arg1, final Object arg2) {
		log(LogLevel.TRACE, format, new Object[] { arg1, arg2 });
	}

	public void trace(final String format, final Object... arguments) {
		log(LogLevel.TRACE, format, arguments);
	}

	public <E extends Exception> void trace(final LoggerConsumer<E> consumer) throws E {
		consumer.accept(this);
	}

	public boolean isDebugEnabled() {
		return isLoggable(LogLevel.DEBUG);
	}

	public void debug(final String message) {
		log(LogLevel.DEBUG, message, new Object[] {});
	}

	public void debug(final String format, final Object arg) {
		log(LogLevel.DEBUG, format, new Object[] { arg });
	}

	public void debug(final String format, final Object arg1, final Object arg2) {
		log(LogLevel.DEBUG, format, new Object[] { arg1, arg2 });
	}

	public void debug(final String format, final Object... arguments) {
		log(LogLevel.DEBUG, format, arguments);
	}

	public <E extends Exception> void debug(final LoggerConsumer<E> consumer) throws E {
		consumer.accept(this);
	}

	public boolean isInfoEnabled() {
		return isLoggable(LogLevel.DEBUG);
	}

	public void info(final String message) {
		log(LogLevel.INFO, message, new Object[] {});
	}

	public void info(final String format, final Object arg) {
		log(LogLevel.INFO, format, new Object[] { arg });
	}

	public void info(final String format, final Object arg1, final Object arg2) {
		log(LogLevel.INFO, format, new Object[] { arg1, arg2 });
	}

	public void info(final String format, final Object... arguments) {
		log(LogLevel.INFO, format, arguments);
	}

	public <E extends Exception> void info(final LoggerConsumer<E> consumer) throws E {
		consumer.accept(this);
	}

	public boolean isWarnEnabled() {
		return isLoggable(LogLevel.WARN);
	}

	public void warn(final String message) {
		log(LogLevel.WARN, message, new Object[] {});
	}

	public void warn(final String format, final Object arg) {
		log(LogLevel.WARN, format, new Object[] { arg });
	}

	public void warn(final String format, final Object arg1, final Object arg2) {
		log(LogLevel.WARN, format, new Object[] { arg1, arg2 });
	}

	public void warn(final String format, final Object... arguments) {
		log(LogLevel.WARN, format, arguments);
	}

	public <E extends Exception> void warn(final LoggerConsumer<E> consumer) throws E {
		consumer.accept(this);
	}

	public boolean isErrorEnabled() {
		return isLoggable(LogLevel.ERROR);
	}

	public void error(final String message) {
		log(LogLevel.ERROR, message, new Object[] {});
	}

	public void error(final String format, final Object arg) {
		log(LogLevel.ERROR, format, new Object[] { arg });
	}

	public void error(final String format, final Object arg1, final Object arg2) {
		log(LogLevel.ERROR, format, new Object[] { arg1, arg2 });
	}

	public void error(final String format, final Object... arguments) {
		log(LogLevel.ERROR, format, arguments);
	}

	public <E extends Exception> void error(final LoggerConsumer<E> consumer) throws E {
		consumer.accept(this);
	}

	public void audit(final String message) {
		log(LogLevel.AUDIT, message, new Object[] {});
	}

	public void audit(final String format, final Object arg) {
		log(LogLevel.AUDIT, format, new Object[] { arg });
	}

	public void audit(final String format, final Object arg1, final Object arg2) {
		log(LogLevel.AUDIT, format, new Object[] { arg1, arg2 });
	}

	public void audit(final String format, final Object... arguments) {
		log(LogLevel.AUDIT, format, arguments);
	}

	public static void main(final String[] args) {
		final Logger logger = new ConsoleLogger("Test");
		logger.audit("Message1");
		logger.audit("Format %d", 42);
		logger.audit("Format %s%d", "08", 15);
		logger.audit("%d %d format %d", 1, 2, 3);

		logger.audit("Format", new NoPermissionException("NPE"));
		logger.audit("Format %s", "08", 15, new NoPermissionException("NPE"));
		logger.audit("%d %d format %d", 1, 2, 3, new NoPermissionException("NPE"));
	}

}
