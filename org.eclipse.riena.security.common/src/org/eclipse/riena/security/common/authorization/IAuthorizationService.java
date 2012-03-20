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
package org.eclipse.riena.security.common.authorization;

import java.security.Permissions;
import java.security.Principal;

/**
 * The IAuthorizationService is a wrapper for the AuthorizationService remote
 * service that sits on the server. Riena supplies a default implementation
 * which will work in most cases @see AuthorizationService (which is when you
 * replace it with your own implementation).
 * 
 * The AuthorizationService will then access the IPermissionStore which needs a
 * custom implementation to access the permissions for a list of principals.
 */
public interface IAuthorizationService {

	String WS_ID = "/AuthorizationService"; //$NON-NLS-1$

	/**
	 * Returns a list of JavaPermissions that a subject (logged in user)
	 * identified by a list of Principal instances has.
	 * 
	 * @param principals
	 *            list of Principal instances that identify the logged in user
	 * @return list of permissions that this user has
	 */
	Permissions[] getPermissions(Principal[] principals);

}
