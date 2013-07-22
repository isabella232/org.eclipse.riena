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
package org.eclipse.riena.communication.core.proxyselector;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.communication.core.proxyselector.IProxySelectorExtension;
import org.eclipse.riena.internal.communication.core.proxyselector.ProxySelectorConfiguration;

/**
 * Test the {@code ProxySelectorConfiguration}.
 */
@NonUITestCase
public class ProxySelectorConfigurationTest extends RienaTestCase {

	@SuppressWarnings("restriction")
	public void testNullConfig() {
		final ProxySelector defaultPS = ProxySelector.getDefault();
		final ProxySelectorConfiguration config = new ProxySelectorConfiguration();
		config.configure(null);
		assertSame(defaultPS, ProxySelector.getDefault());
	}

	@SuppressWarnings("restriction")
	public void testOneProxySelectorConfigAndRestore() throws URISyntaxException {
		final ProxySelector defaultPS = ProxySelector.getDefault();
		final ProxySelectorConfiguration config = new ProxySelectorConfiguration();
		config.configure(new IProxySelectorExtension[] { new ProxySelectorExtension(100, "test", new TestProxySelector( //$NON-NLS-1$
				TestUtil.newProxy("test.de"))) }); //$NON-NLS-1$
		assertNotSame(defaultPS, ProxySelector.getDefault());
		assertEquals("test.de", getHost(0)); //$NON-NLS-1$
		config.configure(null);
		assertSame(defaultPS, ProxySelector.getDefault());
	}

	@SuppressWarnings("restriction")
	public void testTwoProxySelectorsWithOrder100and10ConfigAndRestore() throws URISyntaxException {
		final ProxySelector defaultPS = ProxySelector.getDefault();
		final ProxySelectorConfiguration config = new ProxySelectorConfiguration();
		config.configure(new IProxySelectorExtension[] {
				new ProxySelectorExtension(100, "test1", new TestProxySelector(TestUtil.newProxy("test1.de"))), //$NON-NLS-1$ //$NON-NLS-2$
				new ProxySelectorExtension(10, "test2", new TestProxySelector(TestUtil.newProxy("test2.de"))) }); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotSame(defaultPS, ProxySelector.getDefault());
		assertEquals("test2.de", getHost(0)); //$NON-NLS-1$
		assertEquals("test1.de", getHost(1)); //$NON-NLS-1$
		config.configure(null);
		assertSame(defaultPS, ProxySelector.getDefault());
	}

	@SuppressWarnings("restriction")
	public void testTwoProxySelectorsWithOrder10and100ConfigAndRestore() throws URISyntaxException {
		final ProxySelector defaultPS = ProxySelector.getDefault();
		final ProxySelectorConfiguration config = new ProxySelectorConfiguration();
		config.configure(new IProxySelectorExtension[] {
				new ProxySelectorExtension(10, "test1", new TestProxySelector(TestUtil.newProxy("test1.de"))), //$NON-NLS-1$ //$NON-NLS-2$
				new ProxySelectorExtension(100, "test2", new TestProxySelector(TestUtil.newProxy("test2.de"))) }); //$NON-NLS-1$ //$NON-NLS-2$
		assertNotSame(defaultPS, ProxySelector.getDefault());
		assertEquals("test1.de", getHost(0)); //$NON-NLS-1$
		assertEquals("test2.de", getHost(1)); //$NON-NLS-1$
		config.configure(null);
		assertSame(defaultPS, ProxySelector.getDefault());
	}

	private String getHost(final int indexInProxyList) throws URISyntaxException {
		return ((InetSocketAddress) getProxy(indexInProxyList).address()).getHostName();
	}

	private Proxy getProxy(final int indexInProxyList) throws URISyntaxException {
		return ProxySelector.getDefault().select(new URI("http://www.eclipse.org")).get(indexInProxyList); //$NON-NLS-1$
	}

	@SuppressWarnings("restriction")
	private static final class ProxySelectorExtension implements IProxySelectorExtension {

		private final int order;
		private final String name;
		private final ProxySelector proxySelector;

		private ProxySelectorExtension(final int order, final String name, final ProxySelector proxySelector) {
			this.order = order;
			this.name = name;
			this.proxySelector = proxySelector;
		}

		public ProxySelector createProxySelector() {
			return proxySelector;
		}

		public String getName() {
			return name;
		}

		public int getOrder() {
			return order;
		}

	}
}
