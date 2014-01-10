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
package org.eclipse.riena.example.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example for a view that has no controller. This could make sense for a
 * submodule whose only purpose is to display some static text (like this one)
 * or if the logic behind the UI widgets is really simple.
 */
public class NoControllerSubModuleView extends SubModuleView {

	public static final String ID = NoControllerSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));

		UIControlsFactory.createLabel(parent, "This sentence no verb."); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "This view no controller."); //$NON-NLS-1$
	}

}
