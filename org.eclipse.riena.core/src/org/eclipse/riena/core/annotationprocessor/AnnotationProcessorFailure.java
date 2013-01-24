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
package org.eclipse.riena.core.annotationprocessor;

import org.eclipse.riena.core.exception.Failure;

/**
 * Indicates an error while processing annotations.
 * 
 * @since 4.0
 */
public class AnnotationProcessorFailure extends Failure {

	private static final long serialVersionUID = -8532270042366191739L;

	/**
	 * @param msg
	 */
	public AnnotationProcessorFailure(final String msg) {
		super(msg);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public AnnotationProcessorFailure(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
