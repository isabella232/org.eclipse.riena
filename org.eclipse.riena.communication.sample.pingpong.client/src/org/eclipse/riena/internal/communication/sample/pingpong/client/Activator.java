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
package org.eclipse.riena.internal.communication.sample.pingpong.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import org.eclipse.riena.communication.sample.pingpong.common.IPingPong;
import org.eclipse.riena.communication.sample.pingpong.common.Ping;
import org.eclipse.riena.communication.sample.pingpong.common.Pong;

/**
 * The Ping Pong sample shows how to find a "remote" OSGi Services.
 * <p>
 * This sample Activator finds an IPingPong "remote" OSGi Service by the
 * ServiceRegistry. Sends a Ping to the IPingPong service and writes the pong
 * result into the console.
 */
public class Activator implements BundleActivator {

	private BundleContext context;

	public void start(final BundleContext context) throws Exception {
		this.context = context;
		final ServiceReference pingPongRef = context.getServiceReference(IPingPong.class.getName());
		if (pingPongRef != null) {
			new PingClient().sendPing(pingPongRef);
		} else {
			context.addServiceListener(new PingClient(), "(objectClass=" + IPingPong.class.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void stop(final BundleContext context) throws Exception {
		this.context = null;
	}

	class PingClient implements ServiceListener {

		public void serviceChanged(final ServiceEvent event) {

			final ServiceReference pingPongRef = event.getServiceReference();
			sendPing(pingPongRef);

		}

		void sendPing(final ServiceReference pingPongRef) {
			final IPingPong pingPong = (IPingPong) context.getService(pingPongRef);
			if (pingPong == null) {
				return;
			}

			final Ping ping = new Ping();
			ping.setText("I ping you and you pong me"); //$NON-NLS-1$

			final Pong pong = pingPong.ping(ping);
			System.out.println("PingPong::Client:: " + pong); //$NON-NLS-1$

		}
	}

}
