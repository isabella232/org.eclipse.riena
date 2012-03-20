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
package org.eclipse.riena.communication.core;

import java.util.List;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.communication.core.factory.IRemoteServiceFactory;
import org.eclipse.riena.communication.core.publisher.IServicePublishEventDispatcher;

/**
 * The IRemoteServiceRegistry adds the ability to register, unregister and
 * manage "remote services" as OSGi Services within an OSGi runtime (called
 * client container). A "remote service" could be an OSGi Services which is
 * hosted within another OSGi runtime (called server container). A "remote
 * service" is represented by a {@link IRemoteServiceReference} within the
 * client container. A IRemoteServiceReference holds a proxy reference which
 * points the service end point.
 * <p>
 * A IRemoteServiceReferences becomes registered as "remote" OSGi Service within
 * the client container by call
 * {@link #registerService(IRemoteServiceReference)} . #registerService returns
 * a registration object {@link IRemoteServiceRegistration} which points the
 * registered "remote" OSGi service. Call
 * {@link #unregisterService(IRemoteServiceReference)} or
 * {@link IRemoteServiceRegistration#unregister()} to unregister a "remote" OSGi
 * Service
 * <p>
 * The RemoteServiceRegistry is available as OSGi Service. The registration name
 * is {@link #ID}.
 * <p>
 * The {@link IRemoteServiceFactory} can use to create a IRemoteServiceReference
 * by given end points parameters and to register a IRemoteServiceReference into
 * the IRemoteServiceRegistry.
 * <p>
 * OSGi Service could be published as service end point from a server container
 * by the {@link IServicePublishEventDispatcher}
 * 
 * @see IRemoteServiceReference
 * @see IRemoteServiceFactory
 * @see IServicePublishEventDispatcher
 * 
 */
public interface IRemoteServiceRegistry {

	/**
	 * Registers the given reference as OSGi Service. Answers a
	 * {@link IRemoteServiceRegistration} object which points the registered
	 * reference. A registered reference becomes automatically registered as
	 * "remote" OSGi Service within the local OSGi container.
	 * <p>
	 * To unregister a reference from the Remote Service Registry call
	 * {@link #unregisterService(IRemoteServiceReference)} or
	 * {@link IRemoteServiceRegistration#unregister()}
	 * 
	 * @param reference
	 *            the reference to register
	 * @param context
	 *            the bundle context to in which the service (proxy) will be
	 *            registered
	 * @return the RemoteServiceRegistration object
	 * 
	 * @see #unregisterService(IRemoteServiceReference)
	 * @see IRemoteServiceRegistration#unregister()
	 */
	IRemoteServiceRegistration registerService(IRemoteServiceReference reference, BundleContext context);

	/**
	 * Unregisters the given reference from the Remote Service Registry. The
	 * associated "remote" OSGi Service become automatically unregistered.
	 * 
	 * @param reference
	 *            the reference to unregister
	 * 
	 * @see #registerService(IRemoteServiceReference)
	 */
	void unregisterService(IRemoteServiceReference reference);

	/**
	 * Answers a list of {@link IRemoteServiceRegistration} objects for all
	 * registered "remote" OSGi Services for the given {@code context}
	 * 
	 * @param context
	 *            BundleContext in which the service (proxy) is registered; if
	 *            {@code null} all registered services will be returned
	 * @return a list of IRemoteServiceRegistration objects
	 */
	List<IRemoteServiceRegistration> registeredServices(BundleContext context);
}
