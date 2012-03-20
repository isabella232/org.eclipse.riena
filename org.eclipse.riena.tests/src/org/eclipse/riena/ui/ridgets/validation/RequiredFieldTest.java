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

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests for the RequiredField rule.
 */
@NonUITestCase
public class RequiredFieldTest extends TestCase {

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testDefault() throws Exception {

		final RequiredField rule = new RequiredField();
		assertTrue(rule.validate("a").isOK());
		assertTrue(rule.validate("ab").isOK());
		assertTrue(rule.validate(" ab ").isOK());
		assertTrue(rule.validate("a b ").isOK());
		assertTrue(rule.validate("ab ").isOK());
		assertTrue(rule.validate("$").isOK());
		assertTrue(rule.validate("{ }").isOK());

		assertFalse(rule.validate(null).isOK());
		assertFalse(rule.validate("").isOK());
		assertFalse(rule.validate(" ").isOK());
		assertFalse(rule.validate("  ").isOK());
		assertFalse(rule.validate("\n").isOK());
		assertFalse(rule.validate("\t").isOK());
		assertFalse(rule.validate(" \n").isOK());
		assertFalse(rule.validate("\n ").isOK());
		assertFalse(rule.validate(" \n ").isOK());
		assertFalse(rule.validate(" \n\t ").isOK());
	}

	public void testIgnoreCharacters() {
		final RequiredField rule = new RequiredField();
		// ignore characters are not sorted and contain double characters
		final String ignoreCharacters = "de1z%ec";
		rule.setIgnoreCharacters(ignoreCharacters);

		assertEquals("setter and getter consistent", ignoreCharacters, rule.getIgnoreCharacters());

		assertTrue(rule.validate("a").isOK());
		assertTrue(rule.validate("ab").isOK());
		assertTrue(rule.validate(" ab ").isOK());
		assertTrue(rule.validate("a b ").isOK());
		assertTrue(rule.validate("ab ").isOK());
		assertTrue(rule.validate("$").isOK());
		assertTrue(rule.validate("{ }").isOK());
		assertTrue(rule.validate(" { } ").isOK());

		// combines ignored characters with legal characters
		assertTrue(rule.validate(" {de1z%ec} ").isOK());
		assertTrue(rule.validate(" de1z%eca").isOK());
		assertTrue(rule.validate("de1z%eca ").isOK());
		assertTrue(rule.validate("d e 1 z % e c a ").isOK());
		assertTrue(rule.validate("a d e 1 z % e c ").isOK());

		// must still check for whitespace, even though ignored characters set
		assertFalse(rule.validate(null).isOK());
		assertFalse(rule.validate("").isOK());
		assertFalse(rule.validate(" ").isOK());
		assertFalse(rule.validate("  ").isOK());
		assertFalse(rule.validate("\n").isOK());
		assertFalse(rule.validate("\t").isOK());
		assertFalse(rule.validate(" \n").isOK());
		assertFalse(rule.validate("\n ").isOK());
		assertFalse(rule.validate(" \n ").isOK());
		assertFalse(rule.validate(" \n\t ").isOK());

		// check all ignored characters including whitespace
		assertFalse(rule.validate(ignoreCharacters).isOK());
		assertFalse(rule.validate(" " + ignoreCharacters + " ").isOK());
		assertFalse(rule.validate(" " + ignoreCharacters + "\n").isOK());
		assertFalse(rule.validate("\t" + ignoreCharacters + " ").isOK());

		// check single ignored character with whitespace
		for (int t = 0; t < ignoreCharacters.length() - 1; ++t) {
			final char ignored = ignoreCharacters.charAt(t);
			assertFalse("ignored char: '" + ignored + "'", rule.validate(ignored + "\t\n").isOK());
			assertFalse("ignored char: '" + ignored + "'", rule.validate(ignored + "\t\n" + ignored).isOK());
			assertFalse("ignored char: '" + ignored + "'", rule.validate("\t\n" + ignored).isOK());
			assertFalse("ignored char: '" + ignored + "'", rule.validate("\t " + ignored + " \n").isOK());
		}
	}

	public void testNoNullPointerException() {
		final RequiredField rule = new RequiredField();
		rule.setIgnoreCharacters(null);
		assertTrue(rule.validate("a").isOK());
	}

}
