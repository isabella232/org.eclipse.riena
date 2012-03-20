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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class ComboAndChoiceSubModuleView extends SubModuleView {

	public static final String ID = ComboAndChoiceSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(parent);

		final GridDataFactory gdfFill = GridDataFactory.fillDefaults().grab(true, false);

		final Group grpComboBoxes = createGroupComboBoxes(parent);
		gdfFill.applyTo(grpComboBoxes);

		final Group grpSingleChoice = createGroupSingleChoice(parent);
		gdfFill.applyTo(grpSingleChoice);

		UIControlsFactory.createButton(parent, "update all ridgets from model", //$NON-NLS-1$
				"updateAllRidgetsFromModel"); //$NON-NLS-1$
	}

	// helping methods
	//////////////////

	private Group createGroupComboBoxes(final Composite parent) {
		final Group result = UIControlsFactory.createGroup(parent, "Combo boxes"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().numColumns(3).equalWidth(true).margins(20, 20).applyTo(result);

		final Combo comboBoxWithModel = UIControlsFactory.createCombo(result, "comboBoxWithModel"); //$NON-NLS-1$
		GridDataFactory.swtDefaults().hint(100, SWT.DEFAULT).applyTo(comboBoxWithModel);
		final Label label1 = UIControlsFactory.createLabel(result, "Combo with model", SWT.NONE, //$NON-NLS-1$
				"labelComboBoxWithModel"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(label1);

		final Combo comboBoxWithoutModel = UIControlsFactory.createCombo(result, "comboBoxWithoutModel"); //$NON-NLS-1$
		GridDataFactory.swtDefaults().hint(100, SWT.DEFAULT).applyTo(comboBoxWithoutModel);
		UIControlsFactory.createLabel(result, "Combo without model", //$NON-NLS-1$
				SWT.NONE, "myLabelId"); //$NON-NLS-1$
		UIControlsFactory.createButton(result, "bind to model", "bindComboToModel"); //$NON-NLS-1$ //$NON-NLS-2$

		final String msg = "click \"bind to model\" and \"update all ridgets from model\"\n" //$NON-NLS-1$
				+ "to see the values of the \"Combo without model\""; //$NON-NLS-1$
		final Label label2 = UIControlsFactory.createLabel(result, msg);
		GridDataFactory.fillDefaults().grab(true, false).span(3, 1).applyTo(label2);

		return result;
	}

	private Group createGroupSingleChoice(final Composite parent) {
		final Group result = UIControlsFactory.createGroup(parent, "Single choice"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().numColumns(1).margins(20, 20).applyTo(result);

		final Composite compositeNumberModel = new ChoiceComposite(result, SWT.NONE, false);
		addUIControl(compositeNumberModel, "compositeNumberModel"); //$NON-NLS-1$

		UIControlsFactory.createButton(result, "bind to model", "bindChoiceToModel"); //$NON-NLS-1$ //$NON-NLS-2$

		final Label lblBindChoice = UIControlsFactory.createLabel(result, "lblBindChoice"); //$NON-NLS-1$
		lblBindChoice
				.setText("click \"bind to model\" and \"update all ridgets from model\" to visualize radio buttons"); //$NON-NLS-1$

		return result;
	}
}
