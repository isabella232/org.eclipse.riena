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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.ui.swt.utils.TestUtils;

/**
 * Tests for the {@link NumericString} class.
 */
@NonUITestCase
public class NumericStringTest extends RienaTestCase {

	public void testCreateNumericString() {
		NumericString ns;

		ns = new NumericString("1234", false);
		assertEqualsNS("1234", ns);

		ns = new NumericString("1234", true);
		assertEqualsNS("1.234", ns);

		try {
			new NumericString(null, false);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	/**
	 * Test deleting a single character with DEL and grouping.
	 */
	public void testDelete() {
		NumericString ns;
		int cursor;

		ns = createNumericString("1.234,", true);
		cursor = ns.delete(1, 2, (char) 127);
		assertEqualsNS("134,", ns);
		assertEquals(1, cursor);

		ns = createNumericString("1.234,", true);
		cursor = ns.delete(0, 1, (char) 127);
		assertEqualsNS("234,", ns);
		assertEquals(0, cursor);

		ns = createNumericString("12.345,", true);
		cursor = ns.delete(2, 3, (char) 127);
		assertEqualsNS("1.245,", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1.234.567,", true);
		cursor = ns.delete(5, 6, (char) 127);
		assertEqualsNS("123.467,", ns);
		assertEquals(5, cursor);

		ns = createNumericString("1.234.567,", true);
		cursor = ns.delete(7, 8, (char) 127);
		assertEqualsNS("123.457,", ns);
		assertEquals(cursor, 6);

		ns = createNumericString("-1.234,", true);
		cursor = ns.delete(2, 3, (char) 127);
		assertEqualsNS("-134,", ns);
		assertEquals(cursor, 2);

		ns = createNumericString("-1.234,", true);
		cursor = ns.delete(1, 2, (char) 127);
		assertEqualsNS("-234,", ns);
		assertEquals(1, cursor);

		ns = createNumericString("-1.234.567,", true);
		cursor = ns.delete(8, 9, (char) 127);
		assertEqualsNS("-123.457,", ns);
		assertEquals(7, cursor);

		ns = createNumericString("1.234,5678", true);
		cursor = ns.delete(7, 8, (char) 127);
		assertEqualsNS("1.234,578", ns);
		assertEquals(7, cursor);

		ns = createNumericString("1.234,5678", true);
		cursor = ns.delete(5, 6, (char) 127);
		assertEqualsNS("1.234,678", ns);
		assertEquals(6, cursor);

		ns = createNumericString("1.234,", true);
		cursor = ns.delete(5, 6, (char) 127);
		assertEqualsNS("1.234,", ns);
		assertEquals(6, cursor);

		ns = createNumericString(",123", true);
		cursor = ns.delete(0, 1, (char) 127);
		assertEqualsNS(",23", ns);
		assertEquals(1, cursor);
	}

	/**
	 * Test deleting a single character with DEL and no grouping.
	 */
	public void testDeleteNoGrouping() {
		NumericString ns;
		int cursor;

		ns = createNumericString("1234,", false);
		cursor = ns.delete(1, 2, (char) 127);
		assertEqualsNS("134,", ns);
		assertEquals(1, cursor);

		ns = createNumericString("1234,", false);
		cursor = ns.delete(0, 1, (char) 127);
		assertEqualsNS("234,", ns);
		assertEquals(0, cursor);

		ns = createNumericString("12345,", false);
		cursor = ns.delete(2, 3, (char) 127);
		assertEqualsNS("1245,", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1234567,", false);
		cursor = ns.delete(4, 5, (char) 127);
		assertEqualsNS("123467,", ns);
		assertEquals(4, cursor);

		ns = createNumericString("1234567,", false);
		cursor = ns.delete(5, 6, (char) 127);
		assertEqualsNS("123457,", ns);
		assertEquals(cursor, 5);

		ns = createNumericString("-1234,", false);
		cursor = ns.delete(2, 3, (char) 127);
		assertEqualsNS("-134,", ns);
		assertEquals(cursor, 2);

		ns = createNumericString("-1234,", false);
		cursor = ns.delete(1, 2, (char) 127);
		assertEqualsNS("-234,", ns);
		assertEquals(1, cursor);

		ns = createNumericString("-1234567,", false);
		cursor = ns.delete(6, 7, (char) 127);
		assertEqualsNS("-123457,", ns);
		assertEquals(6, cursor);

		ns = createNumericString("1234,5678", false);
		cursor = ns.delete(6, 7, (char) 127);
		assertEqualsNS("1234,578", ns);
		assertEquals(6, cursor);

		ns = createNumericString("1234,", false);
		cursor = ns.delete(4, 5, (char) 127);
		assertEqualsNS("1234,", ns);
		assertEquals(5, cursor);

		ns = createNumericString(",123", false);
		cursor = ns.delete(0, 1, (char) 127);
		assertEqualsNS(",23", ns);
		assertEquals(1, cursor);
	}

	/**
	 * Test deleting a single character with backspace and grouping.
	 */
	public void testBackspace() {
		NumericString ns;
		int cursor;

		ns = createNumericString("123.456", true);
		cursor = ns.delete(3, 4, '\b');
		assertEqualsNS("12.456", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1.456", true);
		cursor = ns.delete(1, 2, '\b');
		assertEqualsNS("456", ns);
		assertEquals(0, cursor);

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(5, 6, '\b');
		assertEqualsNS("123.567", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1.234", true);
		cursor = ns.delete(3, 4, '\b');
		assertEqualsNS("124", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(7, 8, '\b');
		assertEqualsNS("123.457", ns);
		assertEquals(6, cursor);

		ns = createNumericString("-1.234", true);
		cursor = ns.delete(4, 5, '\b');
		assertEqualsNS("-124", ns);
		assertEquals(3, cursor);

		ns = createNumericString("-1.234", true);
		cursor = ns.delete(1, 2, '\b');
		assertEqualsNS("-234", ns);
		assertEquals(1, cursor);

		ns = createNumericString("-1.234.567", true);
		cursor = ns.delete(8, 9, '\b');
		assertEqualsNS("-123.457", ns);
		assertEquals(7, cursor);

		ns = createNumericString("1.234,5678", true);
		cursor = ns.delete(7, 8, '\b');
		assertEqualsNS("1.234,578", ns);
		assertEquals(7, cursor);

		ns = createNumericString("1.234,", true);
		cursor = ns.delete(5, 6, '\b');
		assertEqualsNS("123,", ns);
		assertEquals(3, cursor);

		ns = createNumericString(",123", true);
		cursor = ns.delete(0, 1, '\b');
		assertEqualsNS(",123", ns);
		assertEquals(0, cursor);
	}

	/**
	 * Test deleting a single character with DEL and no grouping.
	 */
	public void testBackspaceNoGrouping() {
		NumericString ns;
		int cursor;

		ns = createNumericString("123456", false);
		cursor = ns.delete(2, 3, '\b');
		assertEqualsNS("12456", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1456", false);
		cursor = ns.delete(0, 1, '\b');
		assertEqualsNS("456", ns);
		assertEquals(0, cursor);

		ns = createNumericString("1234567", false);
		cursor = ns.delete(3, 4, '\b');
		assertEqualsNS("123567", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1234", false);
		cursor = ns.delete(2, 3, '\b');
		assertEqualsNS("124", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1234567", false);
		cursor = ns.delete(5, 6, '\b');
		assertEqualsNS("123457", ns);
		assertEquals(5, cursor);

		ns = createNumericString("-1234", false);
		cursor = ns.delete(3, 4, '\b');
		assertEqualsNS("-124", ns);
		assertEquals(3, cursor);

		ns = createNumericString("-1234", false);
		cursor = ns.delete(1, 2, '\b');
		assertEqualsNS("-234", ns);
		assertEquals(1, cursor);

		ns = createNumericString("-1234567", false);
		cursor = ns.delete(6, 7, '\b');
		assertEqualsNS("-123457", ns);
		assertEquals(6, cursor);

		ns = createNumericString("1234,5678", false);
		cursor = ns.delete(6, 7, '\b');
		assertEqualsNS("1234,578", ns);
		assertEquals(6, cursor);

		ns = createNumericString("1234,", false);
		cursor = ns.delete(4, 5, '\b');
		assertEqualsNS("123,", ns);
		assertEquals(3, cursor);

		ns = createNumericString(",123", false);
		cursor = ns.delete(0, 1, '\b');
		assertEqualsNS(",123", ns);
		assertEquals(0, cursor);
	}

	/**
	 * Test deleting a sequence of characters with DEL and grouping.
	 */
	public void testDeleteSequence() {
		NumericString ns;
		int cursor;

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(5, 7, (char) 127);
		assertEqualsNS("123.467", ns);
		assertEquals(5, cursor);

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(4, 6, (char) 127);
		assertEqualsNS("123.567", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(4, 7, (char) 127);
		assertEqualsNS("12.367", ns);
		assertEquals(4, cursor);

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(2, 5, (char) 127);
		assertEqualsNS("1.567", ns);
		assertEquals(1, cursor);

		ns = createNumericString("12.345,67", true);
		cursor = ns.delete(4, 6, (char) 127);
		assertEqualsNS("123,67", ns);
		assertEquals(3, cursor);

		ns = createNumericString("12.345,67", true);
		cursor = ns.delete(3, 6, (char) 127);
		assertEqualsNS("12,67", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1.234,567", true);
		cursor = ns.delete(5, 7, (char) 127);
		assertEqualsNS("1.234,67", ns);
		assertEquals(6, cursor);

		ns = createNumericString("1.234,567", true);
		cursor = ns.delete(4, 6, (char) 127);
		assertEqualsNS("123,567", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1.234,567", true);
		cursor = ns.delete(4, 7, (char) 127);
		assertEqualsNS("123,67", ns);
		assertEquals(4, cursor);

		ns = createNumericString("1.234,567", true);
		cursor = ns.delete(2, 9, (char) 127);
		assertEqualsNS("1,", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1.234,567", true);
		cursor = ns.delete(0, 9, (char) 127);
		assertEqualsNS(",", ns);
		assertEquals(0, cursor);

		ns = createNumericString("1.234", true);
		cursor = ns.delete(0, 5, (char) 127);
		assertEqualsNS("", ns);
		assertEquals(0, cursor);

		ns = createNumericString(",", true);
		cursor = ns.delete(0, 1, (char) 127);
		assertEqualsNS(",", ns);
		assertEquals(1, cursor);
	}

	/**
	 * Test deleting a sequence of characters with DEL and no grouping.
	 */
	public void testDeleteSequenceNoGrouping() {
		NumericString ns;
		int cursor;

		ns = createNumericString("1234567", false);
		cursor = ns.delete(3, 5, (char) 127);
		assertEqualsNS("12367", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1234567", false);
		cursor = ns.delete(1, 4, (char) 127);
		assertEqualsNS("1567", ns);
		assertEquals(1, cursor);

		ns = createNumericString("12345,67", false);
		cursor = ns.delete(3, 5, (char) 127);
		assertEqualsNS("123,67", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1234,567", false);
		cursor = ns.delete(4, 6, (char) 127);
		assertEqualsNS("1234,67", ns);
		assertEquals(5, cursor);

		ns = createNumericString("1234,567", false);
		cursor = ns.delete(3, 5, (char) 127);
		assertEqualsNS("123,567", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1234,567", false);
		cursor = ns.delete(3, 6, (char) 127);
		assertEqualsNS("123,67", ns);
		assertEquals(4, cursor);

		ns = createNumericString("1234,567", false);
		cursor = ns.delete(1, 8, (char) 127);
		assertEqualsNS("1,", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1234,567", false);
		cursor = ns.delete(0, 8, (char) 127);
		assertEqualsNS(",", ns);
		assertEquals(0, cursor);

		ns = createNumericString("1234", false);
		cursor = ns.delete(0, 4, (char) 127);
		assertEqualsNS("", ns);
		assertEquals(0, cursor);

		ns = createNumericString(",", false);
		cursor = ns.delete(0, 1, (char) 127);
		assertEqualsNS(",", ns);
		assertEquals(1, cursor);
	}

	/**
	 * Test deleting a sequence of characters with backspace and grouping.
	 */
	public void testBackspaceSequence() {
		NumericString ns;
		int cursor;

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(5, 7, '\b');
		assertEqualsNS("123.467", ns);
		assertEquals(5, cursor);

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(4, 6, '\b');
		assertEqualsNS("123.567", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(4, 7, '\b');
		assertEqualsNS("12.367", ns);
		assertEquals(4, cursor);

		ns = createNumericString("1.234.567", true);
		cursor = ns.delete(2, 5, '\b');
		assertEqualsNS("1.567", ns);
		assertEquals(1, cursor);

		ns = createNumericString("12.345,67", true);
		cursor = ns.delete(4, 6, '\b');
		assertEqualsNS("123,67", ns);
		assertEquals(3, cursor);

		ns = createNumericString("12.345,67", true);
		cursor = ns.delete(3, 6, '\b');
		assertEqualsNS("12,67", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1.234,567", true);
		cursor = ns.delete(5, 7, '\b');
		assertEqualsNS("1.234,67", ns);
		assertEquals(6, cursor);

		ns = createNumericString("1.234,567", true);
		cursor = ns.delete(4, 6, '\b');
		assertEqualsNS("123,567", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1.234,567", true);
		cursor = ns.delete(4, 7, '\b');
		assertEqualsNS("123,67", ns);
		assertEquals(4, cursor);

		ns = createNumericString("1.234,567", true);
		cursor = ns.delete(2, 9, '\b');
		assertEqualsNS("1,", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1.234,567", true);
		cursor = ns.delete(0, 9, '\b');
		assertEqualsNS(",", ns);
		assertEquals(0, cursor);

		ns = createNumericString("1.234", true);
		cursor = ns.delete(0, 5, '\b');
		assertEqualsNS("", ns);
		assertEquals(0, cursor);

		ns = createNumericString(",", true);
		cursor = ns.delete(0, 1, '\b');
		assertEqualsNS(",", ns);
		assertEquals(0, cursor);
	}

	/**
	 * Test deleting a sequence of characters with backspace and no grouping.
	 */
	public void testBackspaceSequenceNoGrouping() {
		NumericString ns;
		int cursor;

		ns = createNumericString("1234567", false);
		cursor = ns.delete(3, 5, '\b');
		assertEqualsNS("12367", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1234567", false);
		cursor = ns.delete(1, 4, '\b');
		assertEqualsNS("1567", ns);
		assertEquals(1, cursor);

		ns = createNumericString("12345,67", false);
		cursor = ns.delete(3, 5, '\b');
		assertEqualsNS("123,67", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1234,567", false);
		cursor = ns.delete(4, 6, '\b');
		assertEqualsNS("1234,67", ns);
		assertEquals(5, cursor);

		ns = createNumericString("1234,567", false);
		cursor = ns.delete(3, 5, '\b');
		assertEqualsNS("123,567", ns);
		assertEquals(3, cursor);

		ns = createNumericString("1234,567", false);
		cursor = ns.delete(3, 6, '\b');
		assertEqualsNS("123,67", ns);
		assertEquals(4, cursor);

		ns = createNumericString("1234,567", false);
		cursor = ns.delete(1, 8, '\b');
		assertEqualsNS("1,", ns);
		assertEquals(2, cursor);

		ns = createNumericString("1234,567", false);
		cursor = ns.delete(0, 8, '\b');
		assertEqualsNS(",", ns);
		assertEquals(0, cursor);

		ns = createNumericString("1234", false);
		cursor = ns.delete(0, 4, '\b');
		assertEqualsNS("", ns);
		assertEquals(0, cursor);

		ns = createNumericString(",", false);
		cursor = ns.delete(0, 1, '\b');
		assertEqualsNS(",", ns);
		assertEquals(0, cursor);
	}

	// helping methods
	//////////////////

	private void assertEqualsNS(final String value, final NumericString ns) {
		assertEquals(TestUtils.getLocalizedNumber(value), ns.toString());
	}

	private NumericString createNumericString(final String value, final boolean withGrouping) {
		return new NumericString(TestUtils.getLocalizedNumber(value), withGrouping);
	}
}
