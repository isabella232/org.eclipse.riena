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
package org.eclipse.riena.internal.sample.app.client;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.sample.app.common.model.IHelloWorldService;
import org.eclipse.riena.ui.swt.AbstractRienaUIPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractRienaUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.sample.app.client.helloworld"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		// hack to force riena.core to load

		plugin = this;

		// register hessian proxy for riena remote service
		Register.remoteProxy(IHelloWorldService.class)
				.usingUrl("http://localhost:8080/hessian/HelloWorldServiceWS").withProtocol("hessian").andStart(context); //$NON-NLS-1$ //$NON-NLS-2$

		Register.remoteProxy(ICustomerSearch.class)
				.usingUrl("http://localhost:8080/hessian/CustomerSearchWS").withProtocol("hessian").andStart(context); //$NON-NLS-1$ //$NON-NLS-2$

	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;

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
