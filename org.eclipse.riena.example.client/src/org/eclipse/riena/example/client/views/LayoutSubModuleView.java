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

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.example.client.controllers.LayoutSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Shows how to use the {@link #layout()} method to recalculate the layout when
 * the text changes. See {@link LayoutSubModuleController} for details.
 */
public class LayoutSubModuleView extends SubModuleView {

	public static final String ID = LayoutSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).spacing(20, 5).applyTo(parent);
		addUIControl(parent, "parent"); //$NON-NLS-1$

		createDetails(parent);
		createButtonBar(parent);
	}

	private void createDetails(final Composite details) {
		UIControlsFactory.createLabel(details, "First Name:", "lblFirst"); //$NON-NLS-1$ //$NON-NLS-2$
		final Text txtFirst = UIControlsFactory.createText(details, SWT.BORDER, "txtFirst"); //$NON-NLS-1$
		txtFirst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		UIControlsFactory.createLabel(details, "Last Name:", "lblLast"); //$NON-NLS-1$ //$NON-NLS-2$
		final Text txtLast = UIControlsFactory.createText(details, SWT.BORDER, "txtLast"); //$NON-NLS-1$
		txtLast.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		UIControlsFactory.createLabel(details, "Gender:", "lblGender"); //$NON-NLS-1$ //$NON-NLS-2$
		final ChoiceComposite ccGender = new ChoiceComposite(details, SWT.NONE, false);
		ccGender.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		ccGender.setOrientation(SWT.HORIZONTAL);
		addUIControl(ccGender, "ccGender"); //$NON-NLS-1$

		UIControlsFactory.createLabel(details, "Pets:", "lblPets"); //$NON-NLS-1$ //$NON-NLS-2$
		final ChoiceComposite ccPets = new ChoiceComposite(details, SWT.NONE, true);
		ccPets.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		ccPets.setOrientation(SWT.HORIZONTAL);
		addUIControl(ccPets, "ccPets"); //$NON-NLS-1$
	}

	private void createButtonBar(final Composite parent) {
		UIControlsFactory.createButton(parent, "More Text", "btnMore"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButton(parent, "Less Text", "btnLess"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
