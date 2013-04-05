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
package org.eclipse.riena.internal.security.authorizationservice;

import java.security.Permissions;
import java.security.Principal;

import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.security.authorizationservice.IPermissionStore;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;

/**
 * The default implementation for IAuthorizationService
 */
public class AuthorizationService implements IAuthorizationService {

	private IPermissionStore permStore;

	public AuthorizationService() {
		super();
	}

	@InjectService(useRanking = true)
	public void bind(final IPermissionStore permStore) {
		this.permStore = permStore;
	}

	public void unbind(final IPermissionStore permStore) {
		if (this.permStore == permStore) {
			this.permStore = null;
		}
	}

	public Permissions[] getPermissions(final Principal[] principals) {
		if (permStore != null) {
			final Permissions[] allPerms = new Permissions[principals.length];
			int i = 0;
			for (final Principal p : principals) {
				final Permissions perms = permStore.loadPermissions(p);
				allPerms[i++] = perms;
			}
			return allPerms;
		} else {
			return new Permissions[principals.length];
		}
	}

}
