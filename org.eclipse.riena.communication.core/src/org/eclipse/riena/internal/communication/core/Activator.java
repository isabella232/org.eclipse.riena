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
package org.eclipse.riena.internal.communication.core;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorRegistry;
import org.eclipse.riena.communication.core.progressmonitor.ProgressMonitorRegistryImpl;
import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.communication.core.factory.OrderedCallHooksExecuter;
import org.eclipse.riena.internal.communication.core.proxyselector.ProxySelectorConfiguration;
import org.eclipse.riena.internal.communication.core.registry.RemoteServiceRegistry;
import org.eclipse.riena.internal.communication.core.ssl.SSLConfiguration;

/**
 * TODO Documentation
 */
public class Activator extends RienaActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.communication.core"; //$NON-NLS-1$

	private RemoteServiceRegistry serviceRegistry;
	private ServiceRegistration regServiceRegistry;

	private SSLConfiguration sslConfiguration;

	private ProxySelectorConfiguration proxySelectorConfiguration;

	// The shared instance
	private static Activator plugin;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
		serviceRegistry = new RemoteServiceRegistry();
		serviceRegistry.start();

		final Hashtable<String, Object> properties = RienaConstants.newDefaultServiceProperties();
		regServiceRegistry = context.registerService(IRemoteServiceRegistry.class.getName(), serviceRegistry,
				properties);

		// SSL configuration
		configureSSL();

		// ProxySelector configuration
		configureProxySelector();

		// Ordered call hooks configuration 
		configureOrderedCallHooks();

		context.registerService(IRemoteProgressMonitorRegistry.class.getName(), new ProgressMonitorRegistryImpl(), null);
	}

	private void configureOrderedCallHooks() {
		final ICallHook hook = new OrderedCallHooksExecuter();
		Wire.instance(hook).andStart(getContext());
		getContext().registerService(ICallHook.class.getName(), hook, null);
	}

	private void configureSSL() {
		sslConfiguration = new SSLConfiguration();
		Wire.instance(sslConfiguration).andStart(getContext());
	}

	private void configureProxySelector() {
		proxySelectorConfiguration = new ProxySelectorConfiguration();
		Wire.instance(proxySelectorConfiguration).andStart(getContext());
	}

	@Override
	public void stop(final BundleContext context) throws Exception {

		regServiceRegistry.unregister();
		regServiceRegistry = null;

		serviceRegistry.stop();
		serviceRegistry = null;

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
