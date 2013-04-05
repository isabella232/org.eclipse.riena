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
package org.eclipse.riena.core.wire;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a ´bind´ method for service injection.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InjectService {

	/**
	 * The service to inject. <br>
	 * If neither service() nor servicName() have been specified the parameter
	 * type of the bind method will be used as service type.
	 */
	Class<?> service() default Void.class;

	/**
	 * The service as string to inject. <br>
	 * If neither service() nor servicName() have been specified the parameter
	 * type of the bind method will be used as service type.
	 */
	String serviceName() default "";

	/**
	 * The name of the un-bind method. <br>
	 * If not specified the un-bind method will be determined by prefixing the
	 * bind method with "un".
	 */
	String unbind() default "";

	/**
	 * A filter for selecting the service/s. Default is NO filter.
	 */
	String useFilter() default "";

	/**
	 * Should ranking be used. Default is {@code false}.
	 */
	boolean useRanking() default false;

	/**
	 * Defines that the 'un/bind' methods will only be called once for instances
	 * of the same class. This can also be forced by declaring both 'bind' and
	 * 'unbind' methods static.<br>
	 * <b>Note: </b>If the 'un/bind' method is not static but annotated with
	 * 'oneOnly' you have to take care that the 'un/bind' method keeps the
	 * service in shared space (e.g. at the class level).
	 * <p>
	 * This can be used for instances that share services to avoid multiple
	 * injection of the same services. This reduces the amount of listeners
	 * (better performance and less memory.
	 */
	boolean onceOnly() default false;

	/**
	 * Defines the order of injections.
	 * <p>
	 * Without a defined order injections (service and extension) will be
	 * injected in an undefined order for each class. This is because of the
	 * 'contract' in {@code Class.getDeclaredMethods()}: "The elements in the
	 * array returned are not sorted and are not in any particular order".<br>
	 * With the {@code order} property it is possible define the sequence of
	 * injections within a class.
	 * <p>
	 * If not defined the default value is {@code 0}.
	 * <p>
	 * <b>Note: </b> An order value of {@code Integer.MAX_VALUE} <b>must</b> be
	 * avoided.
	 * 
	 * @since 3.0
	 */
	int order() default 0;
}
