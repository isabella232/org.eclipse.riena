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
package org.eclipse.riena.communication.core.factory;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.IRemoteServiceRegistry;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.core.util.Companion;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.communication.core.registry.RemoteServiceRegistry;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 *
 */
@NonUITestCase
public class RemoteServiceFactoryTest extends RienaTestCase {

	private final RemoteServiceRegistry remoteServiceRegistry = (RemoteServiceRegistry) Service.get(IRemoteServiceRegistry.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.internal.core.test.RienaTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// some tests are stopping this bundle to test the OSGi service unregistration
		// we want to ensure that this bundle is active at the beginning of each test
		startBundle("org.eclipse.riena.communication.console"); //$NON-NLS-1$
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		final BundleContext context = Activator.getDefault().getContext();
		ServiceReference<?> serviceReference;
		// hack to unregister services from previous testcases that are still hanging around
		while ((serviceReference = context.getServiceReference(IRSFTest.class.getName())) != null) {
			final ServiceRegistration<?> registration = (ServiceRegistration<?>) ReflectionUtils.getHidden(serviceReference, "registration"); //$NON-NLS-1$
			registration.unregister();
		}
	}

	public void testUnregister() throws Exception {
		final BundleContext context = Activator.getDefault().getContext();
		final IRemoteServiceRegistration createAndRegisterProxy = Companion.per(RemoteServiceFactory.class).createAndRegisterProxy(IRSFTest.class,
				"http://localhost", "hessian", context); //$NON-NLS-1$ //$NON-NLS-2$
		final Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		createAndRegisterProxy.unregister();
		final ServiceReference<?> serviceReference = context.getServiceReference(IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when unregister is called", serviceReference); //$NON-NLS-1$
		assertFalse("asssuming that service is no longer in registry if service proxy is gone", //$NON-NLS-1$
				remoteServiceRegistry.hasServiceForUrl("http://localhost")); //$NON-NLS-1$
	}

	public void testUnregisterForOtherBundle() throws Exception {
		final BundleContext context = org.eclipse.riena.internal.communication.console.Activator.getDefault().getContext();
		final IRemoteServiceRegistration createAndRegisterProxy = Companion.per(RemoteServiceFactory.class).createAndRegisterProxy(IRSFTest.class,
				"http://localhost", "hessian", context); //$NON-NLS-1$ //$NON-NLS-2$
		final Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		createAndRegisterProxy.unregister();
		final ServiceReference<?> serviceReference = context.getServiceReference(IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when unregister is called", serviceReference); //$NON-NLS-1$
		assertFalse("asssuming that service is no longer in registry if service proxy is gone", //$NON-NLS-1$
				remoteServiceRegistry.hasServiceForUrl("http://localhost")); //$NON-NLS-1$
	}

	public void testUnregisterForOtherBundleAndStopOtherBundle() throws Exception {
		final BundleContext context = org.eclipse.riena.internal.communication.console.Activator.getDefault().getContext();
		Companion.per(RemoteServiceFactory.class).createAndRegisterProxy(IRSFTest.class, "http://localhost", "hessian", context); //$NON-NLS-1$ //$NON-NLS-2$
		final Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		context.getBundle().stop();
		final ServiceReference<?> serviceReference = Activator.getDefault().getContext().getServiceReference(IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when bundle is stopped", serviceReference); //$NON-NLS-1$
		assertFalse("asssuming that service is no longer in registry if service proxy is gone", //$NON-NLS-1$
				remoteServiceRegistry.hasServiceForUrl("http://localhost")); //$NON-NLS-1$
	}

	public void testUnregisterForOtherBundleAndStopOtherBundleAndUnregister() throws Exception {
		final BundleContext context = org.eclipse.riena.internal.communication.console.Activator.getDefault().getContext();
		final IRemoteServiceRegistration createAndRegisterProxy = Companion.per(RemoteServiceFactory.class).createAndRegisterProxy(IRSFTest.class,
				"http://localhost", "hessian", context); //$NON-NLS-1$ //$NON-NLS-2$
		final Object service = context.getService(context.getServiceReference(IRSFTest.class.getName()));
		assertNotNull(service);
		context.getBundle().stop();
		try {
			createAndRegisterProxy.unregister();
			fail("unregister of proxy for dead context should not be possible"); //$NON-NLS-1$
		} catch (final IllegalStateException e) {
			ok("expected exception"); //$NON-NLS-1$
		}
		final ServiceReference<?> serviceReference = Activator.getDefault().getContext().getServiceReference(IRSFTest.class.getName());
		assertNull("assuming that serviceReference is null when bundle is stopped and unregister is called", //$NON-NLS-1$
				serviceReference);
		assertFalse("asssuming that service is no longer in registry if service proxy is gone", //$NON-NLS-1$
				remoteServiceRegistry.hasServiceForUrl("http://localhost")); //$NON-NLS-1$
	}
}
