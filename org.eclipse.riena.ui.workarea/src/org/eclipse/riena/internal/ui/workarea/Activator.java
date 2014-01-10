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
package org.eclipse.riena.internal.ui.workarea;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.riena.internal.ui.workarea.registry.WorkareaDefinitionRegistryFacade;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The shared instance
	private static Activator plugin;

	private ServiceTracker workareaDefinitionRegistryTracker;

	/**
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	public void start(final BundleContext context) throws Exception {

		super.start(context);
		plugin = this;
		workareaDefinitionRegistryTracker = new ServiceTracker(context,
				org.eclipse.riena.ui.workarea.spi.IWorkareaDefinitionRegistry.class.getName(),
				WorkareaDefinitionRegistryFacade.getInstance());
		workareaDefinitionRegistryTracker.open();
	}

	@Override
	public void stop(final BundleContext context) throws Exception {

		if (workareaDefinitionRegistryTracker != null) {
			workareaDefinitionRegistryTracker.close();
			workareaDefinitionRegistryTracker = null;
		}

		plugin = null;
		super.stop(context);
	}
}
