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
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link ITableRidget} sample.
 */
public class SystemPropertiesSubModuleView extends SubModuleView {

	public static final String ID = SystemPropertiesSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		final Group group1 = createTableGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);

		final Group group2 = createEditGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group2);
	}

	// helping methods
	// ////////////////

	private Group createTableGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "&System Properties:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);

		final Composite tableComposite = createTable(group);
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(tableComposite);

		final Composite buttonComposite = createButtonComposite(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonComposite);

		return group;
	}

	private Group createEditGroup(final Composite parent) {
		final Group group = UIControlsFactory.createGroup(parent, "Edit:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);

		final GridDataFactory fillFactory = GridDataFactory.fillDefaults().grab(true, false);

		UIControlsFactory.createLabel(group, "&Key:"); //$NON-NLS-1$
		final Text textKey = UIControlsFactory.createText(group);
		fillFactory.applyTo(textKey);
		addUIControl(textKey, "textKey"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "&Value:"); //$NON-NLS-1$
		final Text textValue = UIControlsFactory.createText(group);
		fillFactory.applyTo(textValue);
		addUIControl(textValue, "textValue"); //$NON-NLS-1$

		final Button buttonSave = UIControlsFactory.createButton(group);
		final int widthHint = UIControlsFactory.getWidthHint(buttonSave);
		GridDataFactory.fillDefaults().align(SWT.END, SWT.BEGINNING).hint(widthHint, SWT.DEFAULT).span(2, 1)
				.applyTo(buttonSave);
		addUIControl(buttonSave, "buttonSave"); //$NON-NLS-1$

		return group;
	}

	private Composite createTable(final Group group) {
		final Composite tableComposite = new Composite(group, SWT.NONE);

		final Table tableProperties = UIControlsFactory.createTable(tableComposite, SWT.SINGLE | SWT.BORDER
				| SWT.FULL_SELECTION);
		tableProperties.setLinesVisible(true);
		tableProperties.setHeaderVisible(true);
		addUIControl(tableProperties, "tableProperties"); //$NON-NLS-1$

		final TableColumn columnKey = new TableColumn(tableProperties, SWT.LEFT);
		final TableColumn columnValue = new TableColumn(tableProperties, SWT.LEFT);

		final TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(columnKey, new ColumnWeightData(30));
		layout.setColumnData(columnValue, new ColumnWeightData(60));
		tableComposite.setLayout(layout);

		return tableComposite;
	}

	private Composite createButtonComposite(final Group group) {
		final Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(buttonComposite);

		final Button buttonAdd = UIControlsFactory.createButton(buttonComposite);
		final int widthHint = UIControlsFactory.getWidthHint(buttonAdd);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).hint(widthHint, SWT.DEFAULT)
				.applyTo(buttonAdd);
		addUIControl(buttonAdd, "buttonAdd"); //$NON-NLS-1$

		final Button toggleDoubleClick = UIControlsFactory.createButtonToggle(buttonComposite);
		addUIControl(toggleDoubleClick, "toggleDoubleClick"); //$NON-NLS-1$

		return buttonComposite;
	}

}
