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
package org.eclipse.riena.example.client.communication;

import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorList;
import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorRegistry;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.example.client.Activator;

public class InfoServiceFake implements IInfoService {

	private IRemoteProgressMonitorRegistry registry;

	public InfoServiceFake() {
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

	public String getInfo(final String parameter) {
		final IRemoteProgressMonitorList progressMonitors = registry.getProgressMonitors(this);
		new CommunicationFaker().communicate(progressMonitors);
		return "hello, " + parameter + " from remote service"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
