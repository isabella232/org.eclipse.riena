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
package org.eclipse.riena.communication.factory.hessian.serializer;

import com.caucho.hessian.io.AbstractSerializerFactory;

/**
 * A {@code AbstractRienaSerializerFactory} may provide a replacement for an
 * already defined {@code Serializer} and/or {@code Deserializer} within
 * Hessian.<br>
 * Because it may replace an existing type or types it must declare those
 * type(s). This has to be done with the {@code getReplaced..Types} methods.
 * 
 * @since 4.0
 */
public abstract class AbstractRienaSerializerFactory extends AbstractSerializerFactory {

	private final static Class<?>[] EMPTY_ARRAY = new Class<?>[0];

	/**
	 * Return the replaced {@code Serializer} types.
	 * <p>
	 * <b>Note: </b>It is only necessary to specify the type(s) if the type(s)
	 * are within the static maps of the hessian {@code SerializerFactory}!
	 * 
	 * @return the replaced {@code Serializer} types - never {@code null}
	 */
	public Class<?>[] getReplacedSerializerTypes() {
		return EMPTY_ARRAY;
	}

	/**
	 * Return the replaced {@code Deserializer} types.
	 * <p>
	 * <b>Note: </b>It is only necessary to specify the type(s) if the type(s)
	 * are within the static maps of the hessian {@code SerializerFactory}!
	 * 
	 * @return the replaced {@code Deserializer} types - never {@code null}
	 */
	public Class<?>[] getReplacedDeserializerTypes() {
		return EMPTY_ARRAY;
	}
}
