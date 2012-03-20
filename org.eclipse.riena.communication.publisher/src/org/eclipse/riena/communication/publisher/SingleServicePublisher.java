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
package org.eclipse.riena.communication.publisher;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.communication.core.publisher.IServicePublishBinder;
import org.eclipse.riena.core.exception.MurphysLawFailure;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.communication.publisher.Activator;

/**
 * 
 */
public class SingleServicePublisher {

	private final String serviceName;
	private String filter;
	private BundleContext context;
	private String path;
	private String protocol;

	private IServicePublishBinder binder;

	public SingleServicePublisher(final String name) {
		super();
		this.serviceName = name;
		Inject.service(IServicePublishBinder.class).useRanking().into(this)
				.andStart(Activator.getDefault().getContext());
	}

	public SingleServicePublisher useFilter(final String filter) {
		Assert.isNotNull(filter);
		this.filter = filter;
		return this;
	}

	public SingleServicePublisher usingPath(final String path) {
		Assert.isNotNull(path);
		this.path = path;
		return this;
	}

	public SingleServicePublisher withProtocol(final String protocol) {
		Assert.isNotNull(protocol);
		this.protocol = protocol;
		return this;
	}

	public void andStart(final BundleContext context) {
		this.context = context;
		Assert.isNotNull(path);
		Assert.isNotNull(protocol);

		try {
			final ServiceReference[] refs = this.context.getServiceReferences(serviceName, filter);
			if (refs != null) {
				for (final ServiceReference ref : refs) {
					publish(ref);
				}
			}
		} catch (final InvalidSyntaxException e) {
			throw new MurphysLawFailure("Filter '" + filter + "' has invalid syntax", e); //$NON-NLS-1$ //$NON-NLS-2$
		}

		final ServiceListener listener = new ServiceListener() {
			public void serviceChanged(final ServiceEvent event) {
				final String[] serviceInterfaces = (String[]) event.getServiceReference().getProperty(
						Constants.OBJECTCLASS);
				for (final String serviceInterf : serviceInterfaces) {
					if (serviceInterf.equals(serviceName)) {
						if (event.getType() == ServiceEvent.REGISTERED) {
							publish(event.getServiceReference());
						} else {
							if (event.getType() == ServiceEvent.UNREGISTERING) {
								unpublish(event.getServiceReference());
							}
						}
					}
				}
			}
		};
		try {
			Activator.getDefault().getContext().addServiceListener(listener, filter);
		} catch (final InvalidSyntaxException e) {
			throw new MurphysLawFailure("Filter '" + filter + "' has invalid syntax", e); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return;
	}

	public void bind(final IServicePublishBinder binder) {
		this.binder = binder;
	}

	public void unbind(final IServicePublishBinder binder) {
		this.binder = null;
	}

	private void publish(final ServiceReference serviceReference) {
		binder.publish(serviceReference, path, protocol);
	}

	private void unpublish(final ServiceReference serviceReference) {
		binder.unpublish(serviceReference);
	}

}
