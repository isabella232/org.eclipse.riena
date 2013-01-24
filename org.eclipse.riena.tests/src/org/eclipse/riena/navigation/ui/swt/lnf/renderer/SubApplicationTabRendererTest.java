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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import junit.framework.TestCase;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.swt.Activator;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link SubApplicationTabRenderer}.
 */
@UITestCase
public class SubApplicationTabRendererTest extends TestCase {

	private Shell shell;
	private GC gc;
	private RienaDefaultLnf originalLnf;
	private MyLnf lnf;

	@SuppressWarnings("restriction")
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		if (Activator.getDefault() == null) {
			fail("This test only runs correct as JUnit Plug-in Test!");
		}

		shell = new Shell();
		gc = new GC(shell);
		lnf = new MyLnf();
		originalLnf = LnfManager.getLnf();
		LnfManager.setLnf(lnf);
		LnfManager.getLnf().initialize();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		gc.dispose();
		gc = null;
		SwtUtilities.dispose(shell);
		lnf.uninitialize();
		lnf = null;
		LnfManager.setLnf(originalLnf);
	}

	/**
	 * Tests the <i>private</i> method {@code getImageTextWidth(GC)}.
	 */
	public void testGetImageTextWidth() {

		final SubApplicationTabRenderer renderer = new SubApplicationTabRenderer();

		renderer.setLabel("Hello &World!");
		final int widthWithoutMnemonic = ReflectionUtils.invokeHidden(renderer, "getImageTextWidth", gc);
		renderer.setLabel("Hello World!");
		final int widthWithMnemonic = ReflectionUtils.invokeHidden(renderer, "getImageTextWidth", gc);
		assertEquals(widthWithoutMnemonic, widthWithMnemonic);

		renderer.setIcon(null);
		final int widthWithoutIcon = ReflectionUtils.invokeHidden(renderer, "getImageTextWidth", gc);
		renderer.setIcon("eclipse.gif");
		int widthWithIcon = ReflectionUtils.invokeHidden(renderer, "getImageTextWidth", gc);
		assertEquals(widthWithoutIcon, widthWithIcon);

		lnf.setShowIcon(true);
		widthWithIcon = ReflectionUtils.invokeHidden(renderer, "getImageTextWidth", gc);
		assertTrue(widthWithoutIcon < widthWithIcon);

	}

	/**
	 * This Look and Feel returns always the same image.
	 */
	private class MyLnf extends RienaDefaultLnf {

		public void setShowIcon(final boolean show) {
			putLnfSetting(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TAB_SHOW_ICON, show);
		}

	}

}
