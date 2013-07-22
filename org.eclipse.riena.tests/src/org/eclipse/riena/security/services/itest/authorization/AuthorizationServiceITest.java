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
package org.eclipse.riena.security.services.itest.authorization;

import java.io.FilePermission;
import java.net.URL;
import java.security.AccessControlException;
import java.security.Permission;
import java.security.Permissions;
import java.security.Principal;
import java.util.Set;

import org.osgi.framework.ServiceReference;

import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.IntegrationTestCase;
import org.eclipse.riena.core.util.Iter;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.sample.app.common.model.Customer;
import org.eclipse.riena.sample.app.common.model.CustomersPermission;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.security.authentication.callbackhandler.TestLocalCallbackHandler;
import org.eclipse.riena.security.common.authentication.IAuthenticationService;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;

/**
 * 
 */
@IntegrationTestCase
public class AuthorizationServiceITest extends RienaTestCase {

	private IRemoteServiceRegistration authenticationServiceRegistration;
	private IRemoteServiceRegistration authorizationServiceRegistration;
	private IRemoteServiceRegistration customerServiceRegistration;

	private static final String JAAS_CONFIG_FILE = "config/sample_jaas.config"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startBundles("org\\.eclipse\\.riena.communication.core", null);
		startBundles("org\\.eclipse\\.riena.communication.factory.hessian", null);
		startBundles("org\\.eclipse\\.riena.communication.registry", null);
		stopBundles("org\\.eclipse\\.riena.example.client", null);
		stopBundles("org\\.eclipse\\.riena.security.client.startup", null);

		authenticationServiceRegistration = Register.remoteProxy(IAuthenticationService.class)
				.usingUrl("http://localhost:8080/hessian/AuthenticationService").withProtocol("hessian")
				.andStart(Activator.getDefault().getContext());
		authorizationServiceRegistration = Register.remoteProxy(IAuthorizationService.class)
				.usingUrl("http://localhost:8080/hessian/AuthorizationService").withProtocol("hessian")
				.andStart(Activator.getDefault().getContext());
		customerServiceRegistration = Register.remoteProxy(ICustomerSearch.class)
				.usingUrl("http://localhost:8080/hessian/CustomerSearchWS").withProtocol("hessian")
				.andStart(Activator.getDefault().getContext());
	}

	@Override
	protected void tearDown() throws Exception {
		authenticationServiceRegistration.unregister();
		authorizationServiceRegistration.unregister();
		customerServiceRegistration.unregister();
		super.tearDown();
	}

	public void testLoginWithUserWithRights() throws Exception {
		printTestName();
		TestLocalCallbackHandler.setSuppliedCredentials("testuser", "testpass");

		final URL configUrl = Activator.getDefault().getContext().getBundle().getEntry(JAAS_CONFIG_FILE);
		final ILoginContext secureContext = LoginContextFactory.createContext("RemoteTest", configUrl);

		secureContext.login();

		final IAuthenticationService as = Service.get(IAuthenticationService.class);
		System.out.println("subject:" + secureContext.getSubject());
		System.out.println("login in sucessful");

		// call the customerService
		final ICustomerSearch cs = Service.get(ICustomerSearch.class);
		final Customer cust = new Customer();
		cust.setLastName("Solo");
		cust.setFirstName("Han");
		cust.setCustomerNumber(1);
		final Customer[] foundCustomers = cs.findCustomerWithPermission(cust);
		assertTrue(foundCustomers != null);
		assertTrue(foundCustomers.length > 0);
		assertTrue(foundCustomers[0].getLastName().equals("Solo"));

		as.logout();
		System.out.println("logoff sucessful");
	}

	public void testLoginWithUserWithoutRights() throws Exception {
		printTestName();
		TestLocalCallbackHandler.setSuppliedCredentials("testuser1", "testpass2");

		final URL configUrl = Activator.getDefault().getContext().getBundle().getEntry(JAAS_CONFIG_FILE);
		final ILoginContext secureContext = LoginContextFactory.createContext("RemoteTest", configUrl);

		secureContext.login();

		final ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
		final IAuthenticationService as = (IAuthenticationService) getContext().getService(ref);
		System.out.println("subject:" + secureContext.getSubject());
		System.out.println("login in sucessful");
		//		ISessionHolderService shs = (ISessionHolderService) getContext().getService(
		//				getContext().getServiceReference(ISessionHolderService.class.getName()));

		try {
			// call the customerService
			final ICustomerSearch cs = (ICustomerSearch) getContext().getService(
					getContext().getServiceReference(ICustomerSearch.class.getName()));
			final Customer cust = new Customer();
			cust.setLastName("Solo");
			cust.setFirstName("Han");
			cust.setCustomerNumber(1);
			cs.findCustomerWithPermission(cust);
			fail("findCustomerWithPermission must not work for testuser1 since it has to authorization");
			// assertTrue(foundCustomers != null);
			// assertTrue(foundCustomers.length > 0);
			// assertTrue(foundCustomers[0].getLastName().equals("Solo"));
		} catch (final AccessControlException ex) {
			ok("expected exception");
		}

		as.logout();
		System.out.println("logoff sucessful");
	}

	public void testLoginWithUserWithRightsAndGetPermissions() throws Exception {
		printTestName();
		TestLocalCallbackHandler.setSuppliedCredentials("stefan", "passpass");

		final URL configUrl = Activator.getDefault().getContext().getBundle().getEntry(JAAS_CONFIG_FILE);
		final ILoginContext secureContext = LoginContextFactory.createContext("RemoteTest", configUrl);

		secureContext.login();

		final ServiceReference ref = getContext().getServiceReference(IAuthenticationService.class.getName());
		final IAuthenticationService authenticationService = (IAuthenticationService) getContext().getService(ref);
		System.out.println("subject:" + secureContext.getSubject());
		System.out.println("login in sucessful");
		try {
			final ServiceReference authorizationServiceRef = getContext().getServiceReference(
					IAuthorizationService.class.getName());
			final IAuthorizationService authorizationService = (IAuthorizationService) getContext().getService(
					authorizationServiceRef);

			// get the permissions

			final Set<Principal> principals = secureContext.getSubject().getPrincipals();
			assertEquals(1, principals.size());
			final Permissions[] permissionss = authorizationService.getPermissions(principals
					.toArray(new Principal[principals.size()]));
			assertNotNull(permissionss);
			assertEquals(1, permissionss.length);
			final Permissions permissions = permissionss[0];
			assertNotNull(permissions);
			int count = 0;
			for (final Permission permission : Iter.able(permissions.elements())) {
				System.out.println("Permission: " + permission);
				if (permission.getClass() == FilePermission.class) {
					assertEquals("*.tmp", permission.getName());
					assertEquals("write", permission.getActions());
				} else if (permission.getClass() == CustomersPermission.class) {
					assertTrue(permission.getActions().equals("find") || permission.getActions().equals("create"));
					assertTrue(permission.getName().equals("riena.sample.A")
							|| permission.getName().equals("riena.sample.B"));
				} else {
					fail("Unexpected permission: " + permission);
				}
				count++;
			}
			assertEquals(3, count);
		} finally {
			authenticationService.logout();
			System.out.println("logoff sucessful");
		}

		new FilePermission("", "delete");
	}

}
