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
package org.eclipse.riena.core.ping;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * The {@code Sonar} catches up all pingable services and visits them.
 */
public final class Sonar {

	private Sonar() {
		// utility
	}

	/**
	 * Ping all reachable services.
	 * 
	 * @return the List of all {@code PingResult}s
	 */
	public static List<PingResult> pingAllServices() {

		PingVisitor visitor = new PingVisitor();

		// fetch ALL services
		final BundleContext context = FrameworkUtil.getBundle(Sonar.class).getBundleContext();
		ServiceReference[] allServiceReferences;
		try {
			allServiceReferences = context.getAllServiceReferences(null, null);
		} catch (final InvalidSyntaxException e) {
			throw new RuntimeException("This should never happen. We do not use a filter.", e); //$NON-NLS-1$
		}

		// no services at all :-(
		if (allServiceReferences == null) {
			return null;
		}

		// filter the pingable services and ping'em
		for (final ServiceReference serviceReference : allServiceReferences) {
			final Object service = context.getService(serviceReference);
			if (service instanceof IPingable) {
				final IPingable pingable = (IPingable) service;
				visitor = pingable.ping(visitor);
			}
		}

		return visitor.getPingResults();
	}
}
