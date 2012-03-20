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
package org.eclipse.riena.internal.communication.sample.pingpong.client.config;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.communication.sample.pingpong.common.IPingPong;

/**
 * The Ping Pong sample shows to config a "remote" OSGi Services on the base of
 * service end point parameters.
 * 
 * This sample Activator registers manually the remote PingPong end point as
 * remote OSGi Service into the ServiceRegistry. This is reached by a simple way
 * using the Riena communication RemoteServiceFactory. #createAndRegisterProxy
 * creates a proxy references for the end point and registers the proxy
 * references as RemoteServiceReference into the Riena communication
 * IRemoteServiceRegistry OSGi Service. The IRemoteServiceRegistry itself
 * registers and manages the RemoteServiceReferences as remote OSGi service
 * within the ServiceRegistry.
 */
public class Activator implements BundleActivator {

	private IRemoteServiceRegistration pingPongReg;

	/**
	 * Creates a RemoteServiceReferences based on Hessian protocol and registers
	 * this as "remote" OSGi Service
	 */
	public void start(final BundleContext context) throws Exception {
		// register hessian proxy for riena remote service
		pingPongReg = Register.remoteProxy(IPingPong.class).usingUrl("http://${riena.hostname}/hessian/PingPongWS") //$NON-NLS-1$
				.withProtocol("hessian").andStart(context); //$NON-NLS-1$
	}

	/**
	 * unregister end release the "remote" OSGi Service
	 */
	public void stop(final BundleContext context) throws Exception {
		if (pingPongReg != null) {
			pingPongReg.unregister();
			pingPongReg = null;
		}
	}

}
