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
package org.eclipse.riena.security.ui;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.navigation.ui.filter.IUIFilterApplier;
import org.eclipse.riena.security.ui.filter.PermissionUIFilterApplier;

public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.ui"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
		final PermissionUIFilterApplier service = new PermissionUIFilterApplier();
		Wire.instance(service).andStart(context);
		context.registerService(IUIFilterApplier.class.getName(), service, null);
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		Activator.plugin = null;
		super.stop(context);
	}

	/**
	 * @return the plugin instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
