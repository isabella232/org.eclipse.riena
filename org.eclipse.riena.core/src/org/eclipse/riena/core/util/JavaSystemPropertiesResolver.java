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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.osgi.framework.FrameworkUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

import org.eclipse.riena.core.RienaLocations;

/**
 * A dynamic variable resolver for Java system properties.
 * <p>
 * Additionally this resolver also resolves the following <i>synthetic</i>
 * properties:
 * <dl>
 * <dt>riena.host.name</dt>
 * <dd>the value of {@code InetAddress.getLocalHost().getHostName()}</dd>
 * <dt>riena.host.address</dt>
 * <dd>the value of {@code InetAddress.getLocalHost().getHostAddress()}</dd>
 * <dt>riena.locations.data</dt>
 * <dd>the Riena default directory for storing data</dd>
 * </dl>
 * 
 * @since 2.0
 */
public class JavaSystemPropertiesResolver implements IDynamicVariableResolver {

	public String resolveValue(final IDynamicVariable variable, final String argument) throws CoreException {
		if (argument == null) {
			return null;
		}
		final String value = lookForSyntheticProperties(argument);
		if (value != null) {
			return value;
		}
		return System.getProperty(argument);
	}

	private String lookForSyntheticProperties(final String argument) throws CoreException {
		if (argument.equals(HOST_ADDRESS)) {
			return getHostAddress();
		} else if (argument.equals(HOST_NAME)) {
			return getHostName();
		} else if (argument.equals(LOCATIONS_DATA)) {
			return getDataLocation();
		}
		return null;
	}

	/**
	 * Prefix for "riena" variables.
	 */
	private static final String RIENA = "riena."; //$NON-NLS-1$

	/**
	 * "Synthetic" property that retrieves the host's IP address, i.e.
	 * {@code InetAddress.getLocalHost().getHostAddress()}
	 */
	private static final String HOST_ADDRESS = RIENA + "host.address"; //$NON-NLS-1$

	/**
	 * "Synthetic" property that retrieves the host name, i.e.
	 * {@code InetAddress.getLocalHost().getHostName()}
	 */
	private static final String HOST_NAME = RIENA + "host.name"; //$NON-NLS-1$

	/**
	 * "Synthetic" property for the default file system directory for storing
	 * data.
	 */
	private static final String LOCATIONS_DATA = RIENA + "locations.data"; //$NON-NLS-1$

	private static final String UNKNOWN = "?"; //$NON-NLS-1$

	private String hostName;

	private String getHostName() {
		if (hostName == null) {
			try {
				hostName = InetAddress.getLocalHost().getHostName();
			} catch (final UnknownHostException e) {
				hostName = UNKNOWN;
			}
		}
		return hostName;
	}

	private String hostAddress;

	private String getHostAddress() {
		if (hostAddress == null) {
			try {
				hostAddress = InetAddress.getLocalHost().getHostAddress();
			} catch (final UnknownHostException e) {
				hostAddress = UNKNOWN;
			}
		}
		return hostAddress;
	}

	private String dataLocation;

	private String getDataLocation() throws CoreException {
		if (dataLocation == null) {
			try {
				dataLocation = RienaLocations.getDataArea().getCanonicalPath();
			} catch (final IOException e) {
				throw new CoreException(new Status(Status.ERROR, FrameworkUtil.getBundle(
						JavaSystemPropertiesResolver.class).getSymbolicName(),
						"Could not resolve Riena default data location", e)); //$NON-NLS-1$
			}
		}
		return dataLocation;
	}
}
