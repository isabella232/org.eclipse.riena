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

import org.osgi.service.log.LogService;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.core.logging.LogLevelMapper;

/**
 *
 */
@SuppressWarnings("restriction")
@NonUITestCase
public class LogLevelMapperTest extends RienaTestCase {

	public void testLevelToString() {
		assertEquals("CUSTOM(-12)", LogLevelMapper.getValue(-12));
		assertEquals("DEBUG", LogLevelMapper.getValue(LogService.LOG_DEBUG));
		assertEquals("INFO", LogLevelMapper.getValue(LogService.LOG_INFO));
		assertEquals("WARNING", LogLevelMapper.getValue(LogService.LOG_WARNING));
		assertEquals("ERROR", LogLevelMapper.getValue(LogService.LOG_ERROR));
		assertEquals("NONE", LogLevelMapper.getValue(LogService.LOG_ERROR - 1));
	}

	public void testStringToLevel() {
		assertEquals(-12, LogLevelMapper.getValue("CUSTOM(-12)"));
		assertEquals(LogService.LOG_DEBUG, LogLevelMapper.getValue("DEBUG"));
		assertEquals(LogService.LOG_INFO, LogLevelMapper.getValue("INFO"));
		assertEquals(LogService.LOG_WARNING, LogLevelMapper.getValue("WARNING"));
		assertEquals(LogService.LOG_ERROR, LogLevelMapper.getValue("ERROR"));
		assertEquals(LogService.LOG_ERROR - 1, LogLevelMapper.getValue("NONE"));
	}
}
