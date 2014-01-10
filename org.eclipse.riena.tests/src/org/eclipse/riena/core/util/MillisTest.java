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
package org.eclipse.riena.core.util;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Test the {@code Milli} class.
 */
@NonUITestCase
public class MillisTest extends RienaTestCase {

	public void testSeconds() {
		assertEquals(1000, Millis.seconds(1));
	}

	public void testMinutes() {
		assertEquals(60 * 1000, Millis.minutes(1));
	}

	public void testHours() {
		assertEquals(60 * 60 * 1000, Millis.hours(1));
	}

	public void testDays() {
		assertEquals(24 * 60 * 60 * 1000, Millis.days(1));
	}

	public void testNullString() {
		try {
			Millis.valueOf(null);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
	}

	public void testEmptyMillis() {
		assertEquals(0, Millis.valueOf(""));
	}

	public void testStringMillis() {
		assertEquals(1234, Millis.valueOf("1234 ms"));
	}

	public void testStringMillisMillis() {
		assertEquals(80 + 30, Millis.valueOf("80 ms 30 ms"));
	}

	public void testStringSeconds() {
		assertEquals(3 * 1000, Millis.valueOf("3 s"));
	}

	public void testStringMillisSecondsMillis() {
		assertEquals(80 + 1 * 1000 + 30, Millis.valueOf("80 ms 1 s 30 ms"));
	}

	public void testStringMinutes() {
		assertEquals(4 * 60 * 1000, Millis.valueOf("4 m"));
	}

	public void testStringHours() {
		assertEquals(2 * 60 * 60 * 1000, Millis.valueOf("2 h"));
	}

	public void testStringDays() {
		assertEquals(7 * 24 * 60 * 60 * 1000, Millis.valueOf("7 d"));
	}

	public void testStringWeeks() {
		assertEquals(14 * 24 * 60 * 60 * 1000, Millis.valueOf("2 w"));
	}

	public void testStringMonths() {
		assertEquals(3 * 28 * 24 * 60 * 60 * 1000L, Millis.valueOf("3 M"));
	}

	public void testStringMixed() {
		long expected = 7 * 24 * 60 * 60 * 1000;
		expected += 2 * 60 * 60 * 1000;
		expected += 4 * 60 * 1000;
		expected += 3 * 1000;
		expected += 1234;
		assertEquals(expected, Millis.valueOf("7 d 2 h 4 m 3 s 1234 ms"));
	}
}
