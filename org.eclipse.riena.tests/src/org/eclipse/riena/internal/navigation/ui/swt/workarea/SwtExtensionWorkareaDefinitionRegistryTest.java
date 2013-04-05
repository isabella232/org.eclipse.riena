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
package org.eclipse.riena.internal.navigation.ui.swt.workarea;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ApplicationModelFailure;
import org.eclipse.riena.navigation.extension.IModuleGroupNode2Extension;
import org.eclipse.riena.navigation.extension.IModuleNode2Extension;
import org.eclipse.riena.navigation.extension.ISubApplicationNode2Extension;
import org.eclipse.riena.navigation.extension.ISubModuleNode2Extension;
import org.eclipse.riena.navigation.extension.ModuleGroupNode2Extension;
import org.eclipse.riena.navigation.extension.ModuleNode2Extension;
import org.eclipse.riena.navigation.extension.NavigationAssembly2Extension;
import org.eclipse.riena.navigation.extension.SubApplicationNode2Extension;
import org.eclipse.riena.navigation.extension.SubModuleNode2Extension;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaDefinition;

/**
 * Tests of the class {@link SwtExtensionWorkareaDefinitionRegistry}.
 */
@NonUITestCase
public class SwtExtensionWorkareaDefinitionRegistryTest extends RienaTestCase {

	private static final String TEST_TYPE_ID = "TestTypeId";
	private static final String OTHER_TEST_TYPE_ID = "OtherTestTypeId";
	private static final String TEST_VIEW_ID = "TestViewId";
	private static final String OTHER_TEST_VIEW_ID = "OtherTestViewId";

	private MySwtExtensionWorkareaDefinitionRegistry workareaDefinitionRegistry;
	private IWorkareaDefinition workareaDefinition;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		workareaDefinitionRegistry = new MySwtExtensionWorkareaDefinitionRegistry();
		workareaDefinition = new WorkareaDefinition(TestSubModuleController.class, TEST_VIEW_ID);
	}

	public void testRegisterDefinition() throws Exception {

		workareaDefinitionRegistry.register(TEST_TYPE_ID, workareaDefinition);

		assertSame(workareaDefinition, workareaDefinitionRegistry.getDefinition(TEST_TYPE_ID));
	}

	public void testRegisterSameDefinitionDifferentTypeId() throws Exception {

		workareaDefinitionRegistry.register(TEST_TYPE_ID, workareaDefinition);

		final IWorkareaDefinition otherWorkareaDefinition = new WorkareaDefinition(TestSubModuleController.class,
				TEST_VIEW_ID);
		workareaDefinitionRegistry.register(OTHER_TEST_TYPE_ID, otherWorkareaDefinition);

		assertSame(workareaDefinition, workareaDefinitionRegistry.getDefinition(TEST_TYPE_ID));
		assertSame(otherWorkareaDefinition, workareaDefinitionRegistry.getDefinition(OTHER_TEST_TYPE_ID));
	}

	public void testRegisterSameDefinitionSameTypeId() throws Exception {

		workareaDefinitionRegistry.register(TEST_TYPE_ID, workareaDefinition);

		final IWorkareaDefinition newWorkareaDefinition = new WorkareaDefinition(TestSubModuleController.class,
				TEST_VIEW_ID);
		workareaDefinitionRegistry.register(TEST_TYPE_ID, newWorkareaDefinition);

		assertNotSame(workareaDefinition, workareaDefinitionRegistry.getDefinition(TEST_TYPE_ID));
		assertSame(newWorkareaDefinition, workareaDefinitionRegistry.getDefinition(TEST_TYPE_ID));
	}

	public void testRegisterDifferentDefinitionSameTypeId() throws Exception {

		workareaDefinitionRegistry.register(TEST_TYPE_ID, workareaDefinition);

		IWorkareaDefinition otherWorkareaDefinition = null;
		try {
			otherWorkareaDefinition = new WorkareaDefinition(OtherTestSubModuleController.class, TEST_VIEW_ID);
			workareaDefinitionRegistry.register(TEST_TYPE_ID, otherWorkareaDefinition);
			fail("ApplicationModelFailure expected");
		} catch (final ApplicationModelFailure expected) {
			ok("ApplicationModelFailure expected");
		}

		try {
			otherWorkareaDefinition = new WorkareaDefinition(TestSubModuleController.class, OTHER_TEST_VIEW_ID);
			workareaDefinitionRegistry.register(TEST_TYPE_ID, otherWorkareaDefinition);
			fail("ApplicationModelFailure expected");
		} catch (final ApplicationModelFailure expected) {
			ok("ApplicationModelFailure expected");
		}

	}

	private static class TestSubModuleController extends SubModuleController {

	}

	private static class OtherTestSubModuleController extends SubModuleController {

	}

	/**
	 * Tests the <i>private</i> method
	 * {@code register(INavigationAssembly2Extension)}.
	 */
	public void testRegisterINavigationAssembly2Extension() {

		final NavigationAssembly2Extension assembly = new NavigationAssembly2Extension();

		// sub-modules will be registered
		final SubModuleNode2Extension sub1 = new SubModuleNode2Extension();
		sub1.setNodeId("sub1");
		sub1.setViewId("v1");
		final ISubModuleNode2Extension[] subMods = new ISubModuleNode2Extension[] { sub1 };
		assembly.setSubModules(subMods);
		ReflectionUtils.invokeHidden(workareaDefinitionRegistry, "register", assembly);
		IWorkareaDefinition def = workareaDefinitionRegistry.getDefinition("sub1");
		assertNotNull(def);
		assertEquals("v1", def.getViewId());

		// modules will not be registered (because they have no relevant information to register - like view ID)
		// because the assembly has a module the sub-modules will also not be registered.
		workareaDefinitionRegistry = new MySwtExtensionWorkareaDefinitionRegistry();
		final ModuleNode2Extension m1 = new ModuleNode2Extension();
		m1.setNodeId("m1");
		final IModuleNode2Extension[] mods = new IModuleNode2Extension[] { m1 };
		assembly.setModules(mods);
		ReflectionUtils.invokeHidden(workareaDefinitionRegistry, "register", assembly);
		def = workareaDefinitionRegistry.getDefinition("m1");
		assertNull(def);
		def = workareaDefinitionRegistry.getDefinition("sub1");
		assertNull(def);

		// module groups will not be registered (because they have no relevant information to register - like view ID)
		// because the assembly has a module group the sub-modules will also not be registered.
		workareaDefinitionRegistry = new MySwtExtensionWorkareaDefinitionRegistry();
		final ModuleGroupNode2Extension mg1 = new ModuleGroupNode2Extension();
		m1.setNodeId("mg1");
		final IModuleGroupNode2Extension[] groups = new IModuleGroupNode2Extension[] { mg1 };
		assembly.setModuleGroups(groups);
		ReflectionUtils.invokeHidden(workareaDefinitionRegistry, "register", assembly);
		def = workareaDefinitionRegistry.getDefinition("mg1");
		assertNull(def);
		def = workareaDefinitionRegistry.getDefinition("m1");
		assertNull(def);
		def = workareaDefinitionRegistry.getDefinition("sub1");
		assertNull(def);

		// sub-applications will be registered
		// because the assembly has a module group the sub-modules will also not be registered.
		workareaDefinitionRegistry = new MySwtExtensionWorkareaDefinitionRegistry();
		final SubApplicationNode2Extension subApp1 = new SubApplicationNode2Extension();
		subApp1.setNodeId("sa1");
		subApp1.setPerspectiveId("p1");
		final SubApplicationNode2Extension subApp2 = new SubApplicationNode2Extension();
		subApp2.setNodeId("sa2");
		subApp2.setPerspectiveId("p2");
		final ISubApplicationNode2Extension[] subApps = new ISubApplicationNode2Extension[] { subApp1, subApp2 };
		assembly.setSubApplications(subApps);
		ReflectionUtils.invokeHidden(workareaDefinitionRegistry, "register", assembly);
		def = workareaDefinitionRegistry.getDefinition("sa1");
		assertNotNull(def);
		assertEquals("p1", def.getViewId());
		def = workareaDefinitionRegistry.getDefinition("sa2");
		assertNotNull(def);
		assertEquals("p2", def.getViewId());
		def = workareaDefinitionRegistry.getDefinition("sub1");
		assertNull(def);

	}

	/**
	 * Tests the method {@code register(ISubApplicationNode2Extension)}.
	 */
	public void testRegisterISubApplicationNode2Extension() {

		final SubApplicationNode2Extension subApp12 = new SubApplicationNode2Extension();
		subApp12.setNodeId("sa12");
		subApp12.setPerspectiveId("p12");
		workareaDefinitionRegistry.register(subApp12);
		final IWorkareaDefinition def = workareaDefinitionRegistry.getDefinition("sa12");
		assertNotNull(def);
		assertEquals("p12", def.getViewId());

	}

	/**
	 * Tests the method {@code register(ISubModuleNode2Extension)}.
	 */
	public void testRegisterISubModuleNode2Extension() {

		final SubModuleNode2Extension subMod13 = new SubModuleNode2Extension();
		subMod13.setNodeId("sub13");
		subMod13.setViewId("v13");
		final SubModuleNode2Extension subMod123 = new SubModuleNode2Extension();
		subMod123.setNodeId("sub123");
		subMod123.setViewId("v123");
		subMod123.setRequiresPreparation(true);
		subMod123.setSharedView(true);
		subMod13.setChildNodes(new ISubModuleNode2Extension[] { subMod123 });
		workareaDefinitionRegistry.register(subMod13);
		IWorkareaDefinition def = workareaDefinitionRegistry.getDefinition("sub13");
		assertNotNull(def);
		assertEquals("v13", def.getViewId());
		assertFalse(def.isRequiredPreparation());
		assertFalse(def.isRequiredPreparation());
		def = workareaDefinitionRegistry.getDefinition("sub123");
		assertNotNull(def);
		assertEquals("v123", def.getViewId());
		assertTrue(def.isRequiredPreparation());
		assertTrue(def.isRequiredPreparation());

	}

	/**
	 * Changes the visibility of some protected methods for testing.
	 */
	@SuppressWarnings("restriction")
	private class MySwtExtensionWorkareaDefinitionRegistry extends SwtExtensionWorkareaDefinitionRegistry {

		@Override
		public void register(final ISubApplicationNode2Extension subAppicationExt) {
			super.register(subAppicationExt);
		}

		@Override
		public void register(final ISubModuleNode2Extension subModuleExt) {
			super.register(subModuleExt);
		}

	}

}
