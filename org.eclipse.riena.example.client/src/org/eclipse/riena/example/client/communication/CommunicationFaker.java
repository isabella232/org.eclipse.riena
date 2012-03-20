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
package org.eclipse.riena.example.client.communication;

import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorList;
import org.eclipse.riena.core.exception.MurphysLawFailure;

public class CommunicationFaker {

	public void communicate(final IRemoteProgressMonitorList progressMonitors) {
		startCommunication(progressMonitors);
		doCommunicate(progressMonitors);
		endCommuncation(progressMonitors);
	}

	private void endCommuncation(final IRemoteProgressMonitorList progressMonitors) {
		progressMonitors.fireEndEvent(0);
	}

	private void startCommunication(final IRemoteProgressMonitorList progressMonitors) {
		progressMonitors.fireStartEvent();
	}

	private void doCommunicate(final IRemoteProgressMonitorList progressMonitors) {
		for (int i = 0; i < 100; i += 1) {
			sendData(progressMonitors);
			delay();
			receiveData(progressMonitors);
		}
	}

	private void sendData(final IRemoteProgressMonitorList progressMonitors) {
		progressMonitors.fireWriteEvent(100, 1);
	}

	private void receiveData(final IRemoteProgressMonitorList progressMonitors) {
		progressMonitors.fireReadEvent(100, 1);
	}

	private void delay() {
		try {
			Thread.sleep(50);
		} catch (final InterruptedException e) {
			throw new MurphysLawFailure("Sleeping failed", e); //$NON-NLS-1$
		}
	}
}
