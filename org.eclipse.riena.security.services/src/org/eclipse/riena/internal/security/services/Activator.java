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
package org.eclipse.riena.internal.security.services;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.security.authenticationservice.AuthenticationService;
import org.eclipse.riena.internal.security.authorizationservice.AuthorizationService;
import org.eclipse.riena.internal.security.sessionservice.SessionService;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.server.session.ISessionService;
import org.eclipse.riena.security.sessionservice.ISessionProvider;
import org.eclipse.riena.security.sessionservice.SessionProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.services"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
		super();
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
		createAuthenticationServiceAndWire();
		createAuthorizationServiceAndWire();
		createSessionServiceAndWire();
		createSessionProvider();
	}

	private void createAuthenticationServiceAndWire() {
		final IAuthenticationService authenticationService = new AuthenticationService();
		Wire.instance(authenticationService).andStart();

		final Hashtable<String, Object> properties = RienaConstants.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString()); //$NON-NLS-1$
		properties.put("riena.remote.protocol", "hessian"); //$NON-NLS-1$ //$NON-NLS-2$
		properties.put("riena.remote.path", IAuthenticationService.WS_ID); //$NON-NLS-1$
		getContext().registerService(IAuthenticationService.class.getName(), authenticationService, properties);
	}

	private void createAuthorizationServiceAndWire() {
		final IAuthorizationService authorizationService = new AuthorizationService();
		Wire.instance(authorizationService).andStart();

		final Hashtable<String, Object> properties = RienaConstants.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString()); //$NON-NLS-1$
		properties.put("riena.remote.protocol", "hessian"); //$NON-NLS-1$ //$NON-NLS-2$
		properties.put("riena.remote.path", IAuthorizationService.WS_ID); //$NON-NLS-1$
		getContext().registerService(IAuthorizationService.class.getName(), authorizationService, properties);
	}

	private void createSessionServiceAndWire() {
		final ISessionService sessionService = new SessionService();
		Wire.instance(sessionService).andStart();

		final Hashtable<String, Object> properties = RienaConstants.newDefaultServiceProperties();
		properties.put("riena.remote", Boolean.TRUE.toString()); //$NON-NLS-1$
		properties.put("riena.remote.protocol", "hessian"); //$NON-NLS-1$ //$NON-NLS-2$
		properties.put("riena.remote.path", ISessionService.WS_ID); //$NON-NLS-1$
		getContext().registerService(ISessionService.class.getName(), sessionService, properties);
	}

	private void createSessionProvider() {
		getContext().registerService(ISessionProvider.class.getName(), new SessionProvider(),
				RienaConstants.newDefaultServiceProperties());
	}

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
