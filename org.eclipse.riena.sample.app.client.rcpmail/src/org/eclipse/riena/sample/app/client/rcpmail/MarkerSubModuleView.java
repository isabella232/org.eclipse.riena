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
package org.eclipse.riena.sample.app.client.rcpmail;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
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
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
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
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
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
		group.setLayout(createGridLayout(1));

		UIControlsFactory.createButtonCheck(group, "&hidden", "checkHidden"); //$NON-NLS-1$ //$NON-NLS-2$

		return group;
	}

	private Group createControlsGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "UI-Controls:"); //$NON-NLS-1$
		final int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).spacing(10, defaultVSpacing)
				.applyTo(group);

		final GridDataFactory hFillFactory = GridDataFactory.fillDefaults().grab(true, false);

		UIControlsFactory.createLabel(group, "Name:", "labeltextName"); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textName = UIControlsFactory.createText(group, SWT.SINGLE, "textName"); //$NON-NLS-1$
		hFillFactory.applyTo(textName);

		UIControlsFactory.createLabel(group, "Price:", "labeltextPrice"); //$NON-NLS-1$ //$NON-NLS-2$
		final Text textPrice = UIControlsFactory.createTextDecimal(group, "textPrice"); //$NON-NLS-1$
		hFillFactory.applyTo(textPrice);

		UIControlsFactory.createLabel(group, "Age:", "labelcomboAge"); //$NON-NLS-1$ //$NON-NLS-2$
		final Combo comboAge = UIControlsFactory.createCombo(group, "comboAge"); //$NON-NLS-1$
		hFillFactory.applyTo(comboAge);

		UIControlsFactory.createLabel(group, "Type:", "labelchoiceType"); //$NON-NLS-1$ //$NON-NLS-2$
		final ChoiceComposite choiceType = UIControlsFactory.createChoiceComposite(group, SWT.BORDER, false,
				"choiceType"); //$NON-NLS-1$
		choiceType.setOrientation(SWT.HORIZONTAL);

		UIControlsFactory.createLabel(group, "Flavor:", "labelchoiceFlavor"); //$NON-NLS-1$ //$NON-NLS-2$
		final ChoiceComposite choiceFlavor = UIControlsFactory.createChoiceComposite(group, SWT.BORDER, true,
				"choiceFlavor"); //$NON-NLS-1$
		choiceFlavor.setOrientation(SWT.HORIZONTAL);

		final Label lblReviewed = UIControlsFactory.createLabel(group, "Reviewed by:", "labellistPersons"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(lblReviewed);
		final Composite cmpReviewed = createComposite(group);

		final List listPersons = UIControlsFactory.createList(cmpReviewed, false, true, "listPersons"); //$NON-NLS-1$
		final int hHint = UIControlsFactory.getHeightHint(listPersons, 5);
		hFillFactory.hint(150, hHint).applyTo(listPersons);

		final Table tablePersons = new Table(cmpReviewed, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		tablePersons.setLinesVisible(true);
		final TableColumn tac1 = new TableColumn(tablePersons, SWT.NONE);
		tac1.setWidth(100);
		final TableColumn tac2 = new TableColumn(tablePersons, SWT.NONE);
		tac2.setWidth(70);
		hFillFactory.hint(170, hHint).applyTo(tablePersons);
		addUIControl(tablePersons, "tablePersons"); //$NON-NLS-1$

		final Tree treePersons = UIControlsFactory.createTree(cmpReviewed, SWT.V_SCROLL | SWT.BORDER
				| SWT.FULL_SELECTION, "treePersons"); //$NON-NLS-1$
		hFillFactory.hint(150, hHint).applyTo(treePersons);

		final Tree treeWCols = new Tree(cmpReviewed, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		treeWCols.setLinesVisible(true);
		treeWCols.setHeaderVisible(true);
		final TreeColumn trc1 = new TreeColumn(treeWCols, SWT.NONE);
		trc1.setWidth(120);
		final TreeColumn trc2 = new TreeColumn(treeWCols, SWT.NONE);
		trc2.setWidth(40);
		hFillFactory.hint(200, hHint).applyTo(treeWCols);
		addUIControl(treeWCols, "treeWCols"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Buttons:", "labelbuttonToggle"); //$NON-NLS-1$ //$NON-NLS-2$
		final Composite cmpButtons = createComposite(group);
		UIControlsFactory.createButtonToggle(cmpButtons, "Toggle", "buttonToggle"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButton(cmpButtons, "Push", "buttonPush"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonRadio(cmpButtons, "Radio A", "buttonRadioA"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonRadio(cmpButtons, "Radio B", "buttonRadioB"); //$NON-NLS-1$ //$NON-NLS-2$
		UIControlsFactory.createButtonCheck(cmpButtons, "Check", "buttonCheck"); //$NON-NLS-1$ //$NON-NLS-2$

		return group;
	}

	// helping methods
	// ////////////////

	private Composite createComposite(final Group group) {
		final Composite composite = new Composite(group, SWT.NONE);
		composite.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(10, 0).applyTo(composite);
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
