/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.riena.example.client.controllers.CustomerDetailSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CustomerDetailSubModuleView extends SubModuleView<CustomerDetailSubModuleController> {

	public final static String ID = CustomerDetailSubModuleView.class.getName();

	private static final int FIELD_WIDTH = 100;
	private final static int TOP = 10;
	private static final int LEFT = 10;
	private static final int SECTION_LABEL_WIDTH = 100;
	private static final int LABEL_WIDTH = 90;
	private static final int LINE_GAP = 12;
	private static final int COL_GAP = 30;
	private Composite contentArea;

	public CustomerDetailSubModuleView() {
		addPartPropertyListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				System.out.println(event);
			}
		});
	}

	@Override
	public void dispose() {
	}

	// public void basicCreatePartControl(Composite c) {
	// return;
	// }

	@Override
	public void basicCreatePartControl(Composite parent) {
		// super.beforeCreatePartControl();

		this.contentArea = parent;
		contentArea.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		contentArea.setLayout(new FormLayout());

		Label personLabel = createSectionLabel(contentArea, "Person"); //$NON-NLS-1$
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, TOP);
		fd.left = new FormAttachment(0, LEFT);
		personLabel.setLayoutData(fd);

		Label kundennummerLabel = UIControlsFactory.createLabel(contentArea, "Customer No."); //$NON-NLS-1$
		fd = new FormData();
		fd.top = new FormAttachment(personLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(personLabel, SECTION_LABEL_WIDTH, SWT.LEFT);
		kundennummerLabel.setLayoutData(fd);

		Text kundennummerText = new Text(contentArea, SWT.SINGLE);
		kundennummerText.setEditable(false);
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(kundennummerLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		kundennummerText.setLayoutData(fd);

		Label nameLabel = UIControlsFactory.createLabel(contentArea, "Last Name"); //$NON-NLS-1$
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, LINE_GAP);
		fd.left = new FormAttachment(kundennummerLabel, 0, SWT.LEFT);
		nameLabel.setLayoutData(fd);

		Text nameText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		nameText.setData("binding_property", "lastname"); //$NON-NLS-1$ //$NON-NLS-2$
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(kundennummerText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		nameText.setLayoutData(fd);
		nameText.setText(getController().getNavigationNode().getLabel());

		Label vornameLabel = UIControlsFactory.createLabel(contentArea, "First Name"); //$NON-NLS-1$
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(nameLabel, COL_GAP + FIELD_WIDTH + LABEL_WIDTH, SWT.LEFT);
		vornameLabel.setLayoutData(fd);

		Text vornameText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(vornameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(vornameLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		vornameText.setLayoutData(fd);

		Label birthdayLabel = UIControlsFactory.createLabel(contentArea, "Birthday"); //$NON-NLS-1$
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, LINE_GAP);
		fd.left = new FormAttachment(nameLabel, 0, SWT.LEFT);
		birthdayLabel.setLayoutData(fd);

		DateTime birthdayText = new DateTime(contentArea, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(birthdayLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(nameText, 0, SWT.LEFT);
		birthdayText.setLayoutData(fd);

		Label birthplaceLabel = UIControlsFactory.createLabel(contentArea, "Birthplace"); //$NON-NLS-1$
		fd = new FormData();
		fd.top = new FormAttachment(birthdayLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(birthdayLabel, COL_GAP + FIELD_WIDTH + LABEL_WIDTH, SWT.LEFT);
		birthplaceLabel.setLayoutData(fd);

		Text birthplaceText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(birthplaceLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		birthplaceText.setLayoutData(fd);

		Button openOffers = new Button(contentArea, 0);
		openOffers.setText("Offers"); //$NON-NLS-1$
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceText, LINE_GAP);
		fd.left = new FormAttachment(birthdayText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		openOffers.setLayoutData(fd);

		Button saveButton = new Button(contentArea, 0);
		saveButton.setText("Save"); //$NON-NLS-1$
		// saveButton.addSelectionListener(new StoreCustomerListener());
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceText, LINE_GAP);
		fd.left = new FormAttachment(birthplaceText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		saveButton.setLayoutData(fd);

		// super.addBoundUIControls(contentArea);
		// super.afterCreatePartControl();
	}

	private Label createSectionLabel(Composite parent, String text) {
		Label label = UIControlsFactory.createLabel(parent, text);
		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		return label;
	}

}
