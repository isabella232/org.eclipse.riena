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
package org.eclipse.riena.internal.security.sessionservice;

import java.security.Principal;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.security.common.session.Session;
import org.eclipse.riena.security.server.session.ISessionService;
import org.eclipse.riena.security.server.session.SessionFailure;
import org.eclipse.riena.security.sessionservice.ISessionProvider;
import org.eclipse.riena.security.sessionservice.ISessionStore;
import org.eclipse.riena.security.sessionservice.SessionEntry;

/**
 * Class to maintain global sessions for user. Sessions are identified by id and
 * associated with a user (Principal).
 */
public class SessionService implements ISessionService {

	private ISessionStore store;
	private ISessionProvider sessionProvider;

	/**
	 * constructor
	 */
	public SessionService() {
		super();
	}

	@InjectService(useRanking = true)
	public void bind(final ISessionStore store) {
		this.store = store;
	}

	public void unbind(final ISessionStore store) {
		if (this.store == store) {
			this.store = null;
		}
	}

	@InjectService(useRanking = true)
	public void bind(final ISessionProvider provider) {
		this.sessionProvider = provider;
	}

	public void unbind(final ISessionProvider provider) {
		if (this.sessionProvider == provider) {
			this.sessionProvider = null;
		}
	}

	/**
	 * create a local session controller with a specific store
	 * 
	 * @param store
	 * @param sessionProvider
	 */
	public SessionService(final ISessionStore store, final ISessionProvider sessionProvider) {
		super();
		this.store = store;
		this.sessionProvider = sessionProvider;
	}

	public Session generateSession(final Principal[] principals) {
		Assert.isNotNull(store, "store instance is null"); //$NON-NLS-1$

		final Session session = sessionProvider.createSession(principals);

		if (session == null) {
			throw new SessionFailure("internal error, generating a session failed"); //$NON-NLS-1$
		}
		final SessionEntry entry = new SessionEntry(session, principals);
		store.write(entry);
		return session;
	}

	public Principal[] findPrincipals(final Session session) {
		// Assert.isNotNull( session,"session is not null");
		final SessionEntry entry = store.read(session);
		if (entry == null) {
			return null;
		}
		return entry.getPrincipals().toArray(new Principal[entry.getPrincipals().size()]);
	}

	public boolean isValidSession(final Session session) {
		final SessionEntry entry = store.read(session);
		return entry != null && entry.getValid();
	}

	public boolean hasSession(final Session session) {
		final SessionEntry entry = store.read(session);
		return entry != null;
	}

	public void invalidateSession(final Session session) {
		store.delete(session);
	}

}
