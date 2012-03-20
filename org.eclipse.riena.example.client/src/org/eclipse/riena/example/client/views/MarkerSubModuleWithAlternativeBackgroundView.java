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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.DatePickerComposite;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example for various marker types.
 * 
 * @see IMarkableRidget
 */
public class MarkerSubModuleWithAlternativeBackgroundView extends MarkerSubModuleView {

	public static final String ID = MarkerSubModuleWithAlternativeBackgroundView.class.getName();

	@Override
	protected Group createControlsGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "UI-Controls:"); //$NON-NLS-1$
		final Color colorWhite = new Color(null, 255, 255, 255);
		group.setBackground(colorWhite);
		final int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).spacing(10, defaultVSpacing)
				.applyTo(group);

		final GridDataFactory hFillFactory = GridDataFactory.fillDefaults().grab(true, false);

		UIControlsFactory.createLabel(group, "Name:", "labeltextName").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textName = UIControlsFactory.createText(group, SWT.SINGLE, "textName"); //$NON-NLS-1$
		hFillFactory.applyTo(textName);

		UIControlsFactory.createLabel(group, "Price:", "labeltextPrice").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textPrice = UIControlsFactory.createTextDecimal(group, "textPrice"); //$NON-NLS-1$
		hFillFactory.applyTo(textPrice);

		UIControlsFactory.createLabel(group, "Amount:", "labeltextAmount").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textAmount = UIControlsFactory.createTextNumeric(group, "textAmount"); //$NON-NLS-1$
		hFillFactory.applyTo(textAmount);

		UIControlsFactory.createLabel(group, "Production Date:", "labeltextDate").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		final Composite cmpDate = createComposite(group, 3, true);
		cmpDate.setBackground(colorWhite);
		final Text textDate = UIControlsFactory.createTextDate(cmpDate, "textDate"); //$NON-NLS-1$
		textDate.setBackground(colorWhite);
		hFillFactory.applyTo(textDate);
		final DateTime dtDate = UIControlsFactory.createDate(cmpDate, SWT.DATE | SWT.DROP_DOWN | SWT.MEDIUM, "dtDate"); //$NON-NLS-1$
		dtDate.setBackground(colorWhite);
		hFillFactory.applyTo(dtDate);
		final DatePickerComposite dtPicker = UIControlsFactory.createDatePickerComposite(cmpDate, "dtPicker"); //$NON-NLS-1$
		dtPicker.setBackground(colorWhite);
		hFillFactory.applyTo(dtPicker);

		UIControlsFactory.createLabel(group, "Age (Combo):", "labelcomboAge").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		final Combo comboAge = UIControlsFactory.createCombo(group, "comboAge"); //$NON-NLS-1$
		comboAge.setBackground(colorWhite);
		hFillFactory.applyTo(comboAge);

		UIControlsFactory.createLabel(group, "Style (CompletionCombo):", "labelcomboStyle").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		final CompletionCombo comboStyle = UIControlsFactory.createCompletionCombo(group, "comboStyle"); //$NON-NLS-1$
		comboStyle.setBackground(colorWhite);
		hFillFactory.applyTo(comboStyle);

		UIControlsFactory.createLabel(group, "Size (CCombo):", "labelccomboSize").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		final CCombo ccomboSize = UIControlsFactory.createCCombo(group, "ccomboSize"); //$NON-NLS-1$
		ccomboSize.setBackground(colorWhite);
		hFillFactory.applyTo(ccomboSize);

		UIControlsFactory.createLabel(group, "Type:", "labelchoiceType").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		final ChoiceComposite choiceType = UIControlsFactory.createChoiceComposite(group, SWT.BORDER, false,
				"choiceType"); //$NON-NLS-1$
		choiceType.setBackground(colorWhite);
		choiceType.setOrientation(SWT.HORIZONTAL);
		hFillFactory.applyTo(choiceType);

		UIControlsFactory.createLabel(group, "Flavor:", "labelchoiceFlavor").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		final ChoiceComposite choiceFlavor = UIControlsFactory.createChoiceComposite(group, SWT.BORDER, true,
				"choiceFlavor"); //$NON-NLS-1$
		choiceFlavor.setBackground(colorWhite);
		choiceFlavor.setOrientation(SWT.HORIZONTAL);
		hFillFactory.applyTo(choiceFlavor);

		final Label lblReviewed = UIControlsFactory.createLabel(group, "Reviewed by:", "labellistPersons"); //$NON-NLS-1$ //$NON-NLS-2$
		lblReviewed.setBackground(colorWhite);
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(lblReviewed);
		final Composite cmpReviewed = createComposite(group, 2, true);
		cmpReviewed.setBackground(colorWhite);

		final List listPersons = UIControlsFactory.createList(cmpReviewed, false, true, "listPersons"); //$NON-NLS-1$
		final int hHint = UIControlsFactory.getHeightHint(listPersons, 5);
		listPersons.setBackground(colorWhite);
		hFillFactory.hint(150, hHint).applyTo(listPersons);

		final Table tablePersons = UIControlsFactory.createTable(cmpReviewed, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION, "tablePersons"); //$NON-NLS-1$
		tablePersons.setBackground(colorWhite);
		tablePersons.setLinesVisible(true);
		final TableColumn tac1 = new TableColumn(tablePersons, SWT.NONE);
		tac1.setWidth(100);
		final TableColumn tac2 = new TableColumn(tablePersons, SWT.NONE);
		tac2.setWidth(70);
		hFillFactory.hint(170, hHint).applyTo(tablePersons);

		UIControlsFactory.createLabel(group, ""); //$NON-NLS-1$
		final Composite cmpReviewed2 = createComposite(group, 2, true);
		cmpReviewed2.setBackground(colorWhite);

		final Tree treePersons = UIControlsFactory.createTree(cmpReviewed2, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION, "treePersons"); //$NON-NLS-1$
		treePersons.setBackground(colorWhite);
		hFillFactory.hint(150, hHint).applyTo(treePersons);

		final Tree treeWCols = UIControlsFactory.createTree(cmpReviewed2, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI, "treeWCols"); //$NON-NLS-1$
		treeWCols.setBackground(colorWhite);
		treeWCols.setLinesVisible(true);
		treeWCols.setHeaderVisible(true);
		final TreeColumn trc1 = new TreeColumn(treeWCols, SWT.NONE);
		trc1.setWidth(120);
		final TreeColumn trc2 = new TreeColumn(treeWCols, SWT.NONE);
		trc2.setWidth(40);
		hFillFactory.hint(200, hHint).applyTo(treeWCols);

		UIControlsFactory.createLabel(group, "Toggle Buttons:", "labelbuttonToggle").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		final Composite cmpButtons = createComposite(group, 6, false);
		cmpButtons.setBackground(colorWhite);
		UIControlsFactory.createButtonToggle(cmpButtons, "ToggleA", "buttonToggleA").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonToggle(cmpButtons, "ToggleB", "buttonToggleB").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonRadio(cmpButtons, "RadioA", "buttonRadioA").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonRadio(cmpButtons, "RadioB", "buttonRadioB").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(cmpButtons, "CheckA", "buttonCheckA").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(cmpButtons, "CheckB", "buttonCheckB").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$

		UIControlsFactory.createLabel(group, "Push Buttons:", "labelbuttonPush").setBackground(colorWhite); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButton(group, "Push", "buttonPush"); //$NON-NLS-1$ //$NON-NLS-2$

		return group;
	}
}
