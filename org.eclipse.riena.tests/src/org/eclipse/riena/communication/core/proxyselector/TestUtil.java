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

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Little helper.
 */
public final class TestUtil {

	private TestUtil() {
		// Utility
	}

	public static Proxy newProxy(final String host) {
		return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, 80));
	}

}
