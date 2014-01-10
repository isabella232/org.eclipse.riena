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
package org.eclipse.riena.internal.navigation.ui.swt;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.navigation.ui.swt.workarea.SwtExtensionWorkareaDefinitionRegistry;
import org.eclipse.riena.internal.ui.ridgets.swt.StatuslineUIProcessRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.AbstractRienaUIPlugin;
import org.eclipse.riena.ui.swt.StatuslineUIProcess;
import org.eclipse.riena.ui.workarea.spi.IWorkareaDefinitionRegistry;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractRienaUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.navigation.ui.swt"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	@Override
	public void start(final BundleContext context) throws Exception {

		super.start(context);
		plugin = this;

		final SwtExtensionWorkareaDefinitionRegistry registry = new SwtExtensionWorkareaDefinitionRegistry();
		Wire.instance(registry).andStart(context);
		context.registerService(IWorkareaDefinitionRegistry.class.getName(), registry,
				RienaConstants.newDefaultServiceProperties());

		SwtControlRidgetMapper.getInstance().addMapping(StatuslineUIProcess.class, StatuslineUIProcessRidget.class);
	}

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
