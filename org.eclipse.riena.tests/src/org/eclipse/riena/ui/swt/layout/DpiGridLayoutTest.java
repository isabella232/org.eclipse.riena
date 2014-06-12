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
package org.eclipse.riena.ui.swt.layout;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.swt.layout.DpiGridData;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests the class {@linkplain DpiGridLayout}.
 */
@UITestCase
public class DpiGridLayoutTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		// clear cache 
		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 0.0f, 0.0f }); //$NON-NLS-1$
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
		// clear cache 
		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 0.0f, 0.0f }); //$NON-NLS-1$
		super.tearDown();
	}

	/**
	 * Tests the <i>private</i> method {@code getMarginPoint()}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetMarginPoint() throws Exception {

		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 2.0f, 3.0f }); //$NON-NLS-1$
		final DpiGridLayout layout = new DpiGridLayout();
		layout.marginBottom = 2;
		layout.marginHeight = 3;
		layout.marginTop = 4;
		layout.marginLeft = 10;
		layout.marginWidth = 11;
		layout.marginRight = 12;

		final Point point = ReflectionUtils.invokeHidden(layout, "getMarginPoint"); //$NON-NLS-1$
		assertEquals(88, point.x);
		assertEquals(36, point.y);

	}

	/**
	 * Tests the <i>private</i> method {@code getDpiGridData(Control)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetDpiGridData() throws Exception {

		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 2.0f, 3.0f }); //$NON-NLS-1$
		final DpiGridLayout layout = new DpiGridLayout();

		final Label label = new Label(shell, SWT.NONE);
		DpiGridData data = ReflectionUtils.invokeHidden(layout, "getDpiGridData", label); //$NON-NLS-1$
		assertNull(data);

		final GridData gridData = new GridData(12, 34);
		label.setLayoutData(gridData);
		data = ReflectionUtils.invokeHidden(layout, "getDpiGridData", label); //$NON-NLS-1$
		assertNotNull(data);
		assertEquals(24, data.widthHint);
		assertEquals(102, data.heightHint);
		assertSame(gridData, label.getLayoutData());
		assertEquals(12, gridData.widthHint);
		assertEquals(34, gridData.heightHint);

		gridData.widthHint = 10;
		data = ReflectionUtils.invokeHidden(layout, "getDpiGridData", label); //$NON-NLS-1$
		assertNotNull(data);
		assertEquals(20, data.widthHint);
		assertEquals(102, data.heightHint);
		assertSame(gridData, label.getLayoutData());
		assertEquals(10, gridData.widthHint);
		assertEquals(34, gridData.heightHint);

		gridData.heightHint = 1;
		data = ReflectionUtils.invokeHidden(layout, "getDpiGridData", label); //$NON-NLS-1$
		assertNotNull(data);
		assertEquals(20, data.widthHint);
		assertEquals(3, data.heightHint);
		assertSame(gridData, label.getLayoutData());
		assertEquals(10, gridData.widthHint);
		assertEquals(1, gridData.heightHint);

	}

	/**
	 * Tests the <i>package protected</i> method {@code getData}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetData() throws Exception {

		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 2.0f, 3.0f }); //$NON-NLS-1$
		final DpiGridLayout layout = new DpiGridLayout();

		final Label label = new Label(shell, SWT.NONE);
		final GridData gridData = new GridData(12, 34);
		label.setLayoutData(gridData);

		final Control[][] grid = new Control[1][1];
		grid[0][0] = label;

		DpiGridData data = ReflectionUtils.invokeHidden(layout, "getData", grid, 0, 0, 1, 1, true); //$NON-NLS-1$
		data = ReflectionUtils.invokeHidden(layout, "getDpiGridData", label); //$NON-NLS-1$
		assertNotNull(data);
		assertEquals(24, data.widthHint);
		assertEquals(102, data.heightHint);
		assertSame(gridData, label.getLayoutData());
		assertEquals(12, gridData.widthHint);
		assertEquals(34, gridData.heightHint);

	}

}
