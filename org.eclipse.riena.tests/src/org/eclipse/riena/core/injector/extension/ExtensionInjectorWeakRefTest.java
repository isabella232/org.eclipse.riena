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
package org.eclipse.riena.core.injector.extension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.eclipse.core.runtime.IRegistryEventListener;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.util.WeakRef;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code ExtensionInjector} with respect to the {@code WeakRef} to the
 * target.
 */
@NonUITestCase
public class ExtensionInjectorWeakRefTest extends RienaTestCase {

	public void testWeakRefFail() throws IOException {
		try {
			testWeakRef(false);
			fail();
		} catch (final AssertionFailedError e) {
			ok();
		}
	}

	public void testWeakRefSucceed() throws IOException {
		testWeakRef(true);
	}

	private void testWeakRef(final boolean withNulling) throws IOException {
		printTestName();
		addPluginXml(ExtensionInjectorWeakRefTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorWeakRefTest.class, "plugin_ext1.xml");
		addPluginXml(ExtensionInjectorWeakRefTest.class, "plugin_ext2.xml");
		addPluginXml(ExtensionInjectorWeakRefTest.class, "plugin_ext3.xml");
		try {
			ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
			final ExtensionInjector injector = Inject.extension("core.test.extpoint").into(target)
					.andStart(getContext());
			try {
				assertEquals(3, target.getData().length);
				target = null;
				runOutOfMemory();
				final WeakRef<Object> ref = ReflectionUtils.getHidden(injector, "targetRef");
				assertNull(ref.get());
				final List<IRegistryEventListener> injectorListeners = ReflectionUtils.getHidden(injector,
						"injectorListeners");
				assertEquals(0, injectorListeners.size());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtension("core.test.extpoint.id2");
			removeExtension("core.test.extpoint.id3");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	private void runOutOfMemory() throws IOException {
		try {
			final OutputStream os = new ByteArrayOutputStream();
			while (true) {
				os.write(new byte[1024 * 1024]);
			}
		} catch (final OutOfMemoryError e) {
			System.gc();
		}
	}

}
