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
package org.eclipse.riena.ui.ridgets.annotation.processor;

import java.lang.annotation.Annotation;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.ui.ridgets.annotation.handler.IRidgetContainerAnnotationHandler;

/**
 * Extension interface for {@link IRidgetContainerAnnotationHandler}s. The
 * handler is called after the ridgets are injected into the container but
 * before the ridgets are bound to the UI controls. A possible use case is the
 * registration of the annotated method as an event handler for some ridget.
 * 
 * @see org.eclipse.riena.ui.ridgets.swt.uibinding.AnnotationAwareBindingManager
 * 
 * @since 3.0
 */
@ExtensionInterface(id = "annotationHandler")
public interface IRidgetContainerAnnotationExtension {

	/**
	 * @return the annotation class
	 */
	Class<? extends Annotation> getAnnotation();

	/**
	 * @return the freshly created handler
	 */
	IRidgetContainerAnnotationHandler createHandler();
}
