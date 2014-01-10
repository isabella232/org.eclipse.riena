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
package org.eclipse.riena.example.client.communication;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorRegistry;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;

public abstract class RemoteCallProcess<T> extends UIProcess {

	private IRemoteProgressMonitorRegistry registry;
	private ServiceProgressVisualizer serviceProgressVisalizer;

	public RemoteCallProcess(final String name, final boolean user, final Object context) {
		super(name, user, context);
		injectRegistry();
	}

	private void injectRegistry() {
		Inject.service(IRemoteProgressMonitorRegistry.class).into(this).andStart(Activator.getDefault().getContext());
	}

	public void bind(final IRemoteProgressMonitorRegistry registry) {
		this.registry = registry;
	}

	public void unbind(final IRemoteProgressMonitorRegistry registry) {
		this.registry = null;
	}

	@Override
	protected void beforeRun(final IProgressMonitor monitor) {
		super.beforeRun(monitor);
		serviceProgressVisalizer = new ServiceProgressVisualizer(getJob().getName(), monitor);
		registerRemoteServiceMonitor();
	}

	@Override
	protected void afterRun(final IProgressMonitor monitor) {
		super.afterRun(monitor);
		unregisterRemoteServiceMonitor();
	}

	private void registerRemoteServiceMonitor() {
		registry.addProgressMonitor(getService(), serviceProgressVisalizer, getServiceMonitorType());
	}

	private void unregisterRemoteServiceMonitor() {
		registry.removeProgressMonitor(serviceProgressVisalizer);
	}

	protected abstract T getService();

	protected IRemoteProgressMonitorRegistry.RemovalPolicy getServiceMonitorType() {
		return IRemoteProgressMonitorRegistry.RemovalPolicy.AFTER_ONE_CALL;
	}

	@Override
	protected boolean forceMonitorBegin() {
		return false;
	}
}
