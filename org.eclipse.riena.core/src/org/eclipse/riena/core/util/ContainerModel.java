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
package org.eclipse.riena.core.util;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogService;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.core.Activator;

/**
 * This class can be used by other to find out whether the container we are
 * running in is a Riena client or a Riena server. <br>
 * This information may be useful to components that need to behave differently
 * on client and server, e.g. storing objects in a singleton (for client) versa
 * storing them in a ThreadLocal (server). <br>
 * The class can be driven be the system.property "riena.container.model" that
 * can be set to "client" or "server". The ContainerModel also checks to see
 * whether there is a bundle called org.eclipse.equinox.http in which case it
 * assumes a server model. In any case, it creates a LOG_INFO entry to post what
 * it has chosen.
 */
public final class ContainerModel {

	/**
	 * System property defining the container type.
	 */
	public static final String RIENA_CONTAINER_TYPE = "riena.container.type"; //$NON-NLS-1$

	/**
	 * 
	 */
	public static final String ORG_ECLIPSE_EQUINOX_HTTP = "org.eclipse.equinox.http"; //$NON-NLS-1$

	private enum Type {
		CLIENT, SERVER
	};

	private static Type containerType;

	static {
		// This makes the init code available for testing!!
		initialize();
	}

	private ContainerModel() {
		// utility
	}

	/**
	 * Are we running on the client?
	 * 
	 * @return
	 */
	public static boolean isClient() {
		return containerType == Type.CLIENT;
	}

	/**
	 * Are we running on the server?
	 * 
	 * @return
	 */
	public static boolean isServer() {
		return containerType == Type.SERVER;
	}

	private static void initialize() {
		containerType = retrieveContainerType();
		Log4r.getLogger(Activator.getDefault(), ContainerModel.class).log(LogService.LOG_INFO,
				"!!! Riena is running in " + containerType + " mode !!!"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static Type retrieveContainerType() {
		final String s = System.getProperty(RIENA_CONTAINER_TYPE);
		if (s != null) {
			return s.equals("server") ? Type.SERVER : Type.CLIENT; //$NON-NLS-1$
		} else {
			final Bundle[] bundles = Activator.getDefault().getContext().getBundles();
			for (final Bundle bundle : bundles) {
				if (bundle.getSymbolicName().startsWith(ORG_ECLIPSE_EQUINOX_HTTP)) {
					return Type.SERVER;
				}
			}
			return Type.CLIENT;
		}
	}
}
