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
package org.eclipse.riena.navigation.ui.swt.presentation;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.ApplicationModelFailure;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 * Tests for the SwtViewProvider.
 */
@NonUITestCase
public class SwtViewProviderTest extends RienaTestCase {

	private SwtViewProvider swtPresentationManager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		swtPresentationManager = ReflectionUtils.newInstanceHidden(SwtViewProvider.class);
		addPluginXml(SwtViewProviderTest.class, "SwtViewProviderTest.xml");
	}

	@Override
	protected void tearDown() throws Exception {
		removeExtension("swt.view.provider.test");
		super.tearDown();
	}

	public void testGetViewUsers() {
		final ISubModuleNode node1 = new SubModuleNode(new NavigationNodeId("testSharedViewId", "testInstanceId1"));
		final ISubModuleNode node2 = new SubModuleNode(new NavigationNodeId("testSharedViewId", "testInstanceId2"));
		final SwtViewId swtViewId1 = swtPresentationManager.getSwtViewId(node1);
		swtPresentationManager.getSwtViewId(node2);
		assertSame(swtPresentationManager.getViewUsers(swtViewId1).size(), 2);
		node1.dispose();
		assertSame(swtPresentationManager.getViewUsers(swtViewId1).size(), 1);
		node2.dispose();
		assertSame(swtPresentationManager.getViewUsers(swtViewId1).size(), 0);
	}

	public void testGetSwtViewIdSharedView() throws Exception {

		final ISubModuleNode node1 = new SubModuleNode(new NavigationNodeId("testSharedViewId", "testInstanceId1"));
		final ISubModuleNode node2 = new SubModuleNode(new NavigationNodeId("testSharedViewId", "testInstanceId2"));

		final SwtViewId swtViewId1 = swtPresentationManager.getSwtViewId(node1);
		assertEquals("org.eclipse.riena.navigation.ui.swt.views.TestView", swtViewId1.getId());
		assertEquals("shared", swtViewId1.getSecondary());

		final SwtViewId swtViewId2 = swtPresentationManager.getSwtViewId(node2);
		assertEquals("org.eclipse.riena.navigation.ui.swt.views.TestView", swtViewId2.getId());
		assertEquals("shared", swtViewId2.getSecondary());
	}

	public void testGetSwtViewIdNotSharedView() throws Exception {

		final ISubModuleNode node1 = new SubModuleNode(new NavigationNodeId("testNotSharedViewId", "testInstanceId1"));
		final ISubModuleNode node2 = new SubModuleNode(new NavigationNodeId("testNotSharedViewId", "testInstanceId2"));

		final SwtViewId swtViewId1 = swtPresentationManager.getSwtViewId(node1);
		assertEquals("org.eclipse.riena.navigation.ui.swt.views.TestView", swtViewId1.getId());
		assertEquals("1", swtViewId1.getSecondary());

		final SwtViewId swtViewId2 = swtPresentationManager.getSwtViewId(node2);
		assertEquals("org.eclipse.riena.navigation.ui.swt.views.TestView", swtViewId2.getId());
		assertEquals("2", swtViewId2.getSecondary());

		final SwtViewId swtViewId1Again = swtPresentationManager.getSwtViewId(node1);
		assertEquals("org.eclipse.riena.navigation.ui.swt.views.TestView", swtViewId1Again.getId());
		assertEquals("1", swtViewId1Again.getSecondary());
	}

	public void testUnconsistentDefinitionWithAViewBothSharedAndNotShared() throws Exception {

		final ISubModuleNode node1 = new SubModuleNode(new NavigationNodeId("testSharedViewId", "testInstanceId1"));
		final ISubModuleNode node2 = new SubModuleNode(new NavigationNodeId("testNotSharedViewId", "testInstanceId2"));

		swtPresentationManager.getSwtViewId(node1);
		try {
			swtPresentationManager.getSwtViewId(node2);
			fail("ApplicationModelFailure expected");
		} catch (final ApplicationModelFailure expected) {
			ok("ApplicationModelFailure expected");
		}
	}
}
