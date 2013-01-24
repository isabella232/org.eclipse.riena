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
package org.eclipse.riena.internal.example.client;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.communication.core.factory.ProxyAlreadyRegisteredFailure;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.monitor.common.IReceiver;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.example.client"; //$NON-NLS-1$
	private static final String PROTOCOL_HESSIAN = "hessian"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		Register.remoteProxy(IReceiver.class).usingUrl("http://localhost:8080/hessian/CollectibleReceiverWS") //$NON-NLS-1$
				.withProtocol(PROTOCOL_HESSIAN).andStart(context);
		try {
			Register.remoteProxy(IAuthenticationService.class)
					.usingUrl("http://localhost:8080/hessian/AuthenticationService").withProtocol(PROTOCOL_HESSIAN).andStart(context); //$NON-NLS-1$ 
		} catch (final ProxyAlreadyRegisteredFailure e) {
			Nop.reason("do nothing, can happen if some other bundle registered this service"); //$NON-NLS-1$
		}
	}

	/**
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
