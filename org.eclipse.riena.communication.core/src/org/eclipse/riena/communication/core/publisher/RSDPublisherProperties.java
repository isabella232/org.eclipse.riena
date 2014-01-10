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

/**
 * Defines some properties required by "remote" OSGi service registration
 */
public final class RSDPublisherProperties {

	private RSDPublisherProperties() {
		super();
	}

	/**
	 * Defines whether an OSGi Service is remote or not
	 */
	public static final String PROP_IS_REMOTE = "riena.remote"; //$NON-NLS-1$
	/**
	 * Defines the protocol under which the OSGi Serives is published
	 */
	public static final String PROP_REMOTE_PROTOCOL = "riena.remote.protocol"; //$NON-NLS-1$
	/**
	 * Defines the content path where the service end point becomes published
	 */
	public static final String PROP_REMOTE_PATH = "riena.remote.path"; //$NON-NLS-1$

	/**
	 * defines a configuration id with which the proxy can receive configuration
	 * information at runtime
	 */
	public static final String PROP_CONFIG_ID = "riena.config.id"; //$NON-NLS-1$

	public static final String PROP_INTERFACE = "riena.interface"; //$NON-NLS-1$

	public static final String PROP_URL = "riena.url"; //$NON-NLS-1$
}
