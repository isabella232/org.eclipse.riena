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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.StatuslineSpacer;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 *
 */
public class StatusLineViewPart extends ViewPart {

	public final static String ID = "org.eclipse.riena.navigation.ui.swt.views.statusLineView"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 *      .Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
		setPartProperty(TitlelessStackPresentation.PROPERTY_STATUSLINE, String.valueOf(Boolean.TRUE));
		final Statusline statusLine = new Statusline(parent, SWT.None, StatuslineSpacer.class);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(statusLine, "statusline"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// this view is not "focusable"
	}

}
