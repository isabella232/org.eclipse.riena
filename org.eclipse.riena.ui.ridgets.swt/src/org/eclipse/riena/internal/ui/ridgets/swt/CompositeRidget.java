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

import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.ICompositeRidget;

/**
 * Ridget for an SWT {@link Composite}.
 */
public class CompositeRidget extends AbstractCompositeRidget implements ICompositeRidget {

	public void layout() {
		final Composite control = getUIControl();
		if (control != null) {
			control.layout(true, true);
		}
	}

	@Override
	public Composite getUIControl() {
		return (Composite) super.getUIControl();
	}

	// protected methods
	////////////////////

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, Composite.class);
	}

	@Override
	protected boolean isUIControlVisible() {
		return getUIControl().isVisible();
	}

	@Override
	protected void updateEnabled() {
		final Composite control = getUIControl();
		if (control != null) {
			control.setEnabled(isEnabled());
		}
	}

	@Override
	protected void updateToolTipText() {
		final Composite control = getUIControl();
		if (control != null) {
			control.setToolTipText(getToolTipText());
		}
	}

	@Override
	protected void updateVisible() {
		final Composite control = getUIControl();
		if (control != null) {
			control.setVisible(!isMarkedHidden());
		}
	}
}
