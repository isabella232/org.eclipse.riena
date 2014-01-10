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
package org.eclipse.riena.security.sessionservice;

import java.security.Principal;

import org.eclipse.riena.security.common.session.Session;

/**
 * This Provider allows to supply your own implementation to construct the
 * <code>Session</code> object. See comment below. You always have to construct
 * the <code>Session</code> object, however you can bring in your own algorithm
 * for supplying the ID.
 * 
 */
public interface ISessionProvider {

	/**
	 * The method returns an Session object for the list of Principal instances
	 * that are supplied. The Session instance must be unique in the
	 * application. If a custom implementation of ISessionProvider is provided
	 * you can add your own algorithm to calculate a unique sessionId for the
	 * Session instance.
	 * 
	 * @param principals
	 *            list of Principal object for which the Session object is
	 *            created
	 * @return session session object that is created.
	 */
	Session createSession(Principal[] principals);

}
