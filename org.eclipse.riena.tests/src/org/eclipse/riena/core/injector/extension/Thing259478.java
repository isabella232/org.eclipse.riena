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
package org.eclipse.riena.core.injector.extension;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;

import org.eclipse.riena.core.util.PropertiesUtils;

/**
 *
 */
public class Thing259478 implements IExecutableExtension {

	// Ignoring Checkstyle warning about the field not being private:
	// appears to be ok for testing.
	public Map<String, String> properties;

	public Thing259478() {
	}

	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data)
			throws CoreException {
		properties = PropertiesUtils.asMap(data);
	}

}
