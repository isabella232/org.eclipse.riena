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
package org.eclipse.riena.security.authorizationservice;

import java.security.Permissions;
import java.security.Principal;

/**
 * Implementations of IPermissionStore are used by the AuthorizationService to
 * retrieve the Permissions for a specific Principal
 * 
 */
public interface IPermissionStore {

	/**
	 * Returns the permissions for one principal
	 * 
	 * @param principal
	 * @return
	 */
	Permissions loadPermissions(Principal principal);

}
