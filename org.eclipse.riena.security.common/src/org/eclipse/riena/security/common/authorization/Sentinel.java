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

import java.security.Permission;

import org.eclipse.riena.core.singleton.SingletonProvider;
import org.eclipse.riena.core.wire.InjectService;

/**
 * This class can be used as an alternative to the Java SecurityManager if you
 * don't like to switch java security on but still like to check on business
 * permissions in the code. The Sentinel also says true or false which is easier
 * to handle than an AccessControlException.
 */
public final class Sentinel {

	private static final SingletonProvider<Sentinel> SENTINEL = new SingletonProvider<Sentinel>(Sentinel.class);

	private ISentinelService sentinelService;

	private Sentinel() {
	}

	private static Sentinel getInstance() {
		return SENTINEL.getInstance();
	}

	private ISentinelService getSentinelService() {
		return sentinelService;
	}

	@InjectService(useRanking = true)
	public void bind(final ISentinelService sentinelServiceParm) {
		sentinelService = sentinelServiceParm;
	}

	public void unbind(final ISentinelService sentinelServiceParm) {
		sentinelService = null;
	}

	/**
	 * checkAccess reads the current Subject from the SubjectHolderService,
	 * reads all its permissions from the cache and checks if this permission is
	 * allowed for this subject
	 * 
	 * @param permission
	 *            permission to be checked
	 * @return
	 */
	public static boolean checkAccess(final Permission permission) {
		if (SENTINEL.getInstance().getSentinelService() == null) {
			return false;
		}
		return getInstance().getSentinelService().checkAccess(permission);
	}

}
