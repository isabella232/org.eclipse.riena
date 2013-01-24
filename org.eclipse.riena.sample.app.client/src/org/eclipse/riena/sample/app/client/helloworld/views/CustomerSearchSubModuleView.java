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
package org.eclipse.riena.sample.app.client.helloworld.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.sample.app.client.helloworld.controllers.CustomerSearchSubModuleController;
import org.eclipse.riena.sample.app.common.model.Customer;
import org.eclipse.riena.ui.workarea.WorkareaManager;

public class CustomerSearchSubModuleView extends SubModuleView {

	public final static String ID = CustomerSearchSubModuleView.class.getName();

	private static final int FIELD_WIDTH = 100;
	private final static int TOP = 10;
	private static final int LEFT = 10;
	private static final int SECTION_LABEL_WIDTH = 100;
	private static final int LABEL_WIDTH = 90;
	private static final int LINE_GAP = 12;
	private static final int COL_GAP = 30;

	private Table searchResultTable;

	private Text kundennummerText;

	private Text nameText;

	private Text vornameText;

	@Override
	public void basicCreatePartControl(final Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new FormLayout());

		final Label personLabel = createSectionLabel(parent, "Person"); //$NON-NLS-1$
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, TOP);
		fd.left = new FormAttachment(0, LEFT);
		personLabel.setLayoutData(fd);

		final Label kundennummerLabel = new Label(parent, SWT.LEFT);
		kundennummerLabel.setText("Kundennummer"); //$NON-NLS-1$
		kundennummerLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(personLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(personLabel, SECTION_LABEL_WIDTH, SWT.LEFT);
		kundennummerLabel.setLayoutData(fd);

		kundennummerText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(kundennummerLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		kundennummerText.setLayoutData(fd);

		final Label nameLabel = new Label(parent, SWT.LEFT);
		nameLabel.setText("Name"); //$NON-NLS-1$
		nameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, LINE_GAP);
		fd.left = new FormAttachment(kundennummerLabel, 0, SWT.LEFT);
		nameLabel.setLayoutData(fd);

		nameText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(kundennummerText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		nameText.setLayoutData(fd);
		addUIControl(nameText, "lastNameRidget"); //$NON-NLS-1$

		final Label vornameLabel = new Label(parent, SWT.LEFT);
		vornameLabel.setText("Vorname"); //$NON-NLS-1$
		vornameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(nameLabel, COL_GAP + FIELD_WIDTH + LABEL_WIDTH, SWT.LEFT);
		vornameLabel.setLayoutData(fd);

		vornameText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(vornameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(vornameLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		vornameText.setLayoutData(fd);
		addUIControl(vornameText, "firstNameRidget"); //$NON-NLS-1$

		final Button searchButton = new Button(parent, 0);
		searchButton.setText("Suchen"); //$NON-NLS-1$
		fd = new FormData();
		fd.top = new FormAttachment(vornameText, LINE_GAP);
		fd.left = new FormAttachment(vornameText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		searchButton.setLayoutData(fd);
		addUIControl(searchButton, "searchAction"); //$NON-NLS-1$

		final Button clearButton = new Button(parent, 0);
		clearButton.setText("Clear"); //$NON-NLS-1$
		fd = new FormData();
		fd.top = new FormAttachment(vornameText, LINE_GAP);
		fd.left = new FormAttachment(nameText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		clearButton.setLayoutData(fd);
		addUIControl(clearButton, "clearAction"); //$NON-NLS-1$

		// create table
		searchResultTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		searchResultTable.setLinesVisible(true);
		addUIControl(searchResultTable, "tableRidget"); //$NON-NLS-1$
		// searchResultTable.addSelectionListener(new CustomerOpener());
		final TableColumn customerNumberColumn = new TableColumn(searchResultTable, SWT.CENTER);
		final TableColumn lastNameColumn = new TableColumn(searchResultTable, SWT.CENTER);
		final TableColumn firstNameColumn = new TableColumn(searchResultTable, SWT.CENTER);
		final TableColumn phoneColumn = new TableColumn(searchResultTable, SWT.CENTER);
		customerNumberColumn.setWidth(80);
		firstNameColumn.setWidth(120);
		lastNameColumn.setWidth(120);
		phoneColumn.setWidth(100);
		searchResultTable.setHeaderVisible(true);

		// layout table
		fd = new FormData();
		fd.top = new FormAttachment(searchButton, LINE_GAP);
		fd.left = new FormAttachment(0, LEFT);
		fd.height = 200;
		fd.width = 500;
		searchResultTable.setLayoutData(fd);

		// open button
		final Button openButton = new Button(parent, 0);
		openButton.setText("Open"); //$NON-NLS-1$
		fd = new FormData();
		fd.top = new FormAttachment(searchResultTable, LINE_GAP);
		fd.left = new FormAttachment(searchResultTable, 0, SWT.CENTER);
		fd.width = FIELD_WIDTH;
		openButton.setLayoutData(fd);
		addUIControl(openButton, "openAction"); //$NON-NLS-1$

	}

	private ISubModuleNode getNode() {
		return SwtViewProvider.getInstance().getNavigationNode(this.getViewSite().getId(),
				this.getViewSite().getSecondaryId(), ISubModuleNode.class);
	}

	protected void openCustomer() {
		final Customer selected = ((Customer) searchResultTable.getSelection()[0].getData());

		final ISubModuleNode node = getNode();
		final SubModuleNode cNode = new SubModuleNode(null, selected.getFirstName());
		cNode.setContext(Customer.class.getName(), selected);
		WorkareaManager.getInstance().registerDefinition(cNode, CustomerDetailsSubModuleView.ID);
		node.addChild(cNode);
		cNode.activate();
	}

	private Label createSectionLabel(final Composite parent, final String text) {

		final Label label = new Label(parent, SWT.LEFT);
		label.setText(text);
		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		return label;

	}

	@Override
	public void setFocus() {
		super.setFocus();
	}

	@Override
	protected CustomerSearchSubModuleController createController(final ISubModuleNode subModuleNode) {
		return new CustomerSearchSubModuleController(getNode());
	}

}
