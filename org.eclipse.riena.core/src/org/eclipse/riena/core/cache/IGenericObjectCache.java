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

/**
 * The GenericObjectCache is a multipurpose object for storing data without
 * locking all your memory. It works like a regular HashMap with some
 * differences. Item can be removed from the GenericObjectCache under certain
 * conditions:
 * <ul>
 * <li>timeout: xxxx milliseconds after the timeout, an item is no longer valid
 * if found in the cache and automatically removed.</li>
 * <li>GC: if garbage collection is started because of low memory in the JVM, it
 * can remove any entry in the GenericObjectCache.</li>
 * <li>minSize: minSize specifies a number of minimum entries that are held in
 * the cache even if garbage collection occurs. However these minSize entries
 * will still be checked against the timeout. This check only occurs on get(..).
 * The minSize entries contains the objects that were last accessed by put or
 * get.</li>
 * </ul>
 */
public interface IGenericObjectCache<K, V> {

	/**
	 * Get an object by key. Cannot be called for global caches. There you have
	 * to use get(Object,Class), see below.
	 * 
	 * @param key
	 *            the key to look up object.
	 * @return the object looked up.
	 */
	V get(K key);

	/**
	 * Put some object <code>value</code> with <code>key</code> into the cache.
	 * 
	 * @param key
	 *            the key for looking up the object.
	 * 
	 * @param value
	 *            the value that is stored in the cache.
	 */
	void put(K key, V value);

	/**
	 * Remove object with <code>key</code> explicitly (rather than waiting for
	 * timeout) from the cache.
	 * 
	 * @param key
	 *            the key for the value to be removed
	 */
	void remove(K key);

	/**
	 * Clear the complete cache including soft and hard references
	 * 
	 * @post size()==0;
	 */
	void clear();

	/**
	 * Set the timeout for cached objects. After the timeout objects can not be
	 * found with get, even if the GC has not yet cleaned them from the cache.
	 * 
	 * @param milliseconds
	 *            the timeout in milliseconds.
	 */
	void setTimeout(int milliseconds);

	/**
	 * Return number of all cached objects. Some objects in this count might
	 * already be beyond their expiration.
	 * 
	 * @return the number of all cached objects.
	 */
	int size();

	/**
	 * Set the minimum number of entries that are held in the cache. This is the
	 * number of objects that are held in the cache as minimum even if there is
	 * a low-memory situation. There is no guarantee that there are minSize
	 * objects in the cache because objects are also removed if a timeout
	 * occurs. However objects are not automatically garbagecollected if they
	 * are in the pool of minSize objects but only when the application tries to
	 * get them and the expiration of the entry is detected.
	 * 
	 * @param minSize
	 *            the minimum number of entries.
	 */
	void setMinimumSize(int minSize);

	/**
	 * Returns the currently set timeout value
	 * 
	 * @return
	 */
	int getTimeout();

	/**
	 * Returns the currently set minimum size
	 * 
	 * @return
	 */
	int getMinimumSize();

	/**
	 * Descriptive name for this cache (shown in logs)
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * Return number of all cached objects.
	 * 
	 * @return
	 */
	int getSize();

	/**
	 * @return An arbitrary string which is dependent on the cache
	 *         implementation that can be used for log entries to show the
	 *         usage, hit/miss ratio etc. for the cache in use
	 */
	String getStatistic();
}
