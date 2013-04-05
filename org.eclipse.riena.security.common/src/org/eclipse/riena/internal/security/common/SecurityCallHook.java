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
package org.eclipse.riena.internal.security.common;

import java.util.Map;

import org.eclipse.riena.communication.core.hooks.CallContext;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.security.common.session.ISessionHolder;
import org.eclipse.riena.security.common.session.Session;

/**
 * This Call Hook deals with security issues of a web-service calls, it sets the
 * cookie of the session and principal location before the call and checks for
 * set-cookies after the call returns.
 * 
 */
public class SecurityCallHook implements ICallHook {

	private static final String SSOID = "x-compeople-ssoid"; //$NON-NLS-1$
	private ISessionHolder sessionHolder;

	public SecurityCallHook() {
		super();
	}

	@InjectService(useRanking = true)
	public void bind(final ISessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

	public void unbind(final ISessionHolder sessionHolder) {
		if (this.sessionHolder == sessionHolder) {
			this.sessionHolder = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.hooks.ICallHook#beforeCall(org.eclipse
	 * .riena.communication.core.hooks.CallContext)
	 */
	public void beforeCall(final CallContext callback) {
		final Session session = sessionHolder.getSession();

		if (session != null) {
			callback.setCookie(SSOID, session.getSessionId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.hooks.ICallHook#afterCall(org.eclipse
	 * .riena.communication.core.hooks.CallContext)
	 */
	public void afterCall(final CallContext callback) {
		final Map<String, String> map = callback.getSetCookies();
		if (map == null) {
			return;
		}
		final String temp = map.get(SSOID);
		if (temp != null) {
			sessionHolder.setSession(new Session(temp));
		}
	}
}
