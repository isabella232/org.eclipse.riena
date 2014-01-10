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
package org.eclipse.riena.internal.core.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * CacheEntry with SoftReference (=values can be garbage collected)
 * 
 */
public class SoftCacheEntry<K, V> implements ICacheEntry<K, V> {
	private K key;
	private long timestamp;
	private SoftReference<V> value;

	/**
	 * Create a new Cache Entry without value (for subclasses which have their
	 * own implementation for that)
	 * 
	 * @param key
	 */
	public SoftCacheEntry(final K key) {
		super();
		this.key = key;
		timestamp = System.currentTimeMillis();
	}

	/**
	 * Creates a new Cache Entry
	 * 
	 * @param value
	 * @param key
	 * @param queue
	 */
	public SoftCacheEntry(final V value, final K key, final ReferenceQueue<V> queue) {
		this(key);
		if (value != null) {
			this.value = new SoftReference<V>(value, queue);
		}
	}

	/**
	 * @see org.eclipse.riena.internal.core.cache.ICacheEntry#getValue()
	 */
	public V getValue() {
		return value.get();
	}

	/**
	 * @see org.eclipse.riena.internal.core.cache.ICacheEntry#getKey()
	 */
	public K getKey() {
		return key;
	}

	/**
	 * @see org.eclipse.riena.internal.core.cache.ICacheEntry#getTimestamp()
	 */
	public long getTimestamp() {
		return timestamp;
	}
}
