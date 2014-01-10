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
package org.eclipse.riena.core.annotationprocessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.singleton.SingletonProvider;
import org.eclipse.riena.core.util.WeakRef;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.internal.core.annotationprocessor.IAnnotatedMethodHandlerExtension;

/**
 * Annotation processor for {@code Object} annotations.
 * 
 * @since 4.0
 */
public final class AnnotationProcessor {

	private IAnnotatedMethodHandlerExtension[] extensions;
	private Map<Class<? extends Annotation>, IAnnotatedMethodHandler> handlerMap;
	private final Map<Object, ?> alreadyProcessed = new WeakHashMap<Object, Object>();

	private final static IDisposer EMPTY_DISPOSER = new IDisposer() {
		public void dispose() {
		}
	};
	private static final SingletonProvider<AnnotationProcessor> AP = new SingletonProvider<AnnotationProcessor>(
			AnnotationProcessor.class);
	private static final Logger LOGGER = Log4r.getLogger(AnnotationProcessor.class);

	/**
	 * Answer the singleton <code>AnnotationProcessor</code>
	 * 
	 * @return the {@code AnnotationProcessor} singleton
	 */
	public static AnnotationProcessor getInstance() {
		return AP.getInstance();
	}

	private AnnotationProcessor() {
		// singleton
	}

	@InjectExtension()
	public synchronized void update(final IAnnotatedMethodHandlerExtension[] extensions) {
		this.extensions = extensions;
		handlerMap = null;
	}

	/**
	 * Process the annotations on methods for the given {@link Object}.
	 * 
	 * @param object
	 *            the {@link Object} to process
	 * @return a {@code IDisposer} that allows to dispose everything the
	 *         annotation handlers have <i>allocated</i>.
	 */
	public IDisposer processMethods(final Object object) {
		return processMethods(object, Collections.emptyMap());
	}

	/**
	 * Process the annotations on methods for the given {@link Object} with
	 * optional arguments.<br>
	 * Those arguments can be easily specified with the {@code Literal} class,
	 * e.g. {@code Literal.map("node", node)}.
	 * 
	 * @param object
	 *            the {@link Object} to process
	 * @param optionalArgs
	 *            optional argument objects that might be required by the
	 *            handler
	 * @return a {@code IDisposer} that allows to dispose everything the
	 *         annotation handlers have <i>allocated</i>.
	 */
	public IDisposer processMethods(final Object object, final Map<?, ?> optionalArgs) {
		synchronized (alreadyProcessed) {
			if (alreadyProcessed.containsKey(object)) {
				return EMPTY_DISPOSER;
			}
			alreadyProcessed.put(object, null);
		}

		final DisposerList disposers = new DisposerList();
		processMethods(object, object.getClass(), optionalArgs, new AnnotatedOverriddenMethodsGuard(), disposers);
		prepareDispose(object, disposers);
		return disposers;
	}

	private void processMethods(final Object object, final Class<?> objectClass, final Map<?, ?> optionalArgs,
			final AnnotatedOverriddenMethodsGuard guard, final DisposerList disposers) {
		if (objectClass == Object.class) {
			return;
		}
		processMethods(object, objectClass.getSuperclass(), optionalArgs, guard, disposers);

		for (final Method method : objectClass.getDeclaredMethods()) {
			for (final Annotation annotation : method.getAnnotations()) {
				handle(annotation, object, method, optionalArgs, guard, disposers);
			}
		}
	}

	/**
	 * Execute the handler for the given annotation,..
	 * <p>
	 * <b>Note: </b>This method should only be called by annotation handlers
	 * dealing with nested annotations.
	 * 
	 * @param annotation
	 * @param ridgetContainer
	 * @param object
	 * @param method
	 * @param optionalArgs
	 * @param disposers
	 */
	public void handle(final Annotation annotation, final Object object, final Method method,
			final Map<?, ?> optionalArgs, final AnnotatedOverriddenMethodsGuard guard, final DisposerList disposers) {
		final IAnnotatedMethodHandler handler = getHandler(annotation.annotationType());
		if (handler != null && guard.add(annotation, method)) {
			handler.handleAnnotation(annotation, object, method, optionalArgs, guard, disposers);
		}
	}

	private synchronized IAnnotatedMethodHandler getHandler(final Class<? extends Annotation> annotationType) {
		if (handlerMap == null) {
			handlerMap = new HashMap<Class<? extends Annotation>, IAnnotatedMethodHandler>();
			for (final IAnnotatedMethodHandlerExtension extension : extensions) {
				final IAnnotatedMethodHandler next = extension.createHandler();
				final IAnnotatedMethodHandler previous = handlerMap.put(extension.getAnnotation(), next);
				if (previous != null) {
					throw new AnnotationProcessorFailure("There are at least two handlers (" + previous.getClass() //$NON-NLS-1$
							+ ", " + next.getClass() + ") for the same annotation " //$NON-NLS-1$//$NON-NLS-2$
							+ extension.getAnnotation().getName());
				}
			}
		}
		return handlerMap.get(annotationType);
	}

	/**
	 * Bind a {@code DisposerList} via a {@code WeakRef} to an object. The
	 * {@code DisposerList} will be disposed if the specified object gets
	 * garbage collected.
	 * 
	 * @param object
	 * @param disposers
	 */
	private void prepareDispose(final Object object, final DisposerList disposers) {
		if (object == null || disposers == null || disposers.isEmpty()) {
			return;
		}
		new WeakRef<Object>(object, new Runnable() {
			public void run() {
				try {
					disposers.dispose();
				} catch (final Throwable t) {
					LOGGER.log(LogService.LOG_ERROR, "Exception occured while executing a disposer.", t); //$NON-NLS-1$
				}
			}
		});
	}
}
