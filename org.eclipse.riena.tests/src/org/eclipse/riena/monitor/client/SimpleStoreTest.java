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
package org.eclipse.riena.monitor.client;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.monitor.common.Collectible;

/**
 * Test the {@code SimpleStore}.
 */
@NonUITestCase
public class SimpleStoreTest extends RienaTestCase {

	private static final String PAYLOAD = "What goes up, must come down!";
	private static final String CATEGORY = "test";
	private static final String CLIENT_INFO = "unit-test";

	public void testPutCollectibleAndGetCollectible() throws CoreException, IOException {
		final SimpleStore store = new SimpleStore();
		store.setInitializationData(null, null, null);
		final File file = File.createTempFile("SimpleStore", ".test");
		try {
			final Collectible<String> collectible = new Collectible<String>(CLIENT_INFO, CATEGORY, PAYLOAD);
			ReflectionUtils.invokeHidden(store, "putCollectible", collectible, file);
			final Collectible<String> outCollectible = ReflectionUtils.invokeHidden(store, "getCollectible", file);
			assertEquals(CLIENT_INFO, outCollectible.getClientInfo());
			assertEquals(CATEGORY, outCollectible.getCategory());
			assertEquals(PAYLOAD, outCollectible.getPayload());
		} finally {
			file.delete();
		}
	}

	public void testGetCollectibleFromExistingStoreFileForRegression() throws CoreException {
		final SimpleStore store = new SimpleStore();
		store.setInitializationData(null, null, null);
		final File file = getFile("regression.store");
		final Collectible<String> outCollectible = ReflectionUtils.invokeHidden(store, "getCollectible", file);
		assertEquals(CLIENT_INFO, outCollectible.getClientInfo());
		assertEquals(CATEGORY, outCollectible.getCategory());
		assertEquals(PAYLOAD, outCollectible.getPayload());
	}

	public void testGetBadOldCollectibleFromExistingStoreFile() throws CoreException {
		final SimpleStore store = new SimpleStore();
		store.setInitializationData(null, null, null);
		final File file = getFile("regression.store.old");
		final Collectible<String> outCollectible = ReflectionUtils.invokeHidden(store, "getCollectible", file);
		assertNull(outCollectible);
		assertFalse(file.exists());
	}

	public void testSetInitializationData() throws CoreException {
		final SimpleStore store = new SimpleStore();
		store.setInitializationData(null, null, "cleanupDelay=1 m; storePath=c:\\temp\\store");
		assertEquals(60 * 1000, (long) (Long) ReflectionUtils.getHidden(store, "cleanupDelay"));
		assertEquals("c:\\temp\\store", (String) ReflectionUtils.getHidden(store, "storePathName"));
	}
}
