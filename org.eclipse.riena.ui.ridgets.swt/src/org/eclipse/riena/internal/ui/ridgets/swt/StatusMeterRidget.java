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

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.ridgets.IStatusMeterRidget;
import org.eclipse.riena.ui.swt.StatusMeterWidget;

/**
 * Ridget for {@link StatusMeterWidget} widget.
 */
public class StatusMeterRidget extends AbstractTraverseRidget implements IStatusMeterRidget {

	private static final String COLOR_WARNING = "Parameter color is not an instance of 'org.eclipse.swt.graphics.Color'"; //$NON-NLS-1$
	private boolean initialized;
	private boolean maxInitialized;
	private boolean minInitialized;
	private Color borderColor;
	private Color backgroundColor;
	private Color gradientStartColor;
	private Color gradientEndColor;

	@Override
	public StatusMeterWidget getUIControl() {
		return (StatusMeterWidget) super.getUIControl();
	}

	@Override
	public void checkUIControl(final Object uiControl) {
		checkType(uiControl, StatusMeterWidget.class);
	}

	@Override
	protected int getValue(final Control control) {
		return getUIControl() != null ? getUIControl().getValue() : 0;
	}

	@Override
	protected void initFromUIControl() {
		final StatusMeterWidget meter = getUIControl();
		if (meter != null && !initialized) {
			if (!maxInitialized) {
				setMaximum(meter.getMaximum());
			}
			if (!minInitialized) {
				setMinimum(meter.getMinimum());
			}
			setValue(meter.getValue());
			initialized = true;
		}
	}

	@Override
	protected void updateUIMaximum() {
		final StatusMeterWidget control = getUIControl();
		if (control != null) {
			control.setMaximum(getMaximum());
		}

	}

	@Override
	protected void updateUIMinimum() {
		final StatusMeterWidget control = getUIControl();
		if (control != null) {
			control.setMinimum(getMinimum());
		}

	}

	@Override
	protected void updateUIValue() {
		final StatusMeterWidget control = getUIControl();
		if (control != null) {
			control.setValue(getValue());
		}

	}

	@Override
	public void setMaximum(final int maximum) {
		super.setMaximum(maximum);
		maxInitialized = true;
	}

	@Override
	public void setMinimum(final int minimum) {
		super.setMinimum(minimum);
		minInitialized = true;
	}

	@Override
	protected void updateUIControl() {
		super.updateUIControl();
		final StatusMeterWidget control = getUIControl();

		// set the custom colors, when the uiControl is available
		if (control == null) {
			return;
		}

		if (borderColor != null) {
			control.setBorderColor(borderColor);
		}

		if (backgroundColor != null) {
			control.setBackgroundColor(backgroundColor);
		}

		if (gradientStartColor != null) {
			control.setGradientStartColor(gradientStartColor);
		}

		if (gradientEndColor != null) {
			control.setGradientEndColor(gradientEndColor);
		}
	}

	public void setBorderColor(final Object color) {
		Assert.isTrue(color instanceof Color, COLOR_WARNING);
		borderColor = (Color) color;
		updateUIControl();
	}

	public void setBackgroundColor(final Object color) {
		Assert.isTrue(color instanceof Color, COLOR_WARNING);
		backgroundColor = (Color) color;
		updateUIControl();
	}

	public void setGradientStartColor(final Object color) {
		Assert.isTrue(color instanceof Color, COLOR_WARNING);
		gradientStartColor = (Color) color;
		updateUIControl();
	}

	public void setGradientEndColor(final Object color) {
		Assert.isTrue(color instanceof Color, COLOR_WARNING);
		gradientEndColor = (Color) color;
		updateUIControl();
	}

	@Override
	protected void addSelectionListener(final Control control, final SelectionListener listener) {
		// not needed here
	}

	@Override
	protected void removeSelectionListener(final Control control, final SelectionListener listener) {
		// not needed here
	}

	@Override
	protected int getUIControlIncrement() {
		// not needed here
		return 0;
	}

	@Override
	protected int getUIControlMaximum() {
		// not needed here
		return 0;
	}

	@Override
	protected int getUIControlMinimum() {
		// not needed here
		return 0;
	}

	@Override
	protected int getUIControlPageIncrement() {
		// not needed here
		return 0;
	}

	@Override
	protected int getUIControlSelection() {
		// not needed here
		return 0;
	}

	@Override
	protected void initAdditionalsFromUIControl() {
		// not needed here
	}

	@Override
	protected void updateUIIncrement() {
		// not needed here
	}

	@Override
	protected void updateUIPageIncrement() {
		// not needed here
	}
}
