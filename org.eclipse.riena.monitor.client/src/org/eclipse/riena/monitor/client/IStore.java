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
package org.eclipse.riena.monitor.client;

import java.util.List;
import java.util.Map;

import org.eclipse.riena.monitor.common.Collectible;

/**
 * The {@code IStore} defines a store for {@code Collectible}s.
 */
public interface IStore {

	/**
	 * Open the store with the categories it must handle.
	 * 
	 * @param categories
	 *            the categories this store must handle
	 */
	void open(Map<String, Category> categories);

	/**
	 * Close the store.
	 */
	void close();

	/**
	 * In case that the store implementation buffers entries calling this method
	 * assures persisting them.
	 */
	void flush();

	/**
	 * Make the given collectible persistent.
	 * 
	 * @param collectible
	 *            the collectible
	 * @return true on success
	 */
	boolean collect(final Collectible<?> collectible);

	/**
	 * Prepare (mark) {@code Collectible}s for the given category as ready to
	 * transfer.
	 * 
	 * @param category
	 *            the category
	 */
	void prepareTransferables(String category);

	/**
	 * Retrieve all {@code Collectible}s for the given category
	 * 
	 * @param category
	 *            the category
	 * @return a list of ready to transfer {@code Collectible}s
	 */
	List<Collectible<?>> retrieveTransferables(String category);

	/**
	 * Commit the given {@code Collectible}s, i.e. remove them from the
	 * persistent store. These {@code Collectible}s have been previously
	 * retrieved by {@code getTransferables}. This method should only be called
	 * in case of a successful transfer.
	 * 
	 * @param collectibles
	 *            the collectibles
	 */
	void commitTransferred(List<Collectible<?>> collectibles);

}