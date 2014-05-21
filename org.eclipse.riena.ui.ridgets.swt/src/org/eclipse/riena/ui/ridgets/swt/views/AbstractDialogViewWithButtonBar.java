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
package org.eclipse.riena.ui.ridgets.swt.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.swt.layout.DpiGridLayoutFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * A specialized version of AbstractDialogView, e.g. adding default OK and CANCEL buttons.
 * 
 * @since 3.0
 */
public abstract class AbstractDialogViewWithButtonBar extends AbstractDialogView {

	private Button okButton;
	private Button cancelButton;

	protected AbstractDialogViewWithButtonBar(final Shell parentShell) {
		this(parentShell, false);
	}

	/**
	 * @since 5.0
	 */
	protected AbstractDialogViewWithButtonBar(final Shell parentShell, final boolean statusline) {
		super(parentShell, statusline);
	}

	@Override
	protected void createOkCancelButtons(final Composite parent) {
		final Composite buttonComposite = UIControlsFactory.createComposite(parent);
		DpiGridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).applyTo(buttonComposite);
		GridDataFactory.fillDefaults().span(2, 1).grab(true, false).applyTo(buttonComposite);

		fillButtonBar(buttonComposite);
	}

	protected void fillButtonBar(final Composite parent) {
		okButton = UIControlsFactory.createButton(parent, "&Ok", AbstractWindowController.RIDGET_ID_OK); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.END, SWT.END).applyTo(okButton);

		cancelButton = UIControlsFactory.createButton(parent, "&Cancel", AbstractWindowController.RIDGET_ID_CANCEL); //$NON-NLS-1$

		setDefaultButton(cancelButton);
	}

	protected Button getButtonBarOkButton() {
		return okButton;
	}

	protected Button getButtonBarCancelButton() {
		return cancelButton;
	}
}
