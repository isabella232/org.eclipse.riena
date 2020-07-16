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
package org.eclipse.riena.core;

import org.osgi.service.log.LogLevel;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.ReflectionFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.tests.Activator;

/**
 * Test the {@code Log4r} class.
 */
@NonUITestCase
public class Log4rTest extends RienaTestCase {

	private String savedValue;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		assertNotNull("Test must be a plugin unit test.", Activator.getDefault());
		savedValue = System.getProperty(RienaStatus.RIENA_DEVELOPMENT_SYSTEM_PROPERTY);
		System.clearProperty(RienaStatus.RIENA_DEVELOPMENT_SYSTEM_PROPERTY);
	}

	@Override
	protected void tearDown() throws Exception {
		if (savedValue != null) {
			System.setProperty(RienaStatus.RIENA_DEVELOPMENT_SYSTEM_PROPERTY, savedValue);
		}
		super.tearDown();
	}

	public void testWithContext() {
		final Logger logger = Log4r.getLogger(Activator.getDefault(), Log4rTest.class);
		assertNotNull(logger);
		assertNotSame("ConsoleLogger", logger.getClass().getSimpleName());
		assertNotSame("NullLogger", logger.getClass().getSimpleName());
	}

	public void testWithOutContextNoRienaDefaultLogging() {
		final Logger logger = Log4r.getLogger(null, Log4rTest.class);
		assertNotNull(logger);
		final String expectedLogger = isInOsgiDevMode() ? "ConsoleLogger" : "NullLogger";
		assertEquals(expectedLogger, logger.getClass().getSimpleName());
	}

	public void testWithOutContextWithRienaDefaultLoggingFalse() {
		System.setProperty(RienaStatus.RIENA_DEVELOPMENT_SYSTEM_PROPERTY, Boolean.FALSE.toString());
		final Logger logger = Log4r.getLogger(null, Log4rTest.class);
		assertNotNull(logger);
		assertEquals("NullLogger", logger.getClass().getSimpleName());
	}

	public void testWithOutContextWithRienaDefaultLoggingTrue() {
		System.setProperty(RienaStatus.RIENA_DEVELOPMENT_SYSTEM_PROPERTY, Boolean.TRUE.toString());
		final Logger logger = Log4r.getLogger(null, Log4rTest.class);
		assertNotNull(logger);
		assertEquals("ConsoleLogger", logger.getClass().getSimpleName());
	}

	public void testWithContextByName() {
		final Logger logger = Log4r.getLogger(Activator.getDefault(), Log4rTest.class.getName());
		assertNotNull(logger);
		assertNotSame("ConsoleLogger", logger.getClass().getSimpleName());
		assertNotSame("NullLogger", logger.getClass().getSimpleName());
	}

	public void testWithOutContextNoRienaDefaultLoggingByName() {
		final Logger logger = Log4r.getLogger(null, Log4rTest.class.getName());
		assertNotNull(logger);
		final String expectedLogger = isInOsgiDevMode() ? "ConsoleLogger" : "NullLogger";
		assertEquals(expectedLogger, logger.getClass().getSimpleName());
	}

	public void testWithOutContextWithRienaDefaultLoggingFalseByName() {
		System.setProperty(RienaStatus.RIENA_DEVELOPMENT_SYSTEM_PROPERTY, Boolean.FALSE.toString());
		final Logger logger = Log4r.getLogger(null, Log4rTest.class.getName());
		assertNotNull(logger);
		assertEquals("NullLogger", logger.getClass().getSimpleName());
	}

	public void testWithOutContextWithRienaDefaultLoggingTrueByName() {
		System.setProperty(RienaStatus.RIENA_DEVELOPMENT_SYSTEM_PROPERTY, Boolean.TRUE.toString());
		final Logger logger = Log4r.getLogger(null, Log4rTest.class.getName());
		assertNotNull(logger);
		assertEquals("ConsoleLogger", logger.getClass().getSimpleName());
	}

	private boolean isInOsgiDevMode() {
		return System.getProperty("osgi.dev") != null; //$NON-NLS-1$
	}

	public void testThatTheTweakedLoggerImplHasTheEnabledLevelFieldSetToTrace() {
		final Logger logger = Log4r.getLogger(getClass());
		assertEquals("org.eclipse.osgi.internal.log.LoggerImpl", logger.getClass().getName());
		try {
			final LogLevel logLevel = ReflectionUtils.getHidden(logger, "enabledLevel");
			assertEquals(LogLevel.TRACE, logLevel);
		} catch (final ReflectionFailure e) {
			fail("Implementation of Logger has changed. The enabledLevel field is no longer accessible!");
		}
	}
}
