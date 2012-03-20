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
package org.eclipse.riena.demo.client.model;

import java.util.List;

import org.eclipse.riena.demo.common.Email;

/**
 * contains emails
 */
public class EmailsResult {

	private List<Email> emails;

	/**
	 * @return the emails
	 */
	public List<Email> getEmails() {
		return emails;
	}

	/**
	 * @param emails
	 *            the emails to set
	 */
	public void setEmails(final List<Email> emails) {
		this.emails = emails;
	}

}
