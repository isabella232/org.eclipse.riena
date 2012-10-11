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

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class UsersView extends SubModuleView {

	private static final int BUTTON_WIDTH = 125;

	@Override
	protected void basicCreatePartControl(final Composite parent) {

		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(parent);

		final Composite top = createTop(parent);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.BEGINNING).applyTo(top);

	}

	private Composite createTop(final Composite parent) {

		final Composite top = UIControlsFactory.createComposite(parent);

		GridLayoutFactory.fillDefaults().numColumns(3).margins(20, 20).applyTo(top);

		final Button btnCreate = UIControlsFactory.createButton(top, "Create User", "btnCreate"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.fillDefaults().hint(BUTTON_WIDTH, SWT.DEFAULT).applyTo(btnCreate);

		return top;

	}

}