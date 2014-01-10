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
package org.eclipse.riena.internal.communication.core.registry;

import org.eclipse.riena.communication.core.IRemoteServiceReference;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;

/**
 * This is a Implementation of the IRemoteServiceRegistration. It refers
 * registered RemoteServiceReference. By invoke {@link #unregister()} the
 * RemoteServiceReference becomes unregistered from the
 * {@link RemoteServiceRegistry}.
 */
public class RemoteServiceRegistration implements IRemoteServiceRegistration {
	private IRemoteServiceReference reference;
	private IRemoteServiceRegistry registry;

	/**
	 * Creates a instance with the given reference an registry
	 * 
	 * @param reference
	 * @param registry
	 */
	public RemoteServiceRegistration(final IRemoteServiceReference reference, final IRemoteServiceRegistry registry) {
		super();
		this.reference = reference;
		this.registry = registry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceRegistration#getReference
	 * ()
	 */
	public IRemoteServiceReference getReference() {
		return reference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceRegistration#unregister
	 * ()
	 */
	public void unregister() {
		if (registry != null) {
			registry.unregisterService(getReference());
			registry = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceRegistration#setReference
	 * (org.eclipse.riena.communication.core.IRemoteServiceReference)
	 */
	public void setReference(final IRemoteServiceReference reference) {
		this.reference = reference;

	}

}
