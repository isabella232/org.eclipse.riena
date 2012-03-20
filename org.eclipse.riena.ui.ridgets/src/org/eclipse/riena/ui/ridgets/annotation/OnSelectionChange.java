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
package org.eclipse.riena.ui.ridgets.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.riena.ui.ridgets.ISelectionObservable;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;

/**
 * This annotation is used to mark a method as target for an automatically
 * generated listener
 * 
 * <pre>
 * ridget.addSelectionListener( {@link ISelectionListener} )
 * </pre>
 * 
 * for the ridget with the given ridget id.<br>
 * The annotated method may either have the same parameter as the corresponding
 * listener method or none.
 * <p>
 * All {@link ISelectionObservable}s are supported.
 * 
 * @since 3.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnSelectionChange {

	/**
	 * The valid ridget id.
	 * 
	 * @return ridgetId
	 */
	String ridgetId();
}
