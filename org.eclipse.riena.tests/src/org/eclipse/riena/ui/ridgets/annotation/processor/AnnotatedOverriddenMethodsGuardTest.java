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
package org.eclipse.riena.ui.ridgets.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Test the {@code AnnotatedOverriddenMethodsGuard}.
 */
@NonUITestCase
public class AnnotatedOverriddenMethodsGuardTest extends RienaTestCase {

	private AnnotatedOverriddenMethodsGuard guard = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		guard = new AnnotatedOverriddenMethodsGuard();
	}

	public void testM1() throws Throwable {
		assertTrue(add(TestClassA.class, "m1")); //$NON-NLS-1$
		assertFalse(add(TestClassB.class, "m1")); //$NON-NLS-1$
	}

	public void testM2() throws Throwable {
		assertTrue(add(TestClassA.class, "m2")); //$NON-NLS-1$
		assertTrue(add(TestClassB.class, "m2")); //$NON-NLS-1$
	}

	public void testM3() throws Throwable {
		assertTrue(add(TestClassA.class, "m3")); //$NON-NLS-1$
		assertFalse(add(TestClassB.class, "m3")); //$NON-NLS-1$
	}

	public void testM4() throws Throwable {
		assertTrue(add(TestClassA.class, "m4")); //$NON-NLS-1$
		assertTrue(add(TestClassB.class, "m4")); //$NON-NLS-1$
	}

	public void testM5() throws Throwable {
		assertTrue(add(TestClassA.class, "m5")); //$NON-NLS-1$
		assertTrue(add(TestClassB.class, "m5", int.class)); //$NON-NLS-1$
	}

	private boolean add(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes)
			throws Throwable {
		final Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
		final Annotation[] annotations = method.getAnnotations();
		return guard.add(annotations.length == 0 ? null : annotations[0], method);
	}
}
