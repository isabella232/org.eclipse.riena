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

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.core.Activator;
import org.eclipse.riena.internal.core.cache.ICacheEntry;
import org.eclipse.riena.internal.core.cache.SimpleCacheEntry;
import org.eclipse.riena.internal.core.ignore.IgnoreFindBugs;

/**
 * LRU=(Last Recently Used) Implementation for IGenericObjectCache (alternative
 * to GenericObjectCache)
 * 
 * Beyond the description in the interface, this implementation has no more
 * entries than minSize. So minSize is also the maxSize. The oldest (last
 * recently used) entry is removed once minSize/maxSize is reached.
 */
public class LRUCache<K, V> implements IGenericObjectCache<K, V> {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), GenericObjectCache.class);
	private LinkedHashMap<K, ICacheEntry<K, V>> lruMap = null;
	private long timeout;
	/** minimum count of entries to keep * */
	private int minimumSize;
	private int statHit;
	private int statNotFound;
	private int statTimeout;
	private static int statDisplayCount;
	private String name = "LRUCache : "; //$NON-NLS-1$

	/**
	 * 
	 */
	public LRUCache() {
		super();
		LOGGER.log(LogService.LOG_INFO, "creating new LRUCache instance"); //$NON-NLS-1$
		lruMap = new LinkedHashMap<K, ICacheEntry<K, V>>();
		// default timeout 1 minute
		setTimeout(60000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.cache.IGenericObjectCache#setName(java.lang.String
	 * )
	 */
	public void setName(final String name) {
		this.name = name + " : "; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#setTimeout(int)
	 */
	public void setTimeout(final int milliseconds) {
		LOGGER.log(LogService.LOG_INFO, "setTimeout = " + milliseconds); //$NON-NLS-1$
		timeout = milliseconds;
	}

	public int getTimeout() {
		return (int) timeout;
	}

	public synchronized V get(final K key) {
		LOGGER.log(LogService.LOG_DEBUG, "get = " + key); //$NON-NLS-1$
		final ICacheEntry<K, V> entry = lruMap.get(key);
		/** do we find the entry * */
		if (entry == null) {
			statNotFound++;
			printStat();
			return null;
		}
		final long timePassed = System.currentTimeMillis() - entry.getTimestamp();
		/** is the entry expired * */
		if (timePassed < timeout) {
			/** does the soft reference still point anywhere * */
			statHit++;
			printStat();
			return entry.getValue();
		} else {
			remove(key);
			statTimeout++;
			printStat();
			return null;
		}
	}

	private void printStat() {
		statDisplayCount++;
		if (statDisplayCount > 100) {
			LOGGER.log(LogService.LOG_INFO, name + "Hit / NotFound / Timeout " //$NON-NLS-1$
					+ statHit + " / " //$NON-NLS-1$
					+ statNotFound + " / " //$NON-NLS-1$
					+ statTimeout);
			statDisplayCount = 0;
		}
	}

	@IgnoreFindBugs(value = "IS2_INCONSISTENT_SYNC", justification = "not that critical, just statistics")
	public String getStatistic() {
		return name + "Hit / NotFound / Miss / Timeout " //$NON-NLS-1$
				+ statHit + " / " + statNotFound + " / " + statTimeout; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#put(Object,
	 *      java.lang.Object)
	 */
	public synchronized void put(final K key, final V value) {
		LOGGER.log(LogService.LOG_DEBUG, "put = " + key + ", " + value); //$NON-NLS-1$ //$NON-NLS-2$
		lruMap.put(key, new SimpleCacheEntry<K, V>(value, key));
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#clear()
	 */
	public synchronized void clear() {
		LOGGER.log(LogService.LOG_DEBUG, "clear"); //$NON-NLS-1$
		lruMap.clear();
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#remove(Object)
	 */
	public synchronized void remove(final K key) {
		LOGGER.log(LogService.LOG_DEBUG, "remove = " + key); //$NON-NLS-1$
		lruMap.remove(key);
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#size()
	 */
	public synchronized int size() {
		LOGGER.log(LogService.LOG_DEBUG, "size <= " + lruMap.size()); //$NON-NLS-1$
		return lruMap.size();
	}

	public int getSize() {
		return size();
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#setMinimumSize(int)
	 */
	public synchronized void setMinimumSize(final int minSize) {
		LOGGER.log(LogService.LOG_INFO, "setMinSize = " + minSize); //$NON-NLS-1$
		minimumSize = minSize;
		lruMap = new LRUHashMap<K, V>(minSize);
	}

	public synchronized int getMinimumSize() {
		return minimumSize;
	}

	/**
	 * HashMap that implements a Last Recently Used schema on a LinkedHashMap
	 */
	private static class LRUHashMap<K, V> extends LinkedHashMap<K, ICacheEntry<K, V>> {

		private final int minSize;
		private static final long serialVersionUID = 6499327049035525641L;

		/**
		 * @param minSize
		 */
		public LRUHashMap(final int minSize) {
			super(minSize, .75F, true);
			this.minSize = minSize;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
		 */
		@Override
		protected boolean removeEldestEntry(final Entry<K, ICacheEntry<K, V>> eldest) {
			return size() > minSize;
		}
	}

}
