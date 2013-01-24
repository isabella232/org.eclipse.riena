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
package org.eclipse.riena.internal.core.ignore;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the warning issued by CheckStyle can be ´ignored´. The effect
 * of this annotation is only for documentation purposes.
 * <p>
 * It usage is as follow:
 * 
 * <pre>
 * &#064;IgnoreCheckStyle(&quot;oh, come on!&quot;)
 * </pre>
 * 
 * Multiple ignores look like this:
 * 
 * <pre>
 * &#064;IgnoreFindBugs(&quot;oh, come on!&quot;,
 *                 &quot;sigh&quot;)
 * </pre>
 * 
 * The first part (before the comma) of the annotation value is the FindBugs bug
 * detector name.
 */
@Target({ TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE })
@Retention(RetentionPolicy.SOURCE)
public @interface IgnoreCheckStyle {

	/**
	 * The set of check style warnings and their justification
	 */
	String[] value();

}
