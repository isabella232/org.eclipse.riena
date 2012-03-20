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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The {@code ObjectCounter} simply counts equal objects.
 * <p>
 * The objects that can be counted have the same constraints as objects that get
 * stored in {@code Set}s and {@code Map}s.
 * <p>
 * This implementation is thread safe.
 * 
 * @since 3.0
 */
public class ObjectCounter<T> implements Iterable<T> {

	private final Map<T, Integer> objects = new HashMap<T, Integer>();

	/**
	 * Increment the count for {@code object} and return the number of equal
	 * objects (including the current) that had been incremented.
	 * 
	 * @param object
	 *            object to add
	 * @return count of already added equal objects
	 */
	public synchronized int incrementAndGetCount(final T object) {
		Integer count = objects.get(object);
		count = count == null ? 1 : count + 1;
		objects.put(object, count);
		return count;
	}

	/**
	 * Decrement the count for {@code object} and return the number of equal
	 * objects that still remain in the {@code ObjectCounter}.
	 * <p>
	 * <b>Note :</b>It will never return counts &lt; zero
	 * 
	 * @param object
	 *            object to remove
	 * @return count of remaining objects (never less than zero)
	 */
	public synchronized int decrementAndGetCount(final T object) {
		Integer count = objects.get(object);
		if (count == null) {
			return 0;
		}
		if (count == 1) {
			objects.remove(object);
			return 0;
		}
		count--;
		objects.put(object, count);
		return count;
	}

	/**
	 * Return the equal number of objects in the {@code ObjectCounter}.
	 * <p>
	 * <b>Note :</b>It will never return counts &lt; zero
	 * 
	 * @param object
	 *            object to test
	 * @return count of objects (never less than zero)
	 */
	public synchronized int getCount(final T object) {
		final Integer count = objects.get(object);
		return count == null ? 0 : count;
	}

	/**
	 * Return an {@code Iterator} over the counted objects.
	 * 
	 * @return an iterator
	 */
	public Iterator<T> iterator() {
		return objects.keySet().iterator();
	}
}
