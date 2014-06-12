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
package org.eclipse.riena.internal.ui.swt.layout;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@linkplain DpiGridData}.
 */
@UITestCase
public class DpiGridDataTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// clear cache 
		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 0.0f, 0.0f }); //$NON-NLS-1$
	}

	@Override
	protected void tearDown() throws Exception {
		// clear cache 
		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 0.0f, 0.0f }); //$NON-NLS-1$
		super.tearDown();
	}

	/**
	 * Tests the constructors of the class {@linkplain DpiGridData}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testCreate() throws Exception {

		ReflectionUtils.setHidden(SwtUtilities.class, "cachedDpiFactors", new float[] { 3.0f, 4.0f }); //$NON-NLS-1$

		final GridData gridData = new GridData();
		gridData.exclude = true;
		gridData.widthHint = 12;
		gridData.heightHint = 34;
		gridData.minimumWidth = 56;
		gridData.minimumHeight = 78;
		gridData.horizontalIndent = 90;
		gridData.verticalIndent = 123;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 4;
		gridData.verticalSpan = 5;
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.verticalAlignment = SWT.BOTTOM;

		DpiGridData dpiGridData = new DpiGridData(gridData);
		assertEquals(dpiGridData.exclude, true);
		assertEquals(dpiGridData.widthHint, 36);
		assertEquals(dpiGridData.heightHint, 136);
		assertEquals(dpiGridData.minimumWidth, 168);
		assertEquals(dpiGridData.minimumHeight, 312);
		assertEquals(dpiGridData.horizontalIndent, 270);
		assertEquals(dpiGridData.verticalIndent, 492);
		assertEquals(dpiGridData.grabExcessHorizontalSpace, true);
		assertEquals(dpiGridData.grabExcessVerticalSpace, false);
		assertEquals(dpiGridData.horizontalSpan, 4);
		assertEquals(dpiGridData.verticalSpan, 5);
		assertEquals(dpiGridData.horizontalAlignment, SWT.LEFT);
		assertEquals(dpiGridData.verticalAlignment, SWT.BOTTOM);

		dpiGridData = new DpiGridData();
		assertEquals(dpiGridData.exclude, false);
		assertEquals(dpiGridData.widthHint, SWT.DEFAULT);
		assertEquals(dpiGridData.heightHint, SWT.DEFAULT);
		assertEquals(dpiGridData.minimumWidth, 0);
		assertEquals(dpiGridData.minimumHeight, 0);
		assertEquals(dpiGridData.horizontalIndent, 0);
		assertEquals(dpiGridData.verticalIndent, 0);
		assertEquals(dpiGridData.grabExcessHorizontalSpace, false);
		assertEquals(dpiGridData.grabExcessVerticalSpace, false);
		assertEquals(dpiGridData.horizontalSpan, 1);
		assertEquals(dpiGridData.verticalSpan, 1);
		assertEquals(dpiGridData.horizontalAlignment, SWT.BEGINNING);
		assertEquals(dpiGridData.verticalAlignment, GridData.CENTER);

	}

	/**
	 * Tests the method {@code originGridDataEquals(GridData)}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testOriginGridDataEquals() throws Exception {

		final GridData gridData = new GridData();
		gridData.exclude = true;
		gridData.widthHint = 12;
		gridData.heightHint = 34;
		gridData.minimumWidth = 56;
		gridData.minimumHeight = 78;
		gridData.horizontalIndent = 90;
		gridData.verticalIndent = 123;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.horizontalSpan = 4;
		gridData.verticalSpan = 5;
		gridData.horizontalAlignment = SWT.LEFT;
		gridData.verticalAlignment = SWT.BOTTOM;

		DpiGridData dpiGridData = new DpiGridData();
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		dpiGridData = new DpiGridData(gridData);
		assertTrue(dpiGridData.originGridDataEquals(gridData));

		gridData.widthHint = 4711;
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		gridData.widthHint = 12;
		gridData.heightHint = 815;
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		gridData.heightHint = 34;
		gridData.minimumWidth = 1234;
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		gridData.minimumWidth = 56;
		gridData.minimumHeight = 2345;
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		gridData.minimumHeight = 78;
		gridData.horizontalIndent = 3456;
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		gridData.horizontalIndent = 90;
		gridData.verticalIndent = 5678;
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		gridData.verticalIndent = 123;
		gridData.horizontalSpan = 11;
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		gridData.horizontalSpan = 4;
		gridData.verticalSpan = 12;
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		gridData.verticalSpan = 5;
		gridData.horizontalAlignment = SWT.RIGHT;
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		gridData.horizontalAlignment = SWT.LEFT;
		gridData.verticalAlignment = SWT.TOP;
		assertFalse(dpiGridData.originGridDataEquals(gridData));

		gridData.verticalAlignment = SWT.BOTTOM;
		assertTrue(dpiGridData.originGridDataEquals(gridData));

	}

}
