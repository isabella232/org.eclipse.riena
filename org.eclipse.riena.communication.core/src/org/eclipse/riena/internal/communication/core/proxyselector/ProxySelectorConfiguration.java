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
package org.eclipse.riena.internal.communication.core.proxyselector;

import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.internal.communication.core.Activator;

/**
 * Configure the {@code ProxySelector} used by the {@code UrlConnection}.
 */
public class ProxySelectorConfiguration {

	private final ProxySelector previousProxySlector = ProxySelector.getDefault();

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ProxySelectorConfiguration.class);

	@InjectExtension()
	public void configure(final IProxySelectorExtension[] proxySelectorExtensions) {
		uninstall(ProxySelector.getDefault());
		if (proxySelectorExtensions == null || proxySelectorExtensions.length == 0) {
			ProxySelector.setDefault(previousProxySlector);
			return;
		}

		Arrays.sort(proxySelectorExtensions, new Comparator<IProxySelectorExtension>() {
			public int compare(final IProxySelectorExtension e1, final IProxySelectorExtension e2) {
				return e1.getOrder() < e2.getOrder() ? -1 : e1.getOrder() > e2.getOrder() ? 1 : 0;
			}
		});

		LOGGER.log(LogService.LOG_DEBUG, "Configured proxy selectors:"); //$NON-NLS-1$
		final List<ProxySelector> proxySelectors = new ArrayList<ProxySelector>(proxySelectorExtensions.length);
		for (final IProxySelectorExtension extension : proxySelectorExtensions) {
			final ProxySelector proxySelector = extension.createProxySelector();
			LOGGER.log(LogService.LOG_DEBUG, "  - " + extension.getName() + " with order=" + extension.getOrder() //$NON-NLS-1$ //$NON-NLS-2$
					+ " implemented by " + proxySelector.getClass().getName()); //$NON-NLS-1$
			proxySelectors.add(proxySelector);
		}
		ProxySelector.setDefault(new CompoundProxySelector(proxySelectors));
	}

	public void uninstall() {
		uninstall(ProxySelector.getDefault());
		ProxySelector.setDefault(previousProxySlector);
	}

	private void uninstall(final ProxySelector proxySelector) {
		if (proxySelector instanceof CompoundProxySelector) {
			((CompoundProxySelector) proxySelector).uninstall();
		}
	}
}
