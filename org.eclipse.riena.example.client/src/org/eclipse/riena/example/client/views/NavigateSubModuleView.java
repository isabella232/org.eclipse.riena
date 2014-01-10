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

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * View of a sub module to demonstrate the navigate method of
 * {@code INavigationNode}.
 */
public class NavigateSubModuleView extends SubModuleView {

	public static final String ID = NavigateSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));
		final GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		UIControlsFactory.createLabel(parent, "Where do you want to go today?"); //$NON-NLS-1$

		final Button comboAndList = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(comboAndList);
		addUIControl(comboAndList, "comboAndList"); //$NON-NLS-1$

		final Button tableTextAndTree = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(tableTextAndTree);
		addUIControl(tableTextAndTree, "tableTextAndTree"); //$NON-NLS-1$

		final Button textAssembly = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(textAssembly);
		addUIControl(textAssembly, "textAssembly"); //$NON-NLS-1$

		final Button btnNavigateToRidget = UIControlsFactory.createButton(parent, "Navigate to First Name", //$NON-NLS-1$
				"btnNavigateToRidget"); //$NON-NLS-1$
		fillFactory.applyTo(btnNavigateToRidget);

		final Button openAsFirstModule = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(openAsFirstModule);
		addUIControl(openAsFirstModule, "openAsFirstModule"); //$NON-NLS-1$

		final Button openAsFirstSubModule = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(openAsFirstSubModule);
		addUIControl(openAsFirstSubModule, "openAsFirstSubModule"); //$NON-NLS-1$

		final Button openAsThirdSubModule = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(openAsThirdSubModule);
		addUIControl(openAsThirdSubModule, "openAsThirdSubModule"); //$NON-NLS-1$

		final Button openAsOrdinal10 = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(openAsOrdinal10);
		addUIControl(openAsOrdinal10, "openAsOrdinal10"); //$NON-NLS-1$

		final Button openAsOrdinal5 = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(openAsOrdinal5);
		addUIControl(openAsOrdinal5, "openAsOrdinal5"); //$NON-NLS-1$

		final Button moveModule = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(moveModule);
		addUIControl(moveModule, "moveModule"); //$NON-NLS-1$

		final Button moveInActiveModule = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(moveInActiveModule);
		addUIControl(moveInActiveModule, "moveInActiveModule"); //$NON-NLS-1$

		final Button addToModule = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(addToModule);
		addUIControl(addToModule, "addToModule"); //$NON-NLS-1$

		final Button jumpToTargetModule = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(jumpToTargetModule);
		addUIControl(jumpToTargetModule, "jumpToTargetModule"); //$NON-NLS-1$

		final Button jumpToTargetSubModule = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(jumpToTargetSubModule);
		addUIControl(jumpToTargetSubModule, "jumpToTargetSubModule"); //$NON-NLS-1$

		final Button validation = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(validation);
		addUIControl(validation, "validation"); //$NON-NLS-1$
	}

}
