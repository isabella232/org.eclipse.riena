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
package org.eclipse.riena.internal.security.authenticationservice;

import java.security.Principal;
import java.util.Set;

import javax.security.auth.callback.Callback;
import javax.security.auth.login.LoginException;

import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;

import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.authentication.AuthenticationFailure;
import org.eclipse.riena.security.common.authentication.AuthenticationTicket;
import org.eclipse.riena.security.common.authentication.Callback2CredentialConverter;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;
import org.eclipse.riena.security.common.session.ISessionHolder;
import org.eclipse.riena.security.common.session.Session;
import org.eclipse.riena.security.server.session.ISessionService;

/**
 * The <code>AuthenticationService</code> will perform the authentication
 * operations. This class contains the logic implementation of the methods
 * defined in the <code>IAuthenticationService</code> Interface. The real
 * authentication operations are linked into by a dynamically loaded
 * authentication module. The AuthenticationService itself just passes the
 * authentication attributes this module, makes some generic checks to the
 * credentials passed by the client and synchronizes the operations with the
 * session service.
 * 
 */
public class AuthenticationService implements IAuthenticationService {

	/**
	 * version ID (controlled by CVS)
	 */
	public static final String VERSION_ID = "$Id$"; //$NON-NLS-1$

	private ISessionService sessionService;
	private ISubjectHolder subjectHolder;
	private ISessionHolder sessionHolder;

	/**
	 * default constructor
	 * 
	 */
	public AuthenticationService() {
		super();
	}

	@InjectService(useRanking = true)
	public void bind(final ISessionService sessionService) {
		this.sessionService = sessionService;
	}

	public void unbind(final ISessionService sessionService) {
		if (this.sessionService == sessionService) {
			this.sessionService = null;
		}
	}

	@InjectService(useRanking = true)
	public void bind(final ISubjectHolder subjectHolder) {
		this.subjectHolder = subjectHolder;
	}

	public void unbind(final ISubjectHolder subjectHolder) {
		if (this.subjectHolder == subjectHolder) {
			this.subjectHolder = null;
		}
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

	public AuthenticationTicket login(final String loginContext, final AbstractCredential[] credentials) {
		try {
			final Callback[] callbacks = Callback2CredentialConverter.credentials2Callbacks(credentials);
			// create login context and login
			//			LoginContext lc = new LoginContext(loginContext, new MyCallbackHandler(callbacks));
			//			lc.login();
			final ILoginContext secureContext = LoginContextFactory.createContext(loginContext);
			AuthenticationServiceCallbackHandler.setCallbacks(callbacks);

			secureContext.login();
			// if login was successful, create session and add principals to
			// session
			final Set<Principal> principals = secureContext.getSubject().getPrincipals();
			// add only new principals to ticket (not the ones that might exist
			// in the session)
			final AuthenticationTicket ticket = new AuthenticationTicket();
			Session session = sessionHolder.getSession();
			if (session != null) {
				sessionService.invalidateSession(session);
			}
			final Principal[] pArray = principals.toArray(new Principal[principals.size()]);
			session = sessionService.generateSession(pArray);
			sessionHolder.setSession(session);
			for (final Principal p : principals) {
				ticket.getPrincipals().add(p);
			}
			// add session object to authentication ticket
			ticket.setSession(session);
			return ticket;
		} catch (final LoginException e) {
			throw new AuthenticationFailure("AuthenticationService login failed", e); //$NON-NLS-1$
		} finally {
			AuthenticationServiceCallbackHandler.setCallbacks(null);
		}
	}

	public void logout() {
		final Session session = sessionHolder.getSession();
		if (session == null) {
			throw new AuthenticationFailure("no valid session"); //$NON-NLS-1$
		}
		sessionService.invalidateSession(session);
		sessionHolder.setSession(null);
	}
}
