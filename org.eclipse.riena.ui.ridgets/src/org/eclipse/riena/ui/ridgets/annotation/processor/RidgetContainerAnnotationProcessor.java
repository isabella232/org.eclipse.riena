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
package org.eclipse.riena.ui.ridgets.annotation.processor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.core.annotationprocessor.AnnotationProcessor;
import org.eclipse.riena.core.annotationprocessor.IDisposer;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;

/**
 * Annotation processor for {@code IRidgetContainer} annotations.
 * 
 * @since 3.0
 * @deprecated Use the {@link AnnotationProcessor}.
 */
@Deprecated
public final class RidgetContainerAnnotationProcessor {

	/**
	 * @since 6.1
	 */
	public final static String RIDGET_CONTAINER_KEY = "ridgetContainer"; //$NON-NLS-1$

	private RidgetContainerAnnotationProcessor() {
		Nop.reason("utility"); //$NON-NLS-1$
	}

	/**
	 * Process the annotations for the given {@code IRidgetContainer}.
	 * 
	 * @param ridgetContainer
	 */
	public static void processAnnotations(final IRidgetContainer ridgetContainer) {
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
	public static void processAnnotations(final IRidgetContainer ridgetContainer, final Object target) {
		final Map<String, Object> args = new HashMap<String, Object>(2);
		args.put(RIDGET_CONTAINER_KEY, ridgetContainer);
		AnnotationProcessor.getInstance().processMethods(target, args);
	}

	/**
	 * Process the annotations and return the IDisposer.
	 * 
	 * @param controller
	 *            the ridget container whose ridgets fire the events
	 * @return The {@link IDisposer} list
	 * @since 6.1
	 */
	public static IDisposer processMethods(final IRidgetContainer ridgetContainer) {
		final Map<String, Object> args = new HashMap<String, Object>(2);
		args.put(RIDGET_CONTAINER_KEY, ridgetContainer);
		return AnnotationProcessor.getInstance().processMethods(ridgetContainer, args);
	}

}