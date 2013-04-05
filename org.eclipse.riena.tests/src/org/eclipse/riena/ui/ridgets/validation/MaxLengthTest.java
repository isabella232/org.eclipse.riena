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

import junit.framework.TestCase;

import org.eclipse.core.runtime.IStatus;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests for the {@link MaxLength} rule.
 */
@NonUITestCase
public class MaxLengthTest extends TestCase {

	protected MaxLength createRule() {
		return new MaxLength();
	}

	protected MaxLength createRule(final int length) {
		return new MaxLength(length);
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testLength() {
		final MaxLength rule = createRule(10);

		assertTrue(rule.validate("abcde").isOK());
		assertTrue(rule.validate("abcdeabcde").isOK());

		assertFalse(rule.validate("abcdeabcdefg").isOK());
	}

	public void testIsBlocking() {
		final MaxLength rule = createRule(3);
		final IStatus status = rule.validate("abcd");

		assertFalse(status.isOK());
		assertEquals(ValidationRuleStatus.ERROR_BLOCK_WITH_FLASH, status.getCode());
	}

	/**
	 * Tests the method {@code setInitializationData}.
	 * 
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testSetInitializationData() throws Exception {
		MaxLength rule = createRule();
		assertTrue(rule.validate("").isOK());
		assertFalse(rule.validate("1").isOK());

		rule = createRule();
		rule.setInitializationData(null, null, "5");
		assertTrue(rule.validate("1").isOK());
		assertFalse(rule.validate("123456").isOK());

		rule = createRule();
		rule.setInitializationData(null, null, "6,7");
		assertTrue(rule.validate("123456").isOK());
		assertFalse(rule.validate("1234567").isOK());

	}

}
