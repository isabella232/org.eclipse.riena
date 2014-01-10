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
package org.eclipse.riena.internal.ui.ridgets.annotation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ITraverseRidget;
import org.eclipse.riena.ui.ridgets.annotation.OnActionCallback;
import org.eclipse.riena.ui.ridgets.annotation.handler.AbstractRidgetContainerAnnotationHandler;
import org.eclipse.riena.ui.ridgets.annotation.processor.AnnotatedOverriddenMethodsGuard;

/**
 * Annotation handler for {@code @OnActionCallback}
 * 
 * @since 3.0
 */
public class ActionCallbackAnnotationHandler extends AbstractRidgetContainerAnnotationHandler {

	public void handleAnnotation(final Annotation annotation, final IRidgetContainer ridgetContainer,
			final Object target, final Method targetMethod, final AnnotatedOverriddenMethodsGuard guard) {

		if (annotation instanceof OnActionCallback) {
			final IRidget ridget = getRidget(annotation, targetMethod, ridgetContainer,
					((OnActionCallback) annotation).ridgetId());
			final IActionListener actionListener = createListener(IActionListener.class, "callback", target, //$NON-NLS-1$
					targetMethod);
			if (ridget instanceof IActionRidget) {
				((IActionRidget) ridget).addListener(actionListener);
			} else if (ridget instanceof ITraverseRidget) {
				((ITraverseRidget) ridget).addListener(actionListener);
			} else {
				errorUnsupportedRidgetType(annotation, ridget);
			}

		}
	}

}
