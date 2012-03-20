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
package org.eclipse.riena.communication.core;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 * Testing the RemoteServiceDescriptor
 */
@NonUITestCase
public class RemoteServiceDescriptionTest extends RienaTestCase {

	public void testNonDefaultCreationObjectClass() throws ClassNotFoundException {
		final Object service = "Service"; //$NON-NLS-1$
		final RemoteServiceDescription rsd = new RemoteServiceDescription(String.class, null, null, Activator
				.getDefault().getBundle());
		rsd.setService(service);

		assertNotNull(rsd.getBundle());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundle().getSymbolicName());
		assertNull(rsd.getPath());
		assertNull(rsd.getProtocol());
		assertTrue(service == rsd.getService());
		assertEquals(String.class.getName(), rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertEquals(String.class, rsd.getServiceInterfaceClass());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationPropRemoteProtocol() {
		final Object service = "Service"; //$NON-NLS-1$
		final RemoteServiceDescription rsd = new RemoteServiceDescription(String.class, null, "hessian", Activator //$NON-NLS-1$
				.getDefault().getBundle());
		rsd.setService(service);

		assertNotNull(rsd.getBundle());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundle().getSymbolicName());
		assertNull(rsd.getPath());
		assertEquals("hessian", rsd.getProtocol()); //$NON-NLS-1$
		assertTrue(service == rsd.getService());
		assertNotNull(rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNotNull(rsd.getServiceInterfaceClass());
		assertNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationPropRemotePath() {
		final Object service = "Service"; //$NON-NLS-1$
		final RemoteServiceDescription rsd = new RemoteServiceDescription(String.class, null, null, Activator
				.getDefault().getBundle());
		rsd.setService(service);

		assertNotNull(rsd.getBundle());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundle().getSymbolicName());
		//		assertEquals("/server/here", rsd.getPath());
		assertNull(rsd.getProtocol());
		assertTrue(service == rsd.getService());
		assertNotNull(rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNotNull(rsd.getServiceInterfaceClass());
		assertNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationPropConfigId() {
		final Object service = "Service"; //$NON-NLS-1$
		final RemoteServiceDescription rsd = new RemoteServiceDescription(String.class, null, null, Activator
				.getDefault().getBundle());
		rsd.setService(service);

		assertNotNull(rsd.getBundle());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundle().getSymbolicName());
		assertNull(rsd.getPath());
		assertNull(rsd.getProtocol());
		assertTrue(service == rsd.getService());
		assertNotNull(rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNotNull(rsd.getServiceInterfaceClass());
		assertNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationPropElse() {
		final Object service = "Service"; //$NON-NLS-1$
		final RemoteServiceDescription rsd = new RemoteServiceDescription(String.class, null, null, Activator
				.getDefault().getBundle());
		rsd.setService(service);

		assertNotNull(rsd.getBundle());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundle().getSymbolicName());
		assertNull(rsd.getPath());
		assertNull(rsd.getProtocol());
		assertTrue(service == rsd.getService());
		assertNotNull(rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertNotNull(rsd.getServiceInterfaceClass());
		assertNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}

	public void testNonDefaultCreationAllTheStuff() {
		final Object service = "Service"; //$NON-NLS-1$
		final RemoteServiceDescription rsd = new RemoteServiceDescription(String.class, null, "hessian", Activator //$NON-NLS-1$
				.getDefault().getBundle());
		rsd.setService(service);

		assertNotNull(rsd.getBundle());
		assertEquals(Activator.getDefault().getBundle().getSymbolicName(), rsd.getBundle().getSymbolicName());
		//		assertEquals("/server/here", rsd.getPath());
		assertEquals("hessian", rsd.getProtocol()); //$NON-NLS-1$
		assertTrue(service == rsd.getService());
		assertEquals(String.class.getName(), rsd.getServiceInterfaceClassName());
		assertNull(rsd.getURL());
		assertEquals(String.class, rsd.getServiceInterfaceClass());
		assertNull(rsd.getServiceRef());
		assertEquals(RemoteServiceDescription.State.REGISTERED, rsd.getState());
	}
}
