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
package org.eclipse.riena.security.common;

import javax.security.auth.Subject;

/**
 * A <code>ISubjectHolder</code> implementation holds the current subject, i.e.
 * on the client the currently logged on user and on the server the user bound
 * to the current session.
 * 
 */
public interface ISubjectHolder {

	/**
	 * Return the current subject (logged in user)
	 * 
	 * @return current subject
	 */
	Subject getSubject();

	/**
	 * Set the current subject (logged in user)
	 * 
	 * @param subject
	 *            current subject
	 */
	void setSubject(Subject subject);

	/**
	 * Adds the subject change listener
	 * 
	 * @param listener
	 */
	void addSubjectChangeListener(ISubjectChangeListener listener);

	/**
	 * Removes the subject change listener
	 * 
	 * @param listener
	 */
	void removeSubjectChangeListener(ISubjectChangeListener listener);

}
