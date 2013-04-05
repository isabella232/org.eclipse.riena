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
package org.eclipse.riena.communication.core.exception;

import java.io.IOException;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.RemoteFailure;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.exception.ExceptionFailure;
import org.eclipse.riena.core.exception.Failure;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.IntegrationTestCase;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.sample.app.common.exception.IExceptionService;

/**
 * This class is an test client for the exception service.
 * 
 * 
 */
@IntegrationTestCase
public class ExceptionClientITest extends RienaTestCase {
	private IExceptionService exceptionService;
	private IRemoteServiceRegistration regExceptionService;

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ExceptionClientITest.class);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		regExceptionService = Register.remoteProxy(IExceptionService.class)
				.usingUrl("http://localhost:8080/hessian/ExceptionService").withProtocol("hessian") //$NON-NLS-1$ //$NON-NLS-2$
				.andStart(Activator.getDefault().getContext());
		exceptionService = (IExceptionService) Activator.getDefault().getContext()
				.getService(Activator.getDefault().getContext().getServiceReference(IExceptionService.class.getName()));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	public void tearDown() throws Exception {
		regExceptionService.unregister();
		exceptionService = null;
		super.tearDown();
	}

	/**
	 * tests the exception service by requesting certain exceptions
	 * 
	 * @throws Exception
	 */
	public void testStandardExceptions1() throws Exception {

		try {
			exceptionService.throwException("java.io.IOException"); //$NON-NLS-1$
			fail("This call should throw an exception!"); //$NON-NLS-1$
		} catch (final Throwable e) {
			LOGGER.log(LogService.LOG_INFO, "Exception: " + e); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "Exception Type: " + e.getClass()); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "Exception.getMessage(): " + e.getMessage()); //$NON-NLS-1$

			assertTrue(e instanceof IOException);
			assertTrue(e.getMessage().trim().equals("ExceptionService: Here is your requested java.io.IOException...")); //$NON-NLS-1$
			assertNotNull(e.getStackTrace());
			assertNull(e.getCause());
		}
	}

	/**
	 * tests the exception service by requesting certain exceptions
	 * 
	 * @throws Exception
	 */
	public void testStandardExceptions2() throws Exception {

		try {
			exceptionService.throwException("java.lang.ClassNotFoundException"); //$NON-NLS-1$
			fail("This call should throw an exception!"); //$NON-NLS-1$
		} catch (final Throwable e) {
			LOGGER.log(LogService.LOG_INFO, "Exception: " + e); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "Exception Type: " + e.getClass()); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "Exception.getMessage(): " + e.getMessage()); //$NON-NLS-1$

			assertTrue(e instanceof ClassNotFoundException);
			assertTrue(e.getMessage().trim()
					.equals("ExceptionService: Here is your requested java.lang.ClassNotFoundException...")); //$NON-NLS-1$
			assertNull(e.getCause());
			assertNotNull(e.getStackTrace());
		}
	}

	/**
	 * tests the exception service by requesting certain exceptions
	 * 
	 * @throws Exception
	 */
	public void testStandardExceptions3() throws Exception {

		try {
			exceptionService.throwException("java.lang.NullPointerException"); //$NON-NLS-1$
			fail("This call should throw an exception!"); //$NON-NLS-1$
		} catch (final Throwable e) {
			LOGGER.log(LogService.LOG_INFO, "Exception: " + e); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "Exception Type: " + e.getClass()); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "Exception.getMessage(): " + e.getMessage()); //$NON-NLS-1$

			assertTrue(e instanceof NullPointerException);
			assertTrue(e.getMessage().trim()
					.equals("ExceptionService: Here is your requested java.lang.NullPointerException...")); //$NON-NLS-1$
			assertNull(e.getCause());
			assertNotNull(e.getStackTrace());
		}
	}

	/**
	 * tests the exception service by requesting certain exceptions
	 * 
	 * @throws Exception
	 */
	public void testStandardExceptions4() throws Exception {

		try {
			exceptionService.throwException("java.lang.ArrayIndexOutOfBoundsException"); //$NON-NLS-1$
			fail("This call should throw an exception!"); //$NON-NLS-1$
		} catch (final Throwable e) {
			LOGGER.log(LogService.LOG_INFO, "Exception: " + e); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "Exception Type: " + e.getClass()); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "Exception.getMessage(): " + e.getMessage()); //$NON-NLS-1$

			assertTrue(e instanceof java.lang.ArrayIndexOutOfBoundsException);
			assertTrue(e.getMessage().trim()
					.equals("ExceptionService: Here is your requested java.lang.ArrayIndexOutOfBoundsException...")); //$NON-NLS-1$
			assertNull(e.getCause());
			assertNotNull(e.getStackTrace());
		}
	}

	/**
	 * tests the exception service by requesting certain exceptions
	 * 
	 * @throws Exception
	 */
	public void texxstStandardExceptions5() throws Exception {

		try {
			exceptionService.throwRuntimeException("java.lang.RuntimeException"); //$NON-NLS-1$
			fail("This call should throw an exception!"); //$NON-NLS-1$
		} catch (final Throwable e) {
			LOGGER.log(LogService.LOG_INFO, "Exception: " + e); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "Exception Type: " + e.getClass()); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_INFO, "Exception.getMessage(): " + e.getMessage()); //$NON-NLS-1$

			assertTrue(e instanceof java.lang.RuntimeException);
			assertTrue(e.getMessage().trim()
					.equals("ExceptionService: Here is your requested java.lang.RuntimeException...")); //$NON-NLS-1$
			assertNull(e.getCause());
			assertNotNull(e.getStackTrace());
		}
	}

	/**
	 * tests the exception service by requesting certain exceptions
	 * 
	 * @throws Exception
	 */
	public void testRienaFailure() throws Exception {

		try {
			exceptionService.throwException("org.eclipse.riena.core.exception.ExceptionFailure"); //$NON-NLS-1$
			fail("This call should throw an exception!"); //$NON-NLS-1$
		} catch (final Throwable e) {
			assertTrue(e instanceof ExceptionFailure);
			final Failure failure = (Failure) e;
			assertNotNull(failure.getMessage());
			assertTrue(failure.getMessage().indexOf(
					"ExceptionService: Here is your requested org.eclipse.riena.core.exception.ExceptionFailure...") > -1); //$NON-NLS-1$
			// in this case even the cause is supposed to be null.
			assertNull(failure.getCause());
			assertNotNull(failure.getTimestamp());
			assertNotNull(failure.getStackTrace());
			assertTrue(failure.getStackTrace().length > 10);
		}
	}

	/**
	 * tests the exception service by requesting certain exceptions
	 * 
	 * @throws Exception
	 */
	public void testRienaFailure2() throws Exception {

		try {
			exceptionService.throwException("org.eclipse.riena.communication.core.RemoteFailure"); //$NON-NLS-1$
			fail("This call should throw an exception!"); //$NON-NLS-1$
		} catch (final Throwable e) {
			assertTrue(e instanceof RemoteFailure);
			final Failure failure = (Failure) e;
			assertNotNull(failure.getMessage());
			assertTrue(failure.getMessage().indexOf(
					"ExceptionService: Here is your requested org.eclipse.riena.communication.core.RemoteFailure...") > -1); //$NON-NLS-1$
			// in this case even the cause is supposed to be null.
			assertNull(failure.getCause());
			assertNotNull(failure.getTimestamp());
			assertNotNull(failure.getStackTrace());
		}
	}

	public void testTryNestedException() throws Exception {
		try {
			exceptionService.throwNestedException();
			fail("This call should throw an exception!"); //$NON-NLS-1$
		} catch (final Throwable e) {
			assertTrue(e instanceof Exception);
			assertTrue(e.getCause() instanceof NullPointerException);
			assertTrue(e.getCause() != e);
		}

	}
}