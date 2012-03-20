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
package org.eclipse.riena.ui.ridgets.annotation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.annotation.processor.AnnotatedOverriddenMethodsGuard;

/**
 * Handler for method annotations in {@link IRidgetContainer}s. The handler is
 * called after the ridgets are injected into the container but before the
 * ridgets are bound to the UI controls. A possible use case is the registration
 * of the annotated method as an event handler for some ridget.
 * 
 * @see org.eclipse.riena.ui.ridgets.annotation.processor.RidgetContainerAnnotationProcessor
 * @see org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate
 * 
 * @since 3.0
 */
public interface IRidgetContainerAnnotationHandler {

	/**
	 * Handle the annotation.
	 * 
	 * @param annotation
	 *            the annotation to be handled
	 * @param ridgetContainer
	 *            the ridget container whose ridgets fire the events
	 * @param traget
	 *            the object whose annotation method should be handled by the
	 *            receiver
	 * @param targetMethod
	 *            the annotated method
	 * @param guard
	 *            this is used to guard for creating multiple listeners for
	 *            overridden methods. This usually can be ignored, it is only
	 *            required for nested annotations.
	 * @since 4.0
	 */
	void handleAnnotation(Annotation annotation, IRidgetContainer ridgetContainer, Object target, Method targetMethod,
			AnnotatedOverriddenMethodsGuard guard);
}
