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
package org.eclipse.riena.core.wire;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a ´update´ method for extension injection.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InjectExtension {

	/**
	 * The extension point id.
	 * <p>
	 * <b>Note: </b>If not given, it is expected that the
	 * {@code ExtensionInterface} has either set the {@code id()} parameter or
	 * that the extension interface has a {@code String} field named {@code ID}
	 * that contains the extension point id.
	 */
	String id() default "";

	/**
	 * The minimum expected numbers of extensions.
	 */
	int min() default 0;

	/**
	 * The maximum expected numbers of extensions.
	 */
	int max() default Integer.MAX_VALUE;

	/**
	 * Is this a ´heterogeneous´ extension
	 */
	boolean heterogeneous() default false;

	/**
	 * Is this a ´specific´ extension
	 */
	boolean specific() default false;

	/**
	 * If {@code true} symbols (VariableManager) will not be replaced
	 */
	boolean doNotReplaceSymbols() default false;

	/**
	 * Defines that the 'update' method will only be called once for instances
	 * of the same class. This can also be forced by declaring the 'update'
	 * method static.<br>
	 * <b>Note: </b>If the 'update' method is not static but annotated with
	 * 'oneOnly' you have to take care that the 'update' method keeps the
	 * configuration data in shared space (e.g. at the class level).
	 * <p>
	 * This can be used for instances that share configuration data to avoid
	 * multiple injection of the same data. This reduces the amount of listeners
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
