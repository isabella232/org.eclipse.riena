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

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Tests for the ValidDate rule.
 */
@NonUITestCase
public class ValidDateTest extends RienaTestCase {

	/**
	 * Checks for validity of an input string against the date format DDMMYYYY
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public final void testDateDDMMYYYYFormat() throws Exception {

		final ValidDate rule = new ValidDate("dd.MM.yyyy");

		assertTrue(rule.validate("12.12.2004").isOK());
		assertTrue(rule.validate("30.09.1999").isOK());
		assertFalse(rule.validate("9.12.2004").isOK());
		assertFalse(rule.validate("44.12.2004").isOK());
		assertFalse(rule.validate("12.13.2004").isOK());
		assertFalse(rule.validate("12.2004").isOK());
	}

	/**
	 * Checks for validity of an input string against the date format MMYYYY
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public final void testDateMMYYYYFormat() throws Exception {

		final ValidDate rule = new ValidDate("MM.yyyy");

		assertTrue(rule.validate("12.2004").isOK());
		assertTrue(rule.validate("09.1999").isOK());
		assertFalse(rule.validate("13.2004").isOK());
		assertFalse(rule.validate("2004").isOK());
	}

	/**
	 * Checks for validity of an input string against the date format YYYY
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public final void testDateYYYYFormat() throws Exception {

		final ValidDate rule = new ValidDate("yyyy");

		assertTrue(rule.validate("2004").isOK());

		assertFalse(rule.validate("12.2004").isOK());
		assertFalse(rule.validate("004").isOK());
	}

	/**
	 * Test for problem report #651.
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testFebruary29th() throws Exception {
		ValidDate rule = new ValidDate("dd.MM");

		assertTrue(rule.validate("28.02").isOK());
		assertTrue(rule.validate("29.02").isOK());
		assertFalse(rule.validate("30.02").isOK());

		rule = new ValidDate("MM/dd");

		assertTrue(rule.validate("02/28").isOK());
		assertTrue(rule.validate("02/29").isOK());
		assertFalse(rule.validate("02/30").isOK());

		rule = new ValidDate("dd.MM.yyyy");

		assertTrue(rule.validate("28.02.2007").isOK());
		assertFalse(rule.validate("29.02.2007").isOK());
		assertFalse(rule.validate("30.02.2007").isOK());
	}

	public void testException() throws Exception {
		try {
			final ValidDate rule = new ValidDate("dd.MM");
			rule.validate(new Object());
			fail("expected thrown ValidationFailure.");
		} catch (final ValidationFailure e) {
			ok("expected thrown ValidationFailure.");
		} catch (final RuntimeException e) {
			fail("expected ValidationFailure instead of " + e.getClass().getName());
		}
	}

	public void testSetInitializationData() throws Exception {

		ValidDate validator = new ValidDate();
		validator.setInitializationData(null, null, "dd.MM");
		assertTrue(validator.validate("28.03").isOK());
		assertFalse(validator.validate("28.03.2000").isOK());
		assertFalse(validator.validate("32.03").isOK());

		validator = new ValidDate();
		validator.setInitializationData(null, null, "dd.MM.yy");
		assertFalse(validator.validate("28.03").isOK());
		assertFalse(validator.validate("28.03.2000").isOK());
		assertTrue(validator.validate("28.03.00").isOK());
		assertFalse(validator.validate("32.03.00").isOK());

		validator = new ValidDate();
		validator.setInitializationData(null, null, "dd.MM.yyyy");
		assertFalse(validator.validate("28.03").isOK());
		assertTrue(validator.validate("28.03.2000").isOK());
		assertFalse(validator.validate("28.03.00").isOK());
		assertFalse(validator.validate("32.03.2000").isOK());

	}

}
