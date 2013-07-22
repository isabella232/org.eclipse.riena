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

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Tests for the ValidCharacters rule.
 */
@NonUITestCase
public class ValidCharactersTest extends RienaTestCase {

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public final void testValidChars() throws Exception {

		final ValidCharacters rule = new ValidCharacters("0123456789.");
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("0").isOK());
		assertTrue(rule.validate("1").isOK());
		assertTrue(rule.validate("23456789.").isOK());

		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertFalse(rule.validate("123A321").isOK());
		assertFalse(rule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertFalse(rule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		}

		final ValidCharacters nullRule = new ValidCharacters(null);
		assertTrue(nullRule.validate(null).isOK());
		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertTrue(nullRule.validate("0").isOK());
		assertTrue(nullRule.validate("1").isOK());
		assertTrue(nullRule.validate("23456789.").isOK());
		assertTrue(nullRule.validate("123A321").isOK());
		assertTrue(nullRule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertTrue(nullRule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());
		try {
			nullRule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			ok("expected a thrown ValidationFailure");
		}

	}

	public final void testNoNullPointerException() throws Exception {
		final ValidCharacters nullRule = new ValidCharacters("");
		nullRule.setAllowedChars(null);
		assertTrue(nullRule.validate("").isOK());
	}

	/**
	 * Tests the method {@code setInitializationData}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testSetInitializationData() throws Exception {

		ValidCharacters validator = new ValidCharacters();
		assertTrue(validator.validate("A").isOK());

		validator = new ValidCharacters();
		validator.setInitializationData(null, null, "a");
		assertTrue(validator.validate("a").isOK());
		assertFalse(validator.validate("A").isOK());

	}

}
