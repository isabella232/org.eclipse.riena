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
package org.eclipse.riena.core.injector.extension;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code ExtensionInjector} with respect to the wiring feature.
 */
@NonUITestCase
public class ExtensionInjectorWithWiringTest extends RienaTestCase {

	public void testWiring() {
		printTestName();
		addPluginXml(ExtensionInjectorWithWiringTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorWithWiringTest.class, "plugin_ext_wire.xml");

		try {
			final ConfigurableWiredThing target = new ConfigurableWiredThing();
			final ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.andStart(getContext());
			try {
				final IWireData data = target.getData();
				assertNotNull(data);
				// TODO warning suppression. Ignoring three FindBugs problems below
				// about writing to static field. Since this is used only for testing
				// manipulation of multiple instances is not an issue.
				Wireable.wired = false;
				assertTrue(data.createObjectTypeWithWire().isWired());
				Wireable.wired = false;
				assertFalse(data.createObjectTypeWithoutWire().isWired());
				Wireable.wired = false;
				IWireable wireable = data.createLazyObjectTypeWithWire();
				assertFalse(Wireable.wired);
				assertTrue(wireable.isWired());
				Wireable.wired = false;
				wireable = data.createLazyObjectTypeWithoutWire();
				assertFalse(Wireable.wired);
				assertFalse(wireable.isWired());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testWiringWithSystemPropertySetToFalse() {
		printTestName();
		System.setProperty(ExtensionInjector.RIENA_EXTENSIONS_DONOTWIRE_SYSTEM_PROPERTY, Boolean.TRUE.toString());
		addPluginXml(ExtensionInjectorWithWiringTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorWithWiringTest.class, "plugin_ext_wire.xml");

		try {
			final ConfigurableWiredThing target = new ConfigurableWiredThing();
			final ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.andStart(getContext());
			try {
				final IWireData data = target.getData();
				assertNotNull(data);
				// TODO warning suppression. Ignoring three FindBugs problems below
				// about writing to static field. Since this is used only for testing
				// manipulation of multiple instances is not an issue.
				Wireable.wired = false;
				assertFalse(data.createObjectTypeWithWire().isWired());
				Wireable.wired = false;
				assertFalse(data.createObjectTypeWithoutWire().isWired());
				Wireable.wired = false;
				IWireable wireable = data.createLazyObjectTypeWithWire();
				assertFalse(Wireable.wired);
				assertFalse(wireable.isWired());
				Wireable.wired = false;
				wireable = data.createLazyObjectTypeWithoutWire();
				assertFalse(Wireable.wired);
				assertFalse(wireable.isWired());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
			System.clearProperty(ExtensionInjector.RIENA_EXTENSIONS_DONOTWIRE_SYSTEM_PROPERTY);
		}
	}

}
