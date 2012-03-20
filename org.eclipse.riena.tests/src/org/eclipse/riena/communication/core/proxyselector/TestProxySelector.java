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
import java.util.List;

/**
 * A simple mock for testing the {@code CompoundProxySelector}
 */
public class TestProxySelector extends ProxySelector {

	private final Proxy proxy;
	private URI uri;

	public TestProxySelector(final Proxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
		this.uri = uri;
	}

	@Override
	public List<Proxy> select(final URI uri) {
		final List<Proxy> result = new ArrayList<Proxy>();
		result.add(proxy);
		result.add(Proxy.NO_PROXY);
		return result;
	}

	URI getFailedURI() {
		return uri;
	}
}
