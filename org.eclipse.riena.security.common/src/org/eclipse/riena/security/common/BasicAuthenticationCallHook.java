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
package org.eclipse.riena.security.common;

import java.security.Principal;
import java.util.Iterator;

import javax.security.auth.Subject;

import org.eclipse.riena.communication.core.hooks.CallContext;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.Base64;
import org.eclipse.riena.internal.security.common.Activator;

/**
 * This optional hook implements authentication by
 */
public class BasicAuthenticationCallHook implements ICallHook {

	private ISubjectHolder subjectHolder = null;

	public BasicAuthenticationCallHook() {
		super();
		Inject.service(ISubjectHolder.class).useRanking().into(this).andStart(Activator.getDefault().getContext());
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
		this.subjectHolder = null;
	}

	public void afterCall(final CallContext context) {
		// no action necessary
	}

	public void beforeCall(final CallContext context) {
		if (subjectHolder == null) {
			return;
		}
		final Subject subject = subjectHolder.getSubject();
		if (subject == null) {
			return;
		}
		Object credential = null;
		final Iterator<Object> iterator = subject.getPrivateCredentials().iterator();
		if (iterator.hasNext()) {
			credential = iterator.next();
		}
		final String password = credential instanceof String ? (String) credential : ""; //$NON-NLS-1$
		for (final Principal principal : subject.getPrincipals()) {
			final String useridPlusPassword = principal.getName() + ":" + password; //$NON-NLS-1$
			final String authorizationInBase64 = new String(Base64.encode(useridPlusPassword.getBytes()));
			context.getMessageContext().addRequestHeader("Authorization", "Basic " + authorizationInBase64); //$NON-NLS-1$//$NON-NLS-2$
		}
	}
}
