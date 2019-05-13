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
package org.eclipse.riena.internal.communication.core.proxyselector;

import java.io.Closeable;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.core.util.IOUtils;

/**
 * The {@code CompoundProxySelector} gathers all the proxies from the given {@code ProxySelector}s from the list given at instance creation.
 */
public class CompoundProxySelector extends ProxySelector {

	private final List<ProxySelector> proxySelectors;
	private final Set<Proxy> failedProxies = Collections.synchronizedSet(new LinkedHashSet<Proxy>());

	/**
	 * The
	 * 
	 * @param proxySelectors
	 */
	public CompoundProxySelector(final List<ProxySelector> proxySelectors) {
		Assert.isLegal(proxySelectors != null, "proxySelectors must not be null."); //$NON-NLS-1$
		this.proxySelectors = proxySelectors;
	}

	@Override
	public List<Proxy> select(final URI uri) {
		Assert.isLegal(uri != null, "uri must not be null."); //$NON-NLS-1$
		final List<Proxy> result = new ArrayList<Proxy>();
		for (final ProxySelector proxySelector : proxySelectors) {
			final List<Proxy> proxies = proxySelector.select(uri);
			if (proxies == null || proxies.size() == 0) {
				continue;
			}
			// we do not copy NO_PROXY entries because a NO_PROXY stops the loop 
			// within {@code java.net.SocksSocketImpl.connect(SocketAddress, int)}
			// instead we add a final NO_PROXY to the end of the list (see below)
			for (final Proxy proxy : proxies) {
				if (proxy != Proxy.NO_PROXY && !result.contains(proxy)) {
					result.add(proxy);
				}
			}

		}
		// in case we have ´failed proxies´ we move them towards the end of the list
		ProxySelectorUtils.resort(result, failedProxies);
		// add a final NO_PROXY
		result.add(Proxy.NO_PROXY);
		return result;
	}

	@Override
	public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
		// we ´resort´ the resulted proxy list by moving the failed proxies to the end of the list (see select) ... 
		final Proxy failed = ProxySelectorUtils.createProxy(uri.getScheme(), sa);
		failedProxies.add(failed);
		// ... and additionally delegate it to all proxy selectors  so that they can do their job.
		// This assumes that they can deal with failed proxies they have not delivered.
		for (final ProxySelector proxySelector : proxySelectors) {
			proxySelector.connectFailed(uri, sa, ioe);
		}
	}

	public void uninstall() {
		// Check for ProxySelectors that implement the optional Closeable interface and close them.
		for (final ProxySelector proxySelector : proxySelectors) {
			if (proxySelector instanceof Closeable) {
				IOUtils.close((Closeable) proxySelector);
			}
		}
	}

}
