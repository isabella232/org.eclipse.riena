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

import com.swtdesigner.SWTResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.layout.DpiGridLayout;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * This view displays the data of one person.
 */
public class OnePersonSubModuleView extends SubModuleView {
	public OnePersonSubModuleView() {
	}

	private Text townText;
	private Text postalCodeText;
	private Combo countryCombo;
	private Text streetText;
	private Text birthplaceText;
	private Text birthdayText;
	private Text firstNameText;
	private Text lastNameText;
	private Text customerNumberText;
	public static final String ID = OnePersonSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		final DpiGridLayout gridLayout = new DpiGridLayout();
		gridLayout.numColumns = 7;
		parent.setLayout(gridLayout);
		parent.setBackgroundMode(SWT.INHERIT_FORCE);

		final Label personLabel = new Label(parent, SWT.NONE);
		final GridData gdPersonLabel = new GridData(75, SWT.DEFAULT);
		personLabel.setLayoutData(gdPersonLabel);
		personLabel.setData(UIControlsFactory.KEY_LNF_STYLE, "sectionLabel"); //$NON-NLS-1$
		personLabel.setText("Person"); //$NON-NLS-1$

		final Label customerNumberLabel = new Label(parent, SWT.NONE);
		customerNumberLabel.setLayoutData(new GridData());
		customerNumberLabel.setText("Customer Number"); //$NON-NLS-1$

		customerNumberText = new Text(parent, SWT.BORDER);
		customerNumberText.setBackground(SWTResourceManager.getColor(255, 255, 255));
		customerNumberText.setData("type", "numeric"); //$NON-NLS-1$ //$NON-NLS-2$
		customerNumberText.setData("binding_property", "customerNumber"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gdCustomerNumberText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		customerNumberText.setLayoutData(gdCustomerNumberText);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label lastNameLabel = new Label(parent, SWT.NONE);
		lastNameLabel.setLayoutData(new GridData());
		lastNameLabel.setText("Last Name"); //$NON-NLS-1$

		lastNameText = new Text(parent, SWT.BORDER);
		lastNameText.setBackground(SWTResourceManager.getColor(255, 255, 255));
		lastNameText.setData("binding_property", "lastName"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gdLastNameText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		lastNameText.setLayoutData(gdLastNameText);

		final Label firstNameLabel = new Label(parent, SWT.NONE);
		final GridData gdFirstNameLabel = new GridData();
		gdFirstNameLabel.horizontalIndent = 10;
		firstNameLabel.setLayoutData(gdFirstNameLabel);
		firstNameLabel.setText("First Name"); //$NON-NLS-1$

		firstNameText = new Text(parent, SWT.BORDER);
		firstNameText.setBackground(SWTResourceManager.getColor(255, 255, 255));
		firstNameText.setData("binding_property", "firstName"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gdFirstNameText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		firstNameText.setLayoutData(gdFirstNameText);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label birthdayLabel = new Label(parent, SWT.NONE);
		birthdayLabel.setLayoutData(new GridData());
		birthdayLabel.setText("Birthday"); //$NON-NLS-1$

		birthdayText = new Text(parent, SWT.BORDER);
		birthdayText.setBackground(SWTResourceManager.getColor(255, 255, 255));
		birthdayText.setData("binding_property", "birthday"); //$NON-NLS-1$ //$NON-NLS-2$
		birthdayText.setData("type", "date"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gdBirthdayText = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gdBirthdayText.widthHint = 75;
		birthdayText.setLayoutData(gdBirthdayText);

		final Label birthplaceLabel = new Label(parent, SWT.NONE);
		final GridData gdBirthplaceLabel = new GridData();
		gdBirthplaceLabel.horizontalIndent = 10;
		birthplaceLabel.setLayoutData(gdBirthplaceLabel);
		birthplaceLabel.setText("Birthplace"); //$NON-NLS-1$

		birthplaceText = new Text(parent, SWT.BORDER);
		birthplaceText.setBackground(SWTResourceManager.getColor(255, 255, 255));
		birthplaceText.setData("binding_property", "birthplace"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gdBirthplaceText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		birthplaceText.setLayoutData(gdBirthplaceText);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label genderLabel = new Label(parent, SWT.NONE);
		final GridData gdGenderLabel = new GridData();
		genderLabel.setLayoutData(gdGenderLabel);
		genderLabel.setText("Gender"); //$NON-NLS-1$

		final ChoiceComposite genderChoiceComposite = new ChoiceComposite(parent, SWT.NONE, false);
		genderChoiceComposite.setOrientation(SWT.HORIZONTAL);
		genderChoiceComposite.setData("binding_property", "gender"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gdGenderChoiceComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		genderChoiceComposite.setLayoutData(gdGenderChoiceComposite);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		final GridData gdSeparator = new GridData(SWT.FILL, SWT.CENTER, false, false, 7, 1);
		separator.setLayoutData(gdSeparator);

		final Label addressLabel = new Label(parent, SWT.NONE);
		addressLabel.setLayoutData(new GridData());
		addressLabel.setData(UIControlsFactory.KEY_LNF_STYLE, "sectionLabel"); //$NON-NLS-1$
		addressLabel.setText("Address"); //$NON-NLS-1$

		final Label streetAndNumberLabel = new Label(parent, SWT.NONE);
		streetAndNumberLabel.setLayoutData(new GridData());
		streetAndNumberLabel.setText("Street and Number"); //$NON-NLS-1$

		streetText = new Text(parent, SWT.BORDER);
		streetText.setBackground(SWTResourceManager.getColor(255, 255, 255));
		streetText.setData("binding_property", "street"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gdStreetText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		streetText.setLayoutData(gdStreetText);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label countryLabel = new Label(parent, SWT.NONE);
		countryLabel.setLayoutData(new GridData());
		countryLabel.setText("Country"); //$NON-NLS-1$

		countryCombo = new Combo(parent, SWT.READ_ONLY);
		countryCombo.setData("binding_property", "country"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gdCountryCombo = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		countryCombo.setLayoutData(gdCountryCombo);

		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Label postalCodeLabel = new Label(parent, SWT.NONE);
		final GridData gdPostalCodeLabel = new GridData();
		gdPostalCodeLabel.horizontalIndent = 10;
		postalCodeLabel.setLayoutData(gdPostalCodeLabel);
		postalCodeLabel.setText("Postal Code"); //$NON-NLS-1$

		postalCodeText = new Text(parent, SWT.BORDER);
		postalCodeText.setBackground(SWTResourceManager.getColor(255, 255, 255));
		postalCodeText.setData("type", "numeric"); //$NON-NLS-1$ //$NON-NLS-2$
		postalCodeText.setData("binding_property", "postalCode"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gdPostalCodeText = new GridData(SWT.LEFT, SWT.CENTER, true, false);
		gdPostalCodeText.widthHint = 35;
		postalCodeText.setLayoutData(gdPostalCodeText);

		final Label townLabel = new Label(parent, SWT.NONE);
		final GridData gdTownLabel = new GridData();
		gdTownLabel.horizontalIndent = 10;
		townLabel.setLayoutData(gdTownLabel);
		townLabel.setText("Town"); //$NON-NLS-1$

		townText = new Text(parent, SWT.BORDER);
		townText.setBackground(SWTResourceManager.getColor(255, 255, 255));
		townText.setData("binding_property", "town"); //$NON-NLS-1$ //$NON-NLS-2$
		final GridData gdTownText = new GridData(SWT.FILL, SWT.CENTER, true, false);
		townText.setLayoutData(gdTownText);

		final Label label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 7, 1));

		final Button showButton = new Button(parent, SWT.NONE);
		showButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		showButton.setData("binding_property", "show"); //$NON-NLS-1$ //$NON-NLS-2$
		showButton.setText("Show"); //$NON-NLS-1$

		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Button nextButton = new Button(parent, SWT.NONE);
		nextButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		nextButton.setData("binding_property", "next"); //$NON-NLS-1$ //$NON-NLS-2$
		nextButton.setText("Next Person"); //$NON-NLS-1$

		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		final Button jumpBackButton = UIControlsFactory.createButton(parent, "Jump Back", "jumpBack"); //$NON-NLS-1$ //$NON-NLS-2$
		jumpBackButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

	}

}
