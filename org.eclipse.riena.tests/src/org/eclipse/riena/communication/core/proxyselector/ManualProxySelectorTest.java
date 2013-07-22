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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Test the {@code ManualProxySelector}.
 */
@NonUITestCase
public class ManualProxySelectorTest extends TestCase {

	private static Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("test1", 8080)); //$NON-NLS-1$
	private static Proxy proxy2 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("test2", 8080)); //$NON-NLS-1$
	private static Proxy proxy3 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("test3", 8080)); //$NON-NLS-1$
	private static Proxy proxy4 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("test4", 8080)); //$NON-NLS-1$
	private static URI http;
	private static URI socks;
	static {
		try {
			http = new URI("http://web.de"); //$NON-NLS-1$
			socks = new URI("socks://web.de"); //$NON-NLS-1$
		} catch (final URISyntaxException e) {
			fail(e.toString());
		}
	}

	public void testDirect() throws CoreException {
		final ManualProxySelector selector = new ManualProxySelector();
		final List<Proxy> proxies = selector.select(http);
		assertEquals(1, proxies.size());
		assertEquals(Proxy.NO_PROXY, proxies.get(0));
	}

	public void testUniversal() throws CoreException {
		final ManualProxySelector selector = new ManualProxySelector();
		selector.setInitializationData(null, null, "http://test1:8080,http://test2:8080"); //$NON-NLS-1$
		final List<Proxy> proxies = selector.select(http);
		assertEquals(2, proxies.size());
		assertEquals(proxy1, proxies.get(0));
		assertEquals(proxy2, proxies.get(1));
	}

	public void testSpecific() throws CoreException {
		final ManualProxySelector selector = new ManualProxySelector();
		selector.setInitializationData(null, null, "http=http://test1:8080,http=http://test2:8080"); //$NON-NLS-1$
		List<Proxy> proxies = selector.select(http);
		assertEquals(2, proxies.size());
		assertEquals(proxy1, proxies.get(0));
		assertEquals(proxy2, proxies.get(1));

		proxies = selector.select(socks);
		assertEquals(1, proxies.size());
	}

	public void testUniversalAndSpecific() throws CoreException {
		final ManualProxySelector selector = new ManualProxySelector();
		selector.setInitializationData(null, null,
				"http://test1:8080,http://test2:8080,http=http://test3:8080,http=http://test4:8080"); //$NON-NLS-1$
		List<Proxy> proxies = selector.select(http);
		assertEquals(2, proxies.size());
		assertEquals(proxy3, proxies.get(0));
		assertEquals(proxy4, proxies.get(1));

		proxies = selector.select(socks);
		assertEquals(1, proxies.size());
		assertEquals(Proxy.NO_PROXY, proxies.get(0));
	}

	public void testConfigurationBySystemProperty() throws CoreException {
		System.setProperty(ManualProxySelector.RIENA_MANUAL_PROXIES, "http://test1:8080,http://test2:8080"); //$NON-NLS-1$
		final ManualProxySelector selector = new ManualProxySelector();
		selector.setInitializationData(null, null, "http://test3:8080;http://test4:8080"); //$NON-NLS-1$
		final List<Proxy> proxies = selector.select(http);
		assertEquals(2, proxies.size());
		assertEquals(proxy1, proxies.get(0));
		assertEquals(proxy2, proxies.get(1));
		System.clearProperty(ManualProxySelector.RIENA_MANUAL_PROXIES);
	}
}
