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
package org.eclipse.riena.ui.ridgets.annotation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;

/**
 * "Helper" for concrete annotation handlers.
 * 
 * @since 3.0
 */
public abstract class AbstractRidgetContainerAnnotationHandler implements IRidgetContainerAnnotationHandler {

	protected IRidget getRidget(final Annotation annotation, final Method method,
			final IRidgetContainer ridgetContainer, final String ridgetId) {
		final IRidget ridget = ridgetContainer.getRidget(ridgetId);
		if (ridget != null) {
			return ridget;
		}
		throw new IllegalStateException(annotation + " defined unknown ridget id on method '" + method + "'."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected void errorUnsupportedRidgetType(final Annotation annotation, final IRidget ridget) {
		throw new IllegalStateException(annotation + " defined for incompatible ridget type '" //$NON-NLS-1$
				+ ridget.getClass().getName() + "' with id '" + ridget.getID() + "'."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected <L> L createListener(final Class<L> listenerClazz, final String listenerMethodName, final Object target,
			final Method targetMethod) {
		return (L) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[] { listenerClazz },
				new ListenerHandler(target, listenerClazz, listenerMethodName, targetMethod));
	}

	private static class ListenerHandler implements InvocationHandler {

		private final Object target;
		private final Class<?> listenerClazz;
		private final String listenerMethodName;
		private final Method targetMethod;

		public ListenerHandler(final Object target, final Class<?> listenerClazz, final String listenerMethodName,
				final Method targetMethod) {
			this.target = target;
			this.listenerClazz = listenerClazz;
			this.listenerMethodName = listenerMethodName;
			this.targetMethod = targetMethod;
			if (!targetMethod.isAccessible()) {
				targetMethod.setAccessible(true);
			}
		}

		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			final String methodName = method.getName();
			if (method.getDeclaringClass() == Object.class) {
				if (methodName.equals("hashCode")) { //$NON-NLS-1$
					return System.identityHashCode(proxy);
				} else if (methodName.equals("equals")) { //$NON-NLS-1$
					return proxy == args[0];
				} else if (methodName.equals("toString")) { //$NON-NLS-1$
					return "Proxy for " + listenerClazz.getName() + " " + proxy.getClass().getName() + '@' //$NON-NLS-1$ //$NON-NLS-2$
							+ Integer.toHexString(proxy.hashCode());
				}
			}

			if (methodName.equals(listenerMethodName)) {
				try {
					if (targetMethod.getParameterTypes().length == 0) {
						return targetMethod.invoke(target);
					} else {
						return targetMethod.invoke(target, args);
					}
				} catch (final InvocationTargetException e) {
					throw e.getTargetException();
				}
			}

			return null;
		}
	}
}
