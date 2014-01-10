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
package org.eclipse.riena.core.util;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.eclipse.core.runtime.Platform;

import org.eclipse.riena.internal.core.Activator;

/**
 * Tracing support.
 */
public final class Trace {

	private static final String SLASH = "/"; //$NON-NLS-1$

	private Trace() {
		// utility
	}

	/**
	 * This checks whether debug is requested for the debug option string
	 * created from the method parameters as follows:
	 * 
	 * <pre>
	 * &lt;symbolic bundle name of bundle containing clazz&gt; { "/" &lt;simple name of clazz&gt; } "/" &lt;option&gt;
	 * </pre>
	 * 
	 * @param clazz
	 *            the class requesting trace support
	 * @param option
	 *            the option string
	 * @return
	 * 
	 * @see Platform.getDebugOption()
	 */
	public static boolean isOn(final Class<?> clazz, final String option) {
		return isOn(clazz, clazz, option);
	}

	/**
	 * This checks whether debug is requested for the debug option string
	 * created from the method parameters as follows:
	 * 
	 * <pre>
	 * &lt;symbolic bundle name of bundle containing clazzForBundle&gt; { "/" &lt;simple name of clazzForName&gt; } "/" &lt;option&gt;
	 * </pre>
	 * 
	 * If {@code clazzForName} is null the option string misses the { } part.
	 * 
	 * @param clazzForName
	 *            the class requesting trace support or null
	 * @param clazzForBundle
	 *            the class used for retrieving the bundle symbolic name or null
	 * @param option
	 *            the option string
	 * @return
	 * 
	 * @see Platform.getDebugOption()
	 */
	public static boolean isOn(final Class<?> clazzForName, final Class<?> clazzForBundle, final String option) {
		if (Activator.getDefault() == null) {
			// No OSGi? Maybe we are running in a plain jUnit test - so yes we want tracing
			return true;
		}
		if (!Platform.inDebugMode()) {
			return false;
		}
		if (clazzForBundle == null) {
			return false;
		}
		final Bundle bundle = FrameworkUtil.getBundle(clazzForBundle);
		if (bundle == null) {
			return false;
		}
		final StringBuilder bob = new StringBuilder(bundle.getSymbolicName());
		if (clazzForName != null) {
			bob.append(SLASH).append(clazzForName.getSimpleName());
		}
		bob.append(SLASH).append(option);
		final String debug = Platform.getDebugOption(bob.toString());
		return Boolean.parseBoolean(debug);
	}
}
