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
package org.eclipse.riena.security.server.session;

import java.security.Principal;

import org.eclipse.riena.security.common.session.Session;

/**
 * The ISessionService interfaces describes the methods for maintaining global
 * sessions for user. Sessions are identified by id and associated with a user
 * (Principal). Sessions may exists in different zones (secure and high securly
 * zone) and are linked through some "magic" that lies in the implementation of
 * the service.
 * 
 */
public interface ISessionService {

	String WS_ID = "/SessionService"; //$NON-NLS-1$

	/**
	 * Generates a new sessionid for a user with given credentials
	 * 
	 * @param principals
	 *            principal for which a session is generated
	 * @param credential
	 *            credential of the principal
	 * @return ISession the generated session object
	 */
	Session generateSession(Principal[] principals);

	/**
	 * Finds a userid for a specific session
	 * 
	 * @param session
	 *            session for which to search
	 * @return Principal principal (null if session does not exist)
	 */
	Principal[] findPrincipals(Session session);

	/**
	 * Checks whether a session exists and can be used in the zone associated
	 * with this service
	 * 
	 * @param session
	 *            session to check
	 * @return true if session is valid for this zone
	 */
	boolean isValidSession(Session session);

	/**
	 * Checks whether this session exists. If true this session can be forward
	 * to the next deeper level. This method can return true even if
	 * "isValidSession" is false.
	 * 
	 * @param session
	 *            session to check
	 * @return true if this session exists, can be invalid but maybe its valid
	 *         in a "deeper" zone.
	 */
	boolean hasSession(Session session);

	/**
	 * Invalidates the session in the component
	 * 
	 * @param session
	 *            session to invalidate
	 */
	void invalidateSession(Session session);

}
