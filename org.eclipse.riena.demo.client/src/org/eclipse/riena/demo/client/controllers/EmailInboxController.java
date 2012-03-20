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
package org.eclipse.riena.demo.client.controllers;

import java.util.Date;
import java.util.List;

import org.eclipse.riena.demo.common.Email;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.DateColumnFormatter;

/**
 *
 */
public class EmailInboxController extends AbstractEmailController {
	@Override
	public void configureRidgets() {
		super.configureRidgets();
		final ITableRidget emails = getRidget(ITableRidget.class, "emailsTable"); //$NON-NLS-1$

		final String[] columnHeaders = { "From", "Subject", "Date" }; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		final String[] columnPropertyNames = { "emailFrom", "emailSubject", "emailDate" }; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		emails.bindToModel(emailsResult, "emails", Email.class, columnPropertyNames, columnHeaders); //$NON-NLS-1$

		emails.setColumnFormatter(2, new DateColumnFormatter("dd.MMM. HH:mm") { //$NON-NLS-1$
					@Override
					protected Date getDate(final Object element) {
						return ((Email) element).getEmailDate();
					}
				});

		final List<Email> inboxEmailList = mailDemoService.showEmailsList("Inbox"); //$NON-NLS-1$

		emailsResult.setEmails(inboxEmailList);
		getRidget(ITableRidget.class, "emailsTable").updateFromModel(); //$NON-NLS-1$
	}

}
