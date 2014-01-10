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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.core.Activator;

/**
 * The <code>VariableManagerUtil</code> is lightweight wrapper for the
 * <code>StringVariableManager</code>.
 */
public final class VariableManagerUtil {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), VariableManagerUtil.class);

	private VariableManagerUtil() {
		// utility
	}

	/**
	 * Perform a string substitution on the given expression.
	 * 
	 * @param expression
	 *            to substitute in, if {@code null} or empty, {@code null} or
	 *            empty will be returned
	 * @return the substituted expression
	 * @throws CoreException
	 *             if unable to resolve the value of one or more variables
	 */
	public static String substitute(final String expression) throws CoreException {
		if (StringUtils.isEmpty(expression)) {
			return expression;
		}
		final IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		return variableManager.performStringSubstitution(expression);
	}

	/**
	 * Add a new string variable to the <code>StringVariableManager</code>.
	 * 
	 * @param key
	 * @param value
	 * @throws CoreException
	 *             if variable already exists with a different value
	 */
	public static void addVariable(final String key, final String value) throws CoreException {
		final IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		final IValueVariable[] variables = new IValueVariable[] { variableManager.newValueVariable(key, null, true,
				value) };
		try {
			variableManager.addVariables(variables);
		} catch (final CoreException e) {
			final IValueVariable existingValue = variableManager.getValueVariable(key);
			if (existingValue.getValue().equals(value)) {
				LOGGER.log(LogService.LOG_WARNING, "Already defined: (" + key + "," + value + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				throw e;
			}
		}
	}

	public static void addVariables(final Map<String, String> variables) throws CoreException {
		final IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		final List<IValueVariable> add = new ArrayList<IValueVariable>(variables.size());
		for (final Entry<String, String> entry : variables.entrySet()) {
			final IValueVariable value = variableManager.getValueVariable(entry.getKey());
			if (value != null && value.getValue().equals(entry.getValue())) {
				LOGGER.log(LogService.LOG_WARNING,
						"Already defined: (" + value.getName() + "," + value.getValue() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				add.add(variableManager.newValueVariable(entry.getKey(), null, true, entry.getValue()));
			}
		}
		variableManager.addVariables(add.toArray(new IValueVariable[add.size()]));
	}

	/**
	 * Remove a string variable from the <code>StringVariableManager</code>.
	 * 
	 * @param key
	 */
	public static void removeVariable(final String key) {
		final IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		final IValueVariable[] variables = new IValueVariable[] { variableManager.newValueVariable(key, null, true,
				null) };
		variableManager.removeVariables(variables);
	}
}
