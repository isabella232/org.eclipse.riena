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
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializer;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

import org.eclipse.riena.communication.core.RemoteFailure;
import org.eclipse.riena.communication.factory.hessian.serializer.AbstractRienaSerializerFactory;

/**
 * De/SerializerFactory for the {@code XMLGregorianCalendar}.
 */
public class XMLGregorianCalendarSerializerFactory extends AbstractRienaSerializerFactory {

	@Override
	public Deserializer getDeserializer(final Class cl) throws HessianProtocolException {
		if (cl == XMLGregorianCalendar.class) {
			return new XMLGregorianCalendarDeserializer();
		}
		return null;
	}

	@Override
	public Serializer getSerializer(final Class cl) throws HessianProtocolException {
		if (XMLGregorianCalendar.class.isAssignableFrom(cl)) {
			return new XMLGregorianCalendarSerializer();
		}
		return null;
	}

	private static class XMLGregorianCalendarDeserializer extends AbstractDeserializer {

		@Override
		public Object readObject(final AbstractHessianInput in) throws IOException {
			final boolean valid = in.readBoolean();
			final long time = in.readLong();
			final GregorianCalendar gregorian = new GregorianCalendar();
			gregorian.setTimeInMillis(time);
			try {
				final XMLGregorianCalendar xmlGregorian = DatatypeFactory.newInstance().newXMLGregorianCalendar(
						gregorian);
				if (!valid) {
					xmlGregorian.clear();
				}
				return xmlGregorian;
			} catch (final DatatypeConfigurationException e) {
				throw new RemoteFailure(
						"Could not instanciate XMLGegrorianCalendar specific implementation. This may happen if e.g. the SDK defines a different or no implementation of XMLGregorienCalendar", //$NON-NLS-1$
						e);
			}
		}

	}

	private static class XMLGregorianCalendarSerializer extends AbstractSerializer {

		@Override
		public void writeObject(final Object obj, final AbstractHessianOutput out) throws IOException {
			if (obj == null) {
				out.writeNull();
				return;
			} else {
				final XMLGregorianCalendar calendar = (XMLGregorianCalendar) obj;
				out.writeBoolean(calendar.isValid());
				out.writeLong(calendar.toGregorianCalendar().getTimeInMillis());
				return;
			}
		}

	}

}
