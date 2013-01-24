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
package org.eclipse.riena.security.services.itest.authentication;

import javax.security.auth.Subject;

import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.RemoteFailure;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.IntegrationTestCase;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.security.common.BasicAuthenticationCallHook;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.authentication.SimplePrincipal;

/**
 * 
 */
@IntegrationTestCase
public class BasicAuthenticationITest extends RienaTestCase {

	private IRemoteServiceRegistration customerSearchRegistration;

	private final static String TESTURL = "http://localhost:8080/junit/protected";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startBundles("org\\.eclipse\\.equinox\\.cm.*", null); //$NON-NLS-1$
		startBundles("org\\.eclipse\\.equinox\\.log.*", null); //$NON-NLS-1$
		startBundles("org\\.eclipse\\.riena.communication.core", null); //$NON-NLS-1$
		startBundles("org\\.eclipse\\.riena.communication.factory.hessian", null); //$NON-NLS-1$
		stopBundles("org\\.eclipse\\.riena.example.client", null);

		customerSearchRegistration = Register.remoteProxy(ICustomerSearch.class).usingUrl(TESTURL)
				.withProtocol("hessian").andStart(Activator.getDefault().getContext()); //$NON-NLS-1$
	}

	@Override
	protected void tearDown() throws Exception {
		customerSearchRegistration.unregister();
		super.tearDown();
	}

	/**
	 * nomen est omen
	 */
	public void testNoCallHook() {
		try {
			final ICustomerSearch customerSearch = (ICustomerSearch) getContext().getService(
					getContext().getServiceReference(ICustomerSearch.class.getName()));

			customerSearch.findCustomer(null);
			fail("RemoteFailure HTTP=401 expected"); //$NON-NLS-1$
		} catch (final RemoteFailure e) {
			assertTrue(e.getCause().getCause().getMessage().contains("401")); //$NON-NLS-1$
		}
	}

	/**
	 * nomen est omen
	 */
	public void testWithCallHookNoAuthorization() {
		try {
			final ICustomerSearch customerSearch = (ICustomerSearch) getContext().getService(
					getContext().getServiceReference(ICustomerSearch.class.getName()));

			final ServiceRegistration serviceReg = getContext().registerService(ICallHook.class.getName(),
					new BasicAuthenticationCallHook(), null);

			customerSearch.findCustomer(null);

			serviceReg.unregister();
			fail("RemoteFailure HTTP=401 expected"); //$NON-NLS-1$
		} catch (final RemoteFailure e) {
			assertTrue(e.getCause().getCause().getMessage().contains("401")); //$NON-NLS-1$
		}

	}

	/**
	 * nomen est omen
	 */
	public void testWithCallHookWithInvalidAuthorization() {
		try {
			final ICustomerSearch customerSearch = (ICustomerSearch) getContext().getService(
					getContext().getServiceReference(ICustomerSearch.class.getName()));

			final ServiceRegistration serviceReg = getContext().registerService(ICallHook.class.getName(),
					new BasicAuthenticationCallHook(), null);

			//			ISubjectHolderService subjectHolderService = (ISubjectHolderService) getContext().getService(
			//					getContext().getServiceReference(ISubjectHolderService.class.getName()));

			final Subject subject = new Subject();
			subject.getPrincipals().add(new SimplePrincipal("christian"));
			subject.getPrivateCredentials().add("password");
			Service.get(ISubjectHolder.class).setSubject(subject);

			customerSearch.findCustomer(null);

			serviceReg.unregister();
			fail("RemoteFailure HTTP=401 expected"); //$NON-NLS-1$
		} catch (final RemoteFailure e) {
			assertTrue(e.getCause().getCause().getMessage().contains("401")); //$NON-NLS-1$
		}

	}

	/**
	 * nomen est omen
	 */
	public void testWithCallHookWithValidAuthorization() {
		try {
			final ICustomerSearch customerSearch = Service.get(ICustomerSearch.class);

			final ServiceRegistration serviceReg = getContext().registerService(ICallHook.class.getName(),
					new BasicAuthenticationCallHook(), null);

			//			ISubjectHolderService subjectHolderService = (ISubjectHolderService) getContext().getService(
			//					getContext().getServiceReference(ISubjectHolderService.class.getName()));

			final Subject subject = new Subject();
			subject.getPrincipals().add(new SimplePrincipal("scp")); //$NON-NLS-1$
			subject.getPrivateCredentials().add("scptestpassword"); //$NON-NLS-1$
			Service.get(ISubjectHolder.class).setSubject(subject);

			customerSearch.findCustomer(null);

			serviceReg.unregister();
			fail("RemoteFailure with Protocol Error expected"); //$NON-NLS-1$
		} catch (final RemoteFailure e) {
			assertFalse(e.getCause().getCause().getMessage().contains("401")); //$NON-NLS-1$
			assertTrue(e.getCause().getCause().getMessage(),
					e.getCause().getCause().getMessage().contains("unexpected end of file")); //$NON-NLS-1$
		}

	}

	public void testWithCallHookWithMultipleValidAuthorization() {
		final ICustomerSearch customerSearch = (ICustomerSearch) getContext().getService(
				getContext().getServiceReference(ICustomerSearch.class.getName()));

		final ServiceRegistration serviceReg = getContext().registerService(ICallHook.class.getName(),
				new BasicAuthenticationCallHook(), null);

		//		ISubjectHolderService subjectHolderService = (ISubjectHolderService) getContext().getService(
		//				getContext().getServiceReference(ISubjectHolderService.class.getName()));

		final Subject subject = new Subject();
		subject.getPrincipals().add(new SimplePrincipal("scp")); //$NON-NLS-1$
		subject.getPrivateCredentials().add("scptestpassword"); //$NON-NLS-1$
		Service.get(ISubjectHolder.class).setSubject(subject);

		// first call
		try {
			customerSearch.findCustomer(null);

			fail("RemoteFailure with Protocol Error expected"); //$NON-NLS-1$
		} catch (final RemoteFailure e) {
			assertFalse(e.getCause().getCause().getMessage().contains("401")); //$NON-NLS-1$
			assertTrue(e.getCause().getCause().getMessage(),
					e.getCause().getCause().getMessage().contains("unexpected end of file")); //$NON-NLS-1$
		}

		// second call
		try {
			customerSearch.findCustomer(null);

			fail("RemoteFailure with Protocol Error expected"); //$NON-NLS-1$
		} catch (final RemoteFailure e) {
			assertFalse(e.getCause().getCause().getMessage().contains("401")); //$NON-NLS-1$
			assertTrue(e.getCause().getCause().getMessage(),
					e.getCause().getCause().getMessage().contains("unexpected end of file")); //$NON-NLS-1$
		}

		serviceReg.unregister();
	}

}
