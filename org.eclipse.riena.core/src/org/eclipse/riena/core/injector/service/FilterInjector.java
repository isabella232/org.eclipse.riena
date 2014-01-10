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
package org.eclipse.riena.core.injector.service;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.ServiceReference;

/**
 * The specialized filter injector implementation.
 */
public class FilterInjector extends ServiceInjector {

	private List<ServiceReference> trackedServiceRefs = new ArrayList<ServiceReference>(1);

	FilterInjector(final ServiceDescriptor serviceId, final Object target) {
		super(serviceId, target);
	}

	@Override
	protected void doStart() {
		final ServiceReference[] serviceRefs = getServiceReferences();
		if (serviceRefs != null) {
			for (final ServiceReference serviceRef : serviceRefs) {
				doBind(serviceRef);
			}
		}
	}

	@Override
	protected void doStop() {
		// copy list to temporary array so that adding/removing entries from the list can be concurrently
		final ServiceReference[] serviceRefs;
		synchronized (trackedServiceRefs) {
			serviceRefs = trackedServiceRefs.toArray(new ServiceReference[trackedServiceRefs.size()]);
		}
		for (final ServiceReference serviceRef : serviceRefs) {
			doUnbind(serviceRef);
		}
		trackedServiceRefs = null;
	}

	@Override
	protected void doBind(final ServiceReference serviceRef) {
		synchronized (trackedServiceRefs) {
			if (trackedServiceRefs.contains(serviceRef)) {
				return;
			}
			invokeBindMethod(serviceRef);
			trackedServiceRefs.add(serviceRef);
		}
	}

	@Override
	protected void doUnbind(final ServiceReference serviceRef) {
		synchronized (trackedServiceRefs) {
			if (!trackedServiceRefs.contains(serviceRef)) {
				return;
			}
			invokeUnbindMethod(serviceRef);
			trackedServiceRefs.remove(serviceRef);
		}
	}

}
