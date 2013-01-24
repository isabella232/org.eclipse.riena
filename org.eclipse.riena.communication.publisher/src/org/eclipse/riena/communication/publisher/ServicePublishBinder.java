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
package org.eclipse.riena.communication.publisher;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.publisher.IServicePublishBinder;
import org.eclipse.riena.communication.core.publisher.IServicePublisher;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.internal.communication.publisher.Activator;
import org.eclipse.riena.internal.communication.publisher.ServiceHooksProxy;

/**
 * The class publishes all services that are existing as life services in the
 * OSGi Registry as web service endpoints.
 * 
 */
public class ServicePublishBinder implements IServicePublishBinder {

	/**
	 * contains a map of available publishers per protocol
	 */
	private final Map<String, IServicePublisher> servicePublishers = new HashMap<String, IServicePublisher>();
	/**
	 * contains services that are not yet published (due to missing publishers)
	 */
	private final List<RemoteServiceDescription> unpublishedServices = new ArrayList<RemoteServiceDescription>();
	/**
	 * contains registered published Services
	 */
	private final Map<String, RemoteServiceDescription> rsDescs = new HashMap<String, RemoteServiceDescription>();

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ServicePublishBinder.class);

	public ServicePublishBinder() {
		super();
	}

	@InjectService
	public void bind(final IServicePublisher publisher) {
		servicePublishers.put(publisher.getProtocol(), publisher);
		if (unpublishedServices.size() > 0) {
			LOGGER.log(LogService.LOG_DEBUG, "servicePublish=" + publisher.getProtocol() //$NON-NLS-1$
					+ " REGISTER...publishing all services that were waiting for him"); //$NON-NLS-1$
		} else {
			LOGGER.log(LogService.LOG_DEBUG, "servicePublish=" + publisher.getProtocol() //$NON-NLS-1$
					+ " REGISTER...no unpublished services waiting for this protocol"); //$NON-NLS-1$

		}

		// check for services which are missing a publisher
		checkUnpublishedServices(publisher.getProtocol());
	}

	public void unbind(final IServicePublisher publisher) {
		final String protocol = publisher.getProtocol();
		LOGGER.log(LogService.LOG_DEBUG, "servicePublish=" + publisher.getProtocol() //$NON-NLS-1$
				+ " UNREGISTER...unpublishing all its services"); //$NON-NLS-1$
		// unregister all web services for this type

		// for (RemoteServiceDescription rsDesc : rsDescs.values()) {
		// if (protocol.equals(rsDesc.getProtocol())) {
		// unpublish(rsDesc);
		// }
		// }
		servicePublishers.remove(protocol);
	}

	private void checkUnpublishedServices(final String protocol) {
		final List<RemoteServiceDescription> removedItems = new ArrayList<RemoteServiceDescription>();
		for (final RemoteServiceDescription rsd : unpublishedServices) {
			if (rsd.getProtocol().equals(protocol)) {
				publish(rsd);
				removedItems.add(rsd);
			}
		}
		for (final RemoteServiceDescription item : removedItems) {
			unpublishedServices.remove(item);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.publisher.IServicePublishBinder#publish
	 * (org.osgi.framework.ServiceReference, java.lang.String, java.lang.String)
	 */
	public void publish(final ServiceReference ref, final String url, final String protocol) {
		final String[] interfaces = (String[]) ref.getProperty(Constants.OBJECTCLASS);
		Assert.isLegal(interfaces.length == 1, "OSGi service registrations only with one interface supported"); //$NON-NLS-1$
		final String interfaceName = interfaces[0];
		publish(interfaceName, ref, url, protocol);
	}

	public void unpublish(final ServiceReference serviceRef) {
		for (final RemoteServiceDescription rsd : rsDescs.values()) {
			if (serviceRef.equals(rsd.getServiceRef())) {
				final IServicePublisher servicePublisher = servicePublishers.get(rsd.getProtocol());
				if (servicePublisher != null) {
					servicePublisher.unpublishService(rsd);
				}
				rsDescs.remove(rsd.getProtocol() + "::" + rsd.getPath()); //$NON-NLS-1$
				return;
			}
		}
	}

	public void publish(final String interfaceName, final ServiceReference serviceRef, final String path,
			final String protocol) {
		try {
			publish( new RemoteServiceDescription(interfaceName, serviceRef, path, protocol));
		} catch (final ClassNotFoundException e) {
			LOGGER.log(LogService.LOG_WARNING,
					"Could not load class for remote service interface for service reference" + serviceRef, e); //$NON-NLS-1$
		}
	}

	private void publish(final RemoteServiceDescription rsd) {
		synchronized (rsDescs) {

			final ServiceHooksProxy handler = new ServiceHooksProxy(rsd.getService());
			final Object service = Proxy.newProxyInstance(rsd.getServiceClassLoader(),
					new Class[] { rsd.getServiceInterfaceClass() }, handler);
			handler.setRemoteServiceDescription(rsd);
			final RemoteServiceDescription rsDescFound = rsDescs.get(getRSDKey(rsd));
			if (rsDescFound != null) {
				LOGGER.log(LogService.LOG_WARNING, "A service endpoint with path=[" + rsd.getPath() //$NON-NLS-1$
						+ "] and remoteType=[" + rsd.getProtocol() + "] already published... ignored"); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}

			if (rsd.getPath() == null) {
				LOGGER.log(LogService.LOG_WARNING, "no path for service: " + service.toString() //$NON-NLS-1$
						+ " Service not published remote"); //$NON-NLS-1$
				return;
			}

			final IServicePublisher servicePublisher = servicePublishers.get(rsd.getProtocol());
			if (servicePublisher == null) {
				LOGGER.log(LogService.LOG_INFO, "no publisher found for protocol " + rsd.getProtocol()); //$NON-NLS-1$
				unpublishedServices.add(rsd);
				return;
			}
			rsd.setService(service);
			String url = null;
			try {
				url = servicePublisher.publishService(rsd);
			} catch (final RuntimeException e) {
				LOGGER.log(LogService.LOG_ERROR, e.getMessage());
				return;
			}
			// set full URL under which the service is available
			rsd.setURL(url);
			handler.setMessageContextAccessor(servicePublisher.getMessageContextAccessor());
			rsDescs.put(getRSDKey(rsd), rsd);
			LOGGER.log(LogService.LOG_DEBUG, "service endpoints count: " + rsDescs.size()); //$NON-NLS-1$

		}

	}

	private String getRSDKey(final RemoteServiceDescription rsd) {
		return rsd.getProtocol() + "::" + rsd.getPath(); //$NON-NLS-1$
	}

	public RemoteServiceDescription[] getAllServices() {
		final RemoteServiceDescription[] result = new RemoteServiceDescription[rsDescs.size()];
		synchronized (rsDescs) {
			rsDescs.values().toArray(result);
		}
		return result;
	}

}
