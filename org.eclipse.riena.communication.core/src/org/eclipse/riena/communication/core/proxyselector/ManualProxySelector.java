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
package org.eclipse.riena.communication.core.proxyselector;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.riena.internal.communication.core.Activator;
import org.eclipse.riena.internal.communication.core.proxyselector.ProxySelectorUtils;

/**
 * The {@code ManualProxySelector} allows to define manual proxy settings (of
 * course).<br>
 * The manual proxies can be defined with either a system property
 * ("riena.manualproxies") or with configuration data given within an extension
 * to extension point ""org.eclipse.riena.communication.core.proxyselector"".
 * Should both be given the system property has precedence (i.e. overrides the
 * configuration data). <br>
 * However, for both cases the syntax is identical<br>
 * <code><pre>
 *  proxyDef := "" | proxy | {"," proxy} 
 *  proxy := [&lt;scheme&gt;=][&lt;scheme&gt; "://" ]&lt;server&gt;[ ":" &lt;port&gt;]
 * </pre></code>
 */
public class ManualProxySelector extends ProxySelector implements IExecutableExtension {

	public final static String RIENA_MANUAL_PROXIES = "riena.manualproxies"; //$NON-NLS-1$

	private final List<Proxy> universalProxies = new ArrayList<Proxy>();
	private final Map<String, List<Proxy>> protocolSpecificProxies = new HashMap<String, List<Proxy>>();

	public ManualProxySelector() throws CoreException {
		final String data = System.getProperty(RIENA_MANUAL_PROXIES);
		setInitializationData(null, null, data);
	}

	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data)
			throws CoreException {

		if (data == null || !(data instanceof String)
				|| !(universalProxies.isEmpty() && protocolSpecificProxies.isEmpty())) {
			return;
		}
		try {
			ProxySelectorUtils.fillProxies((String) data, universalProxies, protocolSpecificProxies);
		} catch (final IllegalArgumentException e) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Bad configuration.", e)); //$NON-NLS-1$
		}
	}

	@Override
	public List<Proxy> select(final URI uri) {

		if (!protocolSpecificProxies.isEmpty()) {
			final List<Proxy> proxies = protocolSpecificProxies.get(uri.getScheme());
			return proxies != null ? proxies : ProxySelectorUtils.NO_PROXY_LIST;
		} else if (!universalProxies.isEmpty()) {
			return universalProxies;
		} else {
			return ProxySelectorUtils.NO_PROXY_LIST;
		}
	}

	@Override
	public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
		// Nothing yet
	}
}
