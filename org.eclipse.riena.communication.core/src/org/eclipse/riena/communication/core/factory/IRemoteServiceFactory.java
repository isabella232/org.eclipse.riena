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
package org.eclipse.riena.communication.core.factory;

import org.eclipse.riena.communication.core.IRemoteServiceProtocol;
import org.eclipse.riena.communication.core.IRemoteServiceReference;
import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.hooks.ICallMessageContextAccessor;
import org.eclipse.riena.communication.core.publisher.IServicePublisher;

/**
 * The IRemoteServiceFactory creates {@link IRemoteServiceReference} for given
 * protocol specifically service end point description. The
 * IRemoteServiceReference holds a proxy reference instance to the service end
 * point.
 * <p>
 * An implementation of an IRemoteServiceFactory is responsible for a protocol
 * (e.g. Hessian). The implementation has be registered as OSGi Service and set
 * with follow property (see also {@link IRemoteServiceProtocol}):<br>
 * <p>
 * Code sample:<br>
 * 
 * <pre>
 * <code>
 * public void start(BundleContext context) throws Exception {
 *     factory = new RemoteServiceFactoryHessian();
 *     Hashtable properties = new Hashtable(1);
 *     properties.put(IRemoteServiceFactory.PROP_PROTOCOL, factory.getProtocol());
 * 
 *     regFactory = context.registerService(IRemoteServiceFactory.ID, factory, properties);
 * }
 * </pre>
 * 
 * </code>
 * 
 * <b>NOTE</b><br>
 * The Riena communication bundle content includes generic class loading and
 * object instantiation or delegates this behavior to other Riena communication
 * bundles. Riena supports Eclipse-BuddyPolicy concept.
 * 
 * @see <a
 *      href="http://wiki.eclipse.org/Riena_Getting_started_remoteservices">Riena
 *      Wiki</a>
 * 
 * @see IRemoteServiceProtocol
 * @see IRemoteServiceReference
 * @see RemoteServiceFactory
 * @see IServicePublisher
 * 
 */
public interface IRemoteServiceFactory extends IRemoteServiceProtocol {

	/**
	 * Creates a protocol specifically IRemoteServcieRefernce for the given end
	 * point description. Answers the IRemoteServiceReference.
	 * 
	 * @param remoteServiceDesc
	 * @return the remote service references
	 */
	IRemoteServiceReference createProxy(RemoteServiceDescription remoteServiceDesc);

	/**
	 * Returns the instance of the CallMessageContextAccessor for this Remote
	 * Service Factory. The remote service Factory only has one instance that of
	 * such an accessor which can then be used to retrieve the
	 * CallMessageContext for an individual call at call time.
	 * 
	 * @return CallMessageContextAccessor
	 */
	ICallMessageContextAccessor getMessageContextAccessor();
}
