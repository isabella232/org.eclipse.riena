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
package org.eclipse.riena.communication.core.factory;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.communication.core.IRemoteServiceReference;
import org.eclipse.riena.communication.core.RemoteServiceDescription;

/**
 * This is a default implementation of {@link IRemoteServiceReference}.
 * 
 */
public class RemoteServiceReference implements IRemoteServiceReference {
	private Object serviceInstance;
	private ServiceRegistration serviceRegistration;
	private RemoteServiceDescription description;
	private BundleContext context;

	/**
	 * Creates an instance with the given service end point description
	 * 
	 * @param description
	 */
	public RemoteServiceReference(final RemoteServiceDescription description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceReference#getURL()
	 */
	public String getURL() {
		return description.getURL();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.communication.core.IRemoteServiceReference#
	 * getServiceRegistration()
	 */
	public ServiceRegistration getServiceRegistration() {
		return serviceRegistration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.communication.core.IRemoteServiceReference#
	 * setServiceRegistration(org.osgi.framework.ServiceRegistration)
	 */
	public void setServiceRegistration(final ServiceRegistration serviceRegistration) {
		this.serviceRegistration = serviceRegistration;
	}

	/**
	 * Sets the service instance i.e. proxy reference. The service instance
	 * represents the remote OSGi Service
	 * 
	 * @param serviceInstance
	 */
	public void setServiceInstance(final Object serviceInstance) {
		this.serviceInstance = serviceInstance;
	}

	/*
	 * @see xeval.rsd.core.hessian.internal.IServiceEntry#getServiceInstance()
	 */
	public Object getServiceInstance() {
		return serviceInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceReference#getDescription
	 * ()
	 */
	public RemoteServiceDescription getDescription() {
		return description;
	}

	/*
	 * @see xeval.rsd.core.hessian.internal.IServiceEntry#disbose()
	 */
	public void dispose() {
		serviceInstance = null;
		serviceRegistration = null;
		if (description != null) {
			description.dispose();
			description = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.communication.core.IRemoteServiceReference#
	 * getServiceInterfaceClassName()
	 */
	public String getServiceInterfaceClassName() {
		return description.getServiceInterfaceClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceReference#getContext()
	 */
	public BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.IRemoteServiceReference#setContext
	 * (org.osgi.framework.BundleContext)
	 */
	public void setContext(final BundleContext context) {
		this.context = context;
	}

	@Override
	public String toString() {
		String symbolicName = "no context"; //$NON-NLS-1$
		if (context != null) {
			symbolicName = context.getBundle().getSymbolicName();
		}
		return "context for bundle=" + symbolicName + ", end point=(" + getDescription() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
