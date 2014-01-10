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

import java.io.IOException;
import java.math.BigInteger;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.JavaSerializer;
import com.caucho.hessian.io.Serializer;

import org.eclipse.riena.communication.factory.hessian.serializer.AbstractRienaSerializerFactory;

/**
 * Deserializer for the {@code BigInteger}.
 */
public class BigIntegerSerializerFactory extends AbstractRienaSerializerFactory {

	@Override
	public Deserializer getDeserializer(final Class cl) throws HessianProtocolException {
		if (cl == BigInteger.class) {
			return new JavaDeserializer(cl) {

				@Override
				public Class getType() {
					return BigInteger.class;
				}

				@Override
				public Object readObject(final AbstractHessianInput in, final String[] fieldNames) throws IOException {
					final BigInteger result = new BigInteger(in.readString());
					in.addRef(result);
					return result;
				}

			};
		}
		return null;
	}

	@Override
	public Serializer getSerializer(final Class cl) throws HessianProtocolException {
		if (cl == BigInteger.class) {
			return new JavaSerializer(cl) {

				@Override
				public void writeInstance(final Object obj, final AbstractHessianOutput out) throws IOException {
					final BigInteger bi = (BigInteger) obj;
					out.writeString(bi.toString());
				}

			};
		}
		return null;
	}

}
