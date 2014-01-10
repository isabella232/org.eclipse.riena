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
package org.eclipse.riena.security.common.authorization;

import java.security.Permissions;
import java.security.Principal;

import javax.security.auth.Subject;

/**
 * A cache that keeps Permissions for a certain period of time. It has no
 * functionality by itself but is meant to speed access to permissions for a
 * user, especially if permissions are checked frequently very often for the
 * same user.
 */
public interface IPermissionCache {

	/**
	 * Check if the cache contains a Permissions collection for this principal
	 * 
	 * @param principal
	 *            principal for which the cache is checked
	 * @return
	 */
	Permissions getPermissions(Principal principal);

	/**
	 * Adds new entries into the permission cache
	 * 
	 * @param principal
	 *            principal for which to add permissions
	 * @param permissions
	 *            Permissions collection to add to the cache
	 */
	void putPermissions(Principal principal, Permissions permissions);

	/**
	 * Purge Permissions for a Principal instance
	 * 
	 * @param principal
	 *            principal for which all permissions are removed from the cache
	 */
	void purgePermissions(Principal principal);

	/**
	 * Purge Permissions for a Subject instance which then removes the
	 * permissions for all principals of this Subject
	 * 
	 * @param subject
	 *            subject for which to remove all permissions
	 */
	void purgePermissions(Subject subject);

}
