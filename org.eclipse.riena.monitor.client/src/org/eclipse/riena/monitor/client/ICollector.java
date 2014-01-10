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

/**
 * The {@code ICollector} interface defines the protocol for collectors.
 */
public interface ICollector {

	/**
	 * Start the collector, i.e. the collector can now start collecting
	 * 
	 * @param aggregator
	 *            the {@code IAggregator} for this collector
	 * @param category
	 *            the category for this collector
	 * @param clientInfoProvider
	 *            the client information provider
	 */
	void start(IAggregator aggregator, Category category, IClientInfoProvider clientInfoProvider);

	/**
	 * Stop the collector, i.e. the collector must now stop collecting
	 */
	void stop();

	/**
	 * Get the category of this collector
	 * 
	 * @return the category
	 */
	Category getCategory();
}
