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
package org.eclipse.riena.security.common.authentication;

import java.security.Principal;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.login.LoginException;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.security.common.Activator;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;

/**
 * 
 */
public class RemoteLoginProxy {

	private IAuthenticationService authenticationService;
	private ISubjectHolder subjectHolder;
	private final String loginContext;
	private AuthenticationTicket ticket;
	private final Subject subject;

	public RemoteLoginProxy(final String loginContext, final Subject subject) {
		super();
		this.loginContext = loginContext;
		this.subject = subject;
		Inject.service(IAuthenticationService.class).useRanking().into(this)
				.andStart(Activator.getDefault().getContext());
		Inject.service(ISubjectHolder.class).useRanking().into(this).andStart(Activator.getDefault().getContext());
	}

	public void bind(final IAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void unbind(IAuthenticationService authenticationService) {
		if (this.authenticationService == authenticationService) {
			authenticationService = null;
		}
	}

	/**
	 * @since 2.0
	 */
	public void bind(final ISubjectHolder subjectHolderService) {
		this.subjectHolder = subjectHolderService;
	}

	/**
	 * @since 2.0
	 */
	public void unbind(final ISubjectHolder subjectHolderService) {
		if (this.subjectHolder == subjectHolderService) {
			this.subjectHolder = null;
		}
	}

	public boolean login(final Callback[] callbacks) throws LoginException {
		try {
			final AbstractCredential[] creds = Callback2CredentialConverter.callbacks2Credentials(callbacks);
			if (authenticationService == null) {
				throw new AuthenticationFailure("no authentication service"); //$NON-NLS-1$
			}
			ticket = authenticationService.login(loginContext, creds);
			for (final Principal principal : ticket.getPrincipals()) {
				subject.getPrincipals().add(principal);
			}
			subjectHolder.setSubject(subject);
			return true;
		} catch (final AuthenticationFailure failure) {
			throw new LoginException(failure.getMessage());
		}
	}

	public boolean commit() {
		final Set<Principal> pSet = subject.getPrincipals();
		for (final Principal p : ticket.getPrincipals()) {
			pSet.add(p);
		}
		subjectHolder.setSubject(subject);
		return true;
	}

	public boolean logout() throws LoginException {
		try {
			authenticationService.logout();
			subject.getPrincipals().clear();
		} catch (final AuthenticationFailure e) {
			throw new LoginException(e.getMessage());
		}
		return true;
	}
}
