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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link IComboRidget} sample.
 */
public class ComboSubModuleView extends SubModuleView {

	public static final String ID = ComboSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		GridLayoutFactory.swtDefaults().margins(20, 20).applyTo(parent);

		final GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		UIControlsFactory.createLabel(parent, "&Persons:"); //$NON-NLS-1$

		final Combo combo = UIControlsFactory.createCombo(parent);
		fillFactory.applyTo(combo);
		addUIControl(combo, "comboOne"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "&First Name:"); //$NON-NLS-1$
		final Text textFirst = UIControlsFactory.createText(parent);
		fillFactory.applyTo(textFirst);
		addUIControl(textFirst, "textFirst"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "&Last Name:"); //$NON-NLS-1$
		final Text textLast = UIControlsFactory.createText(parent);
		fillFactory.applyTo(textLast);
		addUIControl(textLast, "textLast"); //$NON-NLS-1$

		final Button buttonSave = UIControlsFactory.createButton(parent);
		fillFactory.applyTo(buttonSave);
		addUIControl(buttonSave, "buttonSave"); //$NON-NLS-1$

		final Button buttonSecondValue = UIControlsFactory.createButtonCheck(parent);
		fillFactory.applyTo(buttonSecondValue);
		addUIControl(buttonSecondValue, "buttonSecondValue"); //$NON-NLS-1$
	}

}
