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
package org.eclipse.riena.communication.core.attachment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.ManualTestCase;
import org.eclipse.riena.sample.app.common.calendar.ITestGregorianCalendar;

/**
 * Test the hessian bug (we use our own deserializer now) when transferring
 * {@code GregorianCalendar}.
 * 
 */
@ManualTestCase
public final class GregorianCalendarITest extends RienaTestCase {

	private ITestGregorianCalendar calendarService;
	private IRemoteServiceRegistration regCalenderService;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		regCalenderService = Register.remoteProxy(ITestGregorianCalendar.class)
				.usingUrl("http://localhost:8080/hessian/TestGregorianCalendarWS").withProtocol("hessian") //$NON-NLS-1$ //$NON-NLS-2$
				.andStart(getContext());
		calendarService = (ITestGregorianCalendar) getContext().getService(
				getContext().getServiceReference(ITestGregorianCalendar.class.getName()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	public void tearDown() throws Exception {
		regCalenderService.unregister();
		calendarService = null;
		super.tearDown();
	}

	public void testDiffGregorian() {
		final GregorianCalendar from = new GregorianCalendar(2007, 1, 6);
		final GregorianCalendar till = new GregorianCalendar(2007, 1, 7);
		final long diff = calendarService.diffTimes1(from, till);
		assertEquals(24L * 60L * 60L * 1000L, diff);
	}

	public void testDiffDate() throws ParseException {
		final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy"); //$NON-NLS-1$
		final Date from = dateFormat.parse("6.1.2007"); //$NON-NLS-1$
		final Date till = dateFormat.parse("7.1.2007"); //$NON-NLS-1$
		final long diff = calendarService.diffTimes2(from, till);
		assertEquals(24L * 60L * 60L * 1000L, diff);
	}

}