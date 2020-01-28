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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.ThreadContext;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.riena.core.exception.Failure;
import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.core.util.VariableManagerUtil;
import org.eclipse.riena.internal.core.Activator;

/**
 * The {@code Log4jMDCDiagnosticContext} is a log4j2 diagnostic context that uses log4j2 {@code ThreadContext} class.
 * <p>
 * This diagnostic context can be configured like this:
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.riena.core.log4jDiagnosticContext&quot;&gt;
 *     &lt;diagnosticContext class=&quot;org.eclipse.riena.core.logging.log4j.Log4jMDCDiagnosticContext:user=${java.system.property:riena.user.name}@${java.system.property:riena.host.name}&quot; /&gt;
 * &lt;/extension&gt;
 * </pre>
 * 
 * The {@code Log4jMDCDiagnosticContext} is a configurable {@code IExecutableExtension}. The configuration data (the data after the colon ':' in the class
 * attribute) defines the key(s) and value(s) that get pushed to the {@code ThreadContext}. If multiple keys/values should be pushed to the
 * {@code ThreadContext} than they have to be separated by commas.
 * <p>
 * The values can contain variable substitutions which will be resolved by the {@code IStringVariableManager}.<br>
 * If a key is preceded with an asterisk '*' the key will be evaluated each time the diagnostic context will be set; otherwise only once when this class gets
 * instantiated.
 */
public class Log4jMDCDiagnosticContext implements ILog4jDiagnosticContext, IExecutableExtension {

	private final Map<String, String> staticContextInfo = new HashMap<String, String>();
	private final Map<String, String> dynamicContextInfo = new HashMap<String, String>();

	public void push() {
		for (final Entry<String, String> entry : staticContextInfo.entrySet()) {
			ThreadContext.put(entry.getKey(), entry.getValue());
		}
		for (final Entry<String, String> entry : dynamicContextInfo.entrySet()) {
			try {
				ThreadContext.put(entry.getKey(), VariableManagerUtil.substitute(entry.getValue()));
			} catch (final CoreException e) {
				throw new Log4jMDCDiagnosticContextFailure("Could not perform string substitution for: " //$NON-NLS-1$
						+ entry.getValue(), e);
			}
		}
	}

	public void pop() {
		for (final String key : staticContextInfo.keySet()) {
			ThreadContext.remove(key);
		}
		for (final String key : dynamicContextInfo.keySet()) {
			ThreadContext.remove(key);
		}
	}

	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data) throws CoreException {
		try {
			for (final Entry<String, String> entry : PropertiesUtils.asMap(data, new String[] {}).entrySet()) {
				final String key = entry.getKey();
				if (key.startsWith("*") && key.length() > 1) { //$NON-NLS-1$
					dynamicContextInfo.put(key.substring(1), entry.getValue());
				} else {
					staticContextInfo.put(key, VariableManagerUtil.substitute(entry.getValue()));
				}
			}
		} catch (final IllegalArgumentException e) {
			throw configurationException("Bad configuration.", e); //$NON-NLS-1$
		}
	}

	private CoreException configurationException(final String message, final Exception e) {
		return new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

	private static class Log4jMDCDiagnosticContextFailure extends Failure {

		private static final long serialVersionUID = 6386788702002693828L;

		public Log4jMDCDiagnosticContextFailure(final String msg, final Throwable cause) {
			super(msg, cause);
		}
	}
}
