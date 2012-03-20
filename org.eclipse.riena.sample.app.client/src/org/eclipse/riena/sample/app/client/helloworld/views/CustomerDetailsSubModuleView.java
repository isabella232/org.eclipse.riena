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
package org.eclipse.riena.sample.app.client.helloworld.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.sample.app.client.helloworld.controllers.CustomerDetailsSubModuleController;

public class CustomerDetailsSubModuleView extends SubModuleView {

	public final static String ID = CustomerDetailsSubModuleView.class.getName();

	private static final int FIELD_WIDTH = 100;
	private final static int TOP = 10;
	private static final int LEFT = 10;
	private static final int SECTION_LABEL_WIDTH = 100;
	private static final int LABEL_WIDTH = 90;
	private static final int LINE_GAP = 12;
	private static final int COL_GAP = 30;

	private Composite contentArea;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.ui.swt.views.SubModuleView#
	 * basicCreatePartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void basicCreatePartControl(final Composite parent) {
		this.contentArea = parent;
		contentArea.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		contentArea.setLayout(new FormLayout());

		final Label personLabel = createSectionLabel(contentArea, "Person"); //$NON-NLS-1$
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, TOP);
		fd.left = new FormAttachment(0, LEFT);
		personLabel.setLayoutData(fd);

		final Label kundennummerLabel = new Label(contentArea, SWT.LEFT);
		kundennummerLabel.setText("Customer No."); //$NON-NLS-1$
		kundennummerLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(personLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(personLabel, SECTION_LABEL_WIDTH, SWT.LEFT);
		kundennummerLabel.setLayoutData(fd);

		final Text numberText = new Text(contentArea, SWT.SINGLE);
		addUIControl(numberText, CustomerDetailsSubModuleController.RIDGET_ID_CUSTOMER_NUMBER);
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(kundennummerLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		numberText.setLayoutData(fd);

		final Label lastNameLabel = new Label(contentArea, SWT.LEFT);
		lastNameLabel.setText("Last Name"); //$NON-NLS-1$
		lastNameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, LINE_GAP);
		fd.left = new FormAttachment(kundennummerLabel, 0, SWT.LEFT);
		lastNameLabel.setLayoutData(fd);

		final Text lastNameText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		addUIControl(lastNameText, CustomerDetailsSubModuleController.RIDGET_ID_LAST_NAME);
		fd = new FormData();
		fd.top = new FormAttachment(lastNameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(numberText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		lastNameText.setLayoutData(fd);

		final Label vornameLabel = new Label(contentArea, SWT.LEFT);
		vornameLabel.setText("First Name"); //$NON-NLS-1$
		vornameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(lastNameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(lastNameLabel, COL_GAP + FIELD_WIDTH + LABEL_WIDTH, SWT.LEFT);
		vornameLabel.setLayoutData(fd);

		final Text firstnameText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		addUIControl(firstnameText, CustomerDetailsSubModuleController.RIDGET_ID_FIRST_NAME);
		fd = new FormData();
		fd.top = new FormAttachment(vornameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(vornameLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		firstnameText.setLayoutData(fd);

		final Label birthdayLabel = new Label(contentArea, SWT.LEFT);
		birthdayLabel.setText("Birthday"); //$NON-NLS-1$
		birthdayLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(lastNameLabel, LINE_GAP);
		fd.left = new FormAttachment(lastNameLabel, 0, SWT.LEFT);
		birthdayLabel.setLayoutData(fd);

		final DateTime birthdayText = new DateTime(contentArea, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(birthdayLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(lastNameText, 0, SWT.LEFT);
		birthdayText.setLayoutData(fd);

		final Label birthplaceLabel = new Label(contentArea, SWT.LEFT);
		birthplaceLabel.setText("Birthplace"); //$NON-NLS-1$
		birthplaceLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(birthdayLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(birthdayLabel, COL_GAP + FIELD_WIDTH + LABEL_WIDTH, SWT.LEFT);
		birthplaceLabel.setLayoutData(fd);

		final Text birthplaceText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		addUIControl(birthplaceText, CustomerDetailsSubModuleController.RIDGET_ID_BIRTHPLACE);
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(birthplaceLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		birthplaceText.setLayoutData(fd);

		final Button openOffers = new Button(contentArea, 0);
		addUIControl(openOffers, CustomerDetailsSubModuleController.RIDGET_ID_OPEN_OFFERS);
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceText, LINE_GAP);
		fd.left = new FormAttachment(birthdayText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		openOffers.setLayoutData(fd);

		final Button saveButton = new Button(contentArea, 0);
		addUIControl(saveButton, CustomerDetailsSubModuleController.RIDGET_ID_SAVE);
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceText, LINE_GAP);
		fd.left = new FormAttachment(birthplaceText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		saveButton.setLayoutData(fd);
	}

	private Label createSectionLabel(final Composite parent, final String text) {

		final Label label = new Label(parent, SWT.LEFT);
		label.setText(text);
		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		return label;
	}

	public Image getIcon() {
		return AbstractUIPlugin.imageDescriptorFromPlugin("de.compeople.scp.sample.client", "/icons/user_16.png") //$NON-NLS-1$ //$NON-NLS-2$
				.createImage();
	}

	@Override
	protected CustomerDetailsSubModuleController createController(final ISubModuleNode subModuleNode) {
		return new CustomerDetailsSubModuleController(subModuleNode);
	}
}
