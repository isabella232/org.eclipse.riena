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

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link Statusline}.
 */
@UITestCase
public class StatuslineTest extends TestCase {

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
	 * Tests the method {@code addUIControl(Widget,String)}.
	 */
	public void testAddUIControl() {

		final Label label = new Label(shell, SWT.NONE);

		ReflectionUtils.invokeHidden(statusline, "addUIControl", label, "labelName");
		final List<Object> controls = statusline.getUIControls();
		assertNotNull(controls);
		assertFalse(controls.isEmpty());
		final Label labelOfList = (Label) controls.get(controls.size() - 1);
		assertSame(label, labelOfList);
		assertEquals("labelName", labelOfList.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));

		SwtUtilities.dispose(label);

	}

	/**
	 * Tests the method {@code createContents()}.<br>
	 * <i>The method is already called by the constructor.</i>
	 */
	public void testCreateContents() {

		final Control[] controls = statusline.getChildren();
		boolean time = false;
		boolean date = false;
		boolean number = false;
		for (final Control control : controls) {
			if (control.getClass() == StatuslineTime.class) {
				if (time) {
					fail("More than of time control!");
				}
				time = true;
			}
			if (control.getClass() == StatuslineDate.class) {
				if (date) {
					fail("More than of date control!");
				}
				date = true;
			}
			if (control.getClass() == StatuslineNumber.class) {
				if (number) {
					fail("More than of number control!");
				}
				number = true;
			}
		}
		assertTrue(time);
		assertTrue(date);
		assertTrue(number);
		assertNotNull(statusline.getMessageComposite());

		number = false;
		final List<Object> uicontrols = statusline.getUIControls();
		for (final Object uiControl : uicontrols) {
			if (uiControl.getClass() == StatuslineNumber.class) {
				if (number) {
					fail("More than of number UI-control!");
				}
				number = true;
				final StatuslineNumber statuslineNumber = (StatuslineNumber) uiControl;
				assertEquals("statuslineNumberRidget",
						statuslineNumber.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));
			}
		}
		assertTrue(number);

	}

}
