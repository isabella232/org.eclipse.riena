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
package org.eclipse.riena.example.client.optional.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.swt.optional.OptionalUIControlsFactory;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Simple example to show how to use the nebula Grid.
 */
public class GridSubModuleView extends SubModuleView {

	public static final String BINDING_ID_GRID_TABLE = "gridTable"; //$NON-NLS-1$
	public static final String BINDING_ID_BUTTON_PRINT_SELECTION = "buttonPrintSelection"; //$NON-NLS-1$
	public static final String BINDING_ID_BUTTON_DELETE = "buttonDelete"; //$NON-NLS-1$
	public static final String BINDING_ID_BUTTON_RENAME = "buttonRename"; //$NON-NLS-1$
	public static final String BINDING_ID_BUTTON_ADD_SIBLING = "buttonAddSibling"; //$NON-NLS-1$

	public static final String ID = GridSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		final Grid grid = OptionalUIControlsFactory.createGrid(parent, SWT.BORDER | SWT.SINGLE, BINDING_ID_GRID_TABLE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(grid);
		grid.setHeaderVisible(true);

		final GridColumn columnWord = new GridColumn(grid, SWT.LEFT);
		columnWord.setWidth(125);
		final GridColumn columnUppercase = new GridColumn(grid, SWT.LEFT);
		columnUppercase.setWidth(90);
		final GridColumn columnACount = new GridColumn(grid, SWT.LEFT);
		columnACount.setWidth(90);
		final GridColumn columnAQuota = new GridColumn(grid, SWT.LEFT);
		columnAQuota.setWidth(90);
		final GridColumn columnState = new GridColumn(grid, SWT.CENTER);
		columnState.setWidth(50);

		final Composite buttonComposite = createButtonComposite(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonComposite);

	}

	private Composite createButtonComposite(final Composite parent) {
		final Composite buttonComposite = UIControlsFactory.createComposite(parent);
		GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(false).applyTo(buttonComposite);

		final Button buttonPrintSelection = UIControlsFactory.createButtonCheck(buttonComposite,
				"", BINDING_ID_BUTTON_PRINT_SELECTION); //$NON-NLS-1$ 
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.CENTER).hint(120, SWT.DEFAULT)
				.applyTo(buttonPrintSelection);

		UIControlsFactory.createButton(buttonComposite, "", BINDING_ID_BUTTON_ADD_SIBLING); //$NON-NLS-1$ 

		UIControlsFactory.createButton(buttonComposite, "", BINDING_ID_BUTTON_RENAME); //$NON-NLS-1$ 

		UIControlsFactory.createButton(buttonComposite, "", BINDING_ID_BUTTON_DELETE); //$NON-NLS-1$ 

		return buttonComposite;
	}

}
