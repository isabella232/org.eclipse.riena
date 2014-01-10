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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Helper for dealing with proxies and proxy definition strings.
 */
public final class ProxySelectorUtils {

	static final private int PROXY_DEFAULT_PORT = 80;
	static final private int HTTPPROXY_DEFAULT_PORT = PROXY_DEFAULT_PORT;
	static final private int HTTPSPROXY_DEFAULT_PORT = 443;
	static final private int SOCKSPROXY_DEFAULT_PORT = 1080;

	public final static List<Proxy> NO_PROXY_LIST = Collections.singletonList(Proxy.NO_PROXY);
	public final static List<Proxy> EMPTY_PROXY_LIST = Collections.emptyList();

	private ProxySelectorUtils() {
		// utility
	}

	/**
	 * Scan the proxy definition string and fill this information in the correct
	 * list or map. <br>
	 * The proxy definition is of the form:<br>
	 * <code><pre>
	 *  proxyDef := "" | proxy | {"," proxy} 
	 *  proxy := [&lt;scheme&gt;=][&lt;scheme&gt; "://" ]&lt;server&gt;[ ":" &lt;port&gt;]
	 * </pre></code>
	 * 
	 * @param proxyDefinitions
	 * @param universalProxies
	 * @param protocolSpecificProxies
	 */
	public static void fillProxies(final String proxyDefinitions, final List<Proxy> universalProxies,
			final Map<String, List<Proxy>> protocolSpecificProxies) {
		final Scanner scanner = new Scanner(proxyDefinitions);
		scanner.useDelimiter(","); //$NON-NLS-1$
		while (scanner.hasNext()) {
			fillProxy(scanner.next(), universalProxies, protocolSpecificProxies);
		}
	}

	public static void fillProxy(final String proxyDefinition, final List<Proxy> universalProxies,
			final Map<String, List<Proxy>> protocolSpecificProxies) {
		String protocol = null;
		String host = null;
		int port = 0;

		int urlStart = 0;
		// if there is no '=' character within the proxy definition we have a proxy
		// definition that serves all protocols. In this case we MUST ignore the protocol,
		// otherwise the protocol MUST be used to determine the specific proxy settings
		if (proxyDefinition.indexOf("=") != -1) { //$NON-NLS-1$
			protocol = proxyDefinition.substring(0, proxyDefinition.indexOf("=")); //$NON-NLS-1$
			urlStart = proxyDefinition.indexOf("=") + 1; //$NON-NLS-1$
		}

		try {
			// The scheme of the uri is irrelevant. We add the http://
			// scheme to enable class URI to parse the stuff
			String augmentedURI = proxyDefinition.substring(urlStart);
			if (augmentedURI.indexOf("://") == -1) { //$NON-NLS-1$
				augmentedURI = "http://" + augmentedURI; //$NON-NLS-1$
			}
			final URI uri = new URI(augmentedURI);
			host = uri.getHost();
			port = uri.getPort() > 0 ? uri.getPort() : getProxyDefaultPort(protocol);
		} catch (final Exception ex) {
			throw new ProxySelectorUtils.IllegalFormatException(
					"not a valid proxy definition: '" + proxyDefinition + "'.", ex); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (host == null) {
			throw new ProxySelectorUtils.IllegalFormatException("not a valid proxy definition: '" + proxyDefinition //$NON-NLS-1$
					+ "'."); //$NON-NLS-1$
		}

		if (protocol == null) {
			universalProxies.add(createProxy(Proxy.Type.HTTP, host, port));
		} else {
			addProtocolSpecificProxy(protocolSpecificProxies, protocol,
					createProxy(resolveProxyType(protocol), host, port));
		}
	}

	/**
	 * Resort proxies such that we shuffle the failed proxies to the end of the
	 * list.
	 * 
	 * @param proxies
	 * @param failedProxies
	 */
	public static void resort(final Collection<Proxy> proxies, final Collection<Proxy> failedProxies) {
		if (failedProxies.isEmpty()) {
			return;
		}
		final List<Proxy> moveToTheEnd = new ArrayList<Proxy>(failedProxies.size());
		for (final Proxy badProxy : failedProxies) {
			if (proxies.remove(badProxy)) {
				moveToTheEnd.add(badProxy);
			}
		}
		proxies.addAll(moveToTheEnd);

	}

	private static int getProxyDefaultPort(final String protocol) {
		if (protocol == null) {
			return PROXY_DEFAULT_PORT;
		}
		if ("http".equalsIgnoreCase(protocol)) { //$NON-NLS-1$
			return HTTPPROXY_DEFAULT_PORT;
		}
		if ("https".equalsIgnoreCase(protocol)) { //$NON-NLS-1$
			return HTTPSPROXY_DEFAULT_PORT;
		}
		if ("socks".equalsIgnoreCase(protocol)) { //$NON-NLS-1$
			return SOCKSPROXY_DEFAULT_PORT;
		}
		if ("socket".equalsIgnoreCase(protocol)) { //$NON-NLS-1$
			return SOCKSPROXY_DEFAULT_PORT;
		}
		return PROXY_DEFAULT_PORT;
	}

	private static void addProtocolSpecificProxy(final Map<String, List<Proxy>> protocolSpecificProxies,
			final String protocol, final Proxy proxy) {
		List<Proxy> list = protocolSpecificProxies.get(protocol);
		if (list == null) {
			list = new ArrayList<Proxy>();
			protocolSpecificProxies.put(protocol, list);
		}

		list.add(proxy);
	}

	/**
	 * @param type
	 * @param host
	 * @param port
	 * @return
	 */
	public static Proxy createProxy(final Proxy.Type type, final String host, final int port) {
		return new Proxy(type, InetSocketAddress.createUnresolved(host, port));
	}

	/**
	 * @param protocol
	 * @return
	 */
	public static Proxy.Type resolveProxyType(final String protocol) {
		if (protocol != null && (protocol.equalsIgnoreCase("socks") || protocol.equalsIgnoreCase("socket"))) { //$NON-NLS-1$ //$NON-NLS-2$
			return Proxy.Type.SOCKS;
		} else {
			return Proxy.Type.HTTP;
		}
	}

	/**
	 * @param scheme
	 * @param sa
	 * @return
	 */
	public static Proxy createProxy(final String scheme, final SocketAddress sa) {
		final Proxy.Type type = resolveProxyType(scheme);
		return new Proxy(type, sa);
	}

	public static class IllegalFormatException extends IllegalArgumentException {

		private static final long serialVersionUID = 3597432896264559897L;

		public IllegalFormatException(final String message, final Throwable throwable) {
			super(message, throwable);
		}

		public IllegalFormatException(final String message) {
			super(message);
		}

	}
}
