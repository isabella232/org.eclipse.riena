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

import java.util.UUID;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.Serializer;

import org.eclipse.riena.communication.factory.hessian.serializer.AbstractRienaSerializerFactory;

/**
 * A hessian de/serializer for the {@code UUID} class.
 */
public class UUIDSerializerFactory extends AbstractRienaSerializerFactory {

	@Override
	public Deserializer getDeserializer(final Class cl) throws HessianProtocolException {
		if (cl != UUID.class) {
			return null;
		}
		return new JavaDeserializer(cl) {
			@Override
			protected Object instantiate() throws Exception {
				return new UUID(0, 0);
			}
		};
	}

	@Override
	public Serializer getSerializer(final Class cl) throws HessianProtocolException {
		return null;
	}

}
