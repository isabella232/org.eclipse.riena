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
package org.eclipse.riena.communication.core.factory;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.RemoteFailure;
import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.core.util.Companion;
import org.eclipse.riena.internal.communication.core.Activator;

/**
 * The RemoteServiceBuilder creates, registers and unregisters OSGi Services by
 * Service Declaration. A Service Declaration defines an remote service end
 * point by properties. The RemoteServiceBuilder use the properties to create a
 * proxy reference (see {@link IRemoteServiceFactory}) and register this For
 * this purpose define RemoteServiceBuilder as implementation class within the
 * component declaration and sets follow properties:
 * <p>
 * <ul>
 * <li>'riena.interface' - defines the the interface class of the "remote OSGi
 * Service. The OSGi Service become registered with this name too</li>
 * <li>'riena.remote.path' - defines the context path for the remote service end
 * point</li>
 * <li>'riena.remote.protocol' - defines the web service protocol. See also
 * {@link org.eclipse.riena.communication.core.publisher.IServicePublisher} and
 * {@link IRemoteServiceFactory}
 * <li>
 * </ul>
 * <p>
 * Usage e.g.
 * <p>
 * 
 * &lt;component name=&quot;foo.my.component&quot;
 * immediate=&quot;true&quot;&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;implementation
 * class=&quot;org.eclipse.riena.communication
 * .core.factory.RemoteServiceBuilder&quot;/&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name=&quot;riena.interface&quot;
 * value=&quot;foo.my.service.MyService&quot;/&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name=&quot;riena.remote.path&quot;
 * value=&quot;http://localhost/hessian/MyServiceWS&quot;/&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name=&quot;riena.remote.protocol&quot;
 * value=&quot;hessian&quot;/&gt;<br>
 * &lt;/component&gt;<br>
 */
public class RemoteServiceBuilder {

	private IRemoteServiceRegistration rsReg;

	/**
	 * Create a instance of RemoteServiceBuilder
	 */
	public RemoteServiceBuilder() {
		super();
	}

	/**
	 * Creates a proxy reference for a end point described by Service
	 * Declaration. Register the proxy reference as remote service into the
	 * {@link org.eclipse.riena.communication.core.IRemoteServiceRegistry}. The
	 * "remote" service become registered as OSGi Service within this client
	 * container. The property "riena.interface" defines the interface type and
	 * OSGi Service name. The context holds the declared properties for the
	 * remote end point.
	 * 
	 * @param context
	 */
	protected void activate(final ComponentContext context) {
		final String serviceClassName = (String) context.getProperties().get(RSDPublisherProperties.PROP_INTERFACE);
		try {
			final Class<?> serviceClass = Class.forName(serviceClassName);
			final String path = (String) context.getProperties().get(RSDPublisherProperties.PROP_REMOTE_PATH);
			final String protocol = (String) context.getProperties().get(RSDPublisherProperties.PROP_REMOTE_PROTOCOL);

			rsReg = Companion.per(RemoteServiceFactory.class).createAndRegisterProxy(serviceClass, path, protocol,
					Activator.getDefault().getContext());

		} catch (final ClassNotFoundException e) {
			throw new RemoteFailure("RemoteServiceBuilder could not load class '" + serviceClassName + "'", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Unregister the "remote" service from the
	 * {@link org.eclipse.riena.communication.core.IRemoteServiceRegistry}.
	 * 
	 * @param bundle
	 * @param reg
	 * @param service
	 */
	public void ungetService(final Bundle bundle, final ServiceRegistration reg, final Object service) {
		if (rsReg != null) {
			rsReg.unregister();
			rsReg = null;
		}
	}

}
