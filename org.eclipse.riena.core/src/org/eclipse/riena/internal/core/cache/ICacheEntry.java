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
package org.eclipse.riena.internal.core.cache;

/**
 * Interface for cache entries in the various cache implementations.
 */
public interface ICacheEntry<K, V> {

	/**
	 * Returns the value of the cache entry, null if no value was found
	 * 
	 * @return value
	 */
	V getValue();

	/**
	 * Returns the key of the cache entry
	 * 
	 * @return key
	 */
	K getKey();

	/**
	 * Returns the timestamp of the cache entry, when it was last put or
	 * touched.
	 * 
	 * @return timestamp
	 */
	long getTimestamp();
}
