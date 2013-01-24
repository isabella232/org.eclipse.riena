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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.example.client.controllers.DialogSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT sample view for a Dialog.
 */
public class DialogSubModuleView extends SubModuleView {

	public static final String ID = DialogSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {

		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));
		final GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		final Button openDialog = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(openDialog);
		addUIControl(openDialog, DialogSubModuleController.RIDGET_ID_OPEN_DIALOG);
	}
}
