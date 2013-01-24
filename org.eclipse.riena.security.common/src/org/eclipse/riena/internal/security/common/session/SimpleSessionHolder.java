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
package org.eclipse.riena.internal.security.common.session;

import org.eclipse.riena.security.common.session.ISessionHolder;
import org.eclipse.riena.security.common.session.Session;

/**
 * <code>SimpleSessionHolder</code>. This implementation stores the session and
 * the principal location.
 * 
 */
public class SimpleSessionHolder implements ISessionHolder {

	private Session session;
	private String jSessionCookieValue = null;

	/**
	 * @see org.eclipse.riena.security.common.session.ISessionHolder#getSession()
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @see org.eclipse.riena.security.common.session.ISessionHolder#setSession(org.eclipse.riena.security.common.session.ISession)
	 */
	public void setSession(final Session session) {
		this.session = session;
	}

	/**
	 * @see org.eclipse.riena.security.common.session.ISessionHolder#setJSessionCookieValue(java.lang.String)
	 */
	public void setJSessionCookieValue(final String value) {
		this.jSessionCookieValue = value;
	}

	/**
	 * @see org.eclipse.riena.security.common.session.ISessionHolder#getJSessionCookieValue()
	 */
	public String getJSessionCookieValue() {
		return jSessionCookieValue;
	}

}
