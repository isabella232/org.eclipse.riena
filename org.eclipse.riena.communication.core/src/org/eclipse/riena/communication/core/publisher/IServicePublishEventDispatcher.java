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
package org.eclipse.riena.communication.core.publisher;

import org.eclipse.riena.communication.core.RemoteServiceDescription;

/**
 * Implementations of {@code IServicePublishEventDispatcher} provide a list of
 * all service end points for published OSGi services within a container.
 * 
 * An implementation should be registered as OSGi service and maybe published as
 * "remote" OSGi service itself.
 * 
 * @see RemoteServiceDescription
 * 
 */
public interface IServicePublishEventDispatcher {

	/**
	 * Return the list of service end points for published OSGi services.
	 * 
	 * @return the list of service end points for published OSGi services.
	 */
	RemoteServiceDescription[] getAllServices();
}
