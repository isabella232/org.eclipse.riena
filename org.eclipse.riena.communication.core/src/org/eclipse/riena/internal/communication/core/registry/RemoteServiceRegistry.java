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
package org.eclipse.riena.internal.communication.core.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.communication.core.IRemoteServiceReference;
import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.communication.core.factory.ProxyAlreadyRegisteredFailure;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.communication.core.Activator;

/**
 * TODO Documentation
 */
public class RemoteServiceRegistry implements IRemoteServiceRegistry {

	// outside start() and stop() this field should only be accessed through the method getRegisteredServices() 
	private volatile Map<String, IRemoteServiceRegistration> registeredServices;

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), RemoteServiceRegistry.class);

	public synchronized void start() {
		registeredServices = Collections.synchronizedMap(new HashMap<String, IRemoteServiceRegistration>());
	}

	public synchronized void stop() {
		final Map<String, IRemoteServiceRegistration> tempRegisteredServices = registeredServices;
		registeredServices = null;
		for (final Map.Entry<String, IRemoteServiceRegistration> entry : tempRegisteredServices.entrySet()) {
			unregisterService(entry.getValue().getReference());
		}
	}

	public IRemoteServiceRegistration registerService(final IRemoteServiceReference reference,
			final BundleContext context) {
		final String url = reference.getDescription().getURL();
		final IRemoteServiceRegistration foundRemoteServiceReg = getRegisteredServices().get(url);
		if (foundRemoteServiceReg != null) {
			throw new ProxyAlreadyRegisteredFailure(
					"Cannot register two remote service proxies with the same URL. Proxy for " + url //$NON-NLS-1$
							+ " already exists. Registered by bundle " //$NON-NLS-1$
							+ foundRemoteServiceReg.getReference().getContext().getBundle().getSymbolicName() + "."); //$NON-NLS-1$
		}
		// it is a new entry, set properties
		final Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put("service.url", url); //$NON-NLS-1$
		props.put("service.protocol", reference.getDescription().getProtocol()); //$NON-NLS-1$
		final ServiceRegistration serviceRegistration = context.registerService(
				reference.getServiceInterfaceClassName(), reference.getServiceInstance(), props);
		try {
			Activator.getDefault().getContext().addServiceListener(new ServiceListener() {

				public void serviceChanged(final ServiceEvent event) {
					if (event.getType() == ServiceEvent.UNREGISTERING) {
						getRegisteredServices().remove(url);
						Activator.getDefault().getContext().removeServiceListener(this);
					}
				}
			}, "(service.id=" + serviceRegistration.getReference().getProperty("service.id") + ")"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		} catch (final InvalidSyntaxException e) {
			LOGGER.log(LogService.LOG_WARNING, "Filter is unexpectedly wrong.", e); //$NON-NLS-1$
		}
		reference.setContext(context);
		reference.setServiceRegistration(serviceRegistration);

		final RemoteServiceRegistration remoteServiceReg = new RemoteServiceRegistration(reference, this);
		registeredServices.put(url, remoteServiceReg);

		LOGGER.log(LogService.LOG_DEBUG, "OSGi NEW service registered id: " //$NON-NLS-1$
				+ reference.getServiceInterfaceClassName());
		return remoteServiceReg;
	}

	public void unregisterService(final IRemoteServiceReference reference) {
		Assert.isNotNull(reference, "RemoteServiceReference must not be null"); //$NON-NLS-1$
		getRegisteredServices().remove(reference.getURL());
		reference.getServiceRegistration().unregister();
		final String serviceClassName = reference.getServiceInterfaceClassName();
		reference.dispose();
		LOGGER.log(LogService.LOG_DEBUG, "OSGi service removed: " + serviceClassName); //$NON-NLS-1$
	}

	public List<IRemoteServiceRegistration> registeredServices(final BundleContext context) {
		final List<IRemoteServiceRegistration> result = new ArrayList<IRemoteServiceRegistration>();
		final List<IRemoteServiceRegistration> all = new ArrayList<IRemoteServiceRegistration>(getRegisteredServices()
				.values());
		for (final IRemoteServiceRegistration remoteServiceRegistration : all) {
			if (context == null || context.equals(remoteServiceRegistration.getReference().getContext())) {
				result.add(remoteServiceRegistration);
			}
		}
		return result;
	}

	/**
	 * only meant for testcase that directly access the implementation
	 * 
	 * @param url
	 * @return
	 */
	public boolean hasServiceForUrl(final String url) {
		return getRegisteredServices().get(url) != null;
	}

	/**
	 * Prevents simple NPEs in case that the registry has been stopped or not
	 * yet started.
	 * 
	 * @return the registry mapped
	 */
	private Map<String, IRemoteServiceRegistration> getRegisteredServices() {
		final Map<String, IRemoteServiceRegistration> temp = registeredServices;
		if (temp == null) {
			throw new IllegalStateException("RemoteServiceRegistry is either not started or already stopped!"); //$NON-NLS-1$
		}
		return temp;
	}
}
