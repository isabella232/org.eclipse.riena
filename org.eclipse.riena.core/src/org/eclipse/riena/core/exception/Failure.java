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
package org.eclipse.riena.core.exception;

import java.util.Date;

/**
 * Exception for handling of non-recoverable errors/problems (resource
 * unavailability, runtime exceptions and other system errors). This class
 * extends <code>java.lang.RuntimeException</code> and is therefore an unchecked
 * exception.
 * 
 * @see java.lang.RuntimeException
 */
public abstract class Failure extends RuntimeException {

	private static final long serialVersionUID = -1945022495993627769L;

	private Date timestamp;

	/**
	 * constructor.
	 * 
	 * @param msg
	 *            message text or message code.
	 */
	public Failure(final String msg) {
		super(msg);
		setDetails();
	}

	/**
	 * constructor.
	 * 
	 * @param cause
	 *            exception which has caused this Failure.
	 * @param msg
	 *            message text or message code.
	 */
	public Failure(final String msg, final Throwable cause) {
		super(msg, cause);
		setDetails();
	}

	/**
	 * determine and set the exception details.
	 */
	private void setDetails() {
		setTimestamp(new Date());
	}

	/**
	 * Set value of attribute <code>timestamp</code>.
	 * 
	 * @param timestamp
	 *            parameter value of timestamp.
	 */
	private void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Get value of attribute <code>timestamp</code>.
	 * 
	 * @return value of attribute timestamp.
	 */
	public Date getTimestamp() {
		return new Date(this.timestamp.getTime());
	}

}
