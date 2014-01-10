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
package org.eclipse.riena.navigation.ui.swt.views;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;

/**
 * Tests of the class {@link NavigationViewPart}.
 */
@NonUITestCase
public class NavigationViewPartTest extends TestCase {

	/**
	 * Tests the method {@code getActiveModuleGroupNode()}.
	 */
	public void testGetActiveModuleGroupNode() {

		final ISubApplicationNode sa1 = new SubApplicationNode();
		final IModuleGroupNode mg1 = new ModuleGroupNode(new NavigationNodeId("mg1"));
		sa1.addChild(mg1);
		final IModuleGroupNode mg2 = new ModuleGroupNode(new NavigationNodeId("mg2"));
		sa1.addChild(mg2);
		final IModuleGroupNode mg3 = new ModuleGroupNode(new NavigationNodeId("mg3"));
		sa1.addChild(mg3);

		final MockNavigationViewPart viewPart = new MockNavigationViewPart();
		viewPart.setSubAppNode(sa1);

		IModuleGroupNode activeNode = viewPart.getActiveModuleGroupNode();
		assertNull(activeNode);

		mg2.activate(null);
		activeNode = viewPart.getActiveModuleGroupNode();
		assertSame(mg2, activeNode);

		mg1.activate(null);
		activeNode = viewPart.getActiveModuleGroupNode();
		assertSame(mg1, activeNode);

	}

	private class MockNavigationViewPart extends NavigationViewPart {

		private ISubApplicationNode subAppNode;

		@Override
		public ISubApplicationNode getSubApplicationNode() {
			return getSubAppNode();
		}

		public ISubApplicationNode getSubAppNode() {
			return subAppNode;
		}

		public void setSubAppNode(final ISubApplicationNode subAppNode) {
			this.subAppNode = subAppNode;
		}

	}

}
