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
package org.eclipse.riena.ui.ridgets.validation;

import java.text.DecimalFormat;
import java.util.Locale;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Tests for the ValidCharacters rule.
 */
@NonUITestCase
public class ValidIntegerTest extends RienaTestCase {

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public final void testUSlocale() throws Exception {
		final ValidInteger rule = new ValidInteger(Locale.US);

		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		}

		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("").isOK());
		assertTrue(rule.validate("2").isOK());
		assertTrue(rule.validate("-2").isOK());
		assertTrue(rule.validate("- 2").isOK());

		assertTrue(rule.validate("1,000").isOK());
		assertTrue(rule.validate("-1,000").isOK());
		assertTrue(rule.validate("- 1,000").isOK());
		assertTrue(rule.validate("1,000,200").isOK());
		assertTrue(rule.validate("-1,000,200").isOK());
		assertTrue(rule.validate("- 1,000,200").isOK());

		// valid decimal numbers
		assertFalse(rule.validate("1.0").isOK());
		assertFalse(rule.validate("1,000.0").isOK());
		assertFalse(rule.validate("-1,000.0").isOK());
		assertFalse(rule.validate("- 1,000.0").isOK());

		// for the US locale it appears that the German grouping-character is
		// behind a decimal-separator:
		assertFalse(rule.validate("1.234,31").isOK());
		assertFalse(rule.validate("-1.234,31").isOK());
		assertFalse(rule.validate("- 1.234,31").isOK());

		// wrong symbols
		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertFalse(rule.validate("1.23A").isOK());
		assertFalse(rule.validate("123A321").isOK());
		assertFalse(rule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertFalse(rule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());

	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public final void testGermanLocale() throws Exception {
		final ValidInteger rule = new ValidInteger(Locale.GERMANY);

		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		}

		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("").isOK());
		assertTrue(rule.validate("2").isOK());
		assertTrue(rule.validate("-2").isOK());
		assertTrue(rule.validate("- 2").isOK());

		assertTrue(rule.validate("1.000").isOK());
		assertTrue(rule.validate("-1.000").isOK());
		assertTrue(rule.validate("- 1.000").isOK());
		assertTrue(rule.validate("1.000.200").isOK());
		assertTrue(rule.validate("-1.000.200").isOK());
		assertTrue(rule.validate("- 1.000.200").isOK());

		// valid decimal numbers
		assertFalse(rule.validate("1,0").isOK());
		assertFalse(rule.validate("1.000,0").isOK());
		assertFalse(rule.validate("-1.000,0").isOK());
		assertFalse(rule.validate("- 1.000,0").isOK());

		// for the German locale it appears that the US grouping-character is
		// behind a decimal-separator:
		assertFalse(rule.validate("1,234.31").isOK());
		assertFalse(rule.validate("-1,234.31").isOK());
		assertFalse(rule.validate("- 1,234.31").isOK());

		// wrong symbols
		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertFalse(rule.validate("1.23A").isOK());
		assertFalse(rule.validate("123A321").isOK());
		assertFalse(rule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertFalse(rule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());

	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public final void testSwissLocale() throws Exception {
		final ValidInteger rule = new ValidInteger(new Locale("de", "CH"));

		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		}

		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("").isOK());
		assertTrue(rule.validate("2").isOK());
		assertTrue(rule.validate("-2").isOK());
		assertTrue(rule.validate("- 2").isOK());

		assertTrue(rule.validate("1'000").isOK());
		assertTrue(rule.validate("-1'000").isOK());
		assertTrue(rule.validate("- 1'000").isOK());
		assertTrue(rule.validate("1'000'200").isOK());
		assertTrue(rule.validate("-1'000'200").isOK());
		assertTrue(rule.validate("- 1'000'200").isOK());

		// valid decimal numbers
		assertFalse(rule.validate("1.0").isOK());
		assertFalse(rule.validate("1'000.0").isOK());
		assertFalse(rule.validate("-1'000.0").isOK());
		assertFalse(rule.validate("- 1'000.0").isOK());

		// for the Swiss locale it appears that the German grouping-character is
		// a decimal-separator and the German decimal-separator is an alien
		// character:
		assertFalse(rule.validate("1.234,31").isOK());
		assertFalse(rule.validate("-1.234,31").isOK());
		assertFalse(rule.validate("- 1.234,31").isOK());

		// wrong symbols
		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertFalse(rule.validate("1.23A").isOK());
		assertFalse(rule.validate("123A321").isOK());
		assertFalse(rule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertFalse(rule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public final void testFrenchLocale() throws Exception {
		final ValidInteger rule = new ValidInteger(Locale.FRANCE);

		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		}

		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("").isOK());
		assertTrue(rule.validate("2").isOK());
		assertTrue(rule.validate("-2").isOK());
		assertTrue(rule.validate("- 2").isOK());

		assertTrue(rule.validate("1 000").isOK());
		assertTrue(rule.validate("-1 000").isOK());
		assertTrue(rule.validate("- 1 000").isOK());
		assertTrue(rule.validate("1 000 200").isOK());
		assertTrue(rule.validate("-1 000 200").isOK());
		assertTrue(rule.validate("- 1 000 200").isOK());

		// valid decimal numbers
		assertFalse(rule.validate("1,0").isOK());
		assertFalse(rule.validate("1 000,0").isOK());
		assertFalse(rule.validate("-1 000,0").isOK());
		assertFalse(rule.validate("- 1 000,0").isOK());

		// for the French locale it appears that the German grouping-character
		// is an alien character and the German decimal-separator ',' is a
		// decimal-separator:
		assertFalse(rule.validate("1.234,31").isOK());
		assertFalse(rule.validate("-1.234,31").isOK());
		assertFalse(rule.validate("- 1.234,31").isOK());

		// wrong symbols
		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertFalse(rule.validate("1.23A").isOK());
		assertFalse(rule.validate("123A321").isOK());
		assertFalse(rule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertFalse(rule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());

	}

	public final void testHindiLocale() throws Exception {
		// this language has unusual digits
		final Locale hindi = new Locale("hi", "IN");
		final ValidInteger rule = new ValidInteger(hindi);
		final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(hindi);
		assertTrue(rule.validate(format.format(12)).isOK());
		assertTrue(rule.validate(format.format(12345)).isOK());
		assertTrue(rule.validate(format.format(-12)).isOK());
		assertTrue(rule.validate(format.format(-12345)).isOK());

		assertFalse(rule.validate(format.format(12.5d)).isOK());
		assertFalse(rule.validate(format.format(12345.6d)).isOK());
		assertFalse(rule.validate(format.format(-12.3d)).isOK());
		assertFalse(rule.validate(format.format(-12345.6d)).isOK());

		// TODO: more Tests for for Hindi(Indian)
	}

	public final void testThailandLocale() throws Exception {
		// this language has unusual digits
		final Locale thailand = new Locale("th", "TH");
		final ValidInteger rule = new ValidInteger(thailand);
		final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(thailand);
		assertTrue(rule.validate(format.format(12)).isOK());
		assertTrue(rule.validate(format.format(12345)).isOK());
		assertTrue(rule.validate(format.format(-12)).isOK());
		assertTrue(rule.validate(format.format(-12345)).isOK());

		assertFalse(rule.validate(format.format(12.5d)).isOK());
		assertFalse(rule.validate(format.format(12345.6d)).isOK());
		assertFalse(rule.validate(format.format(-12.3d)).isOK());
		assertFalse(rule.validate(format.format(-12345.6d)).isOK());

		// TODO: more tests for Thai (Thailand,TH)
	}

	public final void testArabLocale() throws Exception {
		// Arab countries seem to have a unified format
		// They have trailing minus signs, instead of leading
		final Locale arabEmirates = new Locale("ar", "AE");
		final ValidInteger rule = new ValidInteger(arabEmirates);
		final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(arabEmirates);
		assertTrue(rule.validate(format.format(12)).isOK());
		assertTrue(rule.validate(format.format(12345)).isOK());
		assertTrue(rule.validate(format.format(-12)).isOK());
		assertTrue(rule.validate(format.format(-12345)).isOK());

		assertFalse(rule.validate(format.format(12.5d)).isOK());
		assertFalse(rule.validate(format.format(12345.6d)).isOK());
		assertFalse(rule.validate(format.format(-12.3d)).isOK());
		assertFalse(rule.validate(format.format(-12345.6d)).isOK());

		// TODO: more tests for Arab.

	}

	public final void testUnsigned() throws Exception {
		final Locale locale = Locale.US;
		final ValidInteger rule = new ValidInteger(false, locale);
		final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(locale);
		assertTrue(rule.validate(format.format(12)).isOK());
		assertFalse(rule.validate(format.format(-12)).isOK());
	}

	//  TODO: Tests for Macedonia (mk_MK) with surrounding brackets
	//	public void testPrintAvailableLocaleFormats() {
	//		final double number = -1234.56;
	//		for (final Locale locale : NumberFormat.getAvailableLocales()) {
	//			if (locale.getCountry().length() != 0) {
	//				System.out.print(locale.getDisplayName() + "(" + locale + "): ");
	//				final NumberFormat format = DecimalFormat.getInstance(locale);
	//				System.out.print('"' + format.format(number) + '"');
	//				try {
	//					System.out.println(" = [" + format.parse(format.format(number)) + ']');
	//				} catch (ParseException e) {
	//					System.out.println(" = [cannot reformat to Number]");
	//				}
	//			}
	//		}
	//	}

	/**
	 * Tests the method {@code setInitializationData}.
	 * 
	 * @throws Exception
	 */
	public void testSetInitializationData() throws Exception {

		ValidInteger validator = new ValidInteger();
		validator.setInitializationData(null, null, "true");
		assertTrue(validator.validate("2").isOK());
		assertTrue(validator.validate("-2").isOK());

		validator = new ValidInteger();
		validator.setInitializationData(null, null, "false");
		assertTrue(validator.validate("2").isOK());
		assertFalse(validator.validate("-2").isOK());

		validator = new ValidInteger();
		String localString = Locale.GERMANY.getLanguage() + "," + Locale.GERMANY.getCountry();
		validator.setInitializationData(null, null, localString);
		assertTrue(validator.validate("2.000").isOK());
		assertFalse(validator.validate("2,000").isOK());
		assertTrue(validator.validate("-2").isOK());

		validator = new ValidInteger();
		localString = Locale.US.getLanguage() + "," + Locale.US.getCountry();
		validator.setInitializationData(null, null, localString);
		assertFalse(validator.validate("2.000").isOK());
		assertTrue(validator.validate("2,000").isOK());
		assertTrue(validator.validate("-2").isOK());

		validator = new ValidInteger();
		localString = Locale.US.getLanguage() + "," + Locale.US.getCountry();
		validator.setInitializationData(null, null, "false," + localString);
		assertFalse(validator.validate("2.000").isOK());
		assertTrue(validator.validate("2,000").isOK());
		assertFalse(validator.validate("-2").isOK());

	}

}
