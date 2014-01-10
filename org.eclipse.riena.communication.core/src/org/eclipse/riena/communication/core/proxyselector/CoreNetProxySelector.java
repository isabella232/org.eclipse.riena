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
package org.eclipse.riena.communication.core.proxyselector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.log.LogService;

import org.eclipse.core.internal.net.ProxyData;
import org.eclipse.core.internal.net.ProxyManager;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.communication.core.Activator;
import org.eclipse.riena.internal.communication.core.proxyselector.ProxySelectorUtils;

/**
 * This {@code ProxySelector} utilizes the {@code ProxyManager}.<br>
 * <b>Note:</b> When using this {@code ProxySelector} do not forget to add the
 * optional required bundle org.eclipse.core.net and the accompanying
 * os-specific fragment bundle.
 */
@SuppressWarnings("restriction")
public class CoreNetProxySelector extends ProxySelector {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), CoreNetProxySelector.class);

	public CoreNetProxySelector() {
		ProxyManager.getProxyManager().setProxiesEnabled(true);
		ProxyManager.getProxyManager().setSystemProxiesEnabled(true);
	}

	@Override
	public List<Proxy> select(final URI uri) {
		Assert.isLegal(uri != null, "uri must not be null."); //$NON-NLS-1$
		final IProxyData[] proxyDatas = ProxyManager.getProxyManager().select(uri);
		if (proxyDatas == null || proxyDatas.length == 0) {
			return ProxySelectorUtils.NO_PROXY_LIST;
		}
		final List<Proxy> proxies = new ArrayList<Proxy>(proxyDatas.length);
		for (final IProxyData proxyData : proxyDatas) {
			final Type type = getProxyType(proxyData);
			if (type != null) {
				final InetSocketAddress address = InetSocketAddress.createUnresolved(proxyData.getHost(),
						proxyData.getPort());
				proxies.add(new Proxy(type, address));
			}
		}
		proxies.add(Proxy.NO_PROXY);
		return proxies;
	}

	private Type getProxyType(final IProxyData proxyData) {
		if (proxyData.getType().equals(ProxyData.HTTP_PROXY_TYPE)
				|| proxyData.getType().equals(ProxyData.HTTPS_PROXY_TYPE)) {
			return Type.HTTP;
		} else if (proxyData.getType().equals(ProxyData.SOCKS_PROXY_TYPE)) {
			return Type.SOCKS;
		}
		LOGGER.log(LogService.LOG_WARNING, "Yet unknown proxy type: " + proxyData.getType() + ". " //$NON-NLS-1$ //$NON-NLS-2$
				+ CoreNetProxySelector.class.getName() + " needs to be extended!"); //$NON-NLS-1$
		return null;
	}

	@Override
	public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
		// TODO The core.net IProxyService has not yet support for this!
		LOGGER.log(LogService.LOG_DEBUG, "Attempt to connect to uri: " + uri + " on proxy " + sa + " failed.", ioe); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
