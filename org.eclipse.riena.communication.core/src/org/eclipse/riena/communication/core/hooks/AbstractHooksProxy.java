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
package org.eclipse.riena.communication.core.hooks;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.security.auth.Subject;

public abstract class AbstractHooksProxy implements InvocationHandler {

	private final Object proxiedInstance;
	private final AtomicReference<Map<MethodKey, Method>> methodTableRef;

	public AbstractHooksProxy(final Object proxiedInstance) {
		this.proxiedInstance = proxiedInstance;
		this.methodTableRef = new AtomicReference<Map<MethodKey, Method>>();
	}

	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		final Subject subject = getSubject();
		if (subject == null) {
			return invoke(method, args);
		} else {
			try {
				return Subject.doAsPrivileged(subject, new PrivilegedExceptionAction<Object>() {
					public Object run() throws Exception {
						return invoke(method, args);
					}
				}, null);
			} catch (final PrivilegedActionException pae) {
				Throwable cause = pae.getCause();
				if (cause instanceof InvocationTargetException) {
					cause = ((InvocationTargetException) cause).getTargetException();
				}
				throw cause;
			}
		}
	}

	private Object invoke(final Method method, final Object[] args) throws NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		Method proxyMethod;
		final Map<MethodKey, Method> methodTable = methodTableRef.get();
		if (methodTable == null) {
			proxyMethod = method;
		} else {
			proxyMethod = methodTable.get(new MethodKey(method));
			if (proxyMethod == null) {
				throw new NoSuchMethodException(proxiedInstance + " " + method.getName() + "," + Arrays.toString(args)); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		try {
			return proxyMethod.invoke(proxiedInstance, args);
		} catch (final IllegalArgumentException e) {
			if (methodTable != null) {
				throw new NoSuchMethodException(proxiedInstance + " " + method.getName() + "," + Arrays.toString(args)); //$NON-NLS-1$ //$NON-NLS-2$
			}
			final Method[] methods = proxiedInstance.getClass().getMethods();
			final Map<MethodKey, Method> tempMethodTable = new HashMap<MethodKey, Method>(methods.length);
			for (final Method keyMethod : methods) {
				tempMethodTable.put(new MethodKey(keyMethod), keyMethod);
			}
			methodTableRef.compareAndSet(null, tempMethodTable);
			return invoke(method, args);
		}
	}

	protected Object getProxiedInstance() {
		return proxiedInstance;
	}

	public abstract Subject getSubject();

	/**
	 * The {@code MethodKey} is an object that can be used as a key for
	 * {@code Method}s in {@code Map}s that does not consider the methods
	 * declaring class.
	 */
	private static final class MethodKey {

		private final Method method;
		private final Class<?>[] params;

		public MethodKey(final Method method) {
			this.method = method;
			this.params = method.getParameterTypes();
		}

		/**
		 * Compares this <code>MethodKey</code> against the specified object.
		 * Returns true if the objects are the same. Two <code>MethodKeys</code>
		 * are the same if there containing method have the same name and formal
		 * parameter types and return type.
		 */
		@Override
		public boolean equals(final Object obj) {
			if (obj == null || !(obj instanceof MethodKey)) {
				return false;
			}
			final MethodKey other = (MethodKey) obj;
			if (!method.getName().equals(other.method.getName())) {
				return false;
			}
			if (!method.getReturnType().equals(other.method.getReturnType())) {
				return false;
			}
			if (params.length != other.params.length) {
				return false;
			}
			for (int i = 0; i < params.length; i++) {
				if (params[i] != other.params[i]) {
					return false;
				}
			}
			return true;
		}

		/**
		 * Returns a hash-code for this <code>Method</code>. The hash-code is
		 * computed as the hash-code of the method's name and the number of
		 * arguments.
		 */
		@Override
		public int hashCode() {
			return method.getName().hashCode() + 31 * params.length;
		}

	}

}
