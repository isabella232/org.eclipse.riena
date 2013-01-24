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
package org.eclipse.riena.internal.monitor.client;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.core.wire.WirePuller;
import org.eclipse.riena.monitor.client.IAggregator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.monitor.client"; //$NON-NLS-1$

	private IAggregator aggregator;
	private WirePuller aggregatorPuller;
	private ServiceRegistration aggregatorRegistration;

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
		plugin = this;
		aggregator = new Aggregator();
		aggregatorPuller = Wire.instance(aggregator).andStart(context);
		aggregator.start();
		aggregatorRegistration = getContext().registerService(IAggregator.class.getName(), aggregator,
				RienaConstants.newDefaultServiceProperties());
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		getContext().ungetService(aggregatorRegistration.getReference());
		aggregator.stop();
		aggregatorPuller.stop();
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
