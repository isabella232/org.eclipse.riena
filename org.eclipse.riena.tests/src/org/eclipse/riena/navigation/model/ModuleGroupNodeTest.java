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
package org.eclipse.riena.navigation.model;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests of the class {@link ModuleGroupNode}.
 */
@NonUITestCase
public class ModuleGroupNodeTest extends TestCase {

	/**
	 * Tests the method {@code isVisible()}.
	 */
	public void testIsVisible() {

		final ModuleGroupNode node = new ModuleGroupNode();
		node.setNavigationProcessor(new NavigationProcessor());
		assertFalse(node.isVisible());

		final ModuleNode mn = new ModuleNode();
		node.addChild(mn);
		assertTrue(node.isVisible());

		mn.setVisible(false);
		assertFalse(node.isVisible());

		mn.setVisible(true);
		assertTrue(node.isVisible());

		node.setVisible(false);
		assertFalse(node.isVisible());

	}

}
