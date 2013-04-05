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
package org.eclipse.riena.communication.core.factory;

import org.eclipse.riena.communication.core.IRemoteServiceRegistration;
import org.eclipse.riena.core.exception.IExceptionHandler;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code ProxyFactory}
 */
@NonUITestCase
public class ProxyFactoryTest extends RienaTestCase {

	public void testProtocolDefaultValue() {
		final IRemoteServiceRegistration rsf = new ProxyFactory(IExceptionHandler.class).usingUrl(
				"http://localhost:8080/hessian/bla").andStart(getContext()); //$NON-NLS-1$
		assertEquals(ProxyFactory.DEFAULT_COMMUNICATION_PROTOCOL, rsf.getReference().getDescription().getProtocol());
	}

	public void testNoneProtocolDefaultValue() {
		final IRemoteServiceRegistration rsf = new ProxyFactory(IExceptionHandler.class).withProtocol("protobuf") //$NON-NLS-1$
				.usingUrl("http://localhost:8080/hessian/blub").andStart(getContext()); //$NON-NLS-1$
		assertEquals("protobuf", rsf.getReference().getDescription().getProtocol()); //$NON-NLS-1$
	}
}
