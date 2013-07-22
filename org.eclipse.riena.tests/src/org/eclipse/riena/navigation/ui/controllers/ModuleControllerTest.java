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
package org.eclipse.riena.navigation.ui.controllers;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.ShellRidget;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 * Tests of the class <code>ModuleController</code>.
 */
@UITestCase
public class ModuleControllerTest extends TestCase {

	public void testAfterBind() throws Exception {

		final ModuleNode node = new ModuleNode();
		node.setClosable(true);
		node.setLabel("Hello"); //$NON-NLS-1$
		final ModuleController controller = new ModuleController(node);
		final ShellRidget shellRidget = new ShellRidget();
		final Shell shell = new Shell();
		shellRidget.setUIControl(shell);
		controller.setWindowRidget(shellRidget);
		controller.afterBind();
		assertTrue(controller.isCloseable());
		assertEquals("Hello", shellRidget.getTitle()); //$NON-NLS-1$

		node.setClosable(false);
		controller.configureRidgets();
		assertFalse(controller.isCloseable());

		shell.dispose();

	}

	public void testUpdateWindowTitle() throws Exception {

		// test updateWindowTitle() with one submodule
		final ModuleNode node = new ModuleNode();
		final SubModuleNode subNode1 = new SubModuleNode();
		node.addChild(subNode1);
		subNode1.activate(null);

		final ModuleController controller = new ModuleController(node);
		final SubModuleController subModuleController1 = new SubModuleController(subNode1);

		final ShellRidget shellRidget = new ShellRidget();
		final Shell shell = new Shell();
		shellRidget.setUIControl(shell);
		controller.setWindowRidget(shellRidget);

		final ShellRidget subModuleShellRidget1 = new ShellRidget();
		final Shell subModuleShell1 = new Shell();
		subModuleShellRidget1.setUIControl(subModuleShell1);
		subModuleController1.setWindowRidget(subModuleShellRidget1);

		node.setLabel("Hello"); //$NON-NLS-1$

		assertEquals("Hello", shellRidget.getTitle()); //$NON-NLS-1$
		assertEquals("Hello", subModuleShellRidget1.getTitle()); //$NON-NLS-1$

		shell.dispose();
		subModuleShell1.dispose();

		// test updateWindowTitle() with two submodules
		final ModuleNode modNode = new ModuleNode();
		final SubModuleNode subModNode1 = new SubModuleNode(new NavigationNodeId("sm1")); //$NON-NLS-1$
		final SubModuleNode subModNode2 = new SubModuleNode(new NavigationNodeId("sm2")); //$NON-NLS-1$
		modNode.addChild(subModNode1);
		modNode.addChild(subModNode2);

		final ModuleController modController = new ModuleController(modNode);
		final SubModuleController subModController1 = new SubModuleController(subModNode1);
		final SubModuleController subModController2 = new SubModuleController(subModNode2);

		final ShellRidget shellRidgetMod = new ShellRidget();
		final Shell shellMod = new Shell();
		shellRidgetMod.setUIControl(shellMod);
		modController.setWindowRidget(shellRidgetMod);

		final ShellRidget subModShellRidget1 = new ShellRidget();
		final Shell subModShell1 = new Shell();
		subModShellRidget1.setUIControl(subModShell1);
		subModController1.setWindowRidget(subModShellRidget1);

		final ShellRidget subModShellRidget2 = new ShellRidget();
		final Shell subModShell2 = new Shell();
		subModShellRidget2.setUIControl(subModShell2);
		subModController2.setWindowRidget(subModShellRidget2);

		modNode.setLabel("Module"); //$NON-NLS-1$
		subModNode1.activate(null);
		subModNode1.setLabel("SubModule 1"); //$NON-NLS-1$
		subModNode1.deactivate(null);
		subModNode2.activate(null);
		subModNode2.setLabel("SubModule 2"); //$NON-NLS-1$

		assertEquals("Module", shellRidgetMod.getTitle()); //$NON-NLS-1$
		assertEquals("Module - SubModule 1", subModShellRidget1.getTitle()); //$NON-NLS-1$
		assertEquals("Module - SubModule 2", subModShellRidget2.getTitle()); //$NON-NLS-1$

		modNode.setLabel("newTitle"); //$NON-NLS-1$
		assertEquals("newTitle", shellRidgetMod.getTitle()); //$NON-NLS-1$
		assertEquals("Module - SubModule 1", subModShellRidget1.getTitle()); //$NON-NLS-1$
		assertEquals("newTitle - SubModule 2", subModShellRidget2.getTitle()); //$NON-NLS-1$

		shellMod.dispose();
		subModShell1.dispose();
		subModShell2.dispose();

	}

	/**
	 * Tests the method {@code getVisibleChildren}.
	 */
	public void testGetVisibleChildren() {

		final ModuleNode node = new ModuleNode();
		node.setNavigationProcessor(new NavigationProcessor());
		final SubModuleNode sub1 = new SubModuleNode(new NavigationNodeId("sm1")); //$NON-NLS-1$
		node.addChild(sub1);
		final SubModuleNode sub2 = new SubModuleNode(new NavigationNodeId("sm2")); //$NON-NLS-1$
		node.addChild(sub2);
		final SubModuleNode sub3 = new SubModuleNode();
		node.addChild(sub3);

		final ModuleController controller = new ModuleController(node);
		List<INavigationNode<?>> nodes = controller.getVisibleChildren(node);
		assertEquals(3, nodes.size());
		assertSame(sub1, nodes.get(0));
		assertSame(sub2, nodes.get(1));
		assertSame(sub3, nodes.get(2));

		sub1.setEnabled(false);
		sub2.setVisible(false);
		nodes = controller.getVisibleChildren(node);
		assertEquals(2, nodes.size());
		assertSame(sub1, nodes.get(0));
		assertSame(sub3, nodes.get(1));

	}

	/**
	 * Tests the method {@code hasSingleLeafChild}.
	 */
	public void testHasSingleLeafChild() {

		final ModuleNode node = new ModuleNode();
		node.setNavigationProcessor(new NavigationProcessor());
		final SubModuleNode sub1 = new SubModuleNode(new NavigationNodeId("sm1")); //$NON-NLS-1$
		node.addChild(sub1);
		final SubModuleNode sub2 = new SubModuleNode(new NavigationNodeId("sm2")); //$NON-NLS-1$
		node.addChild(sub2);
		final SubModuleNode sub3 = new SubModuleNode();
		sub2.addChild(sub3);

		final ModuleController controller = new ModuleController(node);
		assertFalse(controller.hasSingleLeafChild());

		sub1.setVisible(false);
		assertFalse(controller.hasSingleLeafChild());

		sub1.setVisible(true);
		sub2.setVisible(false);
		assertTrue(controller.hasSingleLeafChild());

		sub2.setVisible(true);
		assertFalse(controller.hasSingleLeafChild());

		node.removeChild(sub2);
		assertTrue(controller.hasSingleLeafChild());

	}

}
