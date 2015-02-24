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


/**
 * The {@code AnnotatedOverriddenMethodsGuard} helps annotation processors to
 * figure out whether a ('overridden') method with the same properties has
 * already been handled. And thus avoids registering multiple listeners for a
 * single overridden method.
 * 
 * @since 4.0
 */
@Deprecated
public class AnnotatedOverriddenMethodsGuard extends org.eclipse.riena.core.annotationprocessor.AnnotatedOverriddenMethodsGuard {

	/**
	 * Wrapper around {@link org.eclipse.riena.core.annotationprocessor.AnnotatedOverriddenMethodsGuard}
	 * 
	 * @since 6.1
	 */
	public AnnotatedOverriddenMethodsGuard(final org.eclipse.riena.core.annotationprocessor.AnnotatedOverriddenMethodsGuard guard) {
		annotatedMethods = getAnnotatedMethods();
	}
}