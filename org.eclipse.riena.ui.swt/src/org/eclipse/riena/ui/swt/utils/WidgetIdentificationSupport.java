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
package org.eclipse.riena.ui.swt.utils;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.Trace;
import org.eclipse.riena.internal.ui.swt.Activator;

/**
 * Helper class for setting 'rienaid' on widgets.
 * 
 * @since 1.2
 */
public final class WidgetIdentificationSupport {

	public static final String RIENA_ID = "rienaid"; //$NON-NLS-1$
	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), WidgetIdentificationSupport.class);
	private static boolean debugOutput = Trace.isOn(WidgetIdentificationSupport.class, "debug"); //$NON-NLS-1$

	private WidgetIdentificationSupport() {
		// utility class
	}

	/**
	 * Sets rienaid for shell. If 'riena.testing.widgetid.mainshell' system
	 * property is set - it's value if used. otherwise rienaid is set to value
	 * 'default'
	 * 
	 * @param aShell
	 *            shell
	 */
	public static void setIdentification(final Shell aShell) {
		aShell.setData(RIENA_ID, System.getProperty("riena.testing.widgetid.mainshell", "default")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Sets rienaid for widget. dot-separated concatenated id parts are used as
	 * a value. i.e. parts: ['a', 'b', 'c'], value: 'a.b.c'
	 * 
	 * @param aWidget
	 *            widget
	 * @param aParts
	 *            sequence of id parts
	 */
	public static void setIdentification(final Widget aWidget, final String... aParts) {
		final StringBuilder fullId = new StringBuilder();

		for (final String part : aParts) {
			if (fullId.length() != 0) {
				fullId.append('.');
			}

			fullId.append(part);
		}
		if (debugOutput) {
			LOGGER.log(LogService.LOG_DEBUG,
					String.format("registering widget %s, (class: %s)", fullId, aWidget.getClass())); //$NON-NLS-1$
		}
		aWidget.setData(RIENA_ID, fullId.toString());
	}

	/**
	 * Sets default rienaid for a widget. String representation of widget class
	 * name is used as a value
	 * 
	 * @param aWidget
	 *            widget
	 */
	public static void setDefaultIdentification(final Widget aWidget) {
		setIdentification(aWidget, aWidget.getClass().getName());
	}

	/**
	 * Returns the Riena ID of the given widget.
	 * 
	 * @param aWidget
	 *            widget
	 * @return Riena ID or {@code null} if it has not been set
	 * @since 3.0
	 */
	public static String getIdentification(final Widget aWidget) {
		return (String) aWidget.getData(RIENA_ID);
	}

}
