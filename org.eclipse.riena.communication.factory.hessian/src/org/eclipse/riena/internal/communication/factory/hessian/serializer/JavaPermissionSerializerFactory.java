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
import java.lang.reflect.Constructor;
import java.security.Permission;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.JavaSerializer;
import com.caucho.hessian.io.Serializer;

import org.eclipse.riena.communication.factory.hessian.serializer.AbstractRienaSerializerFactory;
import org.eclipse.riena.core.util.Nop;

/**
 * {@code AbstractSerializerFactory} for the {@code Permission} classes.
 */
public class JavaPermissionSerializerFactory extends AbstractRienaSerializerFactory {

	@Override
	public Deserializer getDeserializer(final Class cl) throws HessianProtocolException {
		if (!Permission.class.isAssignableFrom(cl)) {
			return null;
		}
		return new JavaPermissionDeserializer(cl);
	}

	@Override
	public Serializer getSerializer(final Class cl) throws HessianProtocolException {
		if (!Permission.class.isAssignableFrom(cl)) {
			return null;
		}
		return new JavaPermissionSerializer(cl);
	}

	private static final String ACTIONS_FIELD = "actions"; //$NON-NLS-1$
	private static final String NAME_FIELD = "name"; //$NON-NLS-1$

	private static class JavaPermissionSerializer extends JavaSerializer {

		public JavaPermissionSerializer(final Class<?> cl) {
			super(cl);
		}

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

			final Permission permission = (Permission) obj;

			if (ref < -1) { // hessian 1.0
				out.writeString(NAME_FIELD);
				out.writeString(permission.getName());
				out.writeString(ACTIONS_FIELD);
				out.writeString(permission.getActions());
				out.writeMapEnd();
			} else { // hessian 2.0
				if (ref == -1) {
					out.writeInt(2);
					out.writeString(NAME_FIELD);
					out.writeString(ACTIONS_FIELD);
					out.writeObjectBegin(cl.getName());
				}
				out.writeString(permission.getName());
				out.writeString(permission.getActions());
			}
		}

	}

	private static class JavaPermissionDeserializer extends JavaDeserializer {

		private Constructor<Permission> constructor;

		public JavaPermissionDeserializer(final Class<?> cl) {
			super(cl);
		}

		@Override
		public Object readObject(final AbstractHessianInput in, final String[] fieldNames) throws IOException {
			final int ref = in.addRef(null);

			String name = null;
			String actions = null;

			for (final String key : fieldNames) {
				if (key.equals(NAME_FIELD)) {
					name = in.readString();
				} else if (key.equals(ACTIONS_FIELD)) {
					actions = in.readString();
				} else {
					in.readObject();
				}
			}

			final Permission permission = newPermission(name, actions);
			in.setRef(ref, permission);
			return permission;
		}

		private Permission newPermission(final String name, final String actions) throws HessianProtocolException {
			if (constructor == null) {
				constructor = getConstructor();
			}
			try {
				return (actions == null ? constructor.newInstance(name) : constructor.newInstance(name, actions));
			} catch (final Exception e) {
				throw new HessianProtocolException("Could not create instance for permission " //$NON-NLS-1$
						+ getType().getName() + ".", e); //$NON-NLS-1$
			}
		}

		private Constructor<Permission> getConstructor() throws HessianProtocolException {
			try {
				return getType().getConstructor(String.class, String.class);
			} catch (final NoSuchMethodException e) {
				Nop.reason("Fall throgh!"); //$NON-NLS-1$
			}
			try {
				return getType().getConstructor(String.class);
			} catch (final Exception e) {
				throw new HessianProtocolException("Could not create a constructor for permission " //$NON-NLS-1$
						+ getType().getName() + ".", e); //$NON-NLS-1$
			}

		}
	}

}
