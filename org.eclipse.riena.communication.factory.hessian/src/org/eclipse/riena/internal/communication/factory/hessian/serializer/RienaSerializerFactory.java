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
package org.eclipse.riena.internal.communication.factory.hessian.serializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

import org.eclipse.riena.communication.factory.hessian.serializer.AbstractRienaSerializerFactory;
import org.eclipse.riena.core.util.Orderer;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.communication.factory.hessian.Activator;

/**
 * The {@code RienaSerializerFactory} is a delegating
 * {@code AbstractSerializerFactory}. It´s main purpose is to act like a regular
 * {@code AbstractSerializerFactory} but internally (not visible to hessian) it
 * manages a configurable, ordered list of
 * {@code AbstractRienaSerializerFactory}.
 * 
 * @since 4.0
 */
public class RienaSerializerFactory extends AbstractSerializerFactory {

	private List<AbstractRienaSerializerFactory> serializerFactories = Collections.emptyList();

	public RienaSerializerFactory() {
		Wire.instance(this).andStart(Activator.getDefault().getContext());
	}

	@Override
	public Deserializer getDeserializer(final Class cl) throws HessianProtocolException {
		synchronized (this) {
			for (final AbstractSerializerFactory serializerFactory : serializerFactories) {
				final Deserializer deserializer = serializerFactory.getDeserializer(cl);
				if (deserializer != null) {
					return deserializer;
				}
			}
		}
		return null;
	}

	@Override
	public Serializer getSerializer(final Class cl) throws HessianProtocolException {
		synchronized (this) {
			for (final AbstractSerializerFactory serializerFactory : serializerFactories) {
				final Serializer serializer = serializerFactory.getSerializer(cl);
				if (serializer != null) {
					return serializer;
				}
			}
		}
		return null;
	}

	@InjectExtension
	public void update(final ISerializerFactoryExtension[] serializerFactoryDefinitions) {
		final Orderer<AbstractRienaSerializerFactory> orderer = new Orderer<AbstractRienaSerializerFactory>();
		for (final ISerializerFactoryExtension serializerFactoryExtension : serializerFactoryDefinitions) {
			orderer.add(serializerFactoryExtension.newSerializerFactory(), serializerFactoryExtension.getName(),
					serializerFactoryExtension.getPreSerializerFactories(),
					serializerFactoryExtension.getPostSerializerFactories());
		}
		// order ..
		final List<AbstractRienaSerializerFactory> tempOrdered = orderer.getOrderedObjects();
		// .. get those static maps from the hessian {@code SerializerFactory} ..
		final HashMap<Class<?>, ?> staticDeserializerMap = ReflectionUtils.getHidden(SerializerFactory.class,
				"_staticDeserializerMap"); //$NON-NLS-1$
		final HashMap<Class<?>, ?> staticSerializerMap = ReflectionUtils.getHidden(SerializerFactory.class,
				"_staticSerializerMap"); //$NON-NLS-1$

		synchronized (this) {
			// .. tweak the static hessian maps
			for (final AbstractRienaSerializerFactory serializerFactory : tempOrdered) {
				for (final Class<?> replaced : serializerFactory.getReplacedDeserializerTypes()) {
					staticDeserializerMap.remove(replaced);
				}
				for (final Class<?> replaced : serializerFactory.getReplacedSerializerTypes()) {
					staticSerializerMap.remove(replaced);
				}
			}
			// .. and make active
			serializerFactories = tempOrdered;
		}
	}

}
