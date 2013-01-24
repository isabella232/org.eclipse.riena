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

import java.io.IOException;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code ExtensionInjector} with respect to the onceOnly feature.
 */
@NonUITestCase
public class ExtensionInjectorOnceOnlyTest extends RienaTestCase {

	public void testOnceOnlyViaStaticOneTarget() throws IOException {
		printTestName();
		addPluginXml(ExtensionInjectorOnceOnlyTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorOnceOnlyTest.class, "plugin_ext1.xml");
		try {
			ConfigurableThingMultipleDataOnceOnlyViaStatic.resetUpdateCount();
			final ConfigurableThingMultipleDataOnceOnlyViaStatic target = new ConfigurableThingMultipleDataOnceOnlyViaStatic();
			final ExtensionInjector injector = Inject.extension("core.test.extpoint").into(target)
					.andStart(getContext());
			assertEquals(1, target.getData().length);
			assertEquals(1, ConfigurableThingMultipleDataOnceOnlyViaStatic.getUpdateCount());
			injector.stop();
			assertEquals(0, target.getData().length);
			assertEquals(2, ConfigurableThingMultipleDataOnceOnlyViaStatic.getUpdateCount());
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testOnceOnlyViaStaticTwoTargets() throws IOException {
		printTestName();
		addPluginXml(ExtensionInjectorOnceOnlyTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorOnceOnlyTest.class, "plugin_ext1.xml");
		try {
			ConfigurableThingMultipleDataOnceOnlyViaStatic.resetUpdateCount();

			final ConfigurableThingMultipleDataOnceOnlyViaStatic target1 = new ConfigurableThingMultipleDataOnceOnlyViaStatic();
			final ExtensionInjector injector1 = Inject.extension("core.test.extpoint").into(target1)
					.andStart(getContext());
			assertEquals(1, target1.getData().length);
			assertEquals(1, ConfigurableThingMultipleDataOnceOnlyViaStatic.getUpdateCount());

			final ConfigurableThingMultipleDataOnceOnlyViaStatic target2 = new ConfigurableThingMultipleDataOnceOnlyViaStatic();
			final ExtensionInjector injector2 = Inject.extension("core.test.extpoint").into(target2)
					.andStart(getContext());
			assertEquals(1, target2.getData().length);
			assertEquals(1, ConfigurableThingMultipleDataOnceOnlyViaStatic.getUpdateCount());

			injector1.stop();
			assertEquals(1, target1.getData().length);
			assertEquals(1, ConfigurableThingMultipleDataOnceOnlyViaStatic.getUpdateCount());

			injector2.stop();
			assertEquals(0, target1.getData().length);
			assertEquals(2, ConfigurableThingMultipleDataOnceOnlyViaStatic.getUpdateCount());
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testOnceOnlyViaDefinitionOneTarget() throws IOException {
		printTestName();
		addPluginXml(ExtensionInjectorOnceOnlyTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorOnceOnlyTest.class, "plugin_ext1.xml");
		try {
			ConfigurableThingMultipleDataOnceOnlyViaDefinition.resetUpdateCount();
			final ConfigurableThingMultipleDataOnceOnlyViaDefinition target = new ConfigurableThingMultipleDataOnceOnlyViaDefinition();
			final ExtensionInjector injector = Inject.extension("core.test.extpoint").into(target).onceOnly()
					.andStart(getContext());
			assertEquals(1, target.getData().length);
			assertEquals(1, ConfigurableThingMultipleDataOnceOnlyViaDefinition.getUpdateCount());
			injector.stop();
			assertEquals(0, target.getData().length);
			assertEquals(2, ConfigurableThingMultipleDataOnceOnlyViaDefinition.getUpdateCount());
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testOnceOnlyViaDefinitionTwoTargets() throws IOException {
		printTestName();
		addPluginXml(ExtensionInjectorOnceOnlyTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorOnceOnlyTest.class, "plugin_ext1.xml");
		try {
			ConfigurableThingMultipleDataOnceOnlyViaDefinition.resetUpdateCount();

			final ConfigurableThingMultipleDataOnceOnlyViaDefinition target1 = new ConfigurableThingMultipleDataOnceOnlyViaDefinition();
			final ExtensionInjector injector1 = Inject.extension("core.test.extpoint").into(target1).onceOnly()
					.andStart(getContext());
			assertEquals(1, target1.getData().length);
			assertEquals(1, ConfigurableThingMultipleDataOnceOnlyViaDefinition.getUpdateCount());

			final ConfigurableThingMultipleDataOnceOnlyViaDefinition target2 = new ConfigurableThingMultipleDataOnceOnlyViaDefinition();
			final ExtensionInjector injector2 = Inject.extension("core.test.extpoint").into(target2).onceOnly()
					.andStart(getContext());
			assertEquals(1, target2.getData().length);
			assertEquals(1, ConfigurableThingMultipleDataOnceOnlyViaDefinition.getUpdateCount());

			injector1.stop();
			assertEquals(1, target1.getData().length);
			assertEquals(1, ConfigurableThingMultipleDataOnceOnlyViaDefinition.getUpdateCount());

			injector2.stop();
			assertEquals(0, target1.getData().length);
			assertEquals(2, ConfigurableThingMultipleDataOnceOnlyViaDefinition.getUpdateCount());
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
		}
	}
}
