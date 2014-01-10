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
package org.eclipse.riena.navigation.ui.controllers;

import junit.framework.TestCase;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.ComboRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.EmbeddedTitleBarRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.MultipleChoiceRidget;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.ridgets.IWindowRidget;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link SubModuleController}.
 */
@NonUITestCase
public class SubModuleControllerTest extends TestCase {

	public void testRestoreFocusFromRidgetAfterBlock() {
		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		shell.pack();
		shell.setVisible(true);

		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm); //$NON-NLS-1$

		final SubModuleNode node = new SubModuleNode();
		node.setNavigationProcessor(new NavigationProcessor());
		final SubModuleController controller = new SubModuleController(node);

		final LabelRidget labelRidget = new LabelRidget();
		labelRidget.setRetryRequestFocus(true);
		controller.addRidget("label", labelRidget); //$NON-NLS-1$

		controller.setBlocked(true);
		labelRidget.setRetryRequestFocus(true);
		controller.setBlocked(false);
		assertFalse(labelRidget.isRetryRequestFocus());
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the method {@code getFullTitle()}.
	 */
	public void testGetFullTitle() {

		final ModuleNode module = new ModuleNode("m");
		new ModuleController(module);

		final SubModuleNode subModule1 = new SubModuleNode(new NavigationNodeId("sm", "sm1"), "sm1");
		subModule1.setParent(module);
		module.addChild(subModule1);
		final MySubModuleController smController1 = new MySubModuleController(subModule1);
		assertEquals("m", smController1.getFullTitle());

		module.setPresentSingleSubModule(true);
		assertEquals("m - sm1", smController1.getFullTitle());

		final SubModuleNode subModule2 = new SubModuleNode(new NavigationNodeId("sm", "sm2"), "sm2");
		final MySubModuleController smController2 = new MySubModuleController(subModule2);
		subModule2.setParent(module);
		module.addChild(subModule2);
		assertEquals("m - sm1", smController1.getFullTitle());
		assertEquals("m - sm2", smController2.getFullTitle());

		module.setPresentSingleSubModule(false);
		assertEquals("m - sm1", smController1.getFullTitle());
		assertEquals("m - sm2", smController2.getFullTitle());

		final SubModuleNode subModule3 = new SubModuleNode(new NavigationNodeId("sm", "sm3"), "sm3");
		final MySubModuleController smController3 = new MySubModuleController(subModule3);
		subModule3.setParent(subModule1);
		subModule1.addChild(subModule3);
		assertEquals("m - sm1", smController1.getFullTitle());
		assertEquals("m - sm2", smController2.getFullTitle());
		assertEquals("m - sm1 - sm3", smController3.getFullTitle());

	}

	/**
	 * Tests the method {@code updateIcon(IWindowRidget)}.
	 */
	public void testUpdateIcon() {

		final EmbeddedTitleBarRidget windowRidget = new EmbeddedTitleBarRidget();

		final ModuleNode module = new ModuleNode("m");
		module.setIcon("mIcon");
		new ModuleController(module);

		final SubModuleNode subModule1 = new SubModuleNode(new NavigationNodeId("sm", "sm1"), "sm1");
		subModule1.setIcon("sm1Icon");
		subModule1.setParent(module);
		module.addChild(subModule1);
		final MySubModuleController smController1 = new MySubModuleController(subModule1);
		smController1.updateIcon(windowRidget);
		assertEquals("mIcon", windowRidget.getIcon());

		module.setPresentSingleSubModule(true);
		assertEquals("m - sm1", smController1.getFullTitle());
		smController1.updateIcon(windowRidget);
		assertEquals("sm1Icon", windowRidget.getIcon());

		final SubModuleNode subModule2 = new SubModuleNode(new NavigationNodeId("sm", "sm2"), "sm2");
		subModule2.setIcon("sm2Icon");
		final MySubModuleController smController2 = new MySubModuleController(subModule2);
		subModule2.setParent(module);
		module.addChild(subModule2);
		smController1.updateIcon(windowRidget);
		assertEquals("sm1Icon", windowRidget.getIcon());
		smController2.updateIcon(windowRidget);
		assertEquals("sm2Icon", windowRidget.getIcon());

	}

	/**
	 * Tests the <i>private</i> method {@code isInvisibleInTree()}.
	 */
	public void testIsInvisibleInTree() {

		final SubModuleNode subModule1 = new SubModuleNode(new NavigationNodeId("sm", "sm1"), "sm1");
		final SubModuleController smController1 = new SubModuleController(subModule1);
		boolean ret = ReflectionUtils.invokeHidden(smController1, "isInvisibleInTree");
		assertFalse(ret);

		final ModuleNode module = new ModuleNode("m");
		new ModuleController(module);
		subModule1.setParent(module);
		module.addChild(subModule1);
		ret = ReflectionUtils.invokeHidden(smController1, "isInvisibleInTree");
		assertTrue(ret);

		module.setPresentSingleSubModule(true);
		ret = ReflectionUtils.invokeHidden(smController1, "isInvisibleInTree");
		assertFalse(ret);

		module.setPresentSingleSubModule(false);
		final SubModuleNode subModule2 = new SubModuleNode(new NavigationNodeId("sm", "sm2"), "sm2");
		subModule2.setIcon("sm2Icon");
		final SubModuleController smController2 = new SubModuleController(subModule2);
		subModule2.setParent(module);
		module.addChild(subModule2);
		ret = ReflectionUtils.invokeHidden(smController1, "isInvisibleInTree");
		assertFalse(ret);
		ret = ReflectionUtils.invokeHidden(smController2, "isInvisibleInTree");
		assertFalse(ret);

	}

	public void testUpdateAllRidgetsFromModel() {

		final Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		shell.pack();
		shell.setVisible(true);

		final Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm); //$NON-NLS-1$

		final SubModuleNode node = new SubModuleNode();
		node.setNavigationProcessor(new NavigationProcessor());
		final SubModuleController controller = new SubModuleController(node);

		final LabelRidget labelRidget = new LabelRidget();
		controller.addRidget("label", labelRidget); //$NON-NLS-1$
		assertNotNull(controller.getRidgets());
		assertEquals(1, controller.getRidgets().size());

		final ComboRidget comboRidget = new ComboRidget();
		controller.addRidget("combo ridget", comboRidget); //$NON-NLS-1$
		assertNotNull(controller.getRidgets());
		assertEquals(2, controller.getRidgets().size());

		try {
			controller.updateAllRidgetsFromModel();
		} catch (final BindingException e) {
			fail("unexpected BindingException: " + e.getMessage()); //$NON-NLS-1$
		}

		final MultipleChoiceRidget multipleChoiceRidget = new MultipleChoiceRidget();
		controller.addRidget("multi choice ridget", multipleChoiceRidget); //$NON-NLS-1$
		assertNotNull(controller.getRidgets());
		assertEquals(3, controller.getRidgets().size());

		multipleChoiceRidget.setIgnoreBindingError(false);
		try {
			controller.updateAllRidgetsFromModel();
			fail("BindingException expected!"); //$NON-NLS-1$
		} catch (final BindingException e) {
			Nop.reason("BindingException is expected"); //$NON-NLS-1$
		}

		multipleChoiceRidget.setIgnoreBindingError(true);
		try {
			controller.updateAllRidgetsFromModel();
		} catch (final BindingException e) {
			fail("unexpected BindingException: " + e.getMessage()); //$NON-NLS-1$
		}
		SwtUtilities.dispose(shell);

	}

	/**
	 * This extension of {@code SubModuleController} is only necessary to increase the visibility of protected methods.
	 */
	private static class MySubModuleController extends SubModuleController {

		public MySubModuleController(final ISubModuleNode navigationNode) {
			super(navigationNode);
		}

		@Override
		public String getFullTitle() {
			return super.getFullTitle();
		}

		@Override
		public void updateIcon(final IWindowRidget windowRidget) {
			super.updateIcon(windowRidget);
		}

	}

}
