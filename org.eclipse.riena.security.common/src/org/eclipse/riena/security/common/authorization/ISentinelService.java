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
package org.eclipse.riena.security.common.authorization;

import java.security.Permission;

/**
 * Interface for an SentinelService (an OSGi Service) that is call by the
 * Sentinel and checks whether the current user has enough permission for a
 * certain operation.
 */
public interface ISentinelService {

	/**
	 * Checks that the currently logged in user (identified by the Subject in
	 * the SubjectHolder) @see ISubjectHolder has the rights for the permission
	 * passed as parameter.
	 * 
	 * @param permission
	 *            permission to check for the user
	 * @return true if the user has enough access right, otherwise false
	 */
	boolean checkAccess(Permission permission);

}
