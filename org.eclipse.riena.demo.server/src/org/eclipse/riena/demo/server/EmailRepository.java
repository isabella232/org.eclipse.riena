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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.eclipse.riena.demo.common.Email;

/**
 *
 */
public class EmailRepository implements IEmailRepository {

	private final List<Email> emails = new ArrayList<Email>();
	private List<String> emailAddresses = new ArrayList<String>();
	private String myEmailAddress;
	private List<String> emailSubjects = new ArrayList<String>();
	private List<String> emailBodies = new ArrayList<String>();
	private final Random random = new Random();

	/**
	 * 
	 */
	public EmailRepository() {
		initEmailGenerator();
		init();
	}

	/**
	 * initialize email generator; store date used for generating emails
	 */
	private void initEmailGenerator() {
		emailAddresses = new ArrayList<String>(Arrays.asList(new String[] { "Josef.Mundl@mail.org", //$NON-NLS-1$
				"Robert.Muster@mail.org", "Trulli.Muster-Maier@mail.org", "Elfriede.Mustermann@mail.org", //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
				"Ingo.Mustermann@mail.org", "Max.Mustermann@mail.org" })); //$NON-NLS-1$//$NON-NLS-2$
		myEmailAddress = "riena@eclipse.org"; //$NON-NLS-1$
		emailSubjects = new ArrayList<String>(Arrays.asList(new String[] { "For your information", "Your request", //$NON-NLS-1$ //$NON-NLS-2$
				"Your order", "Your monthly Newsletter", "The information to your order", "How to invest properly" })); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
		emailBodies = new ArrayList<String>(
				Arrays.asList(new String[] {
						"This email is just for your information...", //$NON-NLS-1$
						"Here is the information you asked for...", //$NON-NLS-1$
						"Hello! Send me please the missing data needed for your order completion.", //$NON-NLS-1$
						"Here you find the latest highlights...", //$NON-NLS-1$
						"Specially for you! This email contains the actual data to your account and the new offers.", //$NON-NLS-1$
						"The following range of products may be of interest for you: insurance police, investment offers.. ", })); //$NON-NLS-1$

	}

	/**
	 * generate emails
	 */
	private void init() {
		Email email = new Email();

		for (int i = 0; i < 7; i++) {

			final int number = random.nextInt(5); // 0 <= number < 5
			email = new Email();
			email.setEmailFrom(emailAddresses.get(number));
			email.setEmailTo(myEmailAddress);
			email.setEmailSubject(emailSubjects.get(number));
			final long currentTime = System.currentTimeMillis();
			final long emailDate = nextEmailDate(currentTime);
			email.setEmailDate(new Date(emailDate));
			email.setEmailBody(emailBodies.get(number));
			email.setDirectoryName("Inbox"); //$NON-NLS-1$
			emails.add(email);
		}

		for (int i = 0; i < 2; i++) {

			final int number = random.nextInt(6); // 0 <= number < 5
			email = new Email();
			email.setEmailFrom(myEmailAddress);
			email.setEmailTo(emailAddresses.get(random.nextInt(5)));
			email.setEmailSubject(emailSubjects.get(number));
			final long currentTime = System.currentTimeMillis();
			final long emailDate = nextEmailDate(currentTime);
			email.setEmailDate(new Date(emailDate));
			email.setEmailBody(emailBodies.get(number));
			email.setDirectoryName("Draft"); //$NON-NLS-1$
			emails.add(email);
		}

		for (int i = 0; i < 5; i++) {

			final int number = random.nextInt(5); // 0 <= number < 5
			email = new Email();
			email.setEmailFrom(myEmailAddress);
			email.setEmailTo(emailAddresses.get(random.nextInt(5)));
			email.setEmailSubject(emailSubjects.get(number));
			final long currentTime = System.currentTimeMillis();
			final long emailDate = nextEmailDate(currentTime);
			email.setEmailDate(new Date(emailDate));
			email.setEmailBody(emailBodies.get(number));
			email.setDirectoryName("Sent"); //$NON-NLS-1$
			emails.add(email);
		}

	}

	private long nextEmailDate(final long currentTime) {
		final long result = currentTime - random.nextInt(1000000000);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.demo.server.IEmailRepository#emailsList()
	 */
	public List<Email> emailsList(final String directoryName) {
		final ArrayList<Email> result = new ArrayList<Email>();
		for (final Email email : emails) {
			if (email.getDirectoryName().equals(directoryName)) {
				result.add(email);
			}
		}
		return result;
	}

	public List<Email> findEmailsForCustomer(final String emailAddress) {
		final ArrayList<Email> result = new ArrayList<Email>();
		for (final Email email : emails) {
			if (email.getEmailFrom().equals(emailAddress) || email.getEmailTo().equals(emailAddress)) {
				result.add(email);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.demo.server.IEmailRepository#store(org.eclipse.riena
	 * .demo.common.Email)
	 */
	public void store(final Email email) {
		emails.add(email);

	}

}
