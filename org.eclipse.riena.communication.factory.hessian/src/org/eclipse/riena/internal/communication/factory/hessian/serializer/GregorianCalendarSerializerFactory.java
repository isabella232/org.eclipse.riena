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

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.caucho.hessian.io.CalendarHandle;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.Serializer;

import org.eclipse.riena.communication.factory.hessian.serializer.AbstractRienaSerializerFactory;

/**
 * Deserializer for the {@code GregorianCalendar}.
 */
public class GregorianCalendarSerializerFactory extends AbstractRienaSerializerFactory {

	@Override
	public Deserializer getDeserializer(final Class cl) throws HessianProtocolException {
		if (cl == CalendarHandle.class) {
			return new JavaDeserializer(cl) {

				@Override
				public Class getType() {
					return GregorianCalendar.class;
				}

				@Override
				protected Object instantiate() throws Exception {
					return new CalendarHandle(Calendar.class, 0);
				}
			};
		}
		return null;
	}

	@Override
	public Serializer getSerializer(final Class cl) throws HessianProtocolException {
		return null;
	}

}
