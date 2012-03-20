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
package org.eclipse.riena.core.injector.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.injector.IStoppable;
import org.eclipse.riena.core.injector.InjectionFailure;
import org.eclipse.riena.core.util.ObjectCounter;
import org.eclipse.riena.core.util.WeakRef;

/**
 * The is the abstract base class for the specialized service injectors. See
 * {@link ServiceDescriptor} for explanation and usage. It provides common
 * functionality for the ranking injector and the filtering injector.
 */
public abstract class ServiceInjector implements IStoppable {

	/**
	 * Default ´bind´ method name.
	 */
	public static final String DEFAULT_BIND_METHOD_NAME = "bind"; //$NON-NLS-1$

	/**
	 * Default ´unbind´ method name.
	 */
	public static final String DEFAULT_UNBIND_METHOD_NAME = "unbind"; //$NON-NLS-1$

	private final ServiceDescriptor serviceDesc;
	private final WeakRef<Object> targetRef;
	private final Class<?> targetClass;
	private BundleContext context;
	private final String filter;
	private String bindMethodName;
	private String unbindMethodName;
	private Method bindMethod;
	private Method unbindMethod;
	private boolean onceOnly;
	private State state;
	private ServiceListener serviceListener;
	private BundleListener bundleListener;

	private enum State {
		INITIAL, STARTING, STARTED, STOPPING, STOPPED
	};

	private static final OnceOnlyTracker ONCE_ONLY_METHODS = new OnceOnlyTracker();

	private final static Logger LOGGER = Log4r.getLogger(ServiceInjector.class);

	/**
	 * Constructor for the <code>injectInto()</code> of
	 * <code>ServiceDescriptor</code>.
	 * 
	 * @param serviceDesc
	 * @param target
	 */
	ServiceInjector(final ServiceDescriptor serviceDesc, final Object target) {
		this.state = State.INITIAL;
		this.serviceDesc = serviceDesc;
		this.targetRef = new WeakRef<Object>(target, new Runnable() {
			public void run() {
				stop();
			}
		});
		this.targetClass = target.getClass();
		final StringBuilder bob = new StringBuilder().append("(").append(Constants.OBJECTCLASS).append("=").append( //$NON-NLS-1$ //$NON-NLS-2$
				serviceDesc.getServiceClassName()).append(")"); //$NON-NLS-1$
		if (serviceDesc.getFilter() != null) {
			bob.insert(0, "(&"); //$NON-NLS-1$
			bob.append(serviceDesc.getFilter());
			bob.append(')');
		}
		this.filter = bob.toString();
	}

	/**
	 * Start the binding/un-binding/tracking for the target.
	 * <p>
	 * If there are matching and available services then they will be bound
	 * before returning from this method.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if injector has already been started or stopped.
	 * @throws some_kind_of_unchecked_exception
	 *             if the bind/un-bind methods are wrong
	 * @param context
	 * @return this injector
	 */
	public ServiceInjector andStart(final BundleContext context) {
		Assert.isNotNull(context, "Bundle context must be not null."); //$NON-NLS-1$
		Assert.isTrue(state == State.INITIAL, "ServiceInjector already started or stopped!"); //$NON-NLS-1$
		this.state = State.STARTING;
		this.context = context;

		retrieveBindAndUnbindMethods();

		onceOnly = (Modifier.isStatic(bindMethod.getModifiers()) && Modifier.isStatic(unbindMethod.getModifiers()))
				|| onceOnly;
		if (onceOnly && ONCE_ONLY_METHODS.registerAndGetCount(this, bindMethod) > 1) {
			// prevent another service/bundle listener
			state = State.STARTED;
			return this;
		}

		doStart();
		registerBundleListener();

		state = State.STARTED;
		return this;
	}

	protected boolean isOnceOnly() {
		return onceOnly;
	}

	private void retrieveBindAndUnbindMethods() {
		Class<?> serviceClass;
		if (bindMethod != null) {
			if (bindMethod.getParameterTypes().length != 1) {
				throw new InjectionFailure("Specified bind method '" + bindMethod + "' expects exactly one parameter."); //$NON-NLS-1$ //$NON-NLS-2$
			}
			serviceClass = bindMethod.getParameterTypes()[0];
		} else {
			if (bindMethodName == null) {
				bindMethodName = DEFAULT_BIND_METHOD_NAME;
			}
			if (serviceDesc.getServiceClass() != null) {
				serviceClass = serviceDesc.getServiceClass();
				bindMethod = findBestMethod(targetClass, bindMethodName, serviceClass);
			} else {
				bindMethod = findBestMethod(targetClass, bindMethodName, serviceDesc.getServiceClassName());
				serviceClass = bindMethod.getParameterTypes()[0];
			}
		}
		if (unbindMethodName == null) {
			unbindMethodName = DEFAULT_UNBIND_METHOD_NAME;
		}
		unbindMethod = findBestMethod(targetClass, unbindMethodName, serviceClass);
	}

	private Method findBestMethod(final Class<?> targetClass, final String methodName, final String serviceClassName) {
		try {
			return findBestMethod(targetClass, methodName, targetClass.getClassLoader().loadClass(serviceClassName));
		} catch (final ClassNotFoundException e) {
			for (final Method method : targetClass.getMethods()) {
				if (method.getName().equals(methodName) && method.getParameterTypes().length == 1
						&& method.getParameterTypes()[0].getName().equals(serviceClassName)) {
					return method;
				}
			}
			throw new InjectionFailure("Could not find specified un/bind method: " + targetClass.getName() + "#" //$NON-NLS-1$ //$NON-NLS-2$
					+ methodName + "(" + serviceClassName + ") by service class name."); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private Method findBestMethod(final Class<?> targetClass, final String methodName, final Class<?> serviceClass) {
		Class<?> superWalker = serviceClass;
		while (superWalker != null) {
			Method result = findMethod(targetClass, methodName, superWalker);
			if (result != null) {
				return result;
			}
			for (final Class<?> interfaceWalker : superWalker.getInterfaces()) {
				result = findMethod(targetClass, methodName, interfaceWalker);
				if (result != null) {
					return result;
				}
			}
			superWalker = superWalker.getSuperclass();
		}
		throw new InjectionFailure("Could not find specified un/bind method: " + targetClass.getName() + "#" //$NON-NLS-1$ //$NON-NLS-2$
				+ methodName + "(" + serviceClass.getName() + ") by service class."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private Method findMethod(final Class<?> targetClass, final String methodName, final Class<?> serviceClass) {
		try {
			return targetClass.getMethod(methodName, new Class<?>[] { serviceClass });
		} catch (final SecurityException e) {
			throw new InjectionFailure("Could not find specified un/bind method: " + targetClass.getName() + "#" //$NON-NLS-1$ //$NON-NLS-2$
					+ methodName + "(" + serviceClass.getName() + ") by service class.", e); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (final NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * The <code>doStart</code> method is required to bind all matching the
	 * services with the target.
	 */
	protected abstract void doStart();

	/**
	 * Stops tracking the specified service.
	 * <p>
	 * All prior bound services will be unbound before returning from this
	 * method.
	 */
	public synchronized void stop() {
		if (state != State.STARTED) {
			return;
		}
		state = State.STOPPING;

		if (onceOnly) {
			final ServiceInjector firstInjector = ONCE_ONLY_METHODS.unregisterAndGetFirstInjector(bindMethod);
			if (firstInjector == null) {
				state = State.STOPPED;
				return;
			}
			if (firstInjector != this) {
				firstInjector.stopContinue();
				state = State.STOPPED;
				return;
			}
		}

		stopContinue();
	}

	private void stopContinue() {
		unregisterBundleListener();
		unregisterServiceListener();
		doStop();

		state = State.STOPPED;
	}

	/**
	 * The <code>doStop></code> method is required to unbind all bound services
	 * from the target.
	 */
	protected abstract void doStop();

	/**
	 * Specify the bind method. If not specified
	 * {@link #DEFAULT_BIND_METHOD_NAME} will be used.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if injector has already been started or stopped.
	 * @param bindMethodName
	 * @return this injector
	 */
	public ServiceInjector bind(final String bindMethodName) {
		Assert.isTrue(state == State.INITIAL, "ServiceInjector already started or stopped!"); //$NON-NLS-1$
		this.bindMethodName = bindMethodName;
		return this;
	}

	/**
	 * Specify the bind method. If not specified
	 * {@link #DEFAULT_BIND_METHOD_NAME} will be used.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if injector has already been started or stopped.
	 * @param bindMethod
	 * @return this injector
	 */
	public ServiceInjector bind(final Method bindMethod) {
		Assert.isTrue(state == State.INITIAL, "ServiceInjector already started or stopped!"); //$NON-NLS-1$
		this.bindMethod = bindMethod;
		return this;
	}

	/**
	 * Defines that the 'un/bind' methods will only be called once for instances
	 * of the same class. This can also be forced by declaring the 'un/bind'
	 * methods static.
	 * <p>
	 * This can be used for instances that share configuration data to avoid
	 * multiple injection of the same data. This reduces the amount of listeners
	 * and memory footprint.
	 * 
	 * @return this
	 */
	public ServiceInjector onceOnly() {
		Assert.isTrue(state == State.INITIAL, "ServiceInjector already started or stopped!"); //$NON-NLS-1$
		onceOnly = true;
		return this;
	}

	/**
	 * Specify the un-bind method. If not specified
	 * {@link #DEFAULT_UNBIND_METHOD_NAME} will be used.
	 * 
	 * @throws some_kind_of_unchecked_exception
	 *             if injector has already been started or stopped.
	 * @param unbindMethodName
	 * @return this injector
	 */
	public synchronized ServiceInjector unbind(final String unbindMethodName) {
		Assert.isTrue(state == State.INITIAL, "ServiceInjector already started or stopped!"); //$NON-NLS-1$
		this.unbindMethodName = unbindMethodName;
		return this;
	}

	/**
	 * Registers the listener for service events.
	 */
	private void registerServiceListener() {
		if (serviceListener != null) {
			return;
		}
		serviceListener = new InjectorServiceListener();
		try {
			context.addServiceListener(serviceListener, filter);
		} catch (final InvalidSyntaxException e) {
			throw new InjectionFailure("The specified filter has syntax errors.", e); //$NON-NLS-1$
		}
	}

	/**
	 * Unregisters the listener for service events.
	 */
	private void unregisterServiceListener() {
		if (serviceListener == null) {
			return;
		}
		context.removeServiceListener(serviceListener);
	}

	private void registerBundleListener() {
		if (bundleListener == null) {
			bundleListener = new InjectorBundleListener();
		}
		context.addBundleListener(bundleListener);
	}

	private void unregisterBundleListener() {
		if (bundleListener != null) {
			context.removeBundleListener(bundleListener);
			bundleListener = null;
		}
	}

	protected void handleEvent(final ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
			doBind(event.getServiceReference());
			break;
		case ServiceEvent.UNREGISTERING:
			doUnbind(event.getServiceReference());
			break;
		default:
			// ignore
			break;
		}
	}

	protected abstract void doBind(final ServiceReference serviceRef);

	protected abstract void doUnbind(final ServiceReference serviceRef);

	/**
	 * Get all service references for the service.
	 * 
	 * @return array of service references
	 */
	protected ServiceReference[] getServiceReferences() {
		try {
			final ServiceReference[] serviceReferences = context.getServiceReferences(
					serviceDesc.getServiceClassName(), filter);
			registerServiceListener();
			return serviceReferences;
		} catch (final InvalidSyntaxException e) {
			throw new InjectionFailure("The specified filter has syntax errors.", e); //$NON-NLS-1$
		}
	}

	/**
	 * Find the matching bind method for the specified service reference.
	 * 
	 * @param service
	 * @return the bind method
	 */
	protected void invokeBindMethod(final ServiceReference serviceRef) {
		if (serviceRef == null) {
			return;
		}
		// increments service use count, now it is ´1´
		final Object service = context.getService(serviceRef);
		if (service == null) {
			return;
		}
		invoke(bindMethod, service);
	}

	/**
	 * Find the matching unbind method for the specified service reference.
	 * 
	 * @param service
	 * @return the unbind method
	 */
	protected void invokeUnbindMethod(final ServiceReference serviceRef) {
		if (serviceRef == null) {
			return;
		}
		// need to get the service object, increments the use count, now it is ´2´
		final Object service = context.getService(serviceRef);
		if (service == null) {
			return;
		}
		invoke(unbindMethod, service);
		// decrement the use count from prior getService(), now it is ´1´
		context.ungetService(serviceRef);
		// decrement the use count from from prior bind, now it is ´0´
		context.ungetService(serviceRef);
	}

	private void invoke(final Method method, final Object service) {
		Assert.isNotNull(method);
		Assert.isNotNull(service);
		final Object target = targetRef.get();
		if (target == null) {
			return;
		}
		Throwable t = null;
		String cause = null;

		try {
			method.invoke(target, service);
		} catch (final SecurityException e) {
			t = e;
			cause = "Security exception on invoking '"; //$NON-NLS-1$ 
		} catch (final IllegalArgumentException e) {
			t = e;
			cause = "Illegal argument exception on invoking '"; //$NON-NLS-1$
		} catch (final IllegalAccessException e) {
			t = e;
			cause = "Illegal access exception on invoking '"; //$NON-NLS-1$
		} catch (final InvocationTargetException e) {
			t = e.getTargetException();
			cause = "Invocation target exception on invoking '"; //$NON-NLS-1$
		}
		if (t != null) {
			final String message = cause + method + "' on '" + targetClass.getName() + "'."; //$NON-NLS-1$ //$NON-NLS-2$
			LOGGER.log(LogService.LOG_ERROR, message, t);
			throw new InjectionFailure(message, t);
		}
	}

	/**
	 * The service listener for this injector.
	 */
	class InjectorServiceListener implements ServiceListener {
		public void serviceChanged(final ServiceEvent event) {
			final int eventType = event.getType();
			if (eventType == ServiceEvent.REGISTERED || eventType == ServiceEvent.UNREGISTERING) {
				handleEvent(event);
			}
		}
	}

	/**
	 * The bundle listener for this injector.
	 */
	class InjectorBundleListener implements SynchronousBundleListener {
		public void bundleChanged(final BundleEvent event) {
			if (event.getBundle() != context.getBundle()) {
				return;
			}
			if (event.getType() == BundleEvent.STOPPING) {
				stop();
			}
		}
	}

	/**
	 * The {@code OnceOnlyTracker} remembers the first {@code ServiceInjector}
	 * associated with a bindMethod.<br>
	 * The first {@code ServiceInjector} is the only {@code ServiceInjector}
	 * that has all the necessary information to properly unbind the currently
	 * bound services. All other {@code ServiceInjector}s are not capable of
	 * unbinding!
	 * <p>
	 * This implementation is thread safe.
	 */
	private static class OnceOnlyTracker {

		private final ObjectCounter<Method> counter = new ObjectCounter<Method>();
		private final Map<Method, ServiceInjector> firstInjectors = new HashMap<Method, ServiceInjector>();

		public synchronized int registerAndGetCount(final ServiceInjector serviceInjector, final Method bindMethod) {
			final int count = counter.incrementAndGetCount(bindMethod);
			if (count == 1) {
				firstInjectors.put(bindMethod, serviceInjector);
			}
			return count;
		}

		public synchronized ServiceInjector unregisterAndGetFirstInjector(final Method bindMethod) {
			if (counter.decrementAndGetCount(bindMethod) == 0) {
				return firstInjectors.remove(bindMethod);
			}
			return null;
		}

	}

}
