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
import java.util.Map;

/**
 * Has to be implemented by every annotation handler:
 * 
 * @since 4.0
 */
public interface IAnnotatedMethodHandler {

	/**
	 * Handle the annotation that this handler has bee registered for.
	 * 
	 * @param annotation
	 *            the annotation this handler has been registered for
	 * @param object
	 *            the object the annotation processor has been called on
	 * @param method
	 *            the method that has been annotated
	 * @param optionalArgs
	 *            any optional parameters that have been given to the annotation
	 *            processors
	 * @param guard
	 *            a guard the usually should be ignored by implementations. It
	 *            is only needed in case of <i>nested</i> annotations.
	 * @param disposers
	 *            a list of {@code IDisposer} objects. With this a handler can
	 *            register any clean-up code, e.g. un-subscribe listeners.
	 */
	void handleAnnotation(Annotation annotation, Object object, Method method, Map<?, ?> optionalArgs,
			AnnotatedOverriddenMethodsGuard guard, DisposerList disposers);

}
