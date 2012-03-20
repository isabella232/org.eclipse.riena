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
package org.eclipse.riena.core.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A non-synchronized LRU Map based on the {@code LinkedHashMap}.
 */
public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {

	private final int maxSize;
	private static final long serialVersionUID = -241795650048399135L;

	/**
	 * Creates a LRU map with maximum size {@code maxSize}.
	 * 
	 * @param maxSize
	 *            the LRU map maximum size
	 */
	public LRUHashMap(final int maxSize) {
		super(maxSize, .75F, true);
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
		return size() > maxSize;
	}

	/**
	 * Create a non-synchronized LRU hash map.
	 * 
	 * @param <K>
	 *            type of the key
	 * @param <V>
	 *            type of the value
	 * @param maxSize
	 *            the LRU map maximum size
	 * @return a synchronized LRU hash map
	 */
	public static <K, V> Map<K, V> createLRUHashMap(final int maxSize) {
		return new LRUHashMap<K, V>(maxSize);
	}

	/**
	 * Create a synchronized LRU hash map.
	 * 
	 * @param <K>
	 *            type of the key
	 * @param <V>
	 *            type of the value
	 * @param maxSize
	 *            the LRU map maximum size
	 * @return a synchronized LRU hash map
	 */
	public static <K, V> Map<K, V> createSynchronizedLRUHashMap(final int maxSize) {
		return Collections.synchronizedMap(new LRUHashMap<K, V>(maxSize));
	}
}
