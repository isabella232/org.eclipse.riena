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
package org.eclipse.riena.core.injector.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation specifies the default value of an attribute that has not been
 * defined within an extension., e.g.
 * 
 * <pre>
 * &#064;DefaultValue(&quot;true&quot;)
 * boolean isSelectable();
 * </pre>
 * 
 * <b>Note: </b>This should only be used for value types not for specifying
 * classes or sub elements.
 * 
 * @since 1.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DefaultValue {
	String value();
}
