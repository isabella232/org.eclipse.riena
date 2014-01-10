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
package org.eclipse.riena.core.service;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.ServiceRegistration;

import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Test the {@code Service} class.
 */
@NonUITestCase
public class ServiceTest extends RienaTestCase {

	public void testServiceGetWithRanking() {
		final ServiceRegistration reg1 = getContext().registerService(ITestService.class.getName(), new TestService1(),
				RienaConstants.newDefaultServiceProperties());
		final ServiceRegistration reg2 = getContext().registerService(ITestService.class.getName(), new TestService2(),
				null);

		ITestService service = Service.get(ITestService.class);
		assertEquals("Hallo Welt", service.hello("Welt")); //$NON-NLS-1$ //$NON-NLS-2$
		reg2.unregister();

		service = Service.get(ITestService.class);
		assertEquals("Hello World", service.hello("World")); //$NON-NLS-1$ //$NON-NLS-2$
		reg1.unregister();

		service = Service.get(ITestService.class);
		assertNull(service);
	}

	public void testServiceGetWithFilter() {
		final Dictionary<String, String> dict1 = new Hashtable<String, String>();
		dict1.put("kind", "1"); //$NON-NLS-1$ //$NON-NLS-2$
		final ServiceRegistration reg1 = getContext().registerService(ITestService.class.getName(), new TestService1(),
				dict1);

		final Dictionary<String, String> dict2 = new Hashtable<String, String>();
		dict2.put("kind", "2"); //$NON-NLS-1$ //$NON-NLS-2$
		final ServiceRegistration reg2 = getContext().registerService(ITestService.class.getName(), new TestService2(),
				dict2);

		ITestService service = Service.get(ITestService.class, "(kind=1)"); //$NON-NLS-1$
		assertEquals("Hello Welt", service.hello("Welt")); //$NON-NLS-1$ //$NON-NLS-2$

		service = Service.get(ITestService.class, "(kind=2)"); //$NON-NLS-1$
		assertEquals("Hallo World", service.hello("World")); //$NON-NLS-1$ //$NON-NLS-2$
		reg2.unregister();
		reg1.unregister();

		service = Service.get(ITestService.class);
		assertNull(service);
	}
}
