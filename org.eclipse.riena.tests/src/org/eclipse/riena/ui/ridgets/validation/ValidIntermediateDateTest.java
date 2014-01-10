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

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;

/**
 * Tests for the ValidIntermediateDate rule.
 */
@NonUITestCase
public class ValidIntermediateDateTest extends TestCase {

	/**
	 * Test for problem report #484.
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testFirstMillenniumPartiallyInvalid() throws Exception {

		final ValidIntermediateDate rule = new ValidIntermediateDate("dd.MM.yyyy");

		assertTrue(rule.validate("01.01.").isOK());
		assertTrue(rule.validate("01.01.0").isOK());
		assertTrue(rule.validate("01.01.01").isOK());
		assertFalse(rule.validate("01.01.010").isOK());
		assertFalse(rule.validate("01.01.0101").isOK());

		assertTrue(rule.validate("02.02.").isOK());
		assertFalse(rule.validate("02.02.2").isOK());
		assertTrue(rule.validate("02.02.22").isOK());
		assertFalse(rule.validate("02.02.022").isOK());
		assertTrue(rule.validate("02.02.222").isOK());
		assertFalse(rule.validate("02.02.0002").isOK());
		assertTrue(rule.validate("02.02.2222").isOK());
	}

	/**
	 * Tests dates of format IDateTextFieldAdapter.FORMAT_DDMMYYYY
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testDatesDDMMYYYY() throws Exception {

		final ValidIntermediateDate rule = new ValidIntermediateDate(IDateTextRidget.FORMAT_DDMMYYYY);

		assertTrue(rule.validate("").isOK());
		assertTrue(rule.validate("..").isOK());
		assertTrue(rule.validate("11..").isOK());
		assertTrue(rule.validate("11.1.").isOK());
		assertFalse(rule.validate("11.1.1").isOK());
		assertTrue(rule.validate("11.1.11").isOK());
		assertFalse(rule.validate("55.1.11").isOK());
	}

	/**
	 * Tests dates of format IDateTextFieldAdapter.FORMAT_DDMMYYYYHHMM
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testDatesDDMMYYYYHHMM() throws Exception {

		final ValidIntermediateDate rule = new ValidIntermediateDate(IDateTextRidget.FORMAT_DDMMYYYYHHMM);

		assertFalse(rule.validate("11.1.1 11:11").isOK());
		assertTrue(rule.validate("11.1.11 11:11").isOK());
		assertTrue(rule.validate("11.01.01 :").isOK());
		assertTrue(rule.validate("11.1.11 :").isOK());
		assertFalse(rule.validate("11.1.11 55:11").isOK());
	}

	/**
	 * Tests dates of format IDateTextFieldAdapter.FORMAT_HHMM
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testDatesHHMM() throws Exception {

		final ValidIntermediateDate rule = new ValidIntermediateDate(IDateTextRidget.FORMAT_HHMM);

		assertTrue(rule.validate("11:11").isOK());
		assertTrue(rule.validate("11:00").isOK());
		assertTrue(rule.validate("11:0").isOK());
		assertTrue(rule.validate("11:").isOK());
		assertFalse(rule.validate("55:11").isOK());
	}

	/**
	 * Test for problem report #601.
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void test601() throws Exception {

		final ValidIntermediateDate rule = new ValidIntermediateDate("HH:mm");

		assertTrue(rule.validate("11:").isOK());
		assertFalse(rule.validate("55:").isOK());
	}

	/**
	 * Test for problem report #651.
	 * 
	 * @throws Exception
	 *             handled by JUnit.
	 */
	public void testFebruary29th() throws Exception {
		ValidIntermediateDate rule = new ValidIntermediateDate("dd.MM");

		assertTrue(rule.validate("28.02").isOK());
		assertTrue(rule.validate("29.02").isOK());
		assertFalse(rule.validate("30.02").isOK());

		rule = new ValidIntermediateDate("MM/dd");

		assertTrue(rule.validate("02/28").isOK());
		assertTrue(rule.validate("02/29").isOK());
		assertFalse(rule.validate("02/30").isOK());

		rule = new ValidIntermediateDate("dd.MM.yyyy");

		assertTrue(rule.validate("28.02.2007").isOK());
		assertFalse(rule.validate("29.02.2007").isOK());
		assertFalse(rule.validate("30.02.2007").isOK());
	}

	public void testSetInitializationData() throws Exception {

		ValidIntermediateDate validator = new ValidIntermediateDate();
		validator.setInitializationData(null, null, "dd.MM");
		assertTrue(validator.validate("28.03").isOK());
		assertFalse(validator.validate("32.03").isOK());

		validator = new ValidIntermediateDate();
		validator.setInitializationData(null, null, "dd.MM.yy");
		assertFalse(validator.validate("28.03").isOK());
		assertTrue(validator.validate("28.03.00").isOK());
		assertFalse(validator.validate("32.03.00").isOK());

		validator = new ValidIntermediateDate();
		validator.setInitializationData(null, null, "dd.MM.yyyy");
		assertFalse(validator.validate("28.03").isOK());
		assertTrue(validator.validate("28.03.2000").isOK());
		assertFalse(validator.validate("28.03.00").isOK());
		assertFalse(validator.validate("32.03.2000").isOK());

	}

}
