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
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
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

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
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
public class MarkerSubModuleView extends SubModuleView {

	public static final String ID = MarkerSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		final Group group1 = createMarkerOptionsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group1);
		final Group group2 = createVisibilityOptionsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group2);
		final Group group3 = createControlsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(group3);
	}

	// helping methods
	// ////////////////

	private Group createMarkerOptionsGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "Marker Options:"); //$NON-NLS-1$
		group.setLayout(createGridLayout(4));

		UIControlsFactory.createButtonCheck(group, "&mandatory", "checkMandatory");//$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "&error", "checkError"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "&disabled", "checkDisabled"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "&output", "checkOutput"); //$NON-NLS-1$ //$NON-NLS-2$

		return group;
	}

	private Group createVisibilityOptionsGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "Visibility Options:"); //$NON-NLS-1$
		group.setLayout(createGridLayout(2));

		UIControlsFactory.createButtonCheck(group, "&hidden", "checkHidden"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(group, "hidden &parent", "checkHiddenParent"); //$NON-NLS-1$ //$NON-NLS-2$

		return group;
	}

	protected Group createControlsGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "UI-Controls:"); //$NON-NLS-1$
		final int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).spacing(10, defaultVSpacing).applyTo(group);

		final GridDataFactory hFillFactory = GridDataFactory.fillDefaults().grab(true, false);

		UIControlsFactory.createLabel(group, "Name:", "labeltextName"); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textName = UIControlsFactory.createText(group, SWT.SINGLE, "textName"); //$NON-NLS-1$
		hFillFactory.applyTo(textName);

		UIControlsFactory.createLabel(group, "Price:", "labeltextPrice"); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textPrice = UIControlsFactory.createTextDecimal(group, "textPrice"); //$NON-NLS-1$
		hFillFactory.applyTo(textPrice);

		UIControlsFactory.createLabel(group, "Amount:", "labeltextAmount"); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textAmount = UIControlsFactory.createTextNumeric(group, "textAmount"); //$NON-NLS-1$
		hFillFactory.applyTo(textAmount);

		UIControlsFactory.createLabel(group, "Production Date:", "labeltextDate"); //$NON-NLS-1$ //$NON-NLS-2$
		final Composite cmpDate = createComposite(group, 3, true);
		final Text textDate = UIControlsFactory.createTextDate(cmpDate, "textDate"); //$NON-NLS-1$
		hFillFactory.applyTo(textDate);
		final DateTime dtDate = UIControlsFactory.createDate(cmpDate, SWT.DATE | SWT.DROP_DOWN | SWT.MEDIUM, "dtDate"); //$NON-NLS-1$
		hFillFactory.applyTo(dtDate);
		final DatePickerComposite dtPicker = UIControlsFactory.createDatePickerComposite(cmpDate, "dtPicker"); //$NON-NLS-1$
		hFillFactory.applyTo(dtPicker);

		UIControlsFactory.createLabel(group, "Age (Combo):", "labelcomboAge"); //$NON-NLS-1$ //$NON-NLS-2$
		final Combo comboAge = UIControlsFactory.createCombo(group, "comboAge"); //$NON-NLS-1$
		hFillFactory.applyTo(comboAge);

		UIControlsFactory.createLabel(group, "Style (CompletionCombo):", "labelcomboStyle"); //$NON-NLS-1$ //$NON-NLS-2$
		final CompletionCombo comboStyle = UIControlsFactory.createCompletionCombo(group, "comboStyle"); //$NON-NLS-1$
		hFillFactory.applyTo(comboStyle);

		UIControlsFactory.createLabel(group, "Size (CCombo):", "labelccomboSize"); //$NON-NLS-1$ //$NON-NLS-2$
		final CCombo ccomboSize = UIControlsFactory.createCCombo(group, "ccomboSize"); //$NON-NLS-1$
		hFillFactory.applyTo(ccomboSize);

		UIControlsFactory.createLabel(group, "Type:", "labelchoiceType"); //$NON-NLS-1$ //$NON-NLS-2$
		final ChoiceComposite choiceType = UIControlsFactory.createChoiceComposite(group, SWT.BORDER, false, "choiceType"); //$NON-NLS-1$
		choiceType.setOrientation(SWT.HORIZONTAL);
		hFillFactory.applyTo(choiceType);

		UIControlsFactory.createLabel(group, "Flavor:", "labelchoiceFlavor"); //$NON-NLS-1$ //$NON-NLS-2$
		final ChoiceComposite choiceFlavor = UIControlsFactory.createChoiceComposite(group, SWT.BORDER, true, "choiceFlavor"); //$NON-NLS-1$
		choiceFlavor.setOrientation(SWT.HORIZONTAL);
		hFillFactory.applyTo(choiceFlavor);

		final Label lblReviewed = UIControlsFactory.createLabel(group, "Reviewed by:", "labellistPersons"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(lblReviewed);
		final Composite cmpReviewed = createComposite(group, 2, true);

		final List listPersons = UIControlsFactory.createList(cmpReviewed, false, true, "listPersons"); //$NON-NLS-1$
		final int hHint = UIControlsFactory.getHeightHint(listPersons, 5);
		hFillFactory.hint(150, hHint).applyTo(listPersons);

		final Table tablePersons = UIControlsFactory.createTable(cmpReviewed, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION, "tablePersons"); //$NON-NLS-1$
		tablePersons.setLinesVisible(true);
		final TableColumn tac1 = new TableColumn(tablePersons, SWT.NONE);
		tac1.setWidth(100);
		final TableColumn tac2 = new TableColumn(tablePersons, SWT.NONE);
		tac2.setWidth(70);
		hFillFactory.hint(170, hHint).applyTo(tablePersons);

		UIControlsFactory.createLabel(group, ""); //$NON-NLS-1$
		final Composite cmpReviewed2 = createComposite(group, 2, true);

		final Tree treePersons = UIControlsFactory.createTree(cmpReviewed2, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION, "treePersons"); //$NON-NLS-1$
		hFillFactory.hint(150, hHint).applyTo(treePersons);

		final Tree treeWCols = UIControlsFactory.createTree(cmpReviewed2, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI, "treeWCols"); //$NON-NLS-1$
		treeWCols.setLinesVisible(true);
		treeWCols.setHeaderVisible(true);
		final TreeColumn trc1 = new TreeColumn(treeWCols, SWT.NONE);
		trc1.setWidth(120);
		final TreeColumn trc2 = new TreeColumn(treeWCols, SWT.NONE);
		trc2.setWidth(40);
		hFillFactory.hint(200, hHint).applyTo(treeWCols);

		UIControlsFactory.createLabel(group, "Toggle Buttons:", "labelbuttonToggleA"); //$NON-NLS-1$ //$NON-NLS-2$
		final Composite cmpButtons = createComposite(group, 6, false);
		UIControlsFactory.createButtonToggle(cmpButtons, "ToggleA", "buttonToggleA"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonToggle(cmpButtons, "ToggleB", "buttonToggleB"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonRadio(cmpButtons, "RadioA", "buttonRadioA"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonRadio(cmpButtons, "RadioB", "buttonRadioB"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(cmpButtons, "CheckA", "buttonCheckA"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(cmpButtons, "CheckB", "buttonCheckB"); //$NON-NLS-1$ //$NON-NLS-2$

		UIControlsFactory.createLabel(group, "Push Buttons:", "labelbuttonPush"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButton(group, "Push", "buttonPush"); //$NON-NLS-1$ //$NON-NLS-2$

		UIControlsFactory.createLabel(group, "Link:", "labellink"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createLink(group, SWT.FLAT, "link"); //$NON-NLS-1$

		return group;
	}

	protected Composite createComposite(final Group group, final int numColumns, final boolean equalWidth) {
		final Composite composite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(numColumns).equalWidth(equalWidth).spacing(10, 0).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);
		return composite;
	}

	private GridLayout createGridLayout(final int numColumns) {
		final GridLayout layout = new GridLayout(numColumns, false);
		layout.marginWidth = 20;
		layout.marginHeight = 20;
		return layout;
	}
}
