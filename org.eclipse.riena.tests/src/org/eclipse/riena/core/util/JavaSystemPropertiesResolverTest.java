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
package org.eclipse.riena.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code JavaSystemPropertiesResolver}.
 */
@NonUITestCase
public class JavaSystemPropertiesResolverTest extends RienaTestCase {

	public void testResolveUserDir() throws CoreException {
		final String expected = System.getProperty("user.dir");
		assertEquals(expected, VariableManagerUtil.substitute("${java.system.property:user.dir}"));
	}

	public void testResolveRienaHostName() throws CoreException, UnknownHostException {
		final String expected = InetAddress.getLocalHost().getHostName();
		assertEquals(expected, VariableManagerUtil.substitute("${java.system.property:riena.host.name}"));
	}

	public void testResolveRienaHostAddress() throws CoreException, UnknownHostException {
		final String expected = InetAddress.getLocalHost().getHostAddress();
		assertEquals(expected, VariableManagerUtil.substitute("${java.system.property:riena.host.address}"));
	}

}
