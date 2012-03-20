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
package org.eclipse.riena.internal.communication.factory.hessian.serializer;

import org.eclipse.riena.communication.factory.hessian.serializer.AbstractRienaSerializerFactory;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Extension interface for defining Riena specific serializer factories.
 */
@ExtensionInterface(id = "serializerFactories")
public interface ISerializerFactoryExtension {

	/**
	 * The unique name of the serializer factory.
	 * 
	 * @return the unique name
	 */
	String getName();

	/**
	 * The comma-separated list of serializer factory names that shall be
	 * executed before this serializer factory.
	 * 
	 * @return the pre serializer factories list
	 */
	String getPreSerializerFactories();

	/**
	 * The comma-separated list of serializer factory names that shall be
	 * executed after this hook.
	 * 
	 * @return the post hooks list
	 */
	String getPostSerializerFactories();

	/**
	 * Create a new instance of a {@code AbstractRienaSerializerFactory}.
	 * 
	 * @return a {@code AbstractRienaSerializerFactory}
	 */
	@MapName("class")
	AbstractRienaSerializerFactory newSerializerFactory();

}
