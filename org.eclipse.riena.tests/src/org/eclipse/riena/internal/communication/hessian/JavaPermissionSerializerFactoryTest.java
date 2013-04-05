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
package org.eclipse.riena.internal.communication.hessian;

import java.io.FilePermission;
import java.security.Permission;

import org.eclipse.riena.internal.communication.factory.hessian.serializer.JavaPermissionSerializerFactory;

/**
 * Test the {@code JavaPermissionSerializerFactory} class.
 */
public class JavaPermissionSerializerFactoryTest extends AbstractSerializerFactoryTestCase {

	public void testPermissionFail() {
		assertFalse(isBackAndForthOk(new FilePermission("-", "read"), HessianSerializerVersion.Two, null));
	}

	public void testPermission1() {
		assertTrue(isBackAndForthOk(new FilePermission("-", "read"), HessianSerializerVersion.Two, null,
				new JavaPermissionSerializerFactory()));
	}

	public void testPermission2() {
		assertTrue(isBackAndForthOk(new FilePermission("-", "read"), HessianSerializerVersion.Two,
				FilePermission.class, new JavaPermissionSerializerFactory()));
	}

	public void testPermission3() {
		assertTrue(isBackAndForthOk(new FilePermission("-", "read"), HessianSerializerVersion.Two, Permission.class,
				new JavaPermissionSerializerFactory()));
	}
}
