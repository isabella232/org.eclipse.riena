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
package org.eclipse.riena.communication.core;

/**
 * A IRemoteServiceRegistration holds all information about a
 * {@link IRemoteServiceReference} which is registered in the
 * {@link IRemoteServiceRegistry} OSGi Service. A IRemoteServiceRegistration
 * allows to unregister a IRemoteServiceReference from the
 * {@link IRemoteServiceRegistry} OSGi Service by invoke {@link #unregister()}
 * 
 * @see IRemoteServiceRegistry#registerService(IRemoteServiceReference)
 * @see IRemoteServiceReference
 */
public interface IRemoteServiceRegistration {

	/**
	 * Get the remote service reference.
	 * 
	 * @return the remote service reference
	 */
	IRemoteServiceReference getReference();

	/**
	 * Sets the given remote service reference
	 * 
	 * @param reference
	 *            the remote service reference
	 */
	void setReference(IRemoteServiceReference reference);

	/**
	 * Unregisters the RemoteServiceReference from the RemoteServiceRegistry.
	 * The RemoteServiceReferences is not more available into the OSGi
	 * ServiceRegistry
	 * 
	 * @see IRemoteServiceRegistry#registerService(IRemoteServiceReference)
	 */
	void unregister();

}
