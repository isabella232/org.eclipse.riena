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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests of the class {@link ComboRidget}.
 */
public class ComboRidgetTest extends AbstractComboRidgetTest {

	@Override
	protected Control createWidget(final Composite parent) {
		return new Combo(parent, SWT.READ_ONLY);
	}

	@Override
	protected Control createWidget(final Composite parent, final int style) {
		return new Combo(parent, style);
	}

	@Override
	protected IRidget createRidget() {
		return new ComboRidget();
	}

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertTrue(getWidget() instanceof Combo);
		assertSame(ComboRidget.class, mapper.getRidgetClass(getWidget()));
	}

	// testing methods
	//////////////////

	public void testRequireReadOnlyUIControl() {
		try {
			// not SWT.READ_ONLY => exception 
			getRidget().setUIControl(createWidget(getShell(), SWT.NONE));
			fail();
		} catch (final BindingException bex) {
			ok();
		}
	}

}
