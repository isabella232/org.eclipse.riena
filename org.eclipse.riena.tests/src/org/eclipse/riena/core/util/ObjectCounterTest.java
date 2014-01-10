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

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.ObjectCounter;

/**
 * Test the {@code ObjectCounter}.
 */
@NonUITestCase
public class ObjectCounterTest extends TestCase {

	private ObjectCounter<String> strings;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		strings = new ObjectCounter<String>();
	}

	public void testZero() {
		assertEquals(0, strings.getCount("A"));
	}

	public void testOne() {
		assertEquals(0, strings.getCount("A"));
		assertEquals(1, strings.incrementAndGetCount("A"));
		assertEquals(1, strings.getCount("A"));
		assertEquals(0, strings.decrementAndGetCount("A"));
		assertEquals(0, strings.getCount("A"));
	}

	public void testTwo() {
		assertEquals(0, strings.getCount("A"));
		assertEquals(1, strings.incrementAndGetCount("A"));
		assertEquals(1, strings.getCount("A"));
		assertEquals(2, strings.incrementAndGetCount("A"));
		assertEquals(2, strings.getCount("A"));
		assertEquals(1, strings.decrementAndGetCount("A"));
		assertEquals(1, strings.getCount("A"));
		assertEquals(0, strings.decrementAndGetCount("A"));
		assertEquals(0, strings.getCount("A"));
	}

	public void testTwoNoLessThanZero() {
		assertEquals(0, strings.getCount("A"));
		assertEquals(1, strings.incrementAndGetCount("A"));
		assertEquals(1, strings.getCount("A"));
		assertEquals(2, strings.incrementAndGetCount("A"));
		assertEquals(2, strings.getCount("A"));
		assertEquals(1, strings.decrementAndGetCount("A"));
		assertEquals(1, strings.getCount("A"));
		assertEquals(0, strings.decrementAndGetCount("A"));
		assertEquals(0, strings.getCount("A"));
		// removing again does not go below zero
		assertEquals(0, strings.decrementAndGetCount("A"));
		assertEquals(0, strings.getCount("A"));
		// and again
		assertEquals(0, strings.decrementAndGetCount("A"));
		assertEquals(0, strings.getCount("A"));
	}

	public void testIterator() {
		strings.incrementAndGetCount("A");
		strings.incrementAndGetCount("B");
		strings.incrementAndGetCount("C");
		strings.incrementAndGetCount("B");
		strings.incrementAndGetCount("C");
		strings.incrementAndGetCount("C");
		for (final String s : strings) {
			if (s.equals("A")) {
				assertEquals(1, strings.getCount(s));
			} else if (s.equals("B")) {
				assertEquals(2, strings.getCount(s));
			} else if (s.equals("C")) {
				assertEquals(3, strings.getCount(s));
			}

		}
	}

}
