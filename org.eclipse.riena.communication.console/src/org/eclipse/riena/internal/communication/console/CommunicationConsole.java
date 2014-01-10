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
package org.eclipse.riena.internal.communication.console;

import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import org.eclipse.riena.communication.core.IRemoteServiceReference;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.publisher.IServicePublishBinder;
import org.eclipse.riena.internal.core.ignore.IgnoreCheckStyle;

@SuppressWarnings("restriction")
public class CommunicationConsole implements CommandProvider {
	private IServicePublishBinder servicePublisher;
	private IRemoteServiceRegistry serviceRegistry;

	public String getHelp() {
		return "---riena communication---\n\tremotestatus - list all services"; //$NON-NLS-1$
	}

	@IgnoreCheckStyle("the _ is the pattern for console commands - do not change!!")
	public void _remotestatus(final CommandInterpreter ci) throws Exception {
		printToPublishServices();
		printPublishedServices();
		printRegisteredRemoteServices();

	}

	private void printToPublishServices() {
		if (servicePublisher == null) {
			return;
		}
		final RemoteServiceDescription[] rsDescs = servicePublisher.getAllServices();

		// boolean found = false;
		for (final RemoteServiceDescription rsDesc : rsDescs) {
			if (rsDesc.getState() == RemoteServiceDescription.State.UNREGISTERED) {
				// found = true;
				System.out.println("Riena:: not published end point=(" + rsDesc + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	private void printPublishedServices() {
		if (servicePublisher == null) {
			return;
		}
		final RemoteServiceDescription[] rsDescs = servicePublisher.getAllServices();
		boolean found = false;
		for (final RemoteServiceDescription rsDesc : rsDescs) {
			if (rsDesc.getState() == RemoteServiceDescription.State.REGISTERED) {
				found = true;
				System.out.println("Riena:: Published OSGi Service=(" + rsDesc + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		if (!found) {
			System.out.println("Riena:: no OSGi services published"); //$NON-NLS-1$
			return;
		}

	}

	private void printRegisteredRemoteServices() {
		if (serviceRegistry == null) {
			// System.out.println("Riena:: no OSGi services registered");
			return;
		}
		final List<IRemoteServiceRegistration> rsRegs = serviceRegistry.registeredServices(null);
		if (rsRegs.size() == 0) {
			System.out.println("Riena:: no RemoteServiceProxies registered"); //$NON-NLS-1$
			return;
		}
		for (final IRemoteServiceRegistration rsReg : rsRegs) {
			final IRemoteServiceReference rsRef = rsReg.getReference();
			System.out.println("Riena:: Registered RemoteServiceProxy=(" + rsRef + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * @param servicePublisher
	 *            the servicePublisher to bind
	 */
	public void bind(final IServicePublishBinder servicePublisher) {
		this.servicePublisher = servicePublisher;
	}

	/**
	 * @param servicePublisher
	 *            the servicePublisher to unbind
	 */
	public void unbind(final IServicePublishBinder servicePublisher) {
		this.servicePublisher = null;
	}

	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to bind
	 */
	public void bind(final IRemoteServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to unbind
	 */
	public void unbind(final IRemoteServiceRegistry serviceRegistry) {
		this.serviceRegistry = null;
	}

}
