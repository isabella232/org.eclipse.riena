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
package org.eclipse.riena.internal.core.wire;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.injector.service.ServiceDescriptor;
import org.eclipse.riena.core.injector.service.ServiceInjector;
import org.eclipse.riena.core.wire.InjectService;

/**
 * Build a {@code ServiceInjector} for the given bean and method with the
 * {@code InjectService} annotation.
 */
public class ServiceInjectorBuilder {
	private final Object bean;
	private final Method method;
	private final InjectService annotation;

	/**
	 * Create service injector builder.
	 * 
	 * @param bean
	 * @param method
	 */
	public ServiceInjectorBuilder(final Object bean, final Method method) {
		Assert.isLegal(bean != null, "bean must not be null"); //$NON-NLS-1$
		Assert.isLegal(method != null, "method must not be null"); //$NON-NLS-1$
		this.bean = bean;
		this.method = method;
		this.annotation = method.getAnnotation(InjectService.class);
		Assert.isLegal(annotation != null, "annotation must not be null"); //$NON-NLS-1$
	}

	/**
	 * Build the {@code ServiceInjector}
	 * 
	 * @return
	 */
	public ServiceInjector build() {
		ServiceDescriptor descriptor;
		if (annotation.serviceName().length() != 0) {
			descriptor = Inject.service(annotation.serviceName());
		} else if (annotation.service() != Void.class) {
			descriptor = Inject.service(annotation.service());
		} else {
			final Class<?>[] types = method.getParameterTypes();
			Assert.isLegal(types.length == 1, "only one parameter allowed on ´bind´ method: " + method); //$NON-NLS-1$
			descriptor = Inject.service(types[0]);
		}
		if (annotation.useRanking()) {
			descriptor = descriptor.useRanking();
		} else if (annotation.useFilter().length() != 0) {
			descriptor = descriptor.useFilter(annotation.useFilter());
		}
		final ServiceInjector injector = descriptor.into(bean).bind(method);
		final String unbind = annotation.unbind().length() != 0 ? annotation.unbind() : "un" + method.getName(); //$NON-NLS-1$
		injector.unbind(unbind);
		if (annotation.onceOnly()) {
			injector.onceOnly();
		}
		return injector;
	}
}
