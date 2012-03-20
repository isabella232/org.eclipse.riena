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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import java.util.ArrayList;
import java.util.Arrays;

import org.easymock.EasyMock;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.ISubApplicationNode;

/**
 * Tests for the class {@link CloseModuleGroup}.
 */
@NonUITestCase
public class CloseModuleGroupTest extends RienaTestCase {

	private CloseModuleGroup handler;

	private IApplicationNode appNode;

	private ISubApplicationNode subApp1;
	private IModuleGroupNode a1ModuleGroup1;
	private IModuleGroupNode a1ModuleGroup2;

	private ISubApplicationNode subApp2;
	private IModuleGroupNode a2ModuleGroup1;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		handler = new CloseModuleGroup();
		appNode = EasyMock.createMock(IApplicationNode.class);
		subApp1 = EasyMock.createMock(ISubApplicationNode.class);
		subApp2 = EasyMock.createMock(ISubApplicationNode.class);
		a1ModuleGroup1 = EasyMock.createMock(IModuleGroupNode.class);
		a1ModuleGroup2 = EasyMock.createMock(IModuleGroupNode.class);
		a2ModuleGroup1 = EasyMock.createMock(IModuleGroupNode.class);
	}

	public void testFindModuleGroup() {
		EasyMock.expect(appNode.getChildren()).andReturn(Arrays.asList(subApp1, subApp2));
		EasyMock.expect(subApp1.isActivated()).andReturn(false);
		EasyMock.expect(subApp2.isActivated()).andReturn(true);
		EasyMock.expect(subApp2.getChildren()).andReturn(Arrays.asList(a2ModuleGroup1));
		EasyMock.expect(a2ModuleGroup1.isActivated()).andReturn(true);
		enableReplay();

		assertSame(a2ModuleGroup1, handler.findModuleGroup(appNode));
	}

	public void testFindModuleGroup_NoSubApps() {
		EasyMock.expect(appNode.getChildren()).andReturn(new ArrayList<ISubApplicationNode>());
		enableReplay();

		assertNull(handler.findModuleGroup(appNode));
	}

	public void testFindModuleGroup_NoGroups() {
		EasyMock.expect(appNode.getChildren()).andReturn(Arrays.asList(subApp1, subApp2));
		EasyMock.expect(subApp1.isActivated()).andReturn(true);
		EasyMock.expect(subApp1.getChildren()).andReturn(new ArrayList<IModuleGroupNode>());
		enableReplay();

		assertNull(handler.findModuleGroup(appNode));
	}

	public void testFindModuleGroup_NoActiveGroups() {
		EasyMock.expect(appNode.getChildren()).andReturn(Arrays.asList(subApp1, subApp2));
		EasyMock.expect(subApp1.isActivated()).andReturn(true);
		EasyMock.expect(subApp1.getChildren()).andReturn(Arrays.asList(a1ModuleGroup1, a1ModuleGroup2));
		EasyMock.expect(a1ModuleGroup1.isActivated()).andReturn(false);
		EasyMock.expect(a1ModuleGroup2.isActivated()).andReturn(false);
		enableReplay();

		assertNull(handler.findModuleGroup(appNode));
	}

	// helping methods
	//////////////////

	private void enableReplay() {
		EasyMock.replay(appNode, subApp1, subApp2, a1ModuleGroup1, a1ModuleGroup2, a2ModuleGroup1);
	}

}