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

import java.beans.PropertyChangeListener;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * This annotation is used to mark a method as target for an automatically
 * generated listener
 * 
 * <pre>
 * ridget.addPropertyChangeListener( {@link PropertyChangeListener} )
 * </pre>
 * 
 * for the ridget with the given ridget id.<br>
 * The annotated method may either have the same parameter as the corresponding
 * listener method or none.
 * <p>
 * All {@link IRidget}s are supported.
 * 
 * @since 3.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnPropertyChange {

	/**
	 * The valid ridget id. If a property name is not specified a property
	 * change listener is registered for all properties of the ridget with that
	 * id.
	 * 
	 * @return ridgetId
	 */
	String ridgetId();

	/**
	 * The property name for that of the property of the ridget to be observed.
	 * If not given all properties will be observed.
	 * 
	 * @return propertyName
	 */
	String propertyName() default "";
}
