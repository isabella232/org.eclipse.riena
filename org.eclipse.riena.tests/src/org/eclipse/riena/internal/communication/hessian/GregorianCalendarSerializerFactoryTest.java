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
package org.eclipse.riena.internal.communication.hessian;

import java.util.GregorianCalendar;

import org.eclipse.riena.internal.communication.factory.hessian.serializer.GregorianCalendarSerializerFactory;

/**
 * Test the {@code GregorianCalendarSerializerFactory} class.
 */
public class GregorianCalendarSerializerFactoryTest extends AbstractSerializerFactoryTestCase {

	public void testGregorianCalendarFail() {
		assertTrue(isBackAndForthOk(new GregorianCalendar(1961, 8, 26), HessianSerializerVersion.Two,
				GregorianCalendar.class));
	}

	public void testGregorianCalendar() {
		assertTrue(isBackAndForthOk(new GregorianCalendar(1961, 8, 26), HessianSerializerVersion.Two,
				GregorianCalendar.class, new GregorianCalendarSerializerFactory()));
	}
}
