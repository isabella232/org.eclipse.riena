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
package org.eclipse.riena.internal.core.injector.extension;

import java.lang.reflect.Proxy;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Factory for interface beans.
 */
final class InterfaceBeanFactory {

	private InterfaceBeanFactory() {
		// utility
	}

	static Object newInstance(final boolean symbolReplace, final Class<?> interfaceType,
			final IConfigurationElement configurationElement) {
		return Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType },
				new InterfaceBeanHandler(interfaceType, symbolReplace, configurationElement));
	}
}
