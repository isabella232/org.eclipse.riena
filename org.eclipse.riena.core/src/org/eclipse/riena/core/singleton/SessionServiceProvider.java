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
package org.eclipse.riena.core.singleton;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.riena.internal.core.singleton.RAPSingletonProvider;
import org.eclipse.riena.internal.core.singleton.RCPSingletonProvider;

/**
 * The {@code SessionServiceProvider} should be used for creating state-full
 * OSGi services, e.g. creating actual instances of a service, which may be used
 * in a RAP client.<br>
 * The {@code SessionServiceProvider} assures that when run in a RAP environment
 * the service instance will be created by RAP's session aware singleton
 * creation technique in a lazy manner using Java's dynamic proxies.<br>
 * When not run in a RAP environment the instance will just be created, i.e.
 * {@code Class.newInstance()}.<br>
 * However, in both cases an optional initializer may be executed and the
 * service instance will be wired.
 * <p>
 * Its intended usage is like this, where {@code IServiceInterface} is the
 * services interface and {@code ServiceImplementation} is the services
 * implementation:
 * 
 * <pre>
 * public void start(final BundleContext context) throws Exception {
 * 	super.start(context);
 * 	Activator.plugin = this;
 * 
 * 	IServiceInterface service = SessionServiceProvider.createService(IServiceInterface.class,
 * 			ServiceImplementation.class);
 * 	context.registerService(IServiceInterface.class.getName(), service, null);
 * }
 * </pre>
 * 
 * @since 4.0
 */
public final class SessionServiceProvider {

	private SessionServiceProvider() {
	}

	/**
	 * Create (construct) a service instance.
	 * 
	 * @param serviceInterfaceType
	 *            the service interface type
	 * @param serviceType
	 *            the service implementation type
	 * @return the service implementation
	 */
	public static <S> S createService(final Class<? super S> serviceInterfaceType, final Class<S> serviceType) {
		return createService(serviceInterfaceType, serviceType, null);
	}

	/**
	 * Create (construct) a service instance.
	 * 
	 * @param serviceInterfaceType
	 *            the service interface type
	 * @param serviceType
	 *            the service implementation type
	 * @param initializer
	 *            the optional initializer for the service implementation (may
	 *            be {@code null}.
	 * @return the service implementation
	 */
	public static <S> S createService(final Class<? super S> serviceInterfaceType, final Class<S> serviceType,
			final ISingletonInitializer<S> initializer) {
		if (RAPSingletonProvider.isAvailable()) {
			return (S) Proxy.newProxyInstance(serviceInterfaceType.getClassLoader(),
					new Class<?>[] { serviceInterfaceType }, new ServiceInvocationHandler<S>(serviceType, initializer));
		}
		return RCPSingletonProvider.getInstance(serviceType, initializer);
	}

	private static class ServiceInvocationHandler<S> implements InvocationHandler {

		private final Class<S> serviceType;
		private final ISingletonInitializer<S> initializer;

		public ServiceInvocationHandler(final Class<S> serviceType, final ISingletonInitializer<S> initializer) {
			this.serviceType = serviceType;
			this.initializer = initializer;
		}

		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			final S instance = RAPSingletonProvider.getInstance(serviceType, initializer);
			if (instance != null) {
				try {
					return method.invoke(instance, args);
				} catch (final InvocationTargetException e) {
					throw e.getTargetException();
				}
			}
			throw new SingletonFailure("Could not create a session based RAP instance for type " + serviceType); //$NON-NLS-1$
		}

	}

}
