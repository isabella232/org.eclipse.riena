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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.core.Activator;

/**
 * An invocation handler that lazily creates an executable extension on demand.
 */
final class LazyExecutableExtension implements InvocationHandler {

	private final BundleContext bundleContext;
	private final IConfigurationElement configurationElement;
	private final String attributeName;
	private final boolean wire;
	private volatile Object delegate;

	static Object newInstance(final Class<?> returnType, final IConfigurationElement configurationElement,
			final String attributeName, final boolean wire) {
		return Proxy.newProxyInstance(returnType.getClassLoader(), new Class[] { returnType },
				new LazyExecutableExtension(getBundleContext(returnType), configurationElement, attributeName, wire));
	}

	private LazyExecutableExtension(final BundleContext bundleContext,
			final IConfigurationElement configurationElement, final String attributeName, final boolean wire) {
		this.bundleContext = bundleContext;
		this.configurationElement = configurationElement;
		this.attributeName = attributeName;
		this.wire = wire;
	}

	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		// Double-check idiom for lazy initialization of instance fields (Joshua Bloch, Effective Java, Item 71)
		Object temp = delegate;
		if (temp == null) {
			synchronized (this) {
				temp = delegate;
				if (temp == null) {
					final Object temp2 = configurationElement.createExecutableExtension(attributeName);
					if (wire) {
						Wire.instance(temp2).andStart(bundleContext);
					}
					delegate = temp = temp2;
				}
			}
		}
		try {
			return method.invoke(delegate, args);
		} catch (final InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	private static BundleContext getBundleContext(final Class<?> returnType) {
		final Bundle bundle = FrameworkUtil.getBundle(returnType);
		if (bundle != null) {
			final BundleContext bundleContext = bundle.getBundleContext();
			if (bundleContext != null) {
				return bundleContext;
			}
		}
		return Activator.getDefault().getContext();
	}

}
