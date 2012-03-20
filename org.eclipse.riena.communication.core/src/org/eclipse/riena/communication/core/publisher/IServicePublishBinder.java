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
package org.eclipse.riena.communication.core.publisher;

import org.osgi.framework.ServiceReference;

import org.eclipse.riena.communication.core.RemoteServiceDescription;

/**
 * The ServicePublishBinder collects all OSGi Services that needs to be
 * published. It maintains a list of ServicePublishers that it ask to publish a
 * given service. It can also hold a list of services waiting to be published.
 * As soon as the required ServicePublisher then comes up it triggers it with
 * all the OSGi services that are waiting to be published. The
 * ServicePublishBinder can also manage the situation if a particular
 * ServicePublisher goes down. Then it starts collecting that list again and as
 * soon as the same or a different ServicePublisher goes up again it triggers it
 * again. The ServicePublishBinder DOES NOT keep track of OSGi Services. So it
 * requires that someone else notifies it by calling publish or unpublish on the
 * individual OSGi Services.
 * 
 */
public interface IServicePublishBinder {

	/**
	 * Is called when an individual OSGi Services needs to be publish under a
	 * particular URL and protocol
	 * 
	 * @param ref
	 *            OSGi Service reference
	 * @param url
	 *            url (local path i.e. /CustomerServiceWS)
	 * @param protocol
	 *            protocol under which to publish i.e. hessian
	 */
	void publish(ServiceReference ref, String url, String protocol);

	/**
	 * Is called when an individual OSGi Service should no longer be published.
	 * 
	 * @param serviceReference
	 *            OSGi Service reference
	 */
	void unpublish(ServiceReference serviceReference);

	/**
	 * Returns an array of RemoteServiceDescription for all services that the
	 * ServicePublishBinder knows off.
	 * 
	 * @return Array of RemoteServiceDescription
	 */
	RemoteServiceDescription[] getAllServices();

}
