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
package org.eclipse.riena.communication.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.communication.core.factory.Register;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.ManualTestCase;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.sample.app.common.tests.ITestObjectsOverRemoteService;

/**
 * Test case to test various object types when the are sent over a remote
 * service wire.
 */
@ManualTestCase
public class ObjectsOverRemoteServiceTest extends RienaTestCase {

	private ITestObjectsOverRemoteService testObjectTypesService;
	private IRemoteServiceRegistration regTestObjectTypesService;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		regTestObjectTypesService = Register.remoteProxy(ITestObjectsOverRemoteService.class)
				.usingUrl("http://localhost:8080/hessian/TestObjectTypesWS").withProtocol("hessian") //$NON-NLS-1$ //$NON-NLS-2$
				.andStart(Activator.getDefault().getContext());
		testObjectTypesService = Service.get(ITestObjectsOverRemoteService.class);
	}

	@Override
	protected void tearDown() throws Exception {
		regTestObjectTypesService.unregister();
		testObjectTypesService = null;
		super.tearDown();
	}

	public void testRegularObject() {
		final Object object = testObjectTypesService.returnObject(0);
		assertNotNull(object);
	}

	public void testRegularMap() {
		final Object object = testObjectTypesService.returnMap(0);
		assertNotNull(object);
		assertTrue(object instanceof Map);
		final Map<?, ?> map = (Map<?, ?>) object;
		assertEquals(map.keySet().size(), 1);
		assertTrue(map.keySet().toArray()[0] instanceof String);
		assertTrue(map.values().toArray()[0] instanceof String);
	}

	public void testInvalidObject() {
		try {
			final Object object = testObjectTypesService.returnObject(1);
		} catch (final Exception e) {
			assertTrue(e instanceof RuntimeException);
			return;
		}
		fail("should never get here"); //$NON-NLS-1$
	}

	public void testInvalidMap() {
		try {
			final Object object = testObjectTypesService.returnMap(1);
		} catch (final Exception e) {
			assertTrue(e instanceof RuntimeException);
			return;
		}
		fail("should never get here"); //$NON-NLS-1$
	}

	public void testSendObject() {
		testObjectTypesService.sendObject("Hello"); //$NON-NLS-1$
	}

	public void testSendInvalidObject() {
		try {
			testObjectTypesService.sendObject(new TestClientObject());
		} catch (final Exception e) {
			assertTrue(e instanceof RuntimeException);
			return;
		}
		fail("should never get here"); //$NON-NLS-1$
	}

	public void testSendMap() {
		final HashMap<String, String> map = new HashMap<String, String>();
		map.put("Hello", "world"); //$NON-NLS-1$ //$NON-NLS-2$
		testObjectTypesService.sendMap(map);
	}

	public void testSendInvalidMap() {
		try {
			final HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("Hello", new TestClientObject()); //$NON-NLS-1$
			testObjectTypesService.sendMap(map);
		} catch (final Exception e) {
			assertTrue(e instanceof RuntimeException);
			return;
		}
		fail("should never get here"); //$NON-NLS-1$
	}
}
