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
package org.eclipse.riena.security.simpleservices.sessionservice.store;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.riena.security.common.session.Session;
import org.eclipse.riena.security.sessionservice.ISessionStore;
import org.eclipse.riena.security.sessionservice.SessionEntry;

/**
 * Store for sessions in the memory (<code>HashMap</code>s)
 */
public class MemoryStore implements ISessionStore {

	private final HashMap<String, SessionEntry> sessionTable = new HashMap<String, SessionEntry>();
	private final HashMap<Principal, SessionList> userTable = new HashMap<Principal, SessionList>();

	public synchronized SessionEntry read(final Session session) {
		return sessionTable.get(session.getSessionId());
	}

	public synchronized void write(final SessionEntry entry) {
		sessionTable.put(entry.getSession().getSessionId(), entry);
		for (final Principal p : entry.getPrincipals().toArray(new Principal[entry.getPrincipals().size()])) {
			SessionList sl = userTable.get(p);
			if (sl == null) {
				sl = new SessionList();
			}
			sl.addEntry(entry);
			userTable.put(p, sl);

		}
	}

	public synchronized void delete(final Session session) {
		// Assert.isTrue(session != null,"session must not be null" );

		final SessionEntry entry = sessionTable.get(session.getSessionId());
		if (entry == null) {
			return;
		}
		sessionTable.remove(session.getSessionId());
		for (final Principal p : entry.getPrincipals().toArray(new Principal[entry.getPrincipals().size()])) {
			final SessionList sl = userTable.get(p);
			sl.removeEntry(session);
		}
	}

	static class SessionList {
		private final HashMap<String, SessionEntry> sessions = new HashMap<String, SessionEntry>();
		private final static SessionEntry[] EMPTY_SESSION_ENTRIES = new SessionEntry[0];

		/**
		 * adds a sesion entry
		 * 
		 * @param entry
		 *            session entry
		 */
		public void addEntry(final SessionEntry entry) {
			sessions.put(entry.getSession().getSessionId(), entry);
		}

		/**
		 * removes the session entry for a session id
		 * 
		 * @param session
		 *            session id
		 */
		public void removeEntry(final Session session) {
			sessions.remove(session.getSessionId());
		}

		/**
		 * returns the session entries
		 * 
		 * @return array of session entries
		 */
		public SessionEntry[] entries() {
			if (sessions.size() == 0) {
				return EMPTY_SESSION_ENTRIES;
			}
			final Collection<SessionEntry> values = sessions.values();
			return values.toArray(new SessionEntry[values.size()]);
		}
	}
}
