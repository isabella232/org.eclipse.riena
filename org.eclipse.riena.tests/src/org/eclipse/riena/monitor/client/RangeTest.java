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
package org.eclipse.riena.monitor.client;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code Range} class.
 */
@NonUITestCase
public class RangeTest extends TestCase {

	public void testEmptyRange() {
		final Range range = new Range("");
		assertFalse(range.matches(1));
		assertFalse(range.matches(2));
		assertFalse(range.matches(3));
	}

	public void testAllRange() {
		final Range range = new Range("*");
		assertTrue(range.matches(-3));
		assertTrue(range.matches(-2));
		assertTrue(range.matches(-1));
		assertTrue(range.matches(0));
		assertTrue(range.matches(1));
		assertTrue(range.matches(2));
		assertTrue(range.matches(3));
	}

	public void testSingleValue() {
		final Range range = new Range("2");
		assertFalse(range.matches(1));
		assertTrue(range.matches(2));
		assertFalse(range.matches(3));
	}

	public void testDoubleValue() {
		final Range range = new Range("2, 9");
		assertFalse(range.matches(1));
		assertTrue(range.matches(2));
		assertFalse(range.matches(3));
		assertTrue(range.matches(9));
	}

	public void testSingleRangeInterval() {
		final Range range = new Range("1..3");
		assertTrue(range.matches(1));
		assertTrue(range.matches(2));
		assertTrue(range.matches(3));
	}

	public void testRangeIntervalPlusValue() {
		final Range range = new Range("1..3,5");
		assertTrue(range.matches(1));
		assertTrue(range.matches(2));
		assertTrue(range.matches(3));
		assertFalse(range.matches(4));
		assertTrue(range.matches(5));
	}

	public void testValuePlusRangeIntervalPlusValue() {
		final Range range = new Range("-2, 1..3 , 5");
		assertTrue(range.matches(-2));
		assertFalse(range.matches(-1));
		assertFalse(range.matches(0));
		assertTrue(range.matches(1));
		assertTrue(range.matches(2));
		assertTrue(range.matches(3));
		assertFalse(range.matches(4));
		assertTrue(range.matches(5));
	}

	public void testSingleRangeOpenOpenPlusRangeCloseClose() {
		final Range range = new Range("-2..0, 10..12");
		assertFalse(range.matches(-3));
		assertTrue(range.matches(-2));
		assertTrue(range.matches(-1));
		assertTrue(range.matches(0));
		assertFalse(range.matches(1));
		assertFalse(range.matches(9));
		assertTrue(range.matches(10));
		assertTrue(range.matches(11));
		assertTrue(range.matches(12));
		assertFalse(range.matches(13));
	}

	public void testSingleValueError() {
		try {
			new Range("n");
			fail();
		} catch (final IllegalArgumentException t) {
			System.out.println("Expected error: " + t);
		}
	}

	public void testIncompleteInterval() {
		try {
			new Range("1..");
			fail();
		} catch (final IllegalArgumentException t) {
			System.out.println("Expected error: " + t);
		}
	}

}
