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
package org.eclipse.riena.security.sessionservice;

import java.security.Principal;
import java.security.SecureRandom;

import org.eclipse.riena.security.common.session.Session;

/**
 * This Provider constructs a <code>Session</code> object.
 * 
 */
public class SessionProvider implements ISessionProvider {

	private final SecureRandom random;

	/**
	 * Creates a new instance of <code>SessionProvider</code>
	 */
	public SessionProvider() {
		super();
		random = new SecureRandom();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.security.sessionservice.ISessionProvider#createSession
	 * (java.security.Principal[])
	 */
	public Session createSession(final Principal[] principals) {
		// we could use information from the principal parameter here
		return new Session("ssoid##" + Long.valueOf(random.nextLong() + System.currentTimeMillis()).toString() + "##"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
