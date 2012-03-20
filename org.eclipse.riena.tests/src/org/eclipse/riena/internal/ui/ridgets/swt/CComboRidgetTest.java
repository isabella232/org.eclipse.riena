/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractComboRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests of the class {@link CComboRidget}.
 */
public class CComboRidgetTest extends AbstractComboRidgetTest {

	@Override
	protected Control createWidget(final Composite parent) {
		return new CCombo(parent, SWT.READ_ONLY);
	}

	@Override
	protected Control createWidget(final Composite parent, final int style) {
		return new CCombo(parent, style);
	}

	@Override
	protected IRidget createRidget() {
		return new CComboRidget();
	}

	// testing methods
	//////////////////

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertTrue(getWidget() instanceof CCombo);
		assertSame(CComboRidget.class, mapper.getRidgetClass(getWidget()));
	}

	/**
	 * As per Bug 323449
	 */
	public void testDisabledWidgetHasGrayBackground() {
		final AbstractComboRidget ridget = getRidget();
		final CCombo control = (CCombo) getWidget();
		final Display display = control.getDisplay();
		final Color bgColor = display.getSystemColor(SWT.COLOR_WHITE);
		final Color disabledBg = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);

		assertTrue(ridget.isEnabled());

		control.setBackground(bgColor);
		ridget.setEnabled(false);

		assertEquals(disabledBg, control.getBackground());
		assertEquals(disabledBg, getText(control).getBackground());

		ridget.setEnabled(true);

		assertEquals(bgColor, control.getBackground());
		assertEquals(bgColor, getText(control).getBackground());
	}

	public void testRequireReadOnlyUIControl() {
		try {
			// not SWT.READ_ONLY => exception 
			getRidget().setUIControl(createWidget(getShell(), SWT.NONE));
			fail();
		} catch (final BindingException bex) {
			ok();
		}
	}

	// helping methods
	//////////////////

	private Text getText(final CCombo control) {
		return (Text) ReflectionUtils.getHidden(control, "text");
	}

}
