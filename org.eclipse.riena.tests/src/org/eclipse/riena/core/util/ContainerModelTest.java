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
package org.eclipse.riena.core.util;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonGatherableTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * 
 */
@NonUITestCase
@NonGatherableTestCase("Fails when executed along with other test cases!")
public class ContainerModelTest extends TestCase {

	private static final String INITIALIZE_METHOD_NAME = "initialize"; //$NON-NLS-1$

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// just load the class once
		ContainerModel.isClient();
	}

	public void testOnClient() {
		ReflectionUtils.invokeHidden(ContainerModel.class, INITIALIZE_METHOD_NAME);
		assertTrue(ContainerModel.isClient());
	}

	public void testOnServerOk() {
		System.setProperty(ContainerModel.RIENA_CONTAINER_TYPE, "server"); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(ContainerModel.class, INITIALIZE_METHOD_NAME);
		assertTrue(ContainerModel.isServer());
	}

	public void testOnServerFail() {
		System.setProperty(ContainerModel.RIENA_CONTAINER_TYPE, "Xerver"); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(ContainerModel.class, INITIALIZE_METHOD_NAME);
		assertFalse(ContainerModel.isServer());
	}

}
