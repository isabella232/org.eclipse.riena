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
package org.eclipse.riena.ui.swt.utils;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Tests of the class {@link SwtUtilities}
 */
@UITestCase
public class SwtUtilitiesTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// clear cache 
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 0.0f, 0.0f }); //$NON-NLS-1$
	}

	@Override
	protected void tearDown() throws Exception {
		// clear cache 
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 0.0f, 0.0f }); //$NON-NLS-1$
		super.tearDown();
	}

	/**
	 * Test of the method {@code SwtUtilities.isDisposed(Widget)};
	 */
	public void testIsDisposedWidget() {

		Shell shell = new Shell();
		Label label = new Label(shell, SWT.NONE);

		assertFalse(SwtUtilities.isDisposed(label));

		label.dispose();
		assertTrue(SwtUtilities.isDisposed(label));

		label = null;
		assertTrue(SwtUtilities.isDisposed(label));

		shell.dispose();
		assertTrue(SwtUtilities.isDisposed(shell));

		shell = null;
		assertTrue(SwtUtilities.isDisposed(shell));

	}

	/**
	 * Test of the method {@code SwtUtilities.isDisposed(Resource)};
	 */
	public void testIsDisposedResource() {

		Color color = new Color(Display.getCurrent(), 0, 0, 0);

		assertFalse(SwtUtilities.isDisposed(color));

		color.dispose();
		assertTrue(SwtUtilities.isDisposed(color));

		color = null;
		assertTrue(SwtUtilities.isDisposed(color));

	}

	public void testGetDpiFactors() throws Exception {

		// default values (will be cached)
		final float[] defaultFactors = SwtUtilities.getDpiFactors();
		assertEquals(2, defaultFactors.length);

		// cached values (and not LnF values)
		final MyLnf lnf = new MyLnf();
		LnfManager.setLnf(lnf);
		float[] factors = SwtUtilities.getDpiFactors();
		assertEquals(2, factors.length);
		assertEquals(defaultFactors[0], factors[0]);
		assertEquals(defaultFactors[1], factors[1]);

		// clear cache 
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 0.0f, 0.0f }); //$NON-NLS-1$

		// LnF values
		LnfManager.setLnf(lnf);
		factors = SwtUtilities.getDpiFactors();
		assertEquals(2, factors.length);
		final float[] lnfFactors = lnf.getDpiFactors(new Point(0, 0));
		assertEquals(lnfFactors[0], factors[0]);
		assertEquals(lnfFactors[1], factors[1]);

	}

	public void testConvertXToDpi() throws Exception {

		final MyLnf lnf = new MyLnf();
		LnfManager.setLnf(lnf);

		// convert with LnF values
		int x = SwtUtilities.convertXToDpi(2);
		int expectedValue = 40;
		assertEquals(expectedValue, x);

		// convert with cached values
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 1.2f, 1.3f }); //$NON-NLS-1$
		x = SwtUtilities.convertXToDpi(2);
		expectedValue = 2;
		assertEquals(expectedValue, x);

		// convert with cached values
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 1.9f, 1.0f }); //$NON-NLS-1$
		x = SwtUtilities.convertXToDpi(2);
		expectedValue = 4;
		assertEquals(expectedValue, x);

	}

	public void testConvertYToDpi() throws Exception {

		final MyLnf lnf = new MyLnf();
		LnfManager.setLnf(lnf);

		// convert with LnF values
		int y = SwtUtilities.convertYToDpi(2);
		int expectedValue = 6;
		assertEquals(expectedValue, y);

		// convert with cached values
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 1.2f, 2.4f }); //$NON-NLS-1$
		y = SwtUtilities.convertYToDpi(2);
		expectedValue = 5;
		assertEquals(expectedValue, y);

		// convert with cached values
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 1.9f, 4.4f }); //$NON-NLS-1$
		y = SwtUtilities.convertYToDpi(2);
		expectedValue = 9;
		assertEquals(expectedValue, y);

		// convert with cached values
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 1.9f, 4.8f }); //$NON-NLS-1$
		y = SwtUtilities.convertYToDpi(2);
		expectedValue = 10;
		assertEquals(expectedValue, y);

	}

	public void testConvertXToDpiTruncate() throws Exception {

		final MyLnf lnf = new MyLnf();
		LnfManager.setLnf(lnf);

		// convert with LnF values
		int x = SwtUtilities.convertXToDpiTruncate(2);
		int expectedValue = 40;
		assertEquals(expectedValue, x);

		// convert with cached values
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 1.2f, 1.3f }); //$NON-NLS-1$
		x = SwtUtilities.convertXToDpiTruncate(2);
		expectedValue = 2;
		assertEquals(expectedValue, x);

		// convert with cached values
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 1.9f, 1.0f }); //$NON-NLS-1$
		x = SwtUtilities.convertXToDpiTruncate(2);
		expectedValue = 3;
		assertEquals(expectedValue, x);

	}

	public void testConvertYToDpiTruncate() throws Exception {

		final MyLnf lnf = new MyLnf();
		LnfManager.setLnf(lnf);

		// convert with LnF values
		int y = SwtUtilities.convertYToDpiTruncate(2);
		int expectedValue = 6;
		assertEquals(expectedValue, y);

		// convert with cached values
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 1.2f, 2.4f }); //$NON-NLS-1$
		y = SwtUtilities.convertYToDpiTruncate(2);
		expectedValue = 4;
		assertEquals(expectedValue, y);

		// convert with cached values
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 1.9f, 4.4f }); //$NON-NLS-1$
		y = SwtUtilities.convertYToDpiTruncate(2);
		expectedValue = 8;
		assertEquals(expectedValue, y);

		// convert with cached values
		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 1.9f, 4.8f }); //$NON-NLS-1$
		y = SwtUtilities.convertYToDpiTruncate(2);
		expectedValue = 9;
		assertEquals(expectedValue, y);

	}

	private static class MyLnf extends RienaDefaultLnf {

		@Override
		public float[] getDpiFactors(final Point dpi) {
			return new float[] { 20.0f, 3.0f };
		}

	}

}
