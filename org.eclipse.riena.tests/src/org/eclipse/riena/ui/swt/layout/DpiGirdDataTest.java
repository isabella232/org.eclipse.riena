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
import org.eclipse.swt.layout.GridData;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@linkplain DpiGridData}.
 */
@UITestCase
public class DpiGirdDataTest extends TestCase {

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
	 * Tests the constructors of the class {@linkplain DpiGridData}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testCreate() throws Exception {

		ReflectionUtils.setHidden(SwtUtilities.class, "cacheDpiFactors", new float[] { 3.0f, 4.0f }); //$NON-NLS-1$

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

		DpiGridData dpiGridData = ReflectionUtils.newInstanceHidden(DpiGridData.class, gridData);
		assertHidden(dpiGridData, "exclude", true); //$NON-NLS-1$
		assertHidden(dpiGridData, "widthHint", 36); //$NON-NLS-1$
		assertHidden(dpiGridData, "heightHint", 136); //$NON-NLS-1$
		assertHidden(dpiGridData, "minimumWidth", 168); //$NON-NLS-1$
		assertHidden(dpiGridData, "minimumHeight", 312); //$NON-NLS-1$
		assertHidden(dpiGridData, "horizontalIndent", 270); //$NON-NLS-1$
		assertHidden(dpiGridData, "verticalIndent", 492); //$NON-NLS-1$
		assertHidden(dpiGridData, "grabExcessHorizontalSpace", true); //$NON-NLS-1$
		assertHidden(dpiGridData, "grabExcessVerticalSpace", false); //$NON-NLS-1$
		assertHidden(dpiGridData, "horizontalSpan", 4); //$NON-NLS-1$
		assertHidden(dpiGridData, "verticalSpan", 5); //$NON-NLS-1$
		assertHidden(dpiGridData, "horizontalAlignment", SWT.LEFT); //$NON-NLS-1$
		assertHidden(dpiGridData, "verticalAlignment", SWT.BOTTOM); //$NON-NLS-1$

		dpiGridData = ReflectionUtils.newInstanceHidden(DpiGridData.class);
		assertHidden(dpiGridData, "exclude", false); //$NON-NLS-1$
		assertHidden(dpiGridData, "widthHint", SWT.DEFAULT); //$NON-NLS-1$
		assertHidden(dpiGridData, "heightHint", SWT.DEFAULT); //$NON-NLS-1$
		assertHidden(dpiGridData, "minimumWidth", 0); //$NON-NLS-1$
		assertHidden(dpiGridData, "minimumHeight", 0); //$NON-NLS-1$
		assertHidden(dpiGridData, "horizontalIndent", 0); //$NON-NLS-1$
		assertHidden(dpiGridData, "verticalIndent", 0); //$NON-NLS-1$
		assertHidden(dpiGridData, "grabExcessHorizontalSpace", false); //$NON-NLS-1$
		assertHidden(dpiGridData, "grabExcessVerticalSpace", false); //$NON-NLS-1$
		assertHidden(dpiGridData, "horizontalSpan", 1); //$NON-NLS-1$
		assertHidden(dpiGridData, "verticalSpan", 1); //$NON-NLS-1$
		assertHidden(dpiGridData, "horizontalAlignment", SWT.BEGINNING); //$NON-NLS-1$
		assertHidden(dpiGridData, "verticalAlignment", GridData.CENTER); //$NON-NLS-1$

	}

	private void assertHidden(final Object instance, final String fieldName, final int expectedValue) {
		final int value = ReflectionUtils.getHidden(instance, fieldName);
		assertEquals(expectedValue, value);
	}

	private void assertHidden(final Object instance, final String fieldName, final boolean expectedValue) {
		final boolean value = ReflectionUtils.getHidden(instance, fieldName);
		assertEquals(expectedValue, value);
	}

}
