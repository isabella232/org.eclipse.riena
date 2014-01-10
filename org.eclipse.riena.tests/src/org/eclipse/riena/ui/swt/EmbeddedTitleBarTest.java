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
package org.eclipse.riena.ui.swt;

import java.math.BigInteger;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.lnf.renderer.EmbeddedTitlebarRenderer;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link EmbeddedTitleBar}.
 */
@UITestCase
public class EmbeddedTitleBarTest extends TestCase {

	private Shell shell;
	private EmbeddedTitleBarMock titleBar;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		titleBar = new EmbeddedTitleBarMock(shell, SWT.NONE);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SwtUtilities.dispose(titleBar);
		SwtUtilities.dispose(shell);
	}

	/**
	 * Tests the method {@code addListeners}.
	 */
	public void testAddListeners() {
		// method addListeners was already called while creating instance
		assertEquals(1, titleBar.getListeners(SWT.Paint).length);
	}

	/**
	 * Tests the method {@code getLnfTitlebarRenderer}.
	 */
	public void testGetLnfTitlebarRenderer() {

		final EmbeddedTitlebarRenderer renderer = ReflectionUtils.invokeHidden(titleBar, "getLnfTitlebarRenderer",
				new Object[] {});
		assertNotNull(renderer);

	}

	/**
	 * Tests the method {@code setTitle(String)}.
	 */
	public void testSetTitle() {

		titleBar.setTitle("Hello");
		titleBar.resetRedrawCalled();

		titleBar.setTitle("Hello");
		assertFalse(titleBar.isRedrawCalled());

		titleBar.setTitle("Hi");
		assertTrue(titleBar.isRedrawCalled());

	}

	/**
	 * Tests the method {@code setActive(boolean)}.
	 */
	public void testSetActive() {

		titleBar.setWindowActive(true);
		titleBar.resetRedrawCalled();

		titleBar.setWindowActive(true);
		assertFalse(titleBar.isRedrawCalled());

		titleBar.setWindowActive(false);
		assertTrue(titleBar.isRedrawCalled());

	}

	/**
	 * Tests the method {@code setCloseable(boolean)}.
	 */
	public void testSetCloseable() {

		titleBar.setCloseable(true);
		titleBar.resetRedrawCalled();

		titleBar.setCloseable(true);
		assertFalse(titleBar.isRedrawCalled());

		titleBar.setCloseable(false);
		assertTrue(titleBar.isRedrawCalled());

	}

	/**
	 * Tests the method {@code setHover(boolean)}.
	 */
	public void testSetHover() {

		titleBar.setHover(true);
		titleBar.resetRedrawCalled();

		titleBar.setHover(true);
		assertFalse(titleBar.isRedrawCalled());

		titleBar.setHover(false);
		assertTrue(titleBar.isRedrawCalled());

	}

	/**
	 * Tests the method {@code setPressed(boolean)}.
	 */
	public void testSetPressed() {

		titleBar.setPressed(true);
		titleBar.resetRedrawCalled();

		titleBar.setPressed(true);
		assertFalse(titleBar.isRedrawCalled());

		titleBar.setPressed(false);
		assertTrue(titleBar.isRedrawCalled());

	}

	/**
	 * Tests the (private) method {@code hasChanged}.
	 */
	public void testHasChanged() {

		String strg1 = "1";
		String strg2 = "2";
		boolean changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", strg1, strg2);
		assertTrue(changed);

		strg2 = "1";
		changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", strg1, strg2);
		assertFalse(changed);

		strg1 = null;
		strg2 = null;
		changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", strg1, strg2);
		assertFalse(changed);

		strg1 = "1";
		changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", strg1, strg2);
		assertTrue(changed);

		strg1 = null;
		strg2 = "1";
		changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", strg1, strg2);
		assertTrue(changed);

		final BigInteger bigInt1 = BigInteger.valueOf(1);
		BigInteger bigInt2 = BigInteger.valueOf(2);
		changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", bigInt1, bigInt2);
		assertTrue(changed);

		bigInt2 = BigInteger.valueOf(1);
		changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", bigInt1, bigInt2);
		assertFalse(changed);

		final boolean b1 = true;
		boolean b2 = false;
		changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", b1, b2);
		assertTrue(changed);

		b2 = true;
		changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", b1, b2);
		assertFalse(changed);

		final int i1 = 11;
		int i2 = 12;
		changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", i1, i2);
		assertTrue(changed);

		i2 = 11;
		changed = ReflectionUtils.invokeHidden(titleBar, "hasChanged", i1, i2);
		assertFalse(changed);

	}

	/**
	 * Mock of the class {@link EmbeddedTitleBar}. The mock notice if the method
	 * {@code redraw()} was called.
	 */
	private static class EmbeddedTitleBarMock extends EmbeddedTitleBar {

		private boolean redrawCalled;

		public EmbeddedTitleBarMock(final Composite parent, final int style) {
			super(parent, style);
			resetRedrawCalled();
		}

		public void resetRedrawCalled() {
			redrawCalled = false;
		}

		public boolean isRedrawCalled() {
			return redrawCalled;
		}

		@Override
		public void redraw() {
			super.redraw();
			redrawCalled = true;
		}

	}

}
