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
package org.eclipse.riena.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.internal.ui.ridgets.swt.AbstractRidgetTestCase;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.swt.ImageButton;

/**
 * Tests of the class {@link ImageButtonRidget}.
 */
public class ImageButtonRidgetTest extends AbstractRidgetTestCase {

	@Override
	protected IActionRidget createRidget() {
		return new ImageButtonRidget();
	}

	@Override
	protected ImageButton createWidget(final Composite parent) {
		return new ImageButton(parent, SWT.NONE);
	}

	@Override
	protected IActionRidget getRidget() {
		return (IActionRidget) super.getRidget();
	}

	@Override
	protected ImageButton getWidget() {
		return (ImageButton) super.getWidget();
	}

	/**
	 * Tests the method {@code updateUIIcon()} <i>indirectly</i> by calling the
	 * method {@code setIcon(String)}.
	 */
	public void testUpdateUIIcon() {

		getRidget().setIcon("doesNotExists");
		assertNull(getWidget().getDisabledImage());
		assertNull(getWidget().getPressedImage());
		assertNull(getWidget().getHoverImage());
		assertNull(getWidget().getFocusedImage());
		assertNull(getWidget().getHoverFocusedImage());

		getRidget().setIcon("imagebutton");
		assertNull(getWidget().getDisabledImage());
		assertNotNull(getWidget().getPressedImage());
		assertNotNull(getWidget().getHoverImage());
		assertNull(getWidget().getFocusedImage());
		assertNull(getWidget().getHoverFocusedImage());

	}

}
