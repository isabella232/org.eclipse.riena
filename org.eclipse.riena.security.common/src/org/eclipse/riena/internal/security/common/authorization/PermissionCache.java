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
package org.eclipse.riena.internal.security.common.authorization;

import java.security.Permissions;
import java.security.Principal;

import javax.security.auth.Subject;

import org.eclipse.riena.core.cache.GenericObjectCache;
import org.eclipse.riena.core.util.ContainerModel;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.security.common.authorization.IPermissionCache;

public class PermissionCache implements IPermissionCache {

	private final GenericObjectCache<String, Permissions> permCache = new GenericObjectCache<String, Permissions>();

	public PermissionCache() {
		super();
		permCache.setName("PermissionCache"); //$NON-NLS-1$
		if (ContainerModel.isClient()) {
			permCache.setMinimumSize(1);
			permCache.setTimeout(999999000); // client permissions nearly
			// never timeout
		} else {
			permCache.setMinimumSize(100);
			permCache.setTimeout(360000);
		}
	}

	public Permissions getPermissions(final Principal principal) {
		return permCache.get(principal.getName());
	}

	public void putPermissions(final Principal principal, final Permissions permissions) {
		permCache.put(principal.getName(), permissions);

	}

	public void purgePermissions(final Subject subject) {
	}

	public void purgePermissions(final Principal principal) {
	}

	@InjectExtension(min = 0, max = 1)
	public void update(final IPermissionCacheExtension permissionCacheExtension) {
		if (permissionCacheExtension != null) {
			permCache.setMinimumSize(permissionCacheExtension.getMinimumSize());
			final int timeout = permissionCacheExtension.getTimeout();
			permCache.setTimeout(timeout == -1 ? Integer.MAX_VALUE : timeout);
		}
	}
}
