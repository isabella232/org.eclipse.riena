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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.swt.DatePickerComposite.DatePicker;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link DatePickerComposite}.
 */
@UITestCase
public class DatePickerCompositeTest extends TestCase {

	private Shell shell;
	private DatePickerComposite control;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		control = new DatePickerComposite(shell, SWT.NONE);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SwtUtilities.dispose(shell);
	}

	/**
	 * As per Bug 312541.
	 */
	public void testReopenAfterESC() {
		UITestHelper.fireSelectionEvent(getPickerButton());

		assertFalse(getPickerShell().isDisposed());
		assertTrue(getPicker().isVisible());

		// simulates an ESC keypress, which disposes the shell
		getPickerShell().dispose();

		assertTrue(getPickerShell().isDisposed());

		UITestHelper.fireSelectionEvent(getPickerButton());

		assertFalse(getPickerShell().isDisposed());
		assertTrue(getPicker().isVisible());
	}

	/**
	 * As per Bug 323449
	 */
	public void testDisabledWidgetHasGrayBackground() {
		assertTrue(control.isEnabled());

		final Color defaultBg = control.getBackground();
		final Color disabledBg = control.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);

		control.setEnabled(false);

		assertEquals(disabledBg, control.getBackground());
		assertEquals(disabledBg, control.getTextfield().getBackground());

		control.setEnabled(true);

		assertEquals(defaultBg, control.getBackground());
		assertEquals(defaultBg, control.getTextfield().getBackground());
	}

	/**
	 * As per Bug 323449
	 */
	public void testSetBackgroundColorWhileDisabled() {
		final Display display = control.getDisplay();
		final Color disabledBg = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		final Color red = display.getSystemColor(SWT.COLOR_RED);

		control.setEnabled(false);
		control.setBackground(red);

		assertEquals(disabledBg, control.getBackground());
		assertEquals(disabledBg, control.getTextfield().getBackground());

		control.setEnabled(true);

		assertEquals(red, control.getBackground());
		assertEquals(red, control.getTextfield().getBackground());
	}

	// helping methods
	//////////////////

	private DatePicker getPicker() {
		return ReflectionUtils.getHidden(control, "datePicker");
	}

	private Shell getPickerShell() {
		return ReflectionUtils.getHidden(getPicker(), "shell");
	}

	private Button getPickerButton() {
		return ReflectionUtils.getHidden(control, "pickerButton");
	}

}
