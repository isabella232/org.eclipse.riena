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
package org.eclipse.riena.security.common.session;

/**
 * Implementation of ISessionHolder hold the Session object which identfies a
 * unique sessionId for the currently logged in user (subject).
 * 
 * The implementation of ISessionHolder is a singleton on the client and set
 * once when the user logs in (and is modified when prinicpals are added or
 * removed).
 * 
 * When doing remote service calls the Session is passed transparently to the
 * server where the ISessionHolder implementation is a threadlocal and keeps its
 * own session for each executing thread running.
 * 
 */
public interface ISessionHolder {

	/**
	 * Returns the current Session object
	 * 
	 * @return current session
	 */
	Session getSession();

	/**
	 * Sets the current Session
	 * 
	 * @param session
	 *            current session.
	 */
	void setSession(Session session);

	/**
	 * Set JSessionId
	 * 
	 * @param value
	 */
	void setJSessionCookieValue(String value);

	/**
	 * Return JSessionId
	 * 
	 * @return
	 */
	String getJSessionCookieValue();
}
