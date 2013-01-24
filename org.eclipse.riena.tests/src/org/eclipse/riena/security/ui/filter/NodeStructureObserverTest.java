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
package org.eclipse.riena.security.ui.filter;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ISimpleNavigationNodeListener;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.security.ui.filter.NodeStructureObserver;

/**
 *
 */
@NonUITestCase
public class NodeStructureObserverTest extends TestCase {

	public void testDelegation() {
		final IApplicationNode app = new ApplicationNode();
		final SubApplicationNode subApp1 = new SubApplicationNode(new NavigationNodeId("subapp1"));
		app.addChild(subApp1);
		final SubApplicationNode subApp2 = new SubApplicationNode(new NavigationNodeId("subapp2"));
		app.addChild(subApp2);
		final ModuleGroupNode mg1 = new ModuleGroupNode(new NavigationNodeId("mg1"));
		subApp1.addChild(mg1);
		final ModuleGroupNode mg2 = new ModuleGroupNode(new NavigationNodeId("mg2"));
		subApp2.addChild(mg2);
		final ModuleNode m1 = new ModuleNode(new NavigationNodeId("m1"));
		mg1.addChild(m1);
		final ModuleNode m2 = new ModuleNode(new NavigationNodeId("m2"));
		mg2.addChild(m2);
		final SubModuleNode sm1 = new SubModuleNode(new NavigationNodeId("sm1"));
		m1.addChild(sm1);
		final SubModuleNode sm2 = new SubModuleNode(new NavigationNodeId("sm2"));
		m2.addChild(sm2);
		app.activate();

		final SubModuleNode toAdd1 = new SubModuleNode(new NavigationNodeId("fresh1"));

		final ISimpleNavigationNodeListener listener = EasyMock.createNiceMock(ISimpleNavigationNodeListener.class);
		listener.childAdded(m1, toAdd1);
		EasyMock.replay(listener);
		final NodeStructureObserver observer = new NodeStructureObserver(app, listener);
		observer.start();
		m1.addChild(toAdd1);
		EasyMock.verify(listener);
		observer.stop();
		EasyMock.reset(listener);
		EasyMock.replay(listener);
		final SubModuleNode toAdd2 = new SubModuleNode(new NavigationNodeId("fresh2"));
		m1.addChild(toAdd2);
		EasyMock.verify(listener);
	}

}
