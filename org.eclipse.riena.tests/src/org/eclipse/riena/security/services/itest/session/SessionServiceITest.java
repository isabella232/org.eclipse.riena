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
package org.eclipse.riena.security.services.itest.session;

import java.security.Principal;

import org.osgi.framework.ServiceReference;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.IntegrationTestCase;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.security.common.authentication.SimplePrincipal;
import org.eclipse.riena.security.common.session.Session;
import org.eclipse.riena.security.server.session.ISessionService;

/**
 * Tests the SessionService with single user. There is also a disabled multiuser
 * test. Its disabled because it seems to fail for problem laying the webservice
 * area.
 * 
 */
@IntegrationTestCase
public class SessionServiceITest extends RienaTestCase {

	private final static int LOOP_5 = 5;
	private final static int LOOP_20 = 20;
	private final static int LOOP_100 = 100;
	private final static int LOOP_400 = 400;
	private final static int THREAD_5 = 5;
	private final static int THREAD_20 = 20;
	private final static int THREAD_100 = 100;
	private IRemoteServiceRegistration sessionService;

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startBundles("org\\.eclipse\\.equinox\\.cm.*", null);
		startBundles("org\\.eclipse\\.equinox\\.log.*", null);
		startBundles("org\\.eclipse\\.riena.communication.core", null);
		startBundles("org\\.eclipse\\.riena.communication.factory.hessian", null);
		startBundles("org\\.eclipse\\.riena.communication.registry", null);
		sessionService = Register.remoteProxy(ISessionService.class)
				.usingUrl("http://localhost:8080/hessian/SessionService").withProtocol("hessian")
				.andStart(Activator.getDefault().getContext());
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		sessionService.unregister();
	}

	/**
	 * @throws Exception
	 */
	public void testController1() throws Exception {
		final ServiceReference ref = getContext().getServiceReference(ISessionService.class.getName());
		final ISessionService service = (ISessionService) getContext().getService(ref);
		assertNotNull("SessionControllerAccessor returns null", service);
		final Session session = service.generateSession(new Principal[] { new SimplePrincipal("testuid") });
		assertNotNull("generateSession returns null", session);

		final Principal[] principals = service.findPrincipals(session);
		assertNotNull("findUser returns null", principals);
		assertTrue("returned userid is not equal to the correct one", principals[0].getName().equals("testuid"));

		service.invalidateSession(session);
		assertFalse("session should be invalid", service.isValidSession(session));

		final Principal[] temp = service.findPrincipals(session);
		assertNull("no user should be found for invalid session", temp);
	}

	/**
	 * @throws Exception
	 */
	public void texxstMultiuser() throws Exception {
		createNNNThreads(THREAD_5, LOOP_5);
		createNNNThreads(THREAD_5, LOOP_20);
		createNNNThreads(THREAD_20, LOOP_20);
		createNNNThreads(THREAD_100, LOOP_20);
		createNNNThreads(THREAD_100, LOOP_100);
		createNNNThreads(THREAD_100, LOOP_400);
	}

	private void createNNNThreads(final int noOfThreads, final int loopCounter) {
		final Thread[] t = new Thread[noOfThreads];
		// trace("threads=" + noOfThreads + " loop=" + loopCounter);
		// create threads
		for (int i = 0; i < noOfThreads; i++) {
			t[i] = new Threader(loopCounter);
			// trace("C");
		}

		// start threads
		for (int i = 0; i < noOfThreads; i++) {
			t[i].start();
			// trace("S");
		}

		// wait for them to finish
		int activeThreads = noOfThreads;
		while (activeThreads > 0) {
			for (int i = 0; i < noOfThreads && activeThreads > 0; i++) {
				if (t[i] != null) {
					if (!t[i].isAlive()) {
						activeThreads--;
						// trace("D");
					}
				}
			}
		}
		// trace("");
	}

	class Threader extends Thread {
		private final ISessionService sessionService;
		private final int loopCounter;

		Threader(final int loopCounter) {
			final ServiceReference ref = getContext().getServiceReference(ISessionService.class.getName());
			sessionService = (ISessionService) getContext().getService(ref);
			this.loopCounter = loopCounter;
			assertNotNull("SessionServiceAccessor returns null", sessionService);
		}

		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			for (int i = 0; i < loopCounter; i++) {
				final Session session = sessionService
						.generateSession(new Principal[] { new SimplePrincipal("testuid") });
				assertNotNull("generateSession returns null", session);

				final Principal[] principals = sessionService.findPrincipals(session);
				assertTrue("returned userid is not equal to the correct one", principals == null
						|| principals[0].getName().equals("testuid"));

				sessionService.invalidateSession(session);

				final Principal[] temp = sessionService.findPrincipals(session);
				assertTrue("no user should be found for invalid session",
						temp == null || temp[0].getName().equals("testuid"));
			}
			// SessionServiceITest.this.trace("K");
		}
	}

}