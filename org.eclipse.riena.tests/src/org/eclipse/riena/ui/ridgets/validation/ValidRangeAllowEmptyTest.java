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
package org.eclipse.riena.ui.ridgets.validation;

import java.util.Locale;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests for the {@link ValidRangeAllowEmpty} rule.
 */
@NonUITestCase
public class ValidRangeAllowEmptyTest extends ValidRangeTest {

	/**
	 * Create a range using the 0-arg constructor.
	 */
	@Override
	protected ValidRange createRange() {
		return new ValidRangeAllowEmpty();
	}

	/**
	 * Create a range using the 2-arg constructor.
	 */
	@Override
	protected ValidRange createRange(final Number min, final Number max) {
		return new ValidRangeAllowEmpty(min, max);
	}

	/**
	 * Create a range using the 3-arg constructor.
	 */
	@Override
	protected ValidRange createRange(final Number min, final Number max, final Locale locale) {
		return new ValidRangeAllowEmpty(min, max, locale);
	}

	@Override
	public void testEmptyValuesUSLocale() {
		final ValidRange rule = createRange(10, 20, Locale.US);
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("").isOK());
	}

	@Override
	public void testEmptyValuesGermanLocale() {
		final ValidRange rule = createRange(10, 20, Locale.GERMANY);
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("").isOK());
	}

	@Override
	public void testEmptyValuesArabLocale() {
		final ValidRange rule = createRange(10, 20, new Locale("ar", "AE"));
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("").isOK());
	}

}
