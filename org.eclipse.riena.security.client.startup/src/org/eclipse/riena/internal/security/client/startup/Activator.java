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
package org.eclipse.riena.internal.security.client.startup;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.communication.core.factory.ProxyAlreadyRegisteredFailure;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.client.startup"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;

		try {
			Register.remoteProxy(IAuthenticationService.class)
					.usingUrl("http://${riena.securehostname}/hessian/AuthenticationService").withProtocol("hessian").andStart(context); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (final ProxyAlreadyRegisteredFailure e) {
			Nop.reason("do nothing, can happen if other projects already registered this remote service"); //$NON-NLS-1$
		}

		try {
			Register.remoteProxy(IAuthorizationService.class)
					.usingUrl("http://${riena.securehostname}/hessian/AuthorizationService").withProtocol("hessian").andStart( //$NON-NLS-1$ //$NON-NLS-2$
							context);
		} catch (final ProxyAlreadyRegisteredFailure e) {
			Nop.reason("do nothing, can happen if other projects already registered this remote service"); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		Activator.plugin = null;
		super.stop(context);
	}

	/**
	 * Get the plugin instance.
	 * 
	 * @return
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
