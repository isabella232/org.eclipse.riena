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
 *
 */
public class RemoteProgressMonitorEvent {

	private int totalBytes;
	private int bytesProcessed;

	public RemoteProgressMonitorEvent() {
		super();
	}

	public RemoteProgressMonitorEvent(final int totalBytes) {
		this.totalBytes = totalBytes;
	}

	public RemoteProgressMonitorEvent(final int totalBytes, final int bytesProcessed) {
		this.totalBytes = totalBytes;
		this.bytesProcessed = bytesProcessed;
	}

	public int getTotalBytes() {
		return totalBytes;
	}

	public int getBytesProcessed() {
		return bytesProcessed;
	}

}
