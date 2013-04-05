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
package org.eclipse.riena.core;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.logging.ConsoleLogger;
import org.eclipse.riena.internal.core.logging.LoggerProvider;

/**
 * Gets a logger ({@code Logger}) from the {@code IRienaActivator}. <br>
 * <b>Note: </b> The recommended pattern for classes within bundles where their
 * activator is an instance of {@code IRienaActivator} is: <br>
 * <code>
 * 	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), This.class);
 * </code>
 */
public final class Log4r {

	private Log4r() {
		// utility
	}

	/**
	 * Get a {@code Logger} for class {@code clazz}.
	 * <p>
	 * If this is called in an OSGi context this method will return the
	 * {@code Logger} of the Riena framework. Otherwise if the boolean system
	 * property {@code LoggerMill.RIENA_DEFAULT_LOGGING} is {@code true} a
	 * simple {@code Logger} that writes to the console will be returned. If the
	 * system property is {@code false} a <i>null</i> logger that does nothing
	 * will be returned.
	 * 
	 * @param clazz
	 *            categorizes the logger
	 * @return the {@code Logger}
	 */
	public static Logger getLogger(final Class<?> clazz) {
		final Bundle bundle = FrameworkUtil.getBundle(clazz);
		if (bundle != null) {
			return LoggerProvider.instance().getLogger(clazz);
		}
		return getEmergencyLogger(clazz.getName());
	}

	/**
	 * Get a {@code Logger} for class {@code clazz}.
	 * <p>
	 * <b>Note: </b>Consider using {@code Log4r.getLogger(Class<? clazz)}. It is
	 * functional identical but does not have the dependency to the activator.
	 * 
	 * @param activator
	 *            If the activator is NOT {@code null} this method will return
	 *            the {@code Logger} of the Riena framework. Otherwise if the
	 *            boolean system property
	 *            {@code LoggerMill.RIENA_DEFAULT_LOGGING} is {@code true} a
	 *            simple {@code Logger} that writes to the console will be
	 *            returned. If the system property is {@code false} a
	 *            <i>null</i> logger that does nothing will be returned.
	 * @param clazz
	 *            categorizes the logger
	 * @return the {@code Logger}
	 */
	public static Logger getLogger(final IRienaActivator activator, final Class<?> clazz) {
		if (activator != null) {
			return LoggerProvider.instance().getLogger(clazz);
		}
		return getEmergencyLogger(clazz.getName());
	}

	/**
	 * Get a {@code Logger} for class {@code className}.
	 * 
	 * @param activator
	 *            If the activator is NOT {@code null} this method will return
	 *            the {@code Logger} of the Riena framework. Otherwise if the
	 *            boolean system property
	 *            {@code LoggerMill.RIENA_DEFAULT_LOGGING} is {@code true} a
	 *            simple {@code Logger} that writes to the console will be
	 *            returned. If the system property is {@code false} a
	 *            <i>null</i> logger that does nothing will be returned.
	 * @param className
	 *            categorizes the logger
	 * @return the {@code Logger}
	 */
	public static Logger getLogger(final IRienaActivator activator, final String className) {
		if (activator != null) {
			return LoggerProvider.instance().getLogger(className);
		}
		return getEmergencyLogger(className);
	}

	// use ConsoleLogger unless the system property specifies otherwise
	private static Logger getEmergencyLogger(final String className) {
		return RienaStatus.isDevelopment() ? new ConsoleLogger(className) : new NullLogger();
	}

	private static class NullLogger implements Logger {

		public String getName() {
			return "NullLogger"; //$NON-NLS-1$
		}

		public boolean isLoggable(final int level) {
			return false;
		}

		public void log(final int level, final String message) {
		}

		public void log(final int level, final String message, final Throwable exception) {
		}

		public void log(final ServiceReference sr, final int level, final String message) {
		}

		public void log(final ServiceReference sr, final int level, final String message, final Throwable exception) {
		}

		public void log(final Object context, final int level, final String message) {
		}

		public void log(final Object context, final int level, final String message, final Throwable exception) {
		}

	}
}
