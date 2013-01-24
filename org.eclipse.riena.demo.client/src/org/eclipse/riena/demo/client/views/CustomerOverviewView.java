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
package org.eclipse.riena.demo.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.internal.demo.client.DemoClientUIControlsFactory;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class CustomerOverviewView extends SubModuleView {
	@Override
	public void basicCreatePartControl(final Composite parent) {
		parent.setLayout(new FillLayout());
		final Composite container = new Composite(parent, SWT.NONE);

		final Label personalLabel = DemoClientUIControlsFactory.createSectionLabel(container, "Name"); //$NON-NLS-1$
		personalLabel.setBounds(30, 27, 66, 21);

		final Label lFirstname = UIControlsFactory.createLabel(container, "Firstname"); //$NON-NLS-1$
		lFirstname.setBounds(150, 25, 131, 32);

		final Text firstname = UIControlsFactory.createText(container, SWT.BORDER, "firstname"); //$NON-NLS-1$
		firstname.setBounds(291, 25, 181, 32);

		final Label lLastname = UIControlsFactory.createLabel(container, "Lastname"); //$NON-NLS-1$
		lLastname.setBounds(150, 67, 130, 23);

		final Text lastname = UIControlsFactory.createText(container, SWT.BORDER, "lastname"); //$NON-NLS-1$
		lastname.setBounds(290, 67, 182, 32);

		final Label lZipcity = UIControlsFactory.createLabel(container, "Zipcode / City"); //$NON-NLS-1$
		lZipcity.setBounds(150, 173, 132, 23);

		final Label address = DemoClientUIControlsFactory.createSectionLabel(container, "Address"); //$NON-NLS-1$
		address.setBounds(30, 132, 86, 22);

		final Label lStreet = UIControlsFactory.createLabel(container, "Street"); //$NON-NLS-1$
		lStreet.setBounds(150, 133, 132, 23);

		final Text street = UIControlsFactory.createText(container, SWT.BORDER, "street"); //$NON-NLS-1$
		street.setBounds(291, 125, 182, 32);

		final Text zipcode = UIControlsFactory.createText(container, SWT.BORDER, "zipcode"); //$NON-NLS-1$
		zipcode.setBounds(290, 165, 66, 32);

		final Text city = UIControlsFactory.createText(container, SWT.BORDER, "city"); //$NON-NLS-1$
		city.setBounds(377, 165, 182, 32);

		final Label personal = DemoClientUIControlsFactory.createSectionLabel(container, "Personal"); //$NON-NLS-1$
		personal.setBounds(30, 232, 86, 27);

		final Label lEmailAddress = UIControlsFactory.createLabel(container, "E-mail address"); //$NON-NLS-1$
		lEmailAddress.setBounds(150, 233, 131, 32);

		final Label lBirthday = UIControlsFactory.createLabel(container, "Birthdate"); //$NON-NLS-1$
		lBirthday.setBounds(150, 275, 132, 32);

		final Label lSalary = UIControlsFactory.createLabel(container, "Salary", "salaryLabel"); //$NON-NLS-1$ //$NON-NLS-2$
		lSalary.setBounds(150, 317, 132, 32);

		final Text emailaddress = UIControlsFactory.createText(container, SWT.BORDER, "emailaddress"); //$NON-NLS-1$
		emailaddress.setBounds(291, 233, 250, 32);

		final Text birthdate = UIControlsFactory.createTextDate(container, "birthdate"); //$NON-NLS-1$
		birthdate.setBounds(291, 275, 100, 32);

		final Text salary = UIControlsFactory.createTextDecimal(container, "salary"); //$NON-NLS-1$
		salary.setBounds(291, 317, 100, 32);

		final Button saveButton = UIControlsFactory.createButton(container, "Save", "saveAction"); //$NON-NLS-1$ //$NON-NLS-2$
		saveButton.setBounds(500, 465, 109, 38);

		final Button openEmailsButton = UIControlsFactory.createButton(container, "Open Emails", "openEmailsAction"); //$NON-NLS-1$ //$NON-NLS-2$
		openEmailsButton.setBounds(625, 465, 109, 38);

		final Group grpUifilter = UIControlsFactory.createGroup(container, "UIFilter"); //$NON-NLS-1$
		grpUifilter.setBounds(545, 340, 190, 89);

		final Button buttonA = UIControlsFactory.createButtonToggle(grpUifilter, "Restricted Content", "assistent"); //$NON-NLS-1$ //$NON-NLS-2$
		buttonA.setBounds(28, 20, 143, 26);

		final Button buttonB = UIControlsFactory.createButtonToggle(grpUifilter, "Add Validation", "mandatory"); //$NON-NLS-1$ //$NON-NLS-2$
		buttonB.setBounds(28, 52, 143, 26);

		final Composite composite = DemoClientUIControlsFactory.createSeparator(container);
		composite.setBounds(30, 110, 706, 2);

		final Composite composite2 = DemoClientUIControlsFactory.createSeparator(container);
		composite2.setBounds(30, 214, 706, 2);

		final Composite composite3 = DemoClientUIControlsFactory.createSeparator(container);
		composite3.setBounds(30, 450, 706, 2);
	}
}
