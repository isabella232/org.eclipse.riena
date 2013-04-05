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
package org.eclipse.riena.ui.swt;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link NumberLabel}.
 */
@UITestCase
public class NumberLabelTest extends TestCase {

	private Shell shell;
	private Statusline statusline;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		statusline = new Statusline(shell, SWT.NONE, StatuslineSpacer.class);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
		SwtUtilities.dispose(statusline);
	}

	/**
	 * Tests the method {@code computeSize(int, int, boolean)}.
	 */
	public void testComputeSize() {

		final NumberLabel label = new NumberLabel(statusline, SWT.NONE);

		Point size = label.computeSize(12, 13, false);
		assertEquals(12, size.x);
		assertEquals(13, size.y);

		label.setFixWidth(4711);
		size = label.computeSize(20, 30, false);
		assertEquals(4711, size.x);
		assertEquals(30, size.y);

	}

}
