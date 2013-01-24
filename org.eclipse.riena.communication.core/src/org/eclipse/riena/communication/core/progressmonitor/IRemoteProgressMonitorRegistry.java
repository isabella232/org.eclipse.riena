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
package org.eclipse.riena.communication.core.progressmonitor;

/**
 * Interface for a registry that maintains the list of progress monitors that
 * are interested in monitoring remote service progress (a proxy object). At
 * runtime the proxy can ask the registry for all monitors that are monitoring
 * this proxy and send them an event about the progress. Monitor can either
 * deregister itself, or all monitors for a proxies are deregistered.
 */
public interface IRemoteProgressMonitorRegistry {

	enum RemovalPolicy {
		/**
		 * All progress monitors are removed after one call
		 */
		AFTER_ONE_CALL,

		/**
		 * All progress monitors are removed after all calls
		 */
		AFTER_ALL_CALLS
	}

	/**
	 * Add a new progress monitor for the specified remote Service proxy
	 * 
	 * @param callProxy
	 * @param monitor
	 * @param removalPolicy
	 *            can be AFTER_ONE_CALL or AFTER_ALL_CALLS, the former means all
	 *            progressMonitors are removed after one call
	 */
	void addProgressMonitor(Object callProxy, IRemoteProgressMonitor monitor, RemovalPolicy removalPolicy);

	/**
	 * Removes a specific progress monitor instance
	 * 
	 * @param monitor
	 */
	void removeProgressMonitor(IRemoteProgressMonitor monitor);

	/**
	 * Removes all progress monitors for a specific proxy instance
	 * 
	 * @param callProxy
	 */
	void removeAllProgressMonitors(Object callProxy);

	/**
	 * Gets a list object for all progress listeners for a proxy
	 * 
	 * @param callProxy
	 * @return
	 */
	IRemoteProgressMonitorList getProgressMonitors(Object callProxy);
}
