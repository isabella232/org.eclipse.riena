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
package org.eclipse.riena.internal.core.ignore;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the warning issued by FindBugs http://findbugs.sourceforge.net
 * can be ´ignored´. The effect of this annotation is only for documentation
 * purposes. The annotation support of findbugs is currently not usable because
 * of IP stuff that is now to late. However, the idea is that we can replace
 * this annotation with the findbugs annotation when the IP stuff has been done.
 * <p>
 * It usage is as follow:
 * 
 * <pre>
 * &#064;IgnoreFindBugs(value = &quot;SE_COMPARATOR_SHOULD_BE_SERIALIZABLE&quot;, justification=&quot;only used locally&quot;)
 * </pre>
 * 
 * Multiple ignores look like this:
 * 
 * <pre>
 * &#064;IgnoreFindBugs(value = { &quot;SE_COMPARATOR_SHOULD_BE_SERIALIZABLE&quot;, &quot;IS2_INCONSISTENT_SYN&quot; }, justification = &quot;only used locally&quot;)
 * </pre>
 * 
 * The values of the annotation are the FindBugs bug detector names.
 */
@Target({ TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE })
@Retention(RetentionPolicy.SOURCE)
public @interface IgnoreFindBugs {

	/**
	 * The set of findbugs warnings and their justification
	 */
	String[] value();

	String justification();
}
