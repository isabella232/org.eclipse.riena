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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.riena.ui.swt.separator.Separator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class UserView extends SubModuleView {

	private static final int TEXT_FIELD_WIDTH = 200;
	private static final int BUTTON_WIDTH = 75;

	@Override
	protected void basicCreatePartControl(final Composite parent) {

		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(parent);

		final Composite top = createTop(parent);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(top);

		final Composite center = UIControlsFactory.createComposite(parent);
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(center);

		final Composite bottom = createBottom(parent);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.END).applyTo(bottom);

		final MessageBox messageBox = UIControlsFactory.createMessageBox(parent);
		addUIControl(messageBox, "messageBox"); //$NON-NLS-1$

	}

	private Composite createTop(final Composite parent) {

		final Composite top = UIControlsFactory.createComposite(parent);

		GridLayoutFactory.fillDefaults().numColumns(3).margins(20, 20).applyTo(top);

		UIControlsFactory.createLabel(top, "Firstname:"); //$NON-NLS-1$
		final Text txtFirst = UIControlsFactory.createText(top, SWT.SINGLE, "txtFirst"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.FILL).hint(TEXT_FIELD_WIDTH, SWT.DEFAULT).span(2, 0).applyTo(txtFirst);

		UIControlsFactory.createLabel(top, "Lastname:"); //$NON-NLS-1$
		final Text txtLast = UIControlsFactory.createText(top, SWT.SINGLE, "txtLast"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.FILL).hint(TEXT_FIELD_WIDTH, SWT.DEFAULT).span(2, 0).applyTo(txtLast);

		UIControlsFactory.createLabel(top, "Username:"); //$NON-NLS-1$
		final Text txtUser = UIControlsFactory.createText(top, SWT.SINGLE, "txtUser"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.FILL).hint(TEXT_FIELD_WIDTH, SWT.DEFAULT).span(2, 0).applyTo(txtUser);

		UIControlsFactory.createLabel(top, "Password:"); //$NON-NLS-1$
		final Text txtPassword = UIControlsFactory.createText(top, SWT.SINGLE | SWT.PASSWORD, "txtPassword"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.FILL).hint(TEXT_FIELD_WIDTH, SWT.DEFAULT).applyTo(txtPassword);

		final Label lblLights = UIControlsFactory.createLabel(top, "", "lblLights"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.fillDefaults().applyTo(lblLights);

		UIControlsFactory.createLabel(top, "Comfirm:"); //$NON-NLS-1$
		final Text txtConfirm = UIControlsFactory.createText(top, SWT.SINGLE | SWT.PASSWORD, "txtConfirm"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.FILL).hint(TEXT_FIELD_WIDTH, SWT.DEFAULT).span(2, 0).applyTo(txtConfirm);

		return top;

	}

	private Composite createBottom(final Composite parent) {

		final Composite bottom = UIControlsFactory.createComposite(parent);

		GridLayoutFactory.fillDefaults().numColumns(4).extendedMargins(0, 0, 0, 10).applyTo(bottom);

		final Separator separator = UIControlsFactory.createSeparator(bottom, SWT.HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, false).hint(TEXT_FIELD_WIDTH, SWT.DEFAULT).span(4, 0).applyTo(separator);

		final Button btnSave = UIControlsFactory.createButton(bottom, "Save", "btnSave"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.fillDefaults().hint(BUTTON_WIDTH, SWT.DEFAULT).indent(10, 0).applyTo(btnSave);

		final Button btnEdit = UIControlsFactory.createButton(bottom, "Edit", "btnEdit"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.fillDefaults().hint(BUTTON_WIDTH, SWT.DEFAULT).indent(10, 0).applyTo(btnEdit);

		final Button btnDelete = UIControlsFactory.createButton(bottom, "Delete", "btnDelete"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.fillDefaults().hint(BUTTON_WIDTH, SWT.DEFAULT).indent(10, 0).applyTo(btnDelete);

		return bottom;

	}
}