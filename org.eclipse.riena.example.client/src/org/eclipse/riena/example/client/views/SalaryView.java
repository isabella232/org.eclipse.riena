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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

public class SalaryView extends SubModuleView {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.ui.swt.views.SubModuleView#
	 * basicCreatePartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new RowLayout(SWT.HORIZONTAL));
		final Label lbl = new Label(parent, SWT.None);
		lbl.setText("Salary"); //$NON-NLS-1$
	}
}
