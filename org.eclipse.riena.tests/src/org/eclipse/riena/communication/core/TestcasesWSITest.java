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
package org.eclipse.riena.communication.core;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.ManualTestCase;
import org.eclipse.riena.sample.app.common.BigIntegerContainer;
import org.eclipse.riena.sample.app.common.ITestcasesWS;

/**
 * Test the hessian bug (we use our own deserializer now) when transferring
 * {@code GregorianCalendar}.
 * 
 */
@ManualTestCase
public final class TestcasesWSITest extends RienaTestCase {

	private ITestcasesWS testcasesService;
	private IRemoteServiceRegistration regTestcases;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		regTestcases = Register.remoteProxy(ITestcasesWS.class).usingUrl("http://localhost:8080/hessian/TestcasesWS") //$NON-NLS-1$
				.withProtocol("hessian").andStart(getContext()); //$NON-NLS-1$
		testcasesService = (ITestcasesWS) getContext().getService(
				getContext().getServiceReference(ITestcasesWS.class.getName()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		regTestcases.unregister();
		testcasesService = null;
	}

	/**
	 * nomen est omen
	 */
	public void testBigInteger() {
		final BigInteger bigInteger = new BigInteger(80, new Random());
		final BigInteger result = testcasesService.echoBigInteger(bigInteger);
		assertNotNull("result must not be null", result); //$NON-NLS-1$
		assertEquals("result and echo must be equal", result, bigInteger); //$NON-NLS-1$
	}

	public void testXMLGregorianCalendarImpl() {
		final XMLGregorianCalendarImpl xmlGregorianCalendarImpl = new XMLGregorianCalendarImpl(new GregorianCalendar());
		final XMLGregorianCalendar result = testcasesService.echoXMLGregorianCalendar(xmlGregorianCalendarImpl);
		assertNotNull("result must not be null", result); //$NON-NLS-1$
		assertTrue("result must be instance of XMLGregorianCalendarImpl", result instanceof XMLGregorianCalendarImpl); //$NON-NLS-1$
		final XMLGregorianCalendarImpl result2 = (XMLGregorianCalendarImpl) result;
		assertEquals("result and echo must be equal", result2, xmlGregorianCalendarImpl); //$NON-NLS-1$
	}

	public void testBigIntegerWithNullValue() {
		final BigInteger bigInteger = null;
		final BigInteger result = testcasesService.echoNullBigInteger(bigInteger);
		assertNull("result must be null", result); //$NON-NLS-1$
		assertNull("result must be null", bigInteger); //$NON-NLS-1$
	}

	public void testBigIntegerHashMap() {
		final HashMap<String, BigInteger> hashMap = new HashMap<String, BigInteger>();
		final Random random = new Random();
		for (int i = 0; i < 10; i++) {
			final BigInteger bigInteger = new BigInteger(80, random);
			hashMap.put(Integer.valueOf(random.nextInt()).toString(), bigInteger);
		}
		final HashMap<String, BigInteger> result = testcasesService.echoBigIntegerHashMap(hashMap);
		assertNotNull("result must be not null", result); //$NON-NLS-1$
		assertEquals("hashmaps must be equal", result, hashMap); //$NON-NLS-1$

	}

	public void testBigIntegerList() {
		final ArrayList<BigInteger> list = new ArrayList<BigInteger>();
		final Random random = new Random();
		for (int i = 0; i < 10; i++) {
			final BigInteger bigInteger = new BigInteger(80, random);
			list.add(bigInteger);
		}
		final List<BigInteger> result = testcasesService.echoBigIntegerList(list);
		assertNotNull("result must be not null", result); //$NON-NLS-1$
		assertEquals("lists must be equal", result, list); //$NON-NLS-1$

	}

	public void testBigIntegerContainer() {
		final Random random = new Random();
		final BigIntegerContainer bigIntegerContainer = new BigIntegerContainer(new BigInteger(10, random),
				new BigInteger(30, random), "name4711"); //$NON-NLS-1$
		final BigIntegerContainer result = testcasesService.echoBigIntegerContainer(bigIntegerContainer);
		assertNotNull("result must not be null", result); //$NON-NLS-1$
		assertEquals("result and echo must be equal", result, bigIntegerContainer); //$NON-NLS-1$

	}

}