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
package org.eclipse.riena.core.cache;

import java.util.Map;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Tests the LRUHashMap class.
 * 
 */
@NonUITestCase
public class LRUHashMapTest extends RienaTestCase {

	private final Map<String, Integer> lru = LRUHashMap.createLRUHashMap(3);

	public void testInstantiate() {
		assertEquals(0, lru.size());
	}

	public void testToTheLimit() {
		put(1, 2, 3);
		assertContains(1, 2, 3);
		assertEquals(3, lru.size());
		put(4);
		assertEquals(3, lru.size());
		assertContains(2, 3, 4);
	}

	public void testOverTheLimitAndTouchInTheMiddle() {
		put(1, 2, 3);
		assertContains(1, 2, 3);
		assertEquals(3, lru.size());
		put(4);
		assertEquals(3, lru.size());
		assertContains(2, 3, 4);

		assertContains(2);
		put(5);
		assertContains(2, 4, 5);
	}

	private void put(final int... kvs) {
		for (final int i : kvs) {
			lru.put(Integer.toString(i), i);
		}
	}

	private void assertContains(final int... kvs) {
		for (final int i : kvs) {
			assertEquals((Integer) i, lru.get(Integer.toString(i)));
		}
	}
}