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
package org.eclipse.riena.internal.security.common.session;

import org.eclipse.riena.security.common.session.ISessionHolder;
import org.eclipse.riena.security.common.session.Session;

public class SimpleThreadedSessionHolder implements ISessionHolder {

	private final ThreadLocal<ISessionHolder> sessionHolders = new ThreadLocal<ISessionHolder>() {
		@Override
		protected ISessionHolder initialValue() {
			return new SimpleSessionHolder();
		}
	};

	public void setSession(final Session session) {
		sessionHolders.get().setSession(session);
	}

	public Session getSession() {
		return sessionHolders.get().getSession();
	}

	public void setJSessionCookieValue(final String value) {
		sessionHolders.get().setJSessionCookieValue(value);
	}

	public String getJSessionCookieValue() {
		return sessionHolders.get().getJSessionCookieValue();
	}

}
