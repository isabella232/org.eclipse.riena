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
package org.eclipse.riena.internal.security.common;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.core.util.ContainerModel;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.security.common.authorization.PermissionCache;
import org.eclipse.riena.internal.security.common.session.SimpleSessionHolder;
import org.eclipse.riena.internal.security.common.session.SimpleThreadedSessionHolder;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.authorization.IPermissionCache;
import org.eclipse.riena.security.common.authorization.ISentinelService;
import org.eclipse.riena.security.common.session.ISessionHolder;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.common"; //$NON-NLS-1$

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

		createSessionHolder();
		createSecurityCallHook();
		createSubjectHolder();
		createPermissionCache();
		createSentinelService();
	}

	private void createSessionHolder() {
		getContext().registerService(ISessionHolder.class.getName(),
				ContainerModel.isClient() ? new SimpleSessionHolder() : new SimpleThreadedSessionHolder(), null);
	}

	private void createSubjectHolder() {
		getContext().registerService(ISubjectHolder.class.getName(),
				ContainerModel.isClient() ? new SimpleSubjectHolder() : new SimpleThreadedSubjectHolder(), null);
	}

	private void createSecurityCallHook() {
		final ICallHook hook = new SecurityCallHook();
		Wire.instance(hook).andStart();

		getContext().registerService(ICallHook.class.getName(), hook, null);
	}

	private void createSentinelService() {
		final ISentinelService sentinelService = new SentinelServiceImpl();
		Wire.instance(sentinelService).andStart();

		getContext().registerService(ISentinelService.class.getName(), sentinelService,
				RienaConstants.newDefaultServiceProperties());
	}

	private void createPermissionCache() {
		final PermissionCache permissionCache = new PermissionCache();
		Wire.instance(permissionCache).andStart();

		getContext().registerService(IPermissionCache.class.getName(), permissionCache,
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
