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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.example.client.controllers.DemoTargetSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

public class DemoTargetSubModuleView extends SubModuleView {

	public static final String ID = DemoTargetSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));
		final GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		UIControlsFactory.createLabel(parent, "Want to go home?"); //$NON-NLS-1$

		final Button jumpBack = UIControlsFactory.createButton(parent);
		jumpBack.setText("Jump Back"); //$NON-NLS-1$
		fillFactory.applyTo(jumpBack);
		addUIControl(jumpBack, DemoTargetSubModuleController.JUMP_BACK_RIDGET_ID);
	}

}
