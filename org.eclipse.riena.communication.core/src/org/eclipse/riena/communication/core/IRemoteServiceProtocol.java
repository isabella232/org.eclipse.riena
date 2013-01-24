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

import org.eclipse.riena.communication.core.factory.IRemoteServiceFactory;
import org.eclipse.riena.communication.core.publisher.IServicePublisher;

/**
 * This interface is used for all components that support a specific remote
 * service type i.e. end point transport protocol.
 * 
 * @see IServicePublisher
 * @see IRemoteServiceFactory
 * 
 */
public interface IRemoteServiceProtocol {

	/**
	 * Defines the OSGi Service property for protocol type. An OSGi Service
	 * should set this property and the protocol name
	 */
	String PROP_PROTOCOL = "riena.protocol"; //$NON-NLS-1$

	/**
	 * Get the protocol name.
	 * 
	 * @return the protocol name for this IRemoteServiceFactory is responsible.
	 */
	String getProtocol();
}
