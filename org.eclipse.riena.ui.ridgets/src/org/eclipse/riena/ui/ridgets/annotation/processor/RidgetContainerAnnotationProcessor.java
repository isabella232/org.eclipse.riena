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
package org.eclipse.riena.ui.ridgets.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.core.singleton.SingletonProvider;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.annotation.handler.IRidgetContainerAnnotationHandler;

/**
 * Annotation processor for {@code IRidgetContainer} annotations.
 * 
 * @since 3.0
 */
public final class RidgetContainerAnnotationProcessor {

	private final Map<Class<? extends Annotation>, IRidgetContainerAnnotationHandler> handlerMap = new HashMap<Class<? extends Annotation>, IRidgetContainerAnnotationHandler>();

	private static final SingletonProvider<RidgetContainerAnnotationProcessor> RCAP = new SingletonProvider<RidgetContainerAnnotationProcessor>(
			RidgetContainerAnnotationProcessor.class);

	/**
	 * Answer the singleton <code>RidgetContainerAnnotationProcessor</code>
	 * 
	 * @return the RidgetContainerAnnotationProcessor singleton
	 */
	public static RidgetContainerAnnotationProcessor getInstance() {
		return RCAP.getInstance();
	}

	private RidgetContainerAnnotationProcessor() {
		// singleton
	}

	@InjectExtension()
	public void update(final IRidgetContainerAnnotationExtension[] extensions) {
		handlerMap.clear();
		for (final IRidgetContainerAnnotationExtension extension : extensions) {
			handlerMap.put(extension.getAnnotation(), extension.createHandler());
		}
	}

	/**
	 * Process the annotations for the given {@code IRidgetContainer}.
	 * 
	 * @param ridgetContainer
	 */
	public void processAnnotations(final IRidgetContainer ridgetContainer) {
		processAnnotations(ridgetContainer, ridgetContainer);
	}

	/**
	 * Process the annotations.
	 * 
	 * @param ridgetContainer
	 *            the ridget container whose ridgets fire the events
	 * @param target
	 *            the object whose annotation method should be handled
	 */
	public void processAnnotations(final IRidgetContainer ridgetContainer, final Object target) {
		processAnnotations(ridgetContainer, target, target.getClass(), new AnnotatedOverriddenMethodsGuard());
	}

	private void processAnnotations(final IRidgetContainer ridgetContainer, final Object target,
			final Class<?> targetClass, final AnnotatedOverriddenMethodsGuard guard) {
		if (targetClass == Object.class) {
			return;
		}
		processAnnotations(ridgetContainer, target, targetClass.getSuperclass(), guard);

		for (final Method targetMethod : targetClass.getDeclaredMethods()) {
			for (final Annotation annotation : targetMethod.getAnnotations()) {
				handle(annotation, ridgetContainer, target, targetMethod, guard);
			}
		}
	}

	/**
	 * Execute the handler for the given annotation,..
	 * 
	 * @param annotation
	 * @param ridgetContainer
	 * @param target
	 * @param targetMethod
	 * 
	 * @since 4.0
	 */
	public void handle(final Annotation annotation, final IRidgetContainer ridgetContainer, final Object target,
			final Method targetMethod, final AnnotatedOverriddenMethodsGuard guard) {
		final IRidgetContainerAnnotationHandler handler = handlerMap.get(annotation.annotationType());
		if (handler != null && guard.add(annotation, targetMethod)) {
			handler.handleAnnotation(annotation, ridgetContainer, target, targetMethod, guard);
		}
	}

}