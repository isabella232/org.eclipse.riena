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
package org.eclipse.riena.communication.core.publisher;

import org.eclipse.riena.communication.core.IRemoteServiceProtocol;
import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.hooks.IServiceMessageContextAccessor;

/**
 * Implementations of IServicePublisher publish OSGi services as service end
 * points.
 * 
 * An implementation is responsible for a specifically protocol (e.g. Hessian).
 * The implementation has be registered as OSGi Service and set with follow
 * key/value property (see also {@link IRemoteServiceProtocol}):<br>
 * 
 * 'riena.protocol'=[aProtocol] (e.g. for aProtocol set 'hessian')<br>
 * <p>
 * Code sample:<br>
 * 
 * <pre>
 * <code>
 * public void start(BundleContext context) throws Exception {
 *     publisher = new XYZRemoteServicePublisher(); 
 *     Hashtable properties = new Hashtable(1);     
 *     properties.put(IServicePublisher.PROP_PROTOCOL, publisher.getProtocol());
 *     regPublisher = context.registerService(IServicePublisher.ID, publisher, properties);
 * }     
 * </code>
 * </pre>
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
 * @see RemoteServiceDescription
 * @see org.eclipse.riena.communication.core.factory.IRemoteServiceFactory
 */
public interface IServicePublisher extends IRemoteServiceProtocol {

	/**
	 * Publish Remote Service described in Remote Service Description
	 * 
	 * @param rsd
	 *            Remote Service Description
	 * @return url under which the service was published i.e.
	 *         http://192.168.1.20/hessian/CustomerServiceWS
	 */
	String publishService(RemoteServiceDescription rsd);

	/**
	 * Unpublish a service described with a Remote ServiceDescription
	 * 
	 * @param rsd
	 *            Remote Service Description
	 */
	void unpublishService(RemoteServiceDescription rsd);

	/**
	 * Returns ServiceMessageContextAccessor for this publisher
	 * 
	 * @return ServiceMessageContextAccessor
	 */
	IServiceMessageContextAccessor getMessageContextAccessor();

}
