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
package org.eclipse.riena.demo.server;

import java.util.List;

import org.eclipse.riena.demo.common.Email;
import org.eclipse.riena.demo.common.IEmailService;

/**
 *
 */
public class EmailService implements IEmailService {

	private IEmailRepository emailRepository;

	public EmailService() {
		System.out.println("email service started"); //$NON-NLS-1$
	}

	public void bind(final IEmailRepository emailRepository) {
		this.emailRepository = emailRepository;
		System.out.println("email service: repository bound"); //$NON-NLS-1$
	}

	public void unbind(final IEmailRepository emailRepository) {
		this.emailRepository = null;
		System.out.println("email service: repository unbound"); //$NON-NLS-1$
	}

	public IEmailRepository getRepository() {
		return emailRepository;
	}

	/*
	 * @see org.eclipse.riena.demo.common.IEmailService#emailsList()
	 */
	public List<Email> showEmailsList(final String directoryName) {
		return emailRepository.emailsList(directoryName);
	}

	public List<Email> findEmailsForCustomer(final String emailAddress) {
		return emailRepository.findEmailsForCustomer(emailAddress);
	}

	/*
	 * @see
	 * org.eclipse.riena.demo.common.IEmailService#store(org.eclipse.riena.demo
	 * .common.Email)
	 */
	public boolean store(final Email email) {
		try {
			emailRepository.store(email);
			return true;
		} catch (final RuntimeException r) {
			return false;
		}
	}

}
