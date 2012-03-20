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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the Iter class
 */
@NonUITestCase
public class IterTest extends RienaTestCase {

	/**
	 * Nomen est omen!
	 */
	public void testIteratorTyped() {
		final Scanner scanner = new Scanner("this is a test");
		String result = "";
		for (final String s : Iter.able(scanner)) {
			result = result + s;
		}
		assertEquals("thisisatest", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testIteratorUntyped() {
		final UntypedIteratorReturner utr = new UntypedIteratorReturner(new String[] { "1", "2" });
		String result = "";
		for (final String s : Iter.able(utr.getIterator(), String.class)) {
			result = result + s;
		}
		assertEquals("12", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testNullIterator() {
		String result = "";
		for (final String s : Iter.able((Iterator<String>) null)) {
			result = result + s;
		}
		assertEquals("", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testEnumerationTyped() {
		final Vector<String> v = new Vector<String>();
		v.add("1");
		v.add("2");
		String result = "";
		for (final String o : Iter.able(v.elements())) {
			result = result + o;
		}
		assertEquals("12", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testArray() {
		final String[] strings = new String[] { "a", "b", "c" };
		String result = "";
		for (final String o : Iter.able(strings)) {
			result = result + o;
		}
		assertEquals("abc", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testEmptyArray() {
		final String[] strings = new String[0];
		String result = "";
		for (final String o : Iter.able(strings)) {
			result = result + o;
		}
		assertEquals("", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testNullArray() {
		final String[] strings = null;
		String result = "";
		for (final String o : Iter.able(strings)) {
			result = result + o;
		}
		assertEquals("", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testEnumerationUntyped() {
		final StringTokenizer st = new StringTokenizer("this is a test");
		String result = "";
		for (final String str : Iter.able(st, String.class)) {
			result = result + str;
		}
		assertEquals("thisisatest", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testNullEnumeration() {
		String result = "";
		for (final Object o : Iter.able((Enumeration<Object>) null)) {
			result = result + o;
		}
		assertEquals("", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testCollection() {
		final List<String> strings = new ArrayList<String>();
		strings.add("1");
		strings.add("2");
		String result = "";
		for (final Object o : Iter.able(strings)) {
			result = result + o;
		}
		assertEquals("12", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testNullCollection() {
		final List<String> strings = null;
		String result = "";
		for (final Object o : Iter.able(strings)) {
			result = result + o;
		}
		assertEquals("", result);
	}

	/**
	 * Nomen est omen!
	 */
	public void testAndNowSomethingCompletlyDifferent() throws IOException {
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final Enumeration<URL> enumeration = classLoader.getResources("META-INF/hivemodule.xml");
		for (final URL jarUrl : Iter.able(enumeration)) {
			println(jarUrl.toString());
		}
	}

	/**
	 * Nomen est omen!
	 */
	public void testReverseIterating() {
		final List<Integer> ints = new ArrayList<Integer>();
		ints.add(1);
		ints.add(2);
		ints.add(3);
		int expected = 3;
		for (final Integer i : Iter.ableReverse(ints)) {
			assertEquals(expected--, (int) i);
		}
	}

	/**
	 * Nomen est omen!
	 */
	public void testReverseIteratingWithNull() {
		final List<Integer> ints = null;
		for (final Integer i : Iter.ableReverse(ints)) {
			fail("Unfortunately " + i + " could be reached which should not happen!.");
		}
	}

	private static class UntypedIteratorReturner {
		private final String[] strings;

		/**
		 * @param strings
		 */
		public UntypedIteratorReturner(final String[] strings) {
			this.strings = strings;
		}

		/**
		 * Note: It is intended that the iterator is untyped!!!!
		 * 
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public Iterator getIterator() {
			return new Iterator() {
				private int i = 0;

				public boolean hasNext() {
					return i < strings.length;
				}

				public Object next() {
					if (i == strings.length) {
						throw new NoSuchElementException();
					}
					return strings[i++];
				}

				public void remove() {
				}

			};
		}
	}
}
