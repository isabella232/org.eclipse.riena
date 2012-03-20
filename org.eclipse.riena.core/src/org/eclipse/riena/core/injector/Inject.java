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
package org.eclipse.riena.core.injector;

import org.eclipse.riena.core.injector.extension.ExtensionDescriptor;
import org.eclipse.riena.core.injector.service.ServiceDescriptor;

/**
 * This class begins an injector definition.
 */
public final class Inject {

	private Inject() {
		// utility
	}

	/**
	 * @see ServiceDescriptor
	 * 
	 * @param className
	 *            class name
	 * @return
	 */
	public static ServiceDescriptor service(final String className) {
		return new ServiceDescriptor(className);
	}

	/**
	 * @see ServiceDescriptor
	 * 
	 * @param clazz
	 *            class instance
	 * @return
	 */
	public static ServiceDescriptor service(final Class<?> clazz) {
		return new ServiceDescriptor(clazz);
	}

	/**
	 * @see ExtensionDescriptor
	 * 
	 * @param extensionPointId
	 * @return
	 */
	public static ExtensionDescriptor extension(final String extensionPointId) {
		return new ExtensionDescriptor(extensionPointId);
	}

	/**
	 * @see ExtensionDescriptor
	 * 
	 * @param extensionPointId
	 * @return
	 * @since 1.2
	 */
	public static ExtensionDescriptor extension() {
		return new ExtensionDescriptor();
	}

}
