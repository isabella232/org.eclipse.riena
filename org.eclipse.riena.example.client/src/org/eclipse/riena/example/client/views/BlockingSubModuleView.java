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
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.example.client.controllers.BlockingSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example for blocking different parts of the user interface.
 * 
 * @see BlockingSubModuleController
 */
public class BlockingSubModuleView extends SubModuleView {

	public static final String ID = BlockingSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		// parent.setLayout(new GridLayout(1, false));
		final FillLayout layout = new FillLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		parent.setLayout(layout);

		final Group group = UIControlsFactory.createGroup(parent, "Blocking Demo:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		final GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		final Button button1 = UIControlsFactory.createButton(group, "", BlockingSubModuleController.RIDGET_BLOCK_SUB_MODULE); //$NON-NLS-1$
		fillFactory.applyTo(button1);

		final Button button2 = UIControlsFactory.createButton(group, "", BlockingSubModuleController.RIDGET_BLOCK_MODULE); //$NON-NLS-1$
		fillFactory.applyTo(button2);

		final Button button3 = UIControlsFactory.createButton(group, "", BlockingSubModuleController.RIDGET_BLOCK_SUB_APP); //$NON-NLS-1$
		fillFactory.applyTo(button3);

		final Button button4 = UIControlsFactory.createButton(group, "", BlockingSubModuleController.RIDGET_DISABLE_MODULE); //$NON-NLS-1$
		fillFactory.applyTo(button4);

		final Button button5 = UIControlsFactory.createButton(group, "", BlockingSubModuleController.RIDGET_BLOCK_APPLICATION); //$NON-NLS-1$
		fillFactory.applyTo(button5);

		final Button button6 = UIControlsFactory.createButton(group, "", BlockingSubModuleController.RIDGET_BLOCK_DIALOG); //$NON-NLS-1$
		fillFactory.applyTo(button6);

		final Label label = UIControlsFactory.createLabel(group, "", SWT.CENTER, //$NON-NLS-1$
				BlockingSubModuleController.RIDGET_STATUS);
		fillFactory.applyTo(label);
	}

}
