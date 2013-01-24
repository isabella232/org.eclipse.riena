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

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * View of a sub-module to demonstrate how cells of a table can be edited with
 * data-binding support and Ridgets.
 */
public class CarCatalogSubModuleView extends SubModuleView {

	public static final String ID = CarCatalogSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		final Group group1 = createTableGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	private Group createTableGroup(final Composite parent) {

		final Group group = UIControlsFactory.createGroup(parent, "&Car Catalog:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		final Composite tableComposite = new Composite(group, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tableComposite);

		final Table table = UIControlsFactory.createTable(tableComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION,
				"table"); //$NON-NLS-1$
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		final TableColumn columnMake = new TableColumn(table, SWT.CENTER);
		final TableColumn columnModel = new TableColumn(table, SWT.LEFT);
		final TableColumn columnPower = new TableColumn(table, SWT.RIGHT);
		final TableColumn columnCapacity = new TableColumn(table, SWT.RIGHT);
		final TableColumn columnSpeedUp = new TableColumn(table, SWT.RIGHT);
		final TableColumn columnMilage = new TableColumn(table, SWT.RIGHT);

		final TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(columnMake, new ColumnWeightData(14));
		layout.setColumnData(columnModel, new ColumnWeightData(22));
		layout.setColumnData(columnPower, new ColumnWeightData(14));
		layout.setColumnData(columnCapacity, new ColumnWeightData(14));
		layout.setColumnData(columnSpeedUp, new ColumnWeightData(14));
		layout.setColumnData(columnMilage, new ColumnWeightData(14));
		tableComposite.setLayout(layout);

		return group;
	}

}
