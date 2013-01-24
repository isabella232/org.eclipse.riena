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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineNumberRidget;
import org.eclipse.riena.ui.swt.StatuslineNumber;

/**
 *
 */
public class StatuslineNumberRidgetTest extends AbstractSWTRidgetTest {

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#createRidget()
	 */
	@Override
	protected IRidget createRidget() {
		return new StatuslineNumberRidget();
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#createWidget(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createWidget(final Composite parent) {
		return new StatuslineNumber(parent, SWT.NONE);
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#getRidget()
	 */
	@Override
	protected IStatuslineNumberRidget getRidget() {
		return (IStatuslineNumberRidget) super.getRidget();
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#getWidget()
	 */
	@Override
	protected StatuslineNumber getWidget() {
		return (StatuslineNumber) super.getWidget();
	}

	/**
	 * Returns the label of {@code StatuslineNumber}.
	 * 
	 * @return label
	 */
	private CLabel getLabel() {
		final StatuslineNumber statuslineNumber = getWidget();
		final Control[] controls = statuslineNumber.getChildren();
		return (CLabel) controls[0];
	}

	/**
	 * Tests the method {@code setNumber(Integer)}.
	 */
	public void testSetNumber() {

		getRidget().setNumber(null);
		assertEquals("", getLabel().getText());

		getRidget().setNumber(Integer.valueOf((4711)));
		assertEquals("0004711", getLabel().getText());
		assertEquals(4711, (int) getRidget().getNumber());

	}

	/**
	 * Tests the method {@code setNumberString(String)}.
	 */
	public void testSetNumberString() {

		getRidget().setNumberString(null);
		assertEquals("", getLabel().getText());
		assertNull(getRidget().getNumber());

		getRidget().setNumberString("4711");
		assertEquals("4711", getLabel().getText());
		assertEquals(4711, (int) getRidget().getNumber());

		getRidget().setNumberString("0815-12");
		assertEquals("0815-12", getLabel().getText());
		assertEquals(0, (int) getRidget().getNumber());

	}

	/**
	 * Tests that markers that are irrelavant for this type of Ridget do not
	 * change the widget.
	 */
	public void testUnsupportedMarkersIgnored() {
		assertMarkerIgnored(new ErrorMarker());
		assertMarkerIgnored(new MandatoryMarker());
		assertMarkerIgnored(new OutputMarker());
		assertMarkerIgnored(new NegativeMarker());
	}

}
