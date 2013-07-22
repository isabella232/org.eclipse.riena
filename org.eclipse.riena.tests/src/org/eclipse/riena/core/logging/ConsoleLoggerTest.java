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
package org.eclipse.riena.core.logging;

import java.io.IOException;

import junit.framework.Assert;

import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * 
 */
@NonUITestCase
public class ConsoleLoggerTest extends RienaTestCase {

	private Logger logger;

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		logger = new ConsoleLogger(ConsoleLoggerTest.class.getName());
	}

	public void testLevels() {
		logger.log(LogService.LOG_DEBUG, "This is debug!");
		logger.log(LogService.LOG_ERROR, "This is error!");
		logger.log(LogService.LOG_INFO, "This is info!");
		logger.log(LogService.LOG_WARNING, "This is warning!");
		logger.log(-1, "This is unknown!");
	}

	public void testWithExceptions() {
		final IOException ioe = new IOException("Oops!");
		logger.log(LogService.LOG_ERROR, "This is an error!", ioe);
	}

	public void testWithContext() {
		logger.log("CONTEXT", LogService.LOG_INFO, "Message with context.");
	}

	public void testWithServiceReference() {
		final String stringService = "Very simple service";
		final ServiceRegistration sr = getContext().registerService(String.class.getName(), stringService, null);
		Assert.assertTrue(sr.getReference() != null);
		logger.log(sr.getReference(), LogService.LOG_INFO, "Message with context.");
		sr.unregister();
	}

	public void testWithBadInput() {
		logger.log((ServiceReference) null, 0, null, null);
		logger.log(null, 0, null, null);
	}
}
