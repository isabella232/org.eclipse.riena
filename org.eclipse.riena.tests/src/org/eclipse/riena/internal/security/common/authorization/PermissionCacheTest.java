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
package org.eclipse.riena.internal.security.common.authorization;

import org.eclipse.riena.core.cache.GenericObjectCache;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.ContainerModel;
import org.eclipse.riena.core.util.ReflectionUtils;

/**
 * Test the {@code PermissionCache}.
 */
@NonUITestCase
public class PermissionCacheTest extends RienaTestCase {

	public void testConfigurationServer() {
		setContainerType(ContainerType.SERVER);
		final PermissionCache cache = new PermissionCache();
		assertEquals(100, getCache(cache).getMinimumSize());
		assertEquals(360000, getCache(cache).getTimeout());
	}

	public void testConfigurationClient() {
		setContainerType(ContainerType.CLIENT);
		final PermissionCache cache = new PermissionCache();
		assertEquals(1, getCache(cache).getMinimumSize());
		assertEquals(999999000, getCache(cache).getTimeout());
	}

	public void testConfigurationClientNoExtension() {
		setContainerType(ContainerType.CLIENT);
		final PermissionCache cache = new PermissionCache();
		cache.update(null);
		assertEquals(1, getCache(cache).getMinimumSize());
		assertEquals(999999000, getCache(cache).getTimeout());
	}

	public void testConfigurationServerNoExtension() {
		setContainerType(ContainerType.SERVER);
		final PermissionCache cache = new PermissionCache();
		cache.update(null);
		assertEquals(100, getCache(cache).getMinimumSize());
		assertEquals(360000, getCache(cache).getTimeout());
	}

	public void testConfigurationClientOneExtensionTimeoutMinusOne() {
		setContainerType(ContainerType.CLIENT);
		final PermissionCache cache = new PermissionCache();
		cache.update(new PermissionCacheExtension(5, -1));
		assertEquals(5, getCache(cache).getMinimumSize());
		assertEquals(Integer.MAX_VALUE, getCache(cache).getTimeout());
	}

	public void testConfigurationClientOneExtension() {
		setContainerType(ContainerType.CLIENT);
		final PermissionCache cache = new PermissionCache();
		cache.update(new PermissionCacheExtension(5, 360));
		assertEquals(5, getCache(cache).getMinimumSize());
		assertEquals(360, getCache(cache).getTimeout());
	}

	public void testConfigurationServerOneExtensionTimeoutMinusOne() {
		setContainerType(ContainerType.SERVER);
		final PermissionCache cache = new PermissionCache();
		cache.update(new PermissionCacheExtension(5, -1));
		assertEquals(5, getCache(cache).getMinimumSize());
		assertEquals(Integer.MAX_VALUE, getCache(cache).getTimeout());
	}

	public void testConfigurationServerOneExtension() {
		setContainerType(ContainerType.SERVER);
		final PermissionCache cache = new PermissionCache();
		cache.update(new PermissionCacheExtension(5, 360));
		assertEquals(5, getCache(cache).getMinimumSize());
		assertEquals(360, getCache(cache).getTimeout());
	}

	private enum ContainerType {
		SERVER, CLIENT
	};

	private void setContainerType(final ContainerType containerType) {
		System.setProperty(ContainerModel.RIENA_CONTAINER_TYPE, containerType == ContainerType.SERVER ? "server"
				: "client");
		ReflectionUtils.invokeHidden(ContainerModel.class, "initialize");
	}

	private GenericObjectCache getCache(final PermissionCache permissionCache) {
		return ReflectionUtils.getHidden(permissionCache, "permCache");
	}

	private static class PermissionCacheExtension implements IPermissionCacheExtension {

		private final int minimumSize;
		private final int timeout;

		public PermissionCacheExtension(final int minimumSize, final int timeout) {
			this.minimumSize = minimumSize;
			this.timeout = timeout;
		}

		public int getMinimumSize() {
			return minimumSize;
		}

		public int getTimeout() {
			return timeout;
		}

	}
}
