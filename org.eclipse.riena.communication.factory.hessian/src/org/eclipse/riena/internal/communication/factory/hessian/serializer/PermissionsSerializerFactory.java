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
package org.eclipse.riena.internal.communication.factory.hessian.serializer;

import java.io.IOException;
import java.security.Permission;
import java.security.Permissions;
import java.util.List;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

import org.eclipse.riena.communication.factory.hessian.serializer.AbstractRienaSerializerFactory;
import org.eclipse.riena.core.util.Iter;

/**
 * {@code AbstractSerializerFactory} for the {@code Permissions} class.
 */
public class PermissionsSerializerFactory extends AbstractRienaSerializerFactory {

	private static final String PERMISSIONS_FIELD_NAME = "permissions"; //$NON-NLS-1$

	@Override
	public Deserializer getDeserializer(final Class cl) throws HessianProtocolException {
		if (cl != Permissions.class) {
			return null;
		}
		return new PermissionsDeserializer();
	}

	@Override
	public Serializer getSerializer(final Class cl) throws HessianProtocolException {
		if (cl != Permissions.class) {
			return null;
		}
		return new PermissionsSerializer();
	}

	private static class PermissionsSerializer extends AbstractSerializer {

		@Override
		public void writeObject(final Object obj, final AbstractHessianOutput out) throws IOException {
			if (obj == null) {
				out.writeNull();
				return;
			}
			if (out.addRef(obj)) {
				return;
			}
			final Class<?> cl = obj.getClass();
			final int ref = out.writeObjectBegin(cl.getName());

			final Permissions permissions = (Permissions) obj;

			if (ref < -1) { // hessian 1.0
				out.writeString(PERMISSIONS_FIELD_NAME);
				out.writeObject(permissions.elements());
				out.writeMapEnd();
			} else { // hessian 2.0
				if (ref == -1) {
					out.writeInt(1);
					out.writeString(PERMISSIONS_FIELD_NAME);
					out.writeObjectBegin(cl.getName());
				}
				out.writeObject(permissions.elements());
			}
		}
	}

	private static class PermissionsDeserializer extends AbstractDeserializer {

		@Override
		public Class getType() {
			return Permissions.class;
		}

		@Override
		public Object readObject(final AbstractHessianInput in, final String[] fieldNames) throws IOException {
			final int ref = in.addRef(null);

			List<Permission> initValue = null;

			for (final String key : fieldNames) {
				if (key.equals(PERMISSIONS_FIELD_NAME)) {
					initValue = (List<Permission>) in.readObject();
				} else {
					in.readObject();
				}
			}

			final Permissions permissions = new Permissions();
			for (final Permission permission : Iter.able(initValue)) {
				permissions.add(permission);
			}

			in.setRef(ref, permissions);
			return permissions;
		}

	}

}
