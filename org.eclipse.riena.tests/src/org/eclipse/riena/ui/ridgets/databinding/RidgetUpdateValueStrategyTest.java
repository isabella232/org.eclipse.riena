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
package org.eclipse.riena.ui.ridgets.databinding;

import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.conversion.NumberToStringConverter;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests of the class <code>RidgetUpdateValueStrategy</code>.
 */
@NonUITestCase
public class RidgetUpdateValueStrategyTest extends TestCase {

	public void testCreateConverter() throws Exception {

		final RidgetUpdateValueStrategy strategy = new RidgetUpdateValueStrategy();

		IConverter converter = ReflectionUtils.invokeHidden(strategy, "createConverter", String.class, Double.TYPE);
		assertTrue(converter instanceof StringToNumberAllowingNullConverter);

		converter = ReflectionUtils.invokeHidden(strategy, "createConverter", String.class, Float.TYPE);
		assertTrue(converter instanceof StringToNumberAllowingNullConverter);

		converter = ReflectionUtils.invokeHidden(strategy, "createConverter", String.class, Long.TYPE);
		assertTrue(converter instanceof StringToNumberAllowingNullConverter);

		converter = ReflectionUtils.invokeHidden(strategy, "createConverter", String.class, Integer.TYPE);
		assertTrue(converter instanceof StringToNumberAllowingNullConverter);

		converter = ReflectionUtils.invokeHidden(strategy, "createConverter", String.class, GregorianCalendar.class);
		assertTrue(converter instanceof StringToGregorianCalendarConverter);

		converter = ReflectionUtils.invokeHidden(strategy, "createConverter", GregorianCalendar.class, String.class);
		assertTrue(converter instanceof GregorianCalendarToStringConverter);

		converter = ReflectionUtils.invokeHidden(strategy, "createConverter", Integer.class, String.class);
		assertTrue(converter instanceof NumberToStringConverter);

	}

}
