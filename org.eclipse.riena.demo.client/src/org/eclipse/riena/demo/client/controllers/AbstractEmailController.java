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
package org.eclipse.riena.demo.client.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.riena.beans.common.TypedComparator;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.demo.client.model.EmailsResult;
import org.eclipse.riena.demo.common.Customer;
import org.eclipse.riena.demo.common.Email;
import org.eclipse.riena.demo.common.ICustomerService;
import org.eclipse.riena.demo.common.IEmailService;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.annotation.OnActionCallback;

/**
 * abstract email controller A
 */
public class AbstractEmailController extends SubModuleController {

	protected IEmailService mailDemoService;
	protected Email selectedEmail;
	private ICustomerService customerDemoService;
	protected EmailsResult emailsResult = new EmailsResult();

	@InjectService(useRanking = true)
	public void bind(final IEmailService mailDemoService) {
		this.mailDemoService = mailDemoService;
	}

	public void unbind(final IEmailService mailDemoService) {
		this.mailDemoService = null;
	}

	@InjectService(useRanking = true)
	public void bind(final ICustomerService customerDemoService) {
		this.customerDemoService = customerDemoService;
	}

	public void unbind(final ICustomerService customerDemoService) {
		this.customerDemoService = null;
	}

	@Override
	public void configureRidgets() {
		final ITableRidget emails = getRidget(ITableRidget.class, "emailsTable"); //$NON-NLS-1$
		final ILabelRidget emailSubject = getRidget(ILabelRidget.class, "emailSubject"); //$NON-NLS-1$
		final ILabelRidget emailFrom = getRidget(ILabelRidget.class, "emailFrom"); //$NON-NLS-1$
		final ILabelRidget emailTo = getRidget(ILabelRidget.class, "emailTo"); //$NON-NLS-1$
		final ILabelRidget emailDate = getRidget(ILabelRidget.class, "emailDate"); //$NON-NLS-1$
		final ITextRidget emailBody = getRidget(ITextRidget.class, "emailBody"); //$NON-NLS-1$

		emails.setComparator(3, new TypedComparator<Date>());
		emails.addPropertyChangeListener(new PropertyChangeListener() {

			public void propertyChange(final PropertyChangeEvent evt) {
				if (evt.getPropertyName() == "selection") { //$NON-NLS-1$
					selectedEmail = (Email) emails.getSelection().get(0);
					emailSubject.setText(selectedEmail.getEmailSubject());
					emailFrom.setText(selectedEmail.getEmailFrom());
					emailBody.setText(selectedEmail.getEmailBody());
					emailTo.setText(selectedEmail.getEmailTo());
					DateFormat formatter;
					formatter = new SimpleDateFormat("E dd.MM.yyyy HH:mm"); //$NON-NLS-1$
					emailDate.setText(formatter.format(selectedEmail.getEmailDate()));

				}
			}
		});

		if (getNavigationNode().isJumpTarget()) {
			final IActionRidget openCustomerAction = getRidget(IActionRidget.class, "openCustomer"); //$NON-NLS-1$
			openCustomerAction.setText("Back to Customer"); //$NON-NLS-1$
		}
	}

	@OnActionCallback(ridgetId = "openCustomer")
	protected void openCustomer() {
		if (getNavigationNode().isJumpTarget()) {
			getNavigationNode().jumpBack();
			getNavigationNode().getParent().dispose();
		} else {
			if (selectedEmail != null) {
				final String selectedEmailAddress = openCustomerWithEmailAddress();
				if (selectedEmailAddress != null) {
					final Customer customer = customerDemoService.findCustomerWithEmailAddress(selectedEmailAddress);

					System.out.println("customer " + customer); //$NON-NLS-1$

					if (customer != null) {
						getNavigationNode().navigate(new NavigationNodeId("riena.demo.client.CustomerRecord", selectedEmailAddress), //$NON-NLS-1$
								new NavigationArgument(customer));
					}
				}
			}
		}

	}

	/**
	 * @return the email address of the customer that should be opened
	 */
	protected String openCustomerWithEmailAddress() {
		return selectedEmail.getEmailFrom();
	}
}
