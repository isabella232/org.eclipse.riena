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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ModuleController;
import org.eclipse.riena.navigation.ui.controllers.ModuleGroupController;

/**
 * Testcase for NavigationViewFactory
 */
@UITestCase
public class NavigationViewFactoryTest extends RienaTestCase {

	public void testNormalWithoutInjectionBehaviour() {
		final NavigationViewFactory viewFactory = new NavigationViewFactory();

		final ModuleView moduleView = viewFactory.createModuleView(new Shell());

		assertNotNull(moduleView);
		assertTrue(moduleView.getClass() == ModuleView.class);

		final ModuleGroupView moduleGroupView = viewFactory.createModuleGroupView(new Shell());

		assertNotNull(moduleGroupView);
		assertTrue(moduleGroupView.getClass() == ModuleGroupView.class);
	}

	public void testNormalWithInjectionBehaviour() {
		final NavigationViewFactory viewFactory = new NavigationViewFactory();
		Wire.instance(viewFactory).andStart(getContext());

		final ModuleView moduleView = viewFactory.createModuleView(new Shell());

		assertNotNull(moduleView);
		assertTrue(moduleView.getClass() == ModuleView.class);

		final ModuleGroupView moduleGroupView = viewFactory.createModuleGroupView(new Shell());

		assertNotNull(moduleGroupView);
		assertTrue(moduleGroupView.getClass() == ModuleGroupView.class);
	}

	public void testConfiguredNavigationViewFactory() {
		final NavigationViewFactory viewFactory = new NavigationViewFactory();
		addPluginXml(this.getClass(), "pluginXmlNavigationViewFactory.xml");

		try {
			Wire.instance(viewFactory).andStart(getContext());

			final ModuleView moduleView = viewFactory.createModuleView(new Shell());

			assertNotNull(moduleView);
			assertTrue(moduleView.getClass() == TestModuleView.class);

			final ModuleController moduleController = viewFactory.createModuleController(new ModuleNode());
			assertNotNull(moduleController);
			assertTrue(moduleController.getClass() == SWTModuleController.class);

			final ModuleGroupView moduleGroupView = viewFactory.createModuleGroupView(new Shell());

			assertNotNull(moduleGroupView);
			assertTrue(moduleGroupView.getClass() == TestModuleGroupView.class);

			final ModuleGroupController moduleGroupController = viewFactory
					.createModuleGroupController(new ModuleGroupNode());
			assertNotNull(moduleGroupController);
			assertTrue(moduleGroupController.getClass() == ModuleGroupController.class);
		} finally {
			removeExtension("org.eclipse.riena.test.navigationModuleView");
			removeExtension("org.eclipse.riena.test.navigationModuleGroupView");
		}
	}

	public void testConfiguredNavigationViewFactorySecond() {
		final NavigationViewFactory viewFactory = new NavigationViewFactory();
		addPluginXml(this.getClass(), "pluginXmlNavigationViewFactoryWithController.xml");

		try {
			Wire.instance(viewFactory).andStart(getContext());

			final ModuleView moduleView = viewFactory.createModuleView(new Shell());

			assertNotNull(moduleView);
			assertTrue(moduleView.getClass() == TestModuleView.class);

			final ModuleController moduleController = viewFactory.createModuleController(new ModuleNode());
			assertNotNull(moduleController);
			assertTrue(moduleController.getClass() == TestModuleController.class);

			final ModuleGroupView moduleGroupView = viewFactory.createModuleGroupView(new Shell());

			assertNotNull(moduleGroupView);
			assertTrue(moduleGroupView.getClass() == TestModuleGroupView.class);

			final ModuleGroupController moduleGroupController = viewFactory
					.createModuleGroupController(new ModuleGroupNode());
			assertNotNull(moduleGroupController);
			assertTrue(moduleGroupController.getClass() == TestModuleGroupController.class);
		} finally {
			removeExtension("org.eclipse.riena.test.navigationModuleView");
			removeExtension("org.eclipse.riena.test.navigationModuleGroupView");
		}
	}

	/**
	 * Tests the method {@code createModuleController(IModuleNode)}.
	 * <p>
	 * Tests all three ways of the module controller creation.
	 */
	public void testCreateModuleController() {

		final NavigationViewFactory viewFactory = new NavigationViewFactory();

		final ModuleNode moduleNode0 = new ModuleNode();
		ModuleController moduleController = viewFactory.createModuleController(moduleNode0);
		assertNotNull(moduleController);
		assertTrue(moduleController.getClass() == SWTModuleController.class);
		assertSame(moduleController.getNavigationNode(), moduleNode0);

		final ModuleNode moduleNode1 = new ModuleNode();
		final MyModuleController myController = new MyModuleController(moduleNode1);
		moduleNode1.setNavigationNodeController(myController);
		moduleController = viewFactory.createModuleController(moduleNode1);
		assertNotNull(moduleController);
		assertSame(myController, moduleController);
		assertSame(moduleController.getNavigationNode(), moduleNode1);

		addPluginXml(this.getClass(), "pluginXmlNavigationViewFactoryWithController.xml");

		try {
			Wire.instance(viewFactory).andStart(getContext());

			final ModuleNode moduleNode2 = new ModuleNode();
			moduleController = viewFactory.createModuleController(moduleNode2);
			assertNotNull(moduleController);
			assertTrue(moduleController.getClass() == TestModuleController.class);
			assertSame(moduleController.getNavigationNode(), moduleNode2);

		} finally {
			removeExtension("org.eclipse.riena.test.navigationModuleView");
			removeExtension("org.eclipse.riena.test.navigationModuleGroupView");
		}

	}

	/**
	 * Tests the method {@code createModuleGroupController(IModuleNode)}.
	 * <p>
	 * Tests all three ways of the module group controller creation.
	 */
	public void testCreateModuleGroupController() {

		final NavigationViewFactory viewFactory = new NavigationViewFactory();

		final ModuleGroupNode moduleGroupNode0 = new ModuleGroupNode();
		ModuleGroupController moduleGroupController = viewFactory.createModuleGroupController(moduleGroupNode0);
		assertNotNull(moduleGroupController);
		assertTrue(moduleGroupController.getClass() == ModuleGroupController.class);
		assertSame(moduleGroupController.getNavigationNode(), moduleGroupNode0);

		final ModuleGroupNode moduleGroupNode1 = new ModuleGroupNode();
		final MyModuleGroupController myController = new MyModuleGroupController(moduleGroupNode1);
		moduleGroupNode1.setNavigationNodeController(myController);
		moduleGroupController = viewFactory.createModuleGroupController(moduleGroupNode1);
		assertNotNull(moduleGroupController);
		assertSame(myController, moduleGroupController);
		assertSame(moduleGroupController.getNavigationNode(), moduleGroupNode1);

		addPluginXml(this.getClass(), "pluginXmlNavigationViewFactoryWithController.xml");

		try {
			Wire.instance(viewFactory).andStart(getContext());

			final ModuleGroupNode moduleGroupNode2 = new ModuleGroupNode();
			moduleGroupController = viewFactory.createModuleGroupController(moduleGroupNode2);
			assertNotNull(moduleGroupController);
			assertTrue(moduleGroupController.getClass() == TestModuleGroupController.class);
			assertSame(moduleGroupController.getNavigationNode(), moduleGroupNode2);

		} finally {
			removeExtension("org.eclipse.riena.test.navigationModuleView");
			removeExtension("org.eclipse.riena.test.navigationModuleGroupView");
		}

	}

	private class MyModuleController extends ModuleController {

		public MyModuleController(final IModuleNode navigationNode) {
			super(navigationNode);
		}

	}

	private class MyModuleGroupController extends ModuleGroupController {

		public MyModuleGroupController(final IModuleGroupNode navigationNode) {
			super(navigationNode);
		}

	}

}
