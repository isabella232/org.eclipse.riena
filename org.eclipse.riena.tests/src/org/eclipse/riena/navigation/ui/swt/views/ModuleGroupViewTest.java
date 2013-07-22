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

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.listener.ModuleGroupNodeListener;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationProcessor;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;
import org.eclipse.riena.ui.swt.lnf.ILnfRenderer;
import org.eclipse.riena.ui.swt.lnf.ILnfRendererExtension;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link ModuleGroupView}.
 */
@UITestCase
public class ModuleGroupViewTest extends TestCase {

	private ModuleGroupView view;
	private ModuleGroupNode node;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {

		shell = new Shell();
		view = new ModuleGroupView(shell, SWT.NONE);
		node = new ModuleGroupNode();
		node.setNavigationProcessor(new NavigationProcessor());
		view.bind(node);

	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(view);
		SwtUtilities.dispose(shell);
		node = null;
	}

	/**
	 * Tests the method {@code calculateHeight()}.
	 */
	public void testCalculateHeight() {
		final RienaDefaultLnf originalLnf = LnfManager.getLnf();

		try {
			LnfManager.setLnf(new MyLnF());

			int y = view.calculateHeight(SWT.DEFAULT, 10);
			assertEquals(10, y);

			final ModuleView moduleView = new ModuleView(shell);
			moduleView.setModuleGroupNode(node);
			final ModuleNode moduleNode = new ModuleNode();
			node.addChild(moduleNode);
			moduleView.bind(moduleNode);

			view.registerModuleView(moduleView);
			y = view.calculateHeight(SWT.DEFAULT, 10);
			assertTrue(y > 10);
			final FormData data = (FormData) view.getLayoutData();
			assertEquals(10, data.top.offset);
			assertTrue((data.bottom.offset > 10) && (data.bottom.offset < y));

			node.setVisible(false);
			y = view.calculateHeight(SWT.DEFAULT, 10);
			assertEquals(10, y);
		} finally {
			LnfManager.setLnf(originalLnf);
		}
	}

	public void testUnbind() throws Exception {

		final List<ModuleGroupNodeListener> listeners = ReflectionUtils.getHidden(node, "listeners");

		assertEquals(1, listeners.size());

		node.dispose();

		assertTrue(listeners.isEmpty());
	}

	/**
	 * Tests the <i>private</i> method {@code equals(FormData, FormData)}.
	 */
	public void testEqualsFormDatat() {

		final FormData fd1 = new FormData();
		boolean ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd1);
		assertTrue(ret);

		final FormData fd2 = new FormData();
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertTrue(ret);

		fd1.width = 25;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertFalse(ret);

		fd2.width = 25;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertTrue(ret);

		fd1.height = 25;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertFalse(ret);

		fd2.height = 25;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertTrue(ret);

		final FormAttachment fa1 = new FormAttachment(1, 2);
		final FormAttachment fa2 = new FormAttachment(11, 22);
		final FormAttachment fa3 = new FormAttachment(1, 2);

		fd1.bottom = fa1;
		fd2.bottom = fa2;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertFalse(ret);

		fd2.bottom = fa3;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertTrue(ret);

		fd1.left = fa1;
		fd2.left = fa2;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertFalse(ret);

		fd2.left = fa3;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertTrue(ret);

		fd1.right = fa1;
		fd2.right = fa2;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertFalse(ret);

		fd2.right = fa3;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertTrue(ret);

		fd1.top = fa1;
		fd2.top = fa2;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertFalse(ret);

		fd2.top = fa3;
		ret = ReflectionUtils.invokeHidden(view, "equals", fd1, fd2);
		assertTrue(ret);

	}

	/**
	 * Tests the <i>private</i> method
	 * {@code equals(FormAttachment, FormAttachment)}.
	 */
	public void testEqualsFormAttachment() {

		final FormAttachment fa1 = new FormAttachment();
		boolean ret = ReflectionUtils.invokeHidden(view, "equals", fa1, fa1);
		assertTrue(ret);

		final FormAttachment fa2 = new FormAttachment();
		ret = ReflectionUtils.invokeHidden(view, "equals", fa1, fa2);
		assertTrue(ret);

		fa1.alignment = SWT.RIGHT;
		ret = ReflectionUtils.invokeHidden(view, "equals", fa1, fa2);
		assertFalse(ret);

		fa2.alignment = SWT.RIGHT;
		ret = ReflectionUtils.invokeHidden(view, "equals", fa1, fa2);
		assertTrue(ret);

		fa1.denominator = 50;
		ret = ReflectionUtils.invokeHidden(view, "equals", fa1, fa2);
		assertFalse(ret);

		fa2.denominator = 50;
		ret = ReflectionUtils.invokeHidden(view, "equals", fa1, fa2);
		assertTrue(ret);

		fa1.numerator = 11;
		ret = ReflectionUtils.invokeHidden(view, "equals", fa1, fa2);
		assertFalse(ret);

		fa2.numerator = 11;
		ret = ReflectionUtils.invokeHidden(view, "equals", fa1, fa2);
		assertTrue(ret);

		fa1.offset = 12;
		ret = ReflectionUtils.invokeHidden(view, "equals", fa1, fa2);
		assertFalse(ret);

		fa2.offset = 12;
		ret = ReflectionUtils.invokeHidden(view, "equals", fa1, fa2);
		assertTrue(ret);

	}

	private static class MyLnF extends RienaDefaultLnf {
		{
			update(new ILnfRendererExtension[] { new ILnfRendererExtension() {
				public String getLnfKey() {
					return LnfKeyConstants.MODULE_GROUP_RENDERER;
				}

				public String getLnfId() {
					return "";
				}

				public ILnfRenderer createRenderer() {
					return new ModuleGroupRenderer();
				}
			} });
		}
	}

}
