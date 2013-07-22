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
import org.eclipse.riena.ui.ridgets.IDateTextRidget;

/**
 * Tests for the {@link SegmentedString} class.
 */
@NonUITestCase
public class SegmentedStringTest extends RienaTestCase {

	public void testCreateSegmentedString() {
		SegmentedString ss;

		ss = new SegmentedString(IDateTextRidget.FORMAT_DDMMYYYY);
		assertEquals("dd|dd|dddd", ss.getPattern());
		assertEquals("  .  .    ", ss.toString());

		ss = new SegmentedString("MM/dd/yyyy");
		assertEquals("dd|dd|dddd", ss.getPattern());
		assertEquals("  /  /    ", ss.toString());

		ss = new SegmentedString(IDateTextRidget.FORMAT_HHMM);
		assertEquals("dd|dd", ss.getPattern());
		assertEquals("  :  ", ss.toString());

		ss = new SegmentedString(IDateTextRidget.FORMAT_DDMMYYYYHHMM);
		assertEquals("dd|dd|dddd|dd|dd", ss.getPattern());
		assertEquals("  .  .       :  ", ss.toString());

		try {
			new SegmentedString("ddabcMM");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	public void testInsert() {
		SegmentedString ss;
		final String pat = IDateTextRidget.FORMAT_DDMMYYYY;

		ss = new SegmentedString(pat, "10.10. 200");
		ss.insert(10, "8");
		assertEquals("10.10.2008", ss.toString());

		ss = new SegmentedString(pat, "10.10. 200");
		ss.insert(9, "1");
		assertEquals("10.10.2010", ss.toString());

		ss = new SegmentedString(pat, "10.10. 200");
		ss.insert(8, "1");
		assertEquals("10.10.2100", ss.toString());

		ss = new SegmentedString(pat, "10.10. 200");
		ss.insert(7, "1");
		assertEquals("10.10.1200", ss.toString());

		ss = new SegmentedString(pat, "10.10. 200");
		ss.insert(6, "1");
		assertEquals("10.10.1200", ss.toString());

		ss = new SegmentedString(pat, "10.10.2008");
		ss.insert(5, "1");
		assertEquals("10.10.2008", ss.toString());

		ss = new SegmentedString(pat, "10. 1.2008");
		ss.insert(5, "1");
		assertEquals("10.11.2008", ss.toString());

		ss = new SegmentedString(pat, "10. 1.2008");
		ss.insert(4, "2");
		assertEquals("10.21.2008", ss.toString());

		ss = new SegmentedString(pat, "10. 1. 008");
		ss.insert(5, "23");
		assertEquals("10.12.3008", ss.toString());

		// ---

		ss = new SegmentedString(pat, "  .  .    ");
		ss.insert(1, "01102008");
		assertEquals("01.10.2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .    ");
		ss.insert(1, "01.10.2008");
		assertEquals("01.10.2008", ss.toString());

		ss = new SegmentedString(pat, "  .10.2008");
		ss.insert(1, "1208");
		assertEquals("12.10.2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .2008");
		ss.insert(1, "1208");
		assertEquals("12.08.2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .2008");
		ss.insert(4, "1208");
		assertEquals("  .12.2008", ss.toString());

		ss = new SegmentedString(pat, "01.  .2008");
		ss.insert(6, "10");
		assertEquals("01.  .2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .    ");
		ss.insert(6, "2008");
		assertEquals("  .  .2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .    ");
		ss.insert(3, "102008");
		assertEquals("  .10.2008", ss.toString());

		ss = new SegmentedString(pat, "12. 1.2008");
		ss.insert(5, "0");
		assertEquals("12.10.2008", ss.toString());

		ss = new SegmentedString(pat, " 1.10.2008");
		ss.insert(2, "2");
		assertEquals("12.10.2008", ss.toString());
	}

	public void testDelete() {
		final String pat = IDateTextRidget.FORMAT_DDMMYYYY;
		SegmentedString ss;
		int cursor;

		ss = new SegmentedString(pat, "01.10.2008");
		cursor = ss.delete(5, 5);
		assertEquals("01.10.2008", ss.toString());
		assertEquals(5, cursor);

		ss = new SegmentedString(pat, "01.10.2008");
		cursor = ss.delete(5, 6);
		assertEquals("01.10. 008", ss.toString());
		assertEquals(5, cursor);

		ss = new SegmentedString(pat, "01.10.2008");
		cursor = ss.delete(4, 5);
		assertEquals("01. 1.2008", ss.toString());
		assertEquals(5, cursor);

		ss = new SegmentedString(pat, "01.10.2008");
		cursor = ss.delete(4, 6);
		assertEquals("01. 1. 008", ss.toString());
		assertEquals(5, cursor);

		ss = new SegmentedString(pat, "01.10.2008");
		cursor = ss.delete(0, 9);
		assertEquals("  .  .    ", ss.toString());
		assertEquals(2, cursor);

		ss = new SegmentedString(pat, "01.10.2008");
		cursor = ss.delete(0, 6);
		assertEquals("  .  . 008", ss.toString());
		assertEquals(2, cursor);

		ss = new SegmentedString(pat, "12.10.2008");
		cursor = ss.delete(0, 0);
		assertEquals(" 2.10.2008", ss.toString());
		assertEquals(1, cursor);

		ss = new SegmentedString(pat, "12.10.2008");
		cursor = ss.delete(1, 1);
		assertEquals(" 1.10.2008", ss.toString());
		assertEquals(2, cursor);

		ss = new SegmentedString(pat, "12.10.2008");
		cursor = ss.delete(3, 3);
		assertEquals("12. 0.2008", ss.toString());
		assertEquals(4, cursor);

		ss = new SegmentedString(pat, "12.10.2008");
		cursor = ss.delete(4, 4);
		assertEquals("12. 1.2008", ss.toString());
		assertEquals(5, cursor);

		ss = new SegmentedString(pat, "12.10.2008");
		cursor = ss.delete(7, 8);
		assertEquals("12.10.  28", ss.toString());
		assertEquals(9, cursor);
	}

	public void testReplace() {
		SegmentedString ss;
		final String pat = IDateTextRidget.FORMAT_DDMMYYYY;

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(5, 5, "1");
		assertEquals("01.10.2008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(5, 6, "1");
		assertEquals("01.10.1008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(4, 5, "1");
		assertEquals("01.11.2008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(4, 6, "1");
		assertEquals("01.11. 008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(0, 6, "3");
		assertEquals(" 3.  . 008", ss.toString());

		ss = new SegmentedString(pat, "01.10.2008");
		ss.replace(0, 9, "3");
		assertEquals(" 3.  .    ", ss.toString());
	}

	public void testShiftSpacesLeft() {
		SegmentedString ss;
		final String pat = IDateTextRidget.FORMAT_DDMMYYYY;

		ss = new SegmentedString(pat, "01.10.200 ");
		ss.shiftSpacesLeft(-1);
		assertEquals("01.10. 200", ss.toString());

		ss = new SegmentedString(pat, "01.1 .2008");
		ss.shiftSpacesLeft(-1);
		assertEquals("01. 1.2008", ss.toString());

		ss = new SegmentedString(pat, "0 .10.2008");
		ss.shiftSpacesLeft(-1);
		assertEquals(" 0.10.2008", ss.toString());

		ss = new SegmentedString(pat, "  .  .    ");
		ss.shiftSpacesLeft(-1);
		assertEquals("  .  .    ", ss.toString());
	}
}
