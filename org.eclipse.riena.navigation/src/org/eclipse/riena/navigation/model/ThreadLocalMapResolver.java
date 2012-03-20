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
package org.eclipse.riena.navigation.model;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

/**
 * A resolver for dynamic variables. The map provided to the configure method
 * sets the values for the current thread. The class must be public to be
 * accessible for org.eclipse.core.variables
 */
public class ThreadLocalMapResolver implements IDynamicVariableResolver {

	static private ThreadLocal<Map<String, Object>> variableMapping = new ThreadLocal<Map<String, Object>>();

	/**
	 * 
	 */
	public ThreadLocalMapResolver() {
	}

	public static void configure(final Map<String, Object> mapping) {
		variableMapping.set(mapping);
	}

	public static void cleanup() {
		configure(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.variables.IDynamicVariableResolver#resolveValue(org.
	 * eclipse.core.variables.IDynamicVariable, java.lang.String)
	 */
	/**
	 * @since 4.0
	 */
	public String resolveValue(final IDynamicVariable variable, final String argument) throws CoreException {

		final Map<String, Object> currentMapping = variableMapping.get();
		if (currentMapping == null) {
			return null;
		} else {
			final Object obj = currentMapping.get(variable.getName());
			if (argument == null) {
				return obj == null ? null : String.valueOf(obj);
			} else {
				try {
					return BeanUtils.getProperty(obj, argument);
				} catch (final Exception ex) {
					return obj == null ? null : String.valueOf(obj);
				}
			}
		}
	}
}
