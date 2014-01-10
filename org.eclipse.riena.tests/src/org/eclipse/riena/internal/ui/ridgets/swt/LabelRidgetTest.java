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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.ui.ridgets.ILabelRidget;

/**
 * Tests for the {@link LabelRidget}.
 */
public class LabelRidgetTest extends AbstractLabelRidgetTest {

	@Override
	protected Label createWidget(final Composite parent) {
		return new Label(parent, SWT.NONE);
	}

	@Override
	protected ILabelRidget createRidget() {
		return new LabelRidget();
	}

	@Override
	protected Label getWidget() {
		return (Label) super.getWidget();
	}

	@Override
	protected Image getImage(final Object widget) {
		return ((Label) widget).getImage();
	}

	@Override
	protected String getText(final Object widget) {
		return ((Label) widget).getText();
	}

	@Override
	protected void setImage(final Object widget, final Image image) {
		((Label) widget).setImage(image);
	}

	@Override
	protected void setText(final Object widget, final String text) {
		((Label) widget).setText(text);
	}

	@Override
	protected Class<? extends ILabelRidget> getRidgetClass() {
		return LabelRidget.class;
	}
}
