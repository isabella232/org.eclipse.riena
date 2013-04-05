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
package org.eclipse.riena.example.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.ui.swt.lnf.IgnoreLnFUpdater;

/**
 * Creates the content of a view {@code SalaryIgnoreView}.
 * <p>
 * No properties of this (and only this) composite should be updated.
 */
@IgnoreLnFUpdater("*")
public class SalaryIgnoreComposite extends Composite {

	public SalaryIgnoreComposite(final Composite parent, final int style) {
		super(parent, style);
		build();
	}

	private void build() {
		setLayout(new RowLayout(SWT.VERTICAL));
		final Label salaryLbl = new Label(this, SWT.None);
		salaryLbl.setText("Salary"); //$NON-NLS-1$
		final Label ignoreLbl = new Label(this, SWT.None);
		ignoreLbl.setText("(@IgnoreLnFUpdater(\"*\"))"); //$NON-NLS-1$
	}

}
