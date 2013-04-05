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
package org.eclipse.riena.ui.swt.utils;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.core.test.collect.UITestCase;

/**
 * Tests of the class {@link SwtUtilities}
 */
@UITestCase
public class SwtUtilitiesTest extends TestCase {

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

}
