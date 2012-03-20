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
package org.eclipse.riena.internal.communication.sample.pingpong.server;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.communication.sample.pingpong.common.IPingPong;

/**
 * The Ping Pong sample shows how to publish an OSGi Services.
 * 
 * This sample Activator registers the IPingPong OSGi Service. This service
 * becomes published by the Riena Communication Hessian Publisher
 */
public class Activator implements BundleActivator {
	private ServiceRegistration pingPongRegHessian;

	// private ServiceRegistration pingPongRegXFire;

	public void start(final BundleContext context) throws Exception {
		// create hessian service
		final PingPong pingPong = new PingPong();
		final Hashtable<String, String> properties = new Hashtable<String, String>(3);

		properties.put(RSDPublisherProperties.PROP_IS_REMOTE, Boolean.TRUE.toString());
		properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, "hessian"); //$NON-NLS-1$
		properties.put(RSDPublisherProperties.PROP_REMOTE_PATH, "/PingPongWS"); //$NON-NLS-1$

		pingPongRegHessian = context.registerService(IPingPong.class.getName(), pingPong, properties);

		// create ecf service
		// PingPong pingPong = new PingPong();
		// Hashtable<String, String> properties = new Hashtable<String,
		// String>(3);
		//
		// properties.put(RSDPublisherProperties.PROP_IS_REMOTE,
		// Boolean.TRUE.toString());
		// properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, "ecf");
		// properties.put("ecf.type", "generic");
		// properties.put(RSDPublisherProperties.PROP_REMOTE_PATH,
		// "ecftcp://localhost:30000/server");
		//
		// pingPongRegHessian =
		// context.registerService(IPingPong.class.getName(), pingPong,
		// properties);

	}

	public void stop(final BundleContext context) throws Exception {
		pingPongRegHessian.unregister();
		pingPongRegHessian = null;
	}

}
