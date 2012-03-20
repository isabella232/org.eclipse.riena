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
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.communication.core.publisher.IServicePublishBinder;
import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.communication.core.util.CommunicationUtil;
import org.eclipse.riena.core.exception.MurphysLawFailure;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.communication.publisher.Activator;

/**
 * 
 */
public class MultiServicePublisher {

	private String filter;
	private String protocol;

	private IServicePublishBinder binder;

	public MultiServicePublisher() {
		super();
		Inject.service(IServicePublishBinder.class).useRanking().into(this)
				.andStart(Activator.getDefault().getContext());
	}

	public MultiServicePublisher useFilter(final String filter) {
		Assert.isNotNull(filter);
		this.filter = filter;
		return this;
	}

	public MultiServicePublisher withProtocol(final String protocol) {
		Assert.isNotNull(protocol);
		this.protocol = protocol;
		return this;
	}

	public void andStart(final BundleContext context) {
		Assert.isNotNull(filter);

		try {
			final ServiceReference[] refs = context.getServiceReferences((String) null, filter);
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
				if (event.getType() == ServiceEvent.REGISTERED) {
					publish(event.getServiceReference());
				} else {
					if (event.getType() == ServiceEvent.UNREGISTERING) {
						unpublish(event.getServiceReference());
					}
				}
			}
		};
		try {
			Activator.getDefault().getContext().addServiceListener(listener, filter);
		} catch (final InvalidSyntaxException e) {
			throw new MurphysLawFailure("Filter '" + filter + "' has invalid syntax", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void bind(final IServicePublishBinder binder) {
		this.binder = binder;
	}

	public void unbind(final IServicePublishBinder binder) {
		this.binder = null;
	}

	private void publish(final ServiceReference serviceReference) {
		String usedProtocol;
		if (protocol != null) {
			usedProtocol = protocol;
		} else {
			usedProtocol = CommunicationUtil.accessProperty(
					serviceReference.getProperty(RSDPublisherProperties.PROP_REMOTE_PROTOCOL), null);
		}
		final String path = CommunicationUtil.accessProperty(
				serviceReference.getProperty(RSDPublisherProperties.PROP_REMOTE_PATH), null);
		binder.publish(serviceReference, path, usedProtocol);
	}

	private void unpublish(final ServiceReference serviceReference) {
		binder.unpublish(serviceReference);
	}

}
