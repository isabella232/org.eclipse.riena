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
package org.eclipse.riena.monitor.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.internal.monitor.client.Activator;

/**
 * This is a {@code IClientInfoProvider} that simply collects a few java system
 * properties.
 * <p>
 * The java system properties have to be given as a comma separated list within
 * the extension definition for this client info provider. There a few
 * ´synthetic´ properties that retrieve information about the host:
 * <ul>
 * <li>x-host.address - retrieves the host's ip address as given by
 * InetAddress.getLocalHost().getHostAddress()</li>
 * <li>x-host.name - retrieves the host's name as given by
 * InetAddress.getLocalHost().getHostName()</li>
 * <li>x-host.canonicalname - retrieves the host's canonical name as given by
 * InetAddress.getLocalHost().getCanonicalHostName()</li>
 * </ul>
 * For example:
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.riena.monitor.client.clientInfoProvider&quot;&gt;
 *       &lt;clientInfoProvider
 *             name=&quot;SimpleClientInfoProvider&quot;
 *             class=&quot;org.eclipse.riena.monitor.client.SimpleClientInfoProvider:user.name,x-host.name&quot;&gt;
 *       &lt;/clientInfoProvider&gt;
 * &lt;/extension&gt;
 * </pre>
 */
public class SimpleClientInfoProvider implements IClientInfoProvider, IExecutableExtension {

	/**
	 * ´Synthetic´ property that retrieves the hosts ip address, i.e.
	 * {@code InetAddress.getLocalHost().getHostAddress()}
	 */
	public static final String HOST_ADDRESS = "x-host.address"; //$NON-NLS-1$

	/**
	 * ´Synthetic´ property that retrieves the canonical host name, i.e.
	 * {@code InetAddress.getLocalHost().getCanonicalHostName()}
	 */
	public static final String HOST_CANONICALNAME = "x-host.canonicalname"; //$NON-NLS-1$

	/**
	 * ´Synthetic´ property that retrieves the host name, i.e.
	 * {@code InetAddress.getLocalHost().getHostName()}
	 */
	public static final String HOST_NAME = "x-host.name"; //$NON-NLS-1$

	private String clientInfo;
	private static final String UNKNOWN = "?"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.IClientInfoProvider#getClientInfo()
	 */
	public String getClientInfo() {
		return clientInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org
	 * .eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data)
			throws CoreException {
		try {
			clientInfo = getClientInfo(PropertiesUtils.asArray(data));
		} catch (final IllegalArgumentException e) {
			throw configurationException("Bad configuration.", e); //$NON-NLS-1$
		}
	}

	protected String getClientInfo(final String[] properties) {
		final StringBuilder bob = new StringBuilder();
		for (final String property : properties) {
			bob.append(property).append('=').append(getProperty(property)).append(',');
		}
		if (bob.length() > 0) {
			bob.setLength(bob.length() - 1);
		}
		return bob.toString();
	}

	protected String getProperty(final String property) {
		if (HOST_NAME.equals(property)) {
			try {
				return InetAddress.getLocalHost().getHostName();
			} catch (final UnknownHostException e) {
				return UNKNOWN;
			}
		} else if (HOST_CANONICALNAME.equals(property)) {
			try {
				return InetAddress.getLocalHost().getCanonicalHostName();
			} catch (final UnknownHostException e) {
				return UNKNOWN;
			}
		} else if (HOST_ADDRESS.equals(property)) {
			try {
				return InetAddress.getLocalHost().getHostAddress();
			} catch (final UnknownHostException e) {
				return UNKNOWN;
			}
		}
		return System.getProperty(property, UNKNOWN);
	}

	private CoreException configurationException(final String message, final Exception e) {
		return new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

}
