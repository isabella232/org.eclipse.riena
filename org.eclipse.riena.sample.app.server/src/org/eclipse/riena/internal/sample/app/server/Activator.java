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
package org.eclipse.riena.internal.sample.app.server;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.communication.publisher.Publish;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.monitor.common.IReceiver;
import org.eclipse.riena.sample.app.common.ITestcasesWS;
import org.eclipse.riena.sample.app.common.attachment.IAttachmentService;
import org.eclipse.riena.sample.app.common.calendar.ITestGregorianCalendar;
import org.eclipse.riena.sample.app.common.exception.IExceptionService;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.sample.app.common.model.ICustomers;
import org.eclipse.riena.sample.app.common.model.IHelloWorldService;
import org.eclipse.riena.sample.app.common.tests.ITestObjectsOverRemoteService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaPlugin {

	private static final String REMOTE_PROTOCOL_HESSIAN = "hessian"; //$NON-NLS-1$

	private final Customers customers;
	private ServiceRegistration regCustomerSearch;
	private ServiceRegistration regCustomers;
	private final HelloWorldService helloWorldService;
	private final IReceiver monitoringReceiver;
	private ServiceRegistration regHelloWorldService;
	private ServiceRegistration regCollectibleReceiver;
	private ServiceRegistration regAttachmentService;

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
		super();
		Activator.plugin = this;
		customers = new Customers();
		helloWorldService = new HelloWorldService();
		monitoringReceiver = new SimpleMonitoringReceiver();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		startCustomerSearch(context);
		startCustomers(context);
		startHelloWorldService(context);
		startCollectibleReceiver(context);
		startAttachmentService(context);
		startTestGregorianCalendar(context);
		context.registerService(IExceptionService.class.getName(), new ExceptionService(), null);
		Publish.service(IExceptionService.class.getName()).usingPath("/ExceptionService").withProtocol("hessian") //$NON-NLS-1$ //$NON-NLS-2$
				.andStart(context); // stops automatically when bundle stops
		context.registerService(ITestcasesWS.class.getName(), new TestcasesService(), null);
		Publish.service(ITestcasesWS.class.getName())
				.usingPath("/TestcasesWS").withProtocol("hessian").andStart(context); //$NON-NLS-1$//$NON-NLS-2$
		context.registerService(ITestObjectsOverRemoteService.class.getName(), new TestObjectsOverRemoteService(), null);
		Publish.service(ITestObjectsOverRemoteService.class.getName())
				.usingPath("/TestObjectTypesWS").withProtocol(REMOTE_PROTOCOL_HESSIAN).andStart(context); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		stopCustomerSearch();
		stopCustomers();
		stopHelloWorldService();
		stopAttachmentService();
		Activator.plugin = null;
		super.stop(context);
	}

	/**
	 * Get the plugin instance.
	 * 
	 * @return
	 */
	public static Activator getDefault() {
		return plugin;
	}

	private void startCustomerSearch(final BundleContext context) {

		final Hashtable<String, String> properties = new Hashtable<String, String>(3);

		properties.put(RSDPublisherProperties.PROP_IS_REMOTE, Boolean.TRUE.toString());
		properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, REMOTE_PROTOCOL_HESSIAN);
		properties.put(RSDPublisherProperties.PROP_REMOTE_PATH, "/CustomerSearchWS"); //$NON-NLS-1$

		regCustomerSearch = context.registerService(ICustomerSearch.class.getName(), customers, properties);
	}

	public void stopCustomerSearch() {

		regCustomerSearch.unregister();
		regCustomerSearch = null;
	}

	private void startCustomers(final BundleContext context) {

		final Hashtable<String, String> properties = new Hashtable<String, String>(3);

		properties.put(RSDPublisherProperties.PROP_IS_REMOTE, Boolean.TRUE.toString());
		properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, REMOTE_PROTOCOL_HESSIAN);
		properties.put(RSDPublisherProperties.PROP_REMOTE_PATH, "/CustomersWS"); //$NON-NLS-1$

		regCustomers = context.registerService(ICustomers.class.getName(), customers, properties);
	}

	public void stopCustomers() {

		regCustomers.unregister();
		regCustomers = null;
	}

	private void startHelloWorldService(final BundleContext context) {

		final Hashtable<String, String> properties = new Hashtable<String, String>(3);

		properties.put(RSDPublisherProperties.PROP_IS_REMOTE, Boolean.TRUE.toString());
		properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, REMOTE_PROTOCOL_HESSIAN);
		properties.put(RSDPublisherProperties.PROP_REMOTE_PATH, "/HelloWorldServiceWS"); //$NON-NLS-1$

		regHelloWorldService = context.registerService(IHelloWorldService.class.getName(), helloWorldService,
				properties);
	}

	private void startCollectibleReceiver(final BundleContext context) {

		regCollectibleReceiver = context.registerService(IReceiver.class.getName(), monitoringReceiver, null);
		Publish.service(IReceiver.class).usingPath("/CollectibleReceiverWS").withProtocol(REMOTE_PROTOCOL_HESSIAN) //$NON-NLS-1$
				.andStart(context);
	}

	private void startTestGregorianCalendar(final BundleContext context) {
		context.registerService(ITestGregorianCalendar.class.getName(), new TestGregorianCalendar(), null);
		Publish.service(ITestGregorianCalendar.class).usingPath("/TestGregorianCalendarWS").withProtocol( //$NON-NLS-1$
				REMOTE_PROTOCOL_HESSIAN).andStart(context);
	}

	public void stopHelloWorldService() {

		regHelloWorldService.unregister();
		regHelloWorldService = null;
	}

	public void stopCollectibleReceiver() {
		regCollectibleReceiver.unregister();
		regCollectibleReceiver = null;
	}

	private void startAttachmentService(final BundleContext context) {
		regAttachmentService = context.registerService(IAttachmentService.class.getName(), new AttachmentService(),
				null);

		Publish.service(IAttachmentService.class.getName()).usingPath("/AttachmentService").withProtocol("hessian") //$NON-NLS-1$ //$NON-NLS-2$
				.andStart(context);

	}

	private void stopAttachmentService() {
		regAttachmentService.unregister();
	}

}
