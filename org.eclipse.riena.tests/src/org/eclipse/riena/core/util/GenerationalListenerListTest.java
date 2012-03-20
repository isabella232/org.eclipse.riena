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
package org.eclipse.riena.core.util;

import java.util.Iterator;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code GenerationalListenerList}
 */
@NonUITestCase
public class GenerationalListenerListTest extends RienaTestCase {

	private final GenerationalListenerList<String> list = new GenerationalListenerList<String>();
	private final static String[] NOTHING = new String[0];

	public void testIsEmpty() {
		assertTrue(list.isEmpty());
	}

	public void testIsEmptyAfterAddAndRemove() {
		list.add("one"); //$NON-NLS-1$
		assertEquals(1, list.size());
		list.remove("one"); //$NON-NLS-1$
		assertTrue(list.isEmpty());
		assertFalse(list.contains("one")); //$NON-NLS-1$
		expect(list.iterator(), NOTHING);
	}

	public void testAddIterateRemoveIterateAdd() {
		list.add("one"); //$NON-NLS-1$
		final Iterator<String> iteratorAfterAdd = list.iterator();
		list.remove("one"); //$NON-NLS-1$
		list.printDebugList("t0"); //$NON-NLS-1$
		list.printList("t0"); //$NON-NLS-1$
		final Iterator<String> iteratorAfterRemove = list.iterator();
		list.add("one"); //$NON-NLS-1$
		list.printDebugList("t1"); //$NON-NLS-1$
		list.printList("t1"); //$NON-NLS-1$
		expect(iteratorAfterAdd, "one"); //$NON-NLS-1$
		expect(iteratorAfterRemove, NOTHING);
		list.printDebugList("t2"); //$NON-NLS-1$
		list.printList("t2"); //$NON-NLS-1$
	}

	public void testAddAddAddIterateRemoveWhileIteratingIterate() {
		list.add("one"); //$NON-NLS-1$
		list.add("two"); //$NON-NLS-1$
		list.add("three"); //$NON-NLS-1$
		final Iterator<String> iteratorAfterAdd = list.iterator();
		boolean once = true;
		for (final String s : list) {
			if (once) {
				once = false;
				list.remove("two"); //$NON-NLS-1$
			}
		}
		expect(iteratorAfterAdd, "one", "two", "three"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		final Iterator<String> iteratorAfterRemove = list.iterator();
		expect(iteratorAfterRemove, "one", "three"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void testGCedList() {
		final GenerationalListenerList<String> gcList = new GenerationalListenerList<String>(1, 0.0f, 1);

		gcList.add("one"); //$NON-NLS-1$
		final Iterator<String> iteratorAfterAdd = gcList.iterator();
		gcList.remove("one"); //$NON-NLS-1$
		gcList.printDebugList("t0"); //$NON-NLS-1$
		gcList.printList("t0"); //$NON-NLS-1$
		final Iterator<String> iteratorAfterRemove = gcList.iterator();
		gcList.add("one"); //$NON-NLS-1$
		gcList.printDebugList("t1"); //$NON-NLS-1$
		gcList.printList("t1"); //$NON-NLS-1$
		expect(iteratorAfterAdd, "one"); //$NON-NLS-1$
		expect(iteratorAfterRemove, NOTHING);
		gcList.printDebugList("t2"); //$NON-NLS-1$
		gcList.printList("t2"); //$NON-NLS-1$
	}

	private void expect(final Iterator<String> iterator, final String... expectations) {
		int i = 0;
		for (final String s : Iter.able(iterator)) {
			assertEquals(expectations[i], s);
			i++;
		}
		assertEquals(expectations.length, i);
	}
}
