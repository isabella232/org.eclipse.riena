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
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link ITableRidget} sample.
 */
public class TableSubModuleView extends SubModuleView {

	public static final String ID = TableSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		final Group group1 = createTableGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	// helping methods
	// ////////////////

	private Group createTableGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "&Table:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		final Composite tableComposite = new Composite(group, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tableComposite);

		final Table table = UIControlsFactory.createTable(tableComposite, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION,
				"table"); //$NON-NLS-1$
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn columnState = new TableColumn(table, SWT.CENTER);
		final TableColumn columnWord = new TableColumn(table, SWT.LEFT);
		final TableColumn columnUppercase = new TableColumn(table, SWT.LEFT);
		final TableColumn columnACount = new TableColumn(table, SWT.LEFT);
		final TableColumn columnAQuota = new TableColumn(table, SWT.LEFT);

		final TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(columnState, new ColumnPixelData(30));
		layout.setColumnData(columnWord, new ColumnWeightData(25));
		layout.setColumnData(columnUppercase, new ColumnWeightData(25));
		layout.setColumnData(columnACount, new ColumnWeightData(25));
		layout.setColumnData(columnAQuota, new ColumnWeightData(25));
		tableComposite.setLayout(layout);

		final Composite buttonComposite = createButtonComposite(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonComposite);

		return group;
	}

	private Composite createButtonComposite(final Group group) {
		final Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(false).applyTo(buttonComposite);

		final Button buttonPrintSelection = UIControlsFactory.createButtonCheck(buttonComposite,
				"", "buttonPrintSelection"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.CENTER).hint(120, SWT.DEFAULT)
				.applyTo(buttonPrintSelection);

		UIControlsFactory.createButton(buttonComposite, "", "buttonAddSibling"); //$NON-NLS-1$ //$NON-NLS-2$

		UIControlsFactory.createButton(buttonComposite, "", "buttonRename"); //$NON-NLS-1$ //$NON-NLS-2$

		UIControlsFactory.createButton(buttonComposite, "", "buttonDelete"); //$NON-NLS-1$ //$NON-NLS-2$

		return buttonComposite;
	}
}
