/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
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
 * List object for a list of progress monitors. convient class so that the class
 * sending events to a list of progressmonitors for remote services can send
 * this event to this class and it dispatches the event to all registered
 * progressmonitors
 */
public interface IRemoteProgressMonitorList {

	int BYTE_COUNT_INCR = 512;

	/**
	 * Fired when first byte is sent in a remote service call
	 */
	void fireStartEvent();

	/**
	 * Fired when call is finished and is about to return
	 * 
	 * @param totalBytes
	 */
	void fireEndEvent(int totalBytes);

	/**
	 * Fired for every n bytes that are sent to the server. The size of 'n' is
	 * up to the implementation (currently 512). So typically you will receive
	 * events for increments of 512 and a last call with the complete size when
	 * all data is sent.
	 * 
	 * @param totalBytes
	 * @param bytesSent
	 */
	void fireWriteEvent(int totalBytes, int bytesSent);

	/**
	 * Fire for every n bytes that are read from the server. The size of 'n' is
	 * up to the implementation (currently 512). So typically you will receive
	 * event for increments of 512 and a last call when all data is read from
	 * the server.
	 * 
	 * @param totalBytes
	 * @param bytesRead
	 */
	void fireReadEvent(int totalBytes, int bytesRead);

}
