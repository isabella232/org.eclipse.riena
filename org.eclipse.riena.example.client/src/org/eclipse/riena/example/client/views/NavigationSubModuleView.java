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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * View of the sub-module to add module-groups, modules etc. dynamically.
 */
public class NavigationSubModuleView extends SubModuleView {

	public static final String ID = NavigationSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		final Button addSubModuleToModuleBtn = UIControlsFactory.createButton(parent, "", "addSubModuleToModuleBtn"); //$NON-NLS-1$ //$NON-NLS-2$ 
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		addSubModuleToModuleBtn.setLayoutData(gridData);

		final Button addSubModuleToSelfBtn = UIControlsFactory.createButton(parent, "", "addSubModuleToSelfBtn");//$NON-NLS-1$ //$NON-NLS-2$ 
		gridData = GridDataFactory.copyData(gridData);
		addSubModuleToSelfBtn.setLayoutData(gridData);

		final Button removeSubModuleBtn = UIControlsFactory.createButton(parent, "", "removeSubModuleBtn"); //$NON-NLS-1$ //$NON-NLS-2$ 
		gridData = GridDataFactory.copyData(gridData);
		removeSubModuleBtn.setLayoutData(gridData);

		final Button addModuleBtn = UIControlsFactory.createButton(parent, "", "addModuleBtn"); //$NON-NLS-1$ //$NON-NLS-2$ 
		gridData = GridDataFactory.copyData(gridData);
		addModuleBtn.setLayoutData(gridData);

		final Button addModuleGroupBtn = UIControlsFactory.createButton(parent, "", "addModuleGroupBtn"); //$NON-NLS-1$ //$NON-NLS-2$ 
		gridData = GridDataFactory.copyData(gridData);
		addModuleGroupBtn.setLayoutData(gridData);
	}
}
