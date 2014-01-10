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
package org.eclipse.riena.security.common.authentication;

import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;

/**
 * The IAuthenticationService interface is a wrapper for the
 * AuthenticationService sitting remote on the server testing the login
 * credentials. Riena supplies an implementation with @see AuthenticationService
 * which will work well for most cases (where you can replace it with your own
 * implementation).
 * 
 * Behind the AuthenticationService the IAuthenticationStore which will be in
 * most cases a custom implementation which accesses the store with the
 * credentials.
 * 
 */
public interface IAuthenticationService {

	String WS_ID = "/AuthenticationService"; //$NON-NLS-1$

	/**
	 * Login with specified loginContext and credentials
	 * 
	 * @param loginContext
	 *            name of the equinox.security / JAAS security context
	 * @param credentials
	 *            list of credentials that need to be verified for a successful
	 *            login
	 * 
	 * @throws AuthenticationFailure
	 * 
	 * @return
	 */
	AuthenticationTicket login(String loginContext, AbstractCredential[] credentials) throws AuthenticationFailure;

	/**
	 * Logoff currently logged in user and invalidate current session
	 * 
	 * @throws AuthenticationFailure
	 */
	void logout() throws AuthenticationFailure;

}
