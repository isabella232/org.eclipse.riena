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
package org.eclipse.riena.ui.ridgets.validation;

import java.text.DecimalFormat;
import java.util.Locale;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.ui.swt.utils.TestUtils;

/**
 * Tests for the MaxNumberLength rule.
 */
@NonUITestCase
public class MaxNumberLengthTest extends RienaTestCase {

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testUSLocale() throws Exception {
		// locale has comma as grouping-separator and leading minus
		final MaxNumberLength maxNumberLength = new MaxNumberLength(7, Locale.US);

		try {
			maxNumberLength.validate(new Object());
			fail("expected thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure e) {
			ok("passed test");
		} catch (final RuntimeException e) {
			fail("expected thrown " + ValidationFailure.class.getName());
		}

		assertTrue(maxNumberLength.validate(null).isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertTrue(maxNumberLength.validate("1,234,567").isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertFalse(maxNumberLength.validate("12,345,678").isOK());

		assertTrue(maxNumberLength.validate("-1234567").isOK());
		assertTrue(maxNumberLength.validate("-1,234,567").isOK());

		assertTrue(maxNumberLength.validate("- 1234567").isOK());
		assertTrue(maxNumberLength.validate("- 1,234,567").isOK());

		assertFalse(maxNumberLength.validate("-12345678").isOK());
		assertFalse(maxNumberLength.validate("-12,345.678").isOK());

		assertTrue(maxNumberLength.validate("- 1234567").isOK());
		assertTrue(maxNumberLength.validate("- 1,234,567").isOK());

		assertFalse(maxNumberLength.validate("- 12345678").isOK());
		assertFalse(maxNumberLength.validate("- 12,345.678").isOK());

	}

	public void testGermanLocale() throws Exception {
		// locale has dot as grouping-separator and leading minus
		final MaxNumberLength maxNumberLength = new MaxNumberLength(7, Locale.GERMANY);

		try {
			maxNumberLength.validate(new Object());
			fail("expected thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure e) {
			ok("passed test");
		} catch (final RuntimeException e) {
			fail("expected thrown " + ValidationFailure.class.getName());
		}

		assertTrue(maxNumberLength.validate(null).isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertTrue(maxNumberLength.validate("1.234.567").isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertFalse(maxNumberLength.validate("12.345.678").isOK());

		assertTrue(maxNumberLength.validate("-1234567").isOK());
		assertTrue(maxNumberLength.validate("-1.234.567").isOK());

		assertTrue(maxNumberLength.validate("- 1234567").isOK());
		assertTrue(maxNumberLength.validate("- 1.234.567").isOK());

		assertFalse(maxNumberLength.validate("-12345678").isOK());
		assertFalse(maxNumberLength.validate("-12.345.678").isOK());

		assertFalse(maxNumberLength.validate("- 12345678").isOK());
		assertFalse(maxNumberLength.validate("- 12.345.678").isOK());
	}

	public void testFrenchLocale() throws Exception {
		// locale has char 0xA0 as grouping separator (which may be a blank
		// aswell) and leading minus
		final MaxNumberLength maxNumberLength = new MaxNumberLength(7, Locale.FRANCE);

		try {
			maxNumberLength.validate(new Object());
			fail("expected thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure e) {
			ok("passed test");
		} catch (final RuntimeException e) {
			fail("expected thrown " + ValidationFailure.class.getName());
		}

		assertTrue(maxNumberLength.validate(null).isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertTrue(maxNumberLength.validate("1 234 567").isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertFalse(maxNumberLength.validate("12 345 678").isOK());

		assertTrue(maxNumberLength.validate("-1234567").isOK());
		assertTrue(maxNumberLength.validate("-1 234 567").isOK());

		assertTrue(maxNumberLength.validate("- 1234567").isOK());
		assertTrue(maxNumberLength.validate("- 1 234 567").isOK());

		assertFalse(maxNumberLength.validate("-12345678").isOK());
		assertFalse(maxNumberLength.validate("-12 345 678").isOK());

		assertFalse(maxNumberLength.validate("- 12345678").isOK());
		assertFalse(maxNumberLength.validate("- 12 345 678").isOK());

		// uses the 0xa0 grouping-separator character:
		assertTrue(maxNumberLength.validate(DecimalFormat.getInstance(Locale.FRANCE).format(1234567)).isOK());
		assertFalse(maxNumberLength.validate(DecimalFormat.getInstance(Locale.FRANCE).format(12345678)).isOK());
	}

	public void testArabLocale() throws Exception {
		// locale has a comma as grouping-separator character and
		// features a trailing minus instead of a leading one

		if (!TestUtils.isArabLocaleAvailable()) {
			System.err
					.println(getClass().getName()
							+ ".testArabLocale(): Skipping test because no Arab locale is available. Use international JRE to run all tests.");
			return;
		}

		final MaxNumberLength maxNumberLength = new MaxNumberLength(7, new Locale("ar", "AE"));

		try {
			maxNumberLength.validate(new Object());
			fail("expected thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure e) {
			ok("passed test");
		} catch (final RuntimeException e) {
			fail("expected thrown " + ValidationFailure.class.getName());
		}

		assertTrue(maxNumberLength.validate(null).isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertTrue(maxNumberLength.validate("1,234,567").isOK());

		assertTrue(maxNumberLength.validate("1234567").isOK());
		assertFalse(maxNumberLength.validate("12,345,678").isOK());

		assertTrue(maxNumberLength.validate("1234567-").isOK());
		assertTrue(maxNumberLength.validate("1,234,567-").isOK());

		assertTrue(maxNumberLength.validate("1234567 -").isOK());
		assertTrue(maxNumberLength.validate("1,234,567 -").isOK());

		assertFalse(maxNumberLength.validate("12345678-").isOK());
		assertFalse(maxNumberLength.validate("12,345,678-").isOK());

		assertFalse(maxNumberLength.validate("12345678 -").isOK());
		assertFalse(maxNumberLength.validate("12,345,678 -").isOK());

		assertTrue(maxNumberLength.validate(DecimalFormat.getInstance(new Locale("ar", "AE")).format(1234567)).isOK());
		assertFalse(maxNumberLength.validate(DecimalFormat.getInstance(new Locale("ar", "AE")).format(12345678)).isOK());

	}

	/**
	 * Tests the method {@code setInitializationData}.
	 * 
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testSetInitializationData() throws Exception {

		MaxNumberLength rule = new MaxNumberLength();
		assertTrue(rule.validate("").isOK());
		assertFalse(rule.validate("1").isOK());

		rule = new MaxNumberLength();
		rule.setInitializationData(null, null, "5");
		assertTrue(rule.validate("1").isOK());
		assertTrue(rule.validate("12345").isOK());
		assertFalse(rule.validate("123456").isOK());

		rule = new MaxNumberLength();
		String localString = Locale.GERMANY.getLanguage() + "," + Locale.GERMANY.getCountry();
		rule.setInitializationData(null, null, "5," + localString);
		assertTrue(rule.validate("10000").isOK());
		assertFalse(rule.validate("100000").isOK());
		assertTrue(rule.validate("10.000").isOK());
		assertFalse(rule.validate("10,000").isOK());
		assertFalse(rule.validate("100.000").isOK());
		assertTrue(rule.validate("1,012").isOK());
		assertTrue(rule.validate("123,0").isOK());
		assertFalse(rule.validate("12345,0").isOK());

		rule = new MaxNumberLength();
		localString = Locale.US.getLanguage() + "," + Locale.US.getCountry();
		rule.setInitializationData(null, null, "5," + localString);
		assertTrue(rule.validate("10000").isOK());
		assertFalse(rule.validate("100000").isOK());
		assertTrue(rule.validate("10,000").isOK());
		assertFalse(rule.validate("10.000").isOK());
		assertFalse(rule.validate("100,000").isOK());

	}

}
