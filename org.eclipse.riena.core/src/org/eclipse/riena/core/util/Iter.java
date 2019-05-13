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

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * The Iter class provides conversions from classes only implementing Iterator
 * or Enumeration such that they can be used within the extended for()-loop
 * (foreach).<br>
 * However, all the <code>able()</code> methods can be used with a {@code null}
 * parameter. Most important, checking for {@code null} is not necessary!<br>
 * <b>Note: </b>All these methods induce a small performance penalty.
 */
public final class Iter {

	private Iter() {
		// locked
	}

	/**
	 * Create an {@code Iterable} for the given iterator.
	 * 
	 * @param <T>
	 *            generic type for the {@code Iterable}
	 * @param iterator
	 *            the iterator (might be null!)
	 * 
	 * @return the {@code Iterable}
	 * @since 4.0
	 */
	public static <T> Iterable<T> able(final Iterator<T> iterator) {
		if (iterator == null) {
			return Collections.emptyList();
		}
		return new IteratorIterable<T>(iterator);
	}

	/**
	 * Create an {@code Iterable} for the given iterator.
	 * 
	 * @param <T>
	 *            generic type for the Iterable
	 * @param enumeration
	 *            the enumeration (might be null!)
	 * 
	 * @return the {@code Iterable}
	 */
	public static <T> Iterable<T> able(final Enumeration<T> enumeration) {
		if (enumeration == null) {
			return Collections.emptyList();
		}
		return new IteratorIterable<T>(new EnumerationIterator<T>(enumeration));
	}

	/**
	 * Create an {@code Iterable} for the given iterable.
	 * 
	 * @param <T>
	 *            generic type for the {@code Iterable}
	 * @param iterable
	 *            the iterable (might be null!)
	 * 
	 * @return the {@code Iterable}
	 */
	public static <T> Iterable<T> able(final Iterable<T> iterable) {
		if (iterable == null) {
			return Collections.emptyList();
		}
		return iterable;
	}

	/**
	 * Create an {@code Iterable} that can be used in foreach loops that
	 * iterates in reverse order of the given list.
	 * 
	 * @param list
	 *            the list that should be iterated over in reverse order
	 * @return the {@code Iterable}
	 */
	public static <T> Iterable<T> ableReverse(final List<T> list) {
		if (list == null) {
			return Collections.emptyList();
		}
		return new ReverseIterable<T>(list.listIterator(list.size()));
	}

	/**
	 * Create an {@code Iterable} for the given array.
	 * 
	 * @param <T>
	 *            generic type for the {@code Iterable}
	 * @param array
	 *            the array (might be null!)
	 * 
	 * @return the {@code Iterable}
	 */
	public static <T> Iterable<T> able(final T... array) {
		if (array == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(array);
	}

	/**
	 * Create an {@code Iterable} for the given iterator. This method´s intended
	 * use is for ´pre generic´ apis.
	 * 
	 * @param <T>
	 *            generic type for the {@code Iterable}
	 * @param iterator
	 *            the iterator (might be null!)
	 * @param t
	 *            the type of the iterator
	 * 
	 * @return the {@code Iterable}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Iterable<T> able(final Iterator iterator, final Class<T> t) {
		return able(iterator);
	}

	/**
	 * Create an {@code Iterable} for the given iterator. This method´s intended
	 * use is for ´pre generic´ apis.
	 * 
	 * @param <T>
	 *            generic type for the {@code Iterable}
	 * @param enumeration
	 *            the enumeration (might be null!)
	 * @param t
	 *            the type of the iterator
	 * 
	 * @return the {@code Iterable}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Iterable<T> able(final Enumeration enumeration, final Class<T> t) {
		return able(enumeration);
	}

	private static final class IteratorIterable<T> implements Iterable<T> {

		private final Iterator<T> iterator;

		private IteratorIterable(final Iterator<T> iterator) {
			this.iterator = iterator;
		}

		public Iterator<T> iterator() {
			return iterator;
		}

	}

	private static final class EnumerationIterator<T> implements Iterator<T> {

		private final Enumeration<T> enumeration;

		private EnumerationIterator(final Enumeration<T> enumeration) {
			this.enumeration = enumeration;
		}

		public boolean hasNext() {
			return enumeration.hasMoreElements();
		}

		public T next() {
			return enumeration.nextElement();
		}

		public void remove() {
			throw new NoSuchMethodError("No remove() on an wrapped Enumeration!"); //$NON-NLS-1$
		}

	}

	private static final class ReverseIterable<T> implements Iterable<T> {

		private Iterator<T> reversed;

		private ReverseIterable(final ListIterator<T> iterator) {
			reversed = new Iterator<T>() {

				public boolean hasNext() {
					return iterator.hasPrevious();
				}

				public T next() {
					return iterator.previous();
				}

				public void remove() {
					iterator.remove();
				}
			};
		}

		public Iterator<T> iterator() {
			return reversed;
		}

	}

}
