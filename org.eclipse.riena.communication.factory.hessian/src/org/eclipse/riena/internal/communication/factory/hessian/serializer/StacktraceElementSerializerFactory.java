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

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.Serializer;

import org.eclipse.riena.communication.factory.hessian.serializer.AbstractRienaSerializerFactory;

/**
 *
 */
public class StacktraceElementSerializerFactory extends AbstractRienaSerializerFactory {

	@Override
	public Deserializer getDeserializer(final Class cl) throws HessianProtocolException {
		if (cl.equals(StackTraceElement.class)) {
			return new JavaDeserializer(cl) {

				@Override
				protected Object instantiate() throws Exception {
					// just return a dummy, fields will be set by the JavaDeserializer
					return new StackTraceElement("x", "x", "x", 1); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			};
		}
		return null;
	}

	@Override
	public Serializer getSerializer(final Class cl) throws HessianProtocolException {
		return null;
	}

	@Override
	public Class<?>[] getReplacedDeserializerTypes() {
		return new Class<?>[] { StackTraceElement.class };
	}

}
