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
package org.eclipse.riena.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Helper for creating literal maps, lists and sets.
 */
public final class Literal {

	private Literal() {
		// Utility class
	}

	/**
	 * Start creating a literal {@code Map}.
	 * 
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> LMap<K, V> map(final K key, final V value) {
		return new LMap<K, V>().map(key, value);
	}

	public static class LMap<K, V> extends HashMap<K, V> {

		private static final long serialVersionUID = 4104427953868010622L;

		/**
		 * Add the given key/value pair to this map.
		 * 
		 * @param key
		 * @param value
		 * @return
		 */
		public LMap<K, V> map(final K key, final V value) {
			put(key, value);
			return this;
		}
	}

	/**
	 * Start creating a literal {@code List}.
	 * 
	 * @param <E>
	 * @return
	 */
	public static <E> LList<E> list(final E element) {
		return new LList<E>().list(element);
	}

	/**
	 *
	 */
	public static class LList<E> extends ArrayList<E> {
		private static final long serialVersionUID = 8392251745423189748L;

		public LList<E> list(final E element) {
			add(element);
			return this;
		}
	}

	/**
	 * Start creating a literal {@code Set}.
	 * 
	 * @param <E>
	 * @return
	 */
	public static <E> LSet<E> set(final E element) {
		return new LSet<E>().set(element);
	}

	/**
	 *
	 */
	public static class LSet<E> extends HashSet<E> {

		private static final long serialVersionUID = 1829000701379306982L;

		public LSet<E> set(final E element) {
			add(element);
			return this;
		}
	}

}
