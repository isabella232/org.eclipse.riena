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
package org.eclipse.riena.internal.demo.server;

import org.osgi.framework.BundleContext;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Plugin;

import org.eclipse.riena.communication.publisher.Publish;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.demo.common.ICustomerService;
import org.eclipse.riena.demo.common.IEmailService;
import org.eclipse.riena.demo.server.CustomerRepository;
import org.eclipse.riena.demo.server.CustomerService;
import org.eclipse.riena.demo.server.EmailRepository;
import org.eclipse.riena.demo.server.EmailService;
import org.eclipse.riena.demo.server.ICustomerRepository;
import org.eclipse.riena.demo.server.IEmailRepository;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.demo.server"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		// Customer Service
		final CustomerService customerService = new CustomerService();
		context.registerService(ICustomerService.class.getName(), customerService, null);
		Publish.service(ICustomerService.class).usingPath(ICustomerService.WS_ID).withProtocol("hessian").andStart( //$NON-NLS-1$
				context);

		Inject.service(ICustomerRepository.class).into(customerService).andStart(context);

		context.registerService(ICustomerRepository.class.getName(), new CustomerRepository(), null);

		// checks
		Assert.isNotNull(context.getServiceReference(ICustomerService.class.getName()));
		Assert.isNotNull(context.getService(context.getServiceReference(ICustomerService.class.getName())));
		Assert.isNotNull(context.getServiceReference(ICustomerRepository.class.getName()));
		Assert.isNotNull(context.getService(context.getServiceReference(ICustomerRepository.class.getName())));
		Assert.isNotNull(((CustomerService) context.getService(context.getServiceReference(ICustomerService.class
				.getName()))).getRepository());

		// Email Service
		final EmailService emailService = new EmailService();
		context.registerService(IEmailService.class.getName(), emailService, null);
		Publish.service(IEmailService.class).usingPath(IEmailService.WS_ID).withProtocol("hessian").andStart( //$NON-NLS-1$
				context);

		Inject.service(IEmailRepository.class).into(emailService).andStart(context);

		context.registerService(IEmailRepository.class.getName(), new EmailRepository(), null);

		// checks
		Assert.isNotNull(context.getServiceReference(IEmailService.class.getName()));
		Assert.isNotNull(context.getService(context.getServiceReference(IEmailService.class.getName())));
		Assert.isNotNull(context.getServiceReference(IEmailRepository.class.getName()));
		Assert.isNotNull(context.getService(context.getServiceReference(IEmailRepository.class.getName())));
		Assert.isNotNull(((EmailService) context.getService(context.getServiceReference(IEmailService.class.getName())))
				.getRepository());
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
