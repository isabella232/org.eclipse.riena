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

/**
 * A simple cache entry that is store in GenericObjectCache
 */
public class SimpleCacheEntry<K, V> implements ICacheEntry<K, V> {
	private final V value;
	private final K key;
	private final long timestamp;

	/**
	 * @param value
	 * @param key
	 * @param queue
	 */
	public SimpleCacheEntry(final V value, final K key) {
		super();
		this.value = value;
		this.key = key;
		timestamp = System.currentTimeMillis();
	}

	public V getValue() {
		return value;
	}

	public K getKey() {
		return key;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
