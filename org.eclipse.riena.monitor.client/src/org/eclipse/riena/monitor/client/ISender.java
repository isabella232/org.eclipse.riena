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
package org.eclipse.riena.monitor.client;

import java.util.Collection;

/**
 * The {@code ISender} is responsible for retrieving {@code Collectible}s from
 * the store, transferring them to the receiver.
 */
public interface ISender {

	/**
	 * Start the sender, i.e. from now on the sender is responsible for sending
	 * {@code Collectible}s and maintaining the store appropriately.
	 * <p>
	 * Sender is also responsible for retrying failed send attempts.
	 * 
	 * @param store
	 *            the store for the sender
	 * @param categories
	 *            the categories this sender must handle
	 */
	void start(IStore store, Collection<Category> categories);

	/**
	 * Stop the sender.
	 */
	void stop();

	/**
	 * Trigger the transfer of the {@code Collectible}s registered with the
	 * given category.
	 * 
	 * @param category
	 *            the category of the {@code Collectible}s that should be
	 *            transfered
	 */
	void triggerTransfer(String category);

}
