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
package org.eclipse.riena.communication.core.progressmonitor;

/**
 * Interface that must be implemented by any class that wants to monitor the
 * progress of transporting data in webservices
 */
public interface IRemoteProgressMonitor {

	/**
	 * Called before the first byte is send on the line
	 */
	void start();

	/**
	 * Called after the webservice call is over
	 */
	void end();

	/**
	 * Called in arbitrary (currently not configurable intervals) when data
	 * chunks are sent on the line. The last call for one successful request
	 * will always have bytes == totalBytes.
	 * 
	 * @param event
	 *            ProgressMonitorEvent object with information about the
	 *            progress on this call
	 */
	void request(RemoteProgressMonitorEvent event);

	/**
	 * Called in arbitrary (currently not configurable intervals) when data
	 * chunks are received from the line. the last call for a successful
	 * response will always have bytes == totalBytes
	 * 
	 * @param event
	 *            ProgressMonitorEvent object with information about the
	 *            progress on this call
	 */
	void response(RemoteProgressMonitorEvent event);
}
