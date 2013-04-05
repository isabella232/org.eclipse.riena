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
package org.eclipse.riena.internal.communication.publisher;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.communication.core.hooks.IServiceHook;
import org.eclipse.riena.communication.core.publisher.IServicePublishBinder;
import org.eclipse.riena.communication.publisher.Publish;
import org.eclipse.riena.communication.publisher.ServicePublishBinder;
import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.wire.Wire;

public class Activator extends RienaActivator {

	// The shared instance
	private static Activator plugin;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);

		Activator.plugin = this;

		final IServiceHook serviceHook = new OrderedServiceHooksExecuter();
		Wire.instance(serviceHook).andStart(context);
		context.registerService(IServiceHook.class.getName(), serviceHook, null);

		final IServicePublishBinder binder = new ServicePublishBinder();
		Wire.instance(binder).andStart(context);
		context.registerService(IServicePublishBinder.class.getName(), binder, null);

		Publish.allServices().useFilter("(&(riena.remote=true)(riena.remote.protocol=*))").andStart(context); //$NON-NLS-1$
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
