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
package org.eclipse.riena.ui.ridgets.databinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import com.ibm.icu.util.BuddhistCalendar;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests of the class <code>GregorianCalendarToStringConverter</code>.
 */
@NonUITestCase
public class GregorianCalendarToStringConverterTest extends TestCase {

	public void testConvert() throws Exception {

		final GregorianCalendarToStringConverter converter = new GregorianCalendarToStringConverter();

		Object result = converter.convert(null);
		assertEquals("", result); //$NON-NLS-1$

		result = converter.convert(1);
		assertEquals("", result); //$NON-NLS-1$

		final BuddhistCalendar buddhistCalendar = new BuddhistCalendar(2008, 4, 18);
		result = converter.convert(buddhistCalendar);
		assertEquals("", result); //$NON-NLS-1$

		final Calendar calendar = new GregorianCalendar(2008, 4, 18);
		result = converter.convert(calendar);
		final String expected = SimpleDateFormat.getDateInstance().format(calendar.getTime());
		assertEquals(expected, result);

	}
}
