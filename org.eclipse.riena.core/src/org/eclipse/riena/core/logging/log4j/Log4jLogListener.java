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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.WeakHashMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.LoggerContext;
import org.apache.logging.log4j.status.StatusData;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.log.ExtendedLogEntry;

import org.eclipse.riena.core.logging.ConsoleLogger;
import org.eclipse.riena.core.util.IOUtils;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.util.VariableManagerUtil;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.internal.core.Activator;
import org.eclipse.riena.internal.core.logging.log4j.ILog4jDiagnosticContextExtension;
import org.eclipse.riena.internal.core.logging.log4j.ILog4jLogListenerConfigurationExtension;

/**
 * The <code>Log4LogListener</code> reroutes all logging within Riena into the Log4J logging system.<br>
 * To activate it is necessary to contribute to the extension point "org.eclipse.riena.core.logging.listeners". Within that configuration it is possible to pass
 * a ´log4j.xml´ as a resource to configure Log4j, e.g.
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.riena.core.logListeners&quot;&gt;
 *     &lt;logListener name=&quot;Log4j&quot; 
 *                     listener-class=&quot;org.eclipse.riena.core.logging.log4j.Log4jLogListener:/log4j.xml&quot;
 *                     filter-class="org.eclipse.riena.core.logging.log4j.Log4jLogFilter"
 *                     sync=&quot;true&quot;/&gt;
 * &lt;/extension&gt;
 * </pre>
 * 
 * Additionally it is possible to contribute multiple Log4j xml configuration files from various bundles and fragments with the extension point
 * "org.eclipse.riena.core.log4jConfiguration", e.g.:
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.riena.core.log4jConfiguration&quot;&gt;
 *     &lt;configuration location=&quot;/config/log4j.xml&quot; /&gt;
 * &lt;/extension&gt;
 * </pre>
 * 
 * <b>Note:</b> The logger configuration (log4j.xml) might contain substitution strings, e.g. to specify the target log location of a {@code FileAppender}, e.g.
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8" ?&gt;
 * &lt;!DOCTYPE xml&gt;
 * &lt;Configuration status="warn" name="Spirit default logging"
 * 	packages=""&gt;
 * 	&lt;Appenders&gt;
 * 		&lt;Console name="STDOUT"&gt;
 * 			&lt;PatternLayout pattern="%d [%t] %-5p %c: %m%n" /&gt;
 * 		&lt;/Console&gt;
 * 		&lt;RollingFile name="LOGFILE" fileName="${log4j.log.home}/spirit_default.log"
 * 			filePattern="${log4j.log.home}/spirit_default.log.%d{yyyy-MM-dd}"&gt;
 * 			&lt;PatternLayout pattern="%d [%t] %-5p %c: %m%n" /&gt;
 * 			&lt;Policies&gt;
 * 				&lt;TimeBasedTriggeringPolicy /&gt;
 * 			&lt;/Policies&gt;
 * 		&lt;/RollingFile&gt;
 * 	&lt;/Appenders&gt;
 * 	&lt;Loggers&gt;
 * 		&lt;Root level="warn"&gt;
 * 			&lt;AppenderRef ref="STDOUT" /&gt;
 * 			&lt;AppenderRef ref="LOGFILE" /&gt;
 * 		&lt;/Root&gt;
 * 	&lt;/Loggers&gt;
 * &lt;/Configuration&gt;
 * </pre>
 * 
 * Such substitutions can be defined with {@code StringVariableManager} extension points, e.g.
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.core.variables.valueVariables&quot;&gt;
 *     &lt;variable
 *         description=&quot;Location for the log4j log&quot;
 *         name=&quot;log4j.log.home&quot;
 *         readOnly=&quot;true&quot;
 *         initialValue=&quot;c:/projects/&quot;/&gt;
 * &lt;/extension&gt;
 * </pre>
 */
public class Log4jLogListener implements LogListener, IExecutableExtension {

	/**
	 * The default log4j configuration file (xml).
	 */
	public static final String DEFAULT_CONFIGURATION = "/log4j.default.xml"; //$NON-NLS-1$

	private final static org.eclipse.equinox.log.Logger EMERGENCY_LOGGER = new ConsoleLogger(Log4jLogListener.class.getName());

	private ILog4jDiagnosticContext log4jDiagnosticContext;

	private final WeakHashMap<LoggerContext, String> loggerContexts = new WeakHashMap<LoggerContext, String>();

	public Log4jLogListener() {
	}

	public void logged(final LogEntry entry) {
		final ExtendedLogEntry extendedEntry = (ExtendedLogEntry) entry;
		final String loggerName = extendedEntry.getLoggerName();
		final Logger logger = LogManager.getLogger(loggerName != null ? loggerName : "unknown-logger-name"); //$NON-NLS-1$

		final Level level;
		switch (extendedEntry.getLogLevel()) {
		case DEBUG:
			level = Level.DEBUG;
			break;
		case WARN:
			level = Level.WARN;
			break;
		case ERROR:
			level = Level.ERROR;
			break;
		case AUDIT:
			level = Level.ALL;
			break;
		case INFO:
			level = Level.INFO;
			break;
		case TRACE:
			level = Level.TRACE;
			break;
		default:
			level = Level.OFF;
			break;
		}
		final ILog4jDiagnosticContext diagnosticContext = log4jDiagnosticContext;
		try {
			if (diagnosticContext != null) {
				diagnosticContext.push();
			}
			logger.log(level, extendedEntry.getMessage(), extendedEntry.getException());
		} finally {
			if (diagnosticContext != null) {
				diagnosticContext.pop();
			}
		}
	}

	private IConfigurationElement configElement = null;
	private String configLocation = null;

	public void setInitializationData(final IConfigurationElement configElement, final String propertyName, final Object data) throws CoreException {
		if (data == null) {
			this.configElement = configElement;
			this.configLocation = DEFAULT_CONFIGURATION;
		} else if (data instanceof String) {
			this.configElement = configElement;
			this.configLocation = (String) data;
		}
		//		configure(configElement, (String) data);
	}

	protected void configure(final IConfigurationElement config, final String configuration) throws CoreException {
		// fetch URL of log4j configuration file using the context of the bundle where the configuration resides
		// attention: #getResource(String) would not work for fragments. As we know the exact bundle use #getEntry() instead
		final Bundle bundle = ContributorFactoryOSGi.resolve(config.getContributor());
		final URL url = bundle.getEntry(configuration);
		final String userFriendlyLocation = userFriendlyConfigurationLocation(bundle, configuration);
		if (url != null) {
			configure(userFriendlyLocation, url);
		} else {
			EMERGENCY_LOGGER.error("Could not find specified log4j configuration '" + userFriendlyLocation + "'."); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private void configure(final String userFriendlyLocation, final URL url) throws CoreException {
		EMERGENCY_LOGGER.debug("Trying to configure " + userFriendlyLocation); //$NON-NLS-1$
		File resolvedConfiguration = null;
		try {
			resolvedConfiguration = resolveVariables(url);
			configure(userFriendlyLocation, resolvedConfiguration.toURI());
		} catch (final IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(),
					"Could not configure log4j. Initializing logging from " + userFriendlyLocation + " failed.", e)); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (final CoreException e) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(),
					"Could not configure log4j. Initializing logging from " + userFriendlyLocation + " failed because string substitution failed.", e)); //$NON-NLS-1$ //$NON-NLS-2$
		} finally {
			if (resolvedConfiguration != null) {
				resolvedConfiguration.delete();
			}
		}
	}

	private void configure(final String userFriendlyLocation, final URI uri) {

		final ErrorListener listener = new ErrorListener();
		StatusLogger.getLogger().registerListener(listener);
		final LoggerContext loggerContext = LogManager.getContext(this.getClass().getClassLoader(), false, uri);
		StatusLogger.getLogger().removeListener(listener);

		final String alreadyExisting = loggerContexts.get(loggerContext);
		if (alreadyExisting == null) {
			if (listener.containsErrors()) {
				final String basicErrorMessage = "Initializing logging from '" + userFriendlyLocation + "' failed because of " + listener.getListedErrors(); //$NON-NLS-1$ //$NON-NLS-2$
				if (loggerContext instanceof AutoCloseable) {
					try {
						((AutoCloseable) loggerContext).close();
						EMERGENCY_LOGGER.error(basicErrorMessage + " but could close it."); //$NON-NLS-1$
					} catch (final Exception e) {
						EMERGENCY_LOGGER.error(basicErrorMessage + " but closing failed", e); //$NON-NLS-1$
					}
				} else {
					EMERGENCY_LOGGER.error(basicErrorMessage);
				}
			} else {
				loggerContexts.put(loggerContext, userFriendlyLocation);
				EMERGENCY_LOGGER.info("Configuring loggin from " + userFriendlyLocation + " succeeded."); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} else {
			EMERGENCY_LOGGER.warn("Configuring " + userFriendlyLocation + " is not possible because " + alreadyExisting + " has already been configured."); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	private static String userFriendlyConfigurationLocation(final Bundle bundle, final String configuration) {
		final String bundleName = bundle.getSymbolicName() != null //
				? bundle.getSymbolicName() + "-" + bundle.getVersion() // //$NON-NLS-1$
				: bundle.toString();
		return bundleName + ":" + configuration; //$NON-NLS-1$
	}

	private static File resolveVariables(final URL configuration) throws IOException, CoreException {
		final String resolvedConfiguration = VariableManagerUtil.substitute(read(configuration.openStream()));
		return writeToTemporaryFile(resolvedConfiguration);
	}

	protected static String read(final InputStream inputStream) throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		IOUtils.copy(inputStream, outputStream);
		return outputStream.toString();
	}

	protected static File writeToTemporaryFile(final String content) throws IOException {
		final File tempFile = File.createTempFile("log4j", null); //$NON-NLS-1$
		tempFile.deleteOnExit();
		final OutputStream tempOutputStream = new FileOutputStream(tempFile);
		IOUtils.copy(new ByteArrayInputStream(content.getBytes()), tempOutputStream);
		return tempFile;
	}

	private static class ErrorListener implements StatusListener {

		private final StringBuilder errors = new StringBuilder();

		@Override
		public void close() throws IOException {
		}

		public boolean containsErrors() {
			return errors.length() > 0;
		}

		public String getListedErrors() {
			return errors.toString();
		}

		@Override
		public void log(final StatusData data) {
			errors.append(" * " + data.getLevel() + ": " + data.getMessage().getFormattedMessage()); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Override
		public Level getStatusLevel() {
			return Level.ERROR;
		}

		@Override
		public String toString() {
			return containsErrors() //
					? "Errors: " + errors //$NON-NLS-1$ 
					: "No errors"; //$NON-NLS-1$
		}
	}

	/**
	 * Handle injections from logging configuration extension point. Those extending configurations must be applied AFTER creating the 'root' configuration
	 * which is initiated by the framework through {@link #setInitializationData(IConfigurationElement, String, Object)} when creating this
	 * {@link IExecutableExtension}.
	 * 
	 * @param extensions
	 * @throws CoreException
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@InjectExtension()
	public void update(final ILog4jLogListenerConfigurationExtension[] extensions) throws CoreException {
		for (final ILog4jLogListenerConfigurationExtension ext : extensions) {
			// Ignore the extension from this bundle. Only done to trigger this update method!
			if (FrameworkUtil.getBundle(Log4jLogListener.class) != ContributorFactoryOSGi.resolve(ext.getConfigurationElement().getContributor())) {
				configure(ext.getConfigurationElement(), ext.getLocation());
			}
		}
		// And finally try to configure the data from the {@link #setInitializationData(IConfigurationElement, String, Object)}
		if (configElement != null && !StringUtils.isDeepEmpty(configLocation)) {
			configure(configElement, configLocation);
		}
	}

	@InjectExtension(min = 0, max = 1)
	public void update(final ILog4jDiagnosticContextExtension log4jDiagnosticContextExtension) {
		log4jDiagnosticContext = log4jDiagnosticContextExtension == null ? null : log4jDiagnosticContextExtension.createDiagnosticContext();
	}

}
