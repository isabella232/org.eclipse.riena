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
package org.eclipse.riena.internal.security.server;

import java.security.Principal;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.core.cache.GenericObjectCache;
import org.eclipse.riena.core.cache.IGenericObjectCache;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.server"; //$NON-NLS-1$

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
		Activator.plugin = this;
		final GenericObjectCache<String, Principal[]> principalCache = new GenericObjectCache<String, Principal[]>();
		principalCache.setName("principalCache"); //$NON-NLS-1$
		final Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("cache.type", "PrincipalCache"); //$NON-NLS-1$ //$NON-NLS-2$
		context.registerService(IGenericObjectCache.class.getName(), principalCache, props);
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
