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
package org.eclipse.riena.internal.security.common;

import java.security.Permission;
import java.security.Permissions;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;

import javax.security.auth.Subject;

import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.common.authorization.IPermissionCache;
import org.eclipse.riena.security.common.authorization.ISentinelService;

/**
 * An implementation of the {@code ISentinelService} which will be registered as 'default' OSGi service that can be overwritten.
 */
public class SentinelServiceImpl implements ISentinelService {

	private IPermissionCache permCache;
	private ISubjectHolder subjectHolder;
	private IAuthorizationService authService;

	@InjectService
	public void bind(final IPermissionCache permCache) {
		this.permCache = permCache;
	}

	public void unbind(final IPermissionCache permCache) {
		if (permCache == this.permCache) {
			this.permCache = null;
		}
	}

	@InjectService
	public void bind(final ISubjectHolder subjectHolder) {
		this.subjectHolder = subjectHolder;
	}

	public void unbind(final ISubjectHolder subjectHolder) {
		if (subjectHolder == this.subjectHolder) {
			this.subjectHolder = null;
		}
	}

	@InjectService
	public void bind(final IAuthorizationService authService) {
		this.authService = authService;
	}

	public void unbind(final IAuthorizationService authService) {
		if (authService == this.authService) {
			this.authService = null;
		}
	}

	/**
	 * Check whether the current user is granted the given {@code permission}.
	 * 
	 * @param permission
	 *            permission to be checked
	 * @return on true permission granted; otherwise false
	 */
	public boolean checkAccess(final Permission permission) {
		final Subject subject = getSubjectHolder().getSubject();
		if (subject != null) {
			final Permissions permissions = getPermissions(subject);
			final boolean result = permissions.implies(permission);
			return result;
		} else {
			return false;
		}
	}

	protected IPermissionCache getPermissionCache() {
		return permCache;
	}

	protected ISubjectHolder getSubjectHolder() {
		return subjectHolder;
	}

	/**
	 * Get Permissions for a specific Subject
	 * 
	 * @param subject
	 * @return
	 */
	private Permissions getPermissions(final Subject subject) {
		final Set<Principal> principals = subject.getPrincipals();
		final Permissions allPerms = new Permissions();
		final ArrayList<Principal> missingPrincipals = new ArrayList<Principal>();
		final IPermissionCache thePermCache = getPermissionCache();

		// iterate over the principals in the subject and try to find an entry in the PermissionCache
		// add principals for which there are no permissions into the missingPrincipals ArrayList
		for (final Principal principal : principals) {
			final Permissions perms = thePermCache.getPermissions(principal);
			if (perms == null) {
				missingPrincipals.add(principal);
			} else {
				// if we find permissions add them to the pool of permissions
				final Enumeration<Permission> permEnum = perms.elements();
				while (permEnum.hasMoreElements()) {
					allPerms.add(permEnum.nextElement());
				}
			}
		}

		// if there are principals with no permissions, retrieve them from the server
		if (missingPrincipals.size() > 0) {
			if (authService == null) {
				return new Permissions();
			}
			final Permissions[] permissionsArray = authService.getPermissions(missingPrincipals.toArray(new Principal[missingPrincipals.size()]));
			for (int i = 0; i < missingPrincipals.size(); i++) {
				thePermCache.putPermissions(missingPrincipals.get(i), permissionsArray[i]);
				final Enumeration<Permission> permEnum = permissionsArray[i].elements();
				while (permEnum.hasMoreElements()) {
					allPerms.add(permEnum.nextElement());
				}
			}
		}
		return allPerms;
	}

}
