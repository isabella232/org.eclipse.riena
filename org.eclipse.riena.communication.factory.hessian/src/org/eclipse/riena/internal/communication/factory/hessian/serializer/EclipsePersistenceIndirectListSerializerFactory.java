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

import java.io.IOException;
import java.util.List;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

import org.eclipse.riena.communication.factory.hessian.serializer.AbstractRienaSerializerFactory;

/**
 * Serializer for the {@code org.eclipse.persistence.indirection.IndirectList}.
 * <p>
 * This serializer resolves the laziness of the {@code IndirectList} and
 * transports the data with a {@code ArrayList}.
 */
public class EclipsePersistenceIndirectListSerializerFactory extends AbstractRienaSerializerFactory {

	private static final String ORG_ECLIPSE_PERSISTENCE_INDIRECTION_INDIRECT_LIST = "org.eclipse.persistence.indirection.IndirectList"; //$NON-NLS-1$

	@Override
	public Deserializer getDeserializer(final Class cl) throws HessianProtocolException {
		return null;
	}

	@Override
	public Serializer getSerializer(final Class cl) throws HessianProtocolException {
		if (!cl.getName().equals(ORG_ECLIPSE_PERSISTENCE_INDIRECTION_INDIRECT_LIST)) {
			return null;
		}
		return new AbstractSerializer() {

			@Override
			public void writeObject(final Object obj, final AbstractHessianOutput out) throws IOException {
				if (out.addRef(obj)) {
					return;
				}
				final List<?> list = (List<?>) obj;
				final boolean hasEnd = out.writeListBegin(list.size(), null);

				for (final Object value : list) {
					out.writeObject(value);
				}

				if (hasEnd) {
					out.writeListEnd();
				}
			}

		};
	}
}
