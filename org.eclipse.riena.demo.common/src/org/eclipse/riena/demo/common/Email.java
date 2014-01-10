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
package org.eclipse.riena.demo.common;

import java.util.Date;

public class Email {

	private String emailFrom;
	private String emailTo;
	private String emailSubject;
	private Date emailDate;
	private String emailBody;
	private String directoryName;

	public Email() {
		emailFrom = ""; //$NON-NLS-1$
		setEmailTo(""); //$NON-NLS-1$
		emailSubject = ""; //$NON-NLS-1$
		emailDate = null;
		emailBody = ""; //$NON-NLS-1$
		directoryName = null;
	}

	/**
	 * @return the emailFrom
	 */
	public String getEmailFrom() {
		return emailFrom;
	}

	/**
	 * @param emailFrom
	 *            the emailFrom to set
	 */
	public void setEmailFrom(final String emailFrom) {
		this.emailFrom = emailFrom;
	}

	/**
	 * @param emailTo
	 *            the emailTo to set
	 */
	public void setEmailTo(final String emailTo) {
		this.emailTo = emailTo;
	}

	/**
	 * @return the emailTo
	 */
	public String getEmailTo() {
		return emailTo;
	}

	/**
	 * @return the emailSubject
	 */
	public String getEmailSubject() {
		return emailSubject;
	}

	/**
	 * @param emailSubject
	 *            the emailSubject to set
	 */
	public void setEmailSubject(final String emailSubject) {
		this.emailSubject = emailSubject;
	}

	/**
	 * @return the emailDate
	 */
	public Date getEmailDate() {
		return emailDate;
	}

	/**
	 * @param emailDate
	 *            the emailDate to set
	 */
	public void setEmailDate(final Date emailDate) {
		this.emailDate = emailDate;
	}

	/**
	 * @return the emailBody
	 */
	public String getEmailBody() {
		return emailBody;
	}

	/**
	 * @param emailBody
	 *            the emailBody to set
	 */
	public void setEmailBody(final String emailBody) {
		this.emailBody = emailBody;
	}

	/**
	 * @param directoryName
	 *            the directoryName to set
	 */
	public void setDirectoryName(final String directoryName) {
		this.directoryName = directoryName;
	}

	/**
	 * @return the directoryName
	 */
	public String getDirectoryName() {
		return directoryName;
	}

}
