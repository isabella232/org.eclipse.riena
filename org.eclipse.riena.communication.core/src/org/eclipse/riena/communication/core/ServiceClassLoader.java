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
package org.eclipse.riena.communication.core;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * A {@link ClassLoader} that uses the given {@code serviceBundle} for class
 * loading and if that fails it falls back to the internal {@code buddyBundle}
 * which should allow buddy class-loading.<br>
 * All other requests to this class loader will be delegated to the given
 * {@code parent} class loader.
 * 
 * @since 3.0
 */
public class ServiceClassLoader extends ClassLoader {

	private final Bundle serviceBundle;
	private final Bundle buddyBundle = FrameworkUtil.getBundle(ServiceClassLoader.class);

	/**
	 * Creates a {@link ClassLoader} that uses the {@code serviceBundle} with
	 * the internal {@link buddyBundle} as a fall-back for class loading and
	 * with the parent class loader of this classes class loader for all other
	 * requests.
	 * 
	 * @param serviceBundle
	 */
	public ServiceClassLoader(final Bundle serviceBundle) {
		this(ServiceClassLoader.class.getClassLoader(), serviceBundle);
	}

	/**
	 * Creates a {@link ClassLoader} that uses the {@code serviceBundle} with
	 * the internal {@link buddyBundle} as a fall-back for class loading and
	 * with the given parent class loader for all other requests.
	 * 
	 * @param parent
	 * @param serviceBundle
	 */
	public ServiceClassLoader(final ClassLoader parent, final Bundle serviceBundle) {
		super(parent);
		this.serviceBundle = serviceBundle;
	}

	@Override
	public Class<?> loadClass(final String name) throws ClassNotFoundException {
		try {
			return serviceBundle.loadClass(name);
		} catch (final ClassNotFoundException e) {
			// Fallback which allows the use of buddy-classloading
			return buddyBundle.loadClass(name);
		}
	}

}
