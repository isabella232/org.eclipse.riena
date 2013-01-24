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
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * View with SWT {@link Table} with style <code>SWT.CHECK</code>.
 */
public class CheckBoxTableSubModuleView extends SubModuleView {

	public static final String ID = CheckBoxTableSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		final Group group1 = createTableGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	private Group createTableGroup(final Composite parent) {

		final Group group = UIControlsFactory.createGroup(parent, "Car E&xtras Catalog"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		final Composite choiceComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(choiceComposite);
		final ChoiceComposite choice = UIControlsFactory.createChoiceComposite(choiceComposite, SWT.NONE, false, "choice"); //$NON-NLS-1$
		choice.setOrientation(SWT.HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(choice);

		final Composite tableComposite = UIControlsFactory.createComposite(group);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tableComposite);

		final Table table = UIControlsFactory.createTable(tableComposite, SWT.CHECK | SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION, "table"); //$NON-NLS-1$
		table.setLinesVisible(true);
		table.setHeaderVisible(false);

		final TableColumn columnBye = new TableColumn(table, SWT.CENTER);
		final TableColumn columnName = new TableColumn(table, SWT.LEFT);
		final TableColumn columnCategory = new TableColumn(table, SWT.LEFT);
		final TableColumn columnPrice = new TableColumn(table, SWT.RIGHT);

		final TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(columnBye, new ColumnWeightData(10));
		layout.setColumnData(columnName, new ColumnWeightData(50));
		layout.setColumnData(columnCategory, new ColumnWeightData(20));
		layout.setColumnData(columnPrice, new ColumnWeightData(20));
		tableComposite.setLayout(layout);

		final Composite totalComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(totalComposite);
		UIControlsFactory.createLabel(totalComposite, "Total Price:"); //$NON-NLS-1$
		final Text total = UIControlsFactory.createTextDecimal(totalComposite, "total"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().hint(100, SWT.DEFAULT).applyTo(total);

		return group;

	}
}
