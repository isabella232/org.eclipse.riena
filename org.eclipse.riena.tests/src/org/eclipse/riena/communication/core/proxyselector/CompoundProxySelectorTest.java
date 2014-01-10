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
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.internal.communication.core.proxyselector.CompoundProxySelector;

/**
 * Test the {@code CompoundProxySelector}.
 */
@NonUITestCase
public class CompoundProxySelectorTest extends RienaTestCase {

	private static URI uri;
	private final static Proxy PROXY_1 = TestUtil.newProxy("test1.de"); //$NON-NLS-1$
	private final static Proxy PROXY_2 = TestUtil.newProxy("test2.de"); //$NON-NLS-1$

	static {
		try {
			uri = new URI("http://www.eclipse.org"); //$NON-NLS-1$
		} catch (final URISyntaxException e) {
			fail();
		}
	}

	@SuppressWarnings("restriction")
	public void testNullSelectors() {
		try {
			new CompoundProxySelector(null);
			fail();
		} catch (final IllegalArgumentException e) {
			Nop.reason("ok - expected"); //$NON-NLS-1$
		}
	}

	@SuppressWarnings("restriction")
	public void testNullSelect() {
		final CompoundProxySelector compoundPS = new CompoundProxySelector(new ArrayList<ProxySelector>());
		try {
			compoundPS.select(null);
			fail();
		} catch (final IllegalArgumentException e) {
			Nop.reason("ok - expected"); //$NON-NLS-1$
		}
	}

	@SuppressWarnings("restriction")
	public void testSelectWithOneSelector() {
		final List<ProxySelector> selectors = new ArrayList<ProxySelector>();
		selectors.add(new TestProxySelector(PROXY_1));
		final CompoundProxySelector compoundPS = new CompoundProxySelector(selectors);
		final List<Proxy> proxies = compoundPS.select(uri);
		assertEquals(PROXY_1, proxies.get(0));
		assertEquals(Proxy.NO_PROXY, proxies.get(1));
	}

	@SuppressWarnings("restriction")
	public void testSelectWithTwoSelectors() {
		final List<ProxySelector> selectors = new ArrayList<ProxySelector>();
		selectors.add(new TestProxySelector(PROXY_1));
		selectors.add(new TestProxySelector(PROXY_2));
		final CompoundProxySelector compoundPS = new CompoundProxySelector(selectors);
		final List<Proxy> proxies = compoundPS.select(uri);
		assertEquals(PROXY_1, proxies.get(0));
		assertEquals(PROXY_2, proxies.get(1));
		assertEquals(Proxy.NO_PROXY, proxies.get(2));
	}

	@SuppressWarnings("restriction")
	public void testConnectFailedWithTwoSelectors() {
		final List<ProxySelector> selectors = new ArrayList<ProxySelector>();
		final TestProxySelector selector1 = new TestProxySelector(PROXY_1);
		selectors.add(selector1);
		final TestProxySelector selector2 = new TestProxySelector(PROXY_2);
		selectors.add(selector2);
		final CompoundProxySelector compoundPS = new CompoundProxySelector(selectors);
		List<Proxy> proxies = compoundPS.select(uri);
		assertEquals(3, proxies.size());
		assertEquals(PROXY_1, proxies.get(0));
		assertEquals(PROXY_2, proxies.get(1));
		assertEquals(Proxy.NO_PROXY, proxies.get(2));
		compoundPS.connectFailed(uri, PROXY_1.address(), new IOException());
		assertEquals(uri, selector1.getFailedURI());
		assertEquals(uri, selector2.getFailedURI());
		proxies = compoundPS.select(uri);
		assertEquals(3, proxies.size());
		assertEquals(PROXY_2, proxies.get(0));
		assertEquals(PROXY_1, proxies.get(1));
		assertEquals(Proxy.NO_PROXY, proxies.get(2));
	}
}
