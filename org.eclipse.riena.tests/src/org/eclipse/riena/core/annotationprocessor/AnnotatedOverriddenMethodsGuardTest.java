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
package org.eclipse.riena.core.annotationprocessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.annotation.OnActionCallback;

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
		assertTrue(add(A.class, "m1")); //$NON-NLS-1$
		assertFalse(add(B.class, "m1")); //$NON-NLS-1$
	}

	public void testM2() throws Throwable {
		assertTrue(add(A.class, "m2")); //$NON-NLS-1$
		assertTrue(add(B.class, "m2")); //$NON-NLS-1$
	}

	public void testM3() throws Throwable {
		assertTrue(add(A.class, "m3")); //$NON-NLS-1$
		assertFalse(add(B.class, "m3")); //$NON-NLS-1$
	}

	public void testM4() throws Throwable {
		assertTrue(add(A.class, "m4")); //$NON-NLS-1$
		assertTrue(add(B.class, "m4")); //$NON-NLS-1$
	}

	public void testM5() throws Throwable {
		assertTrue(add(A.class, "m5")); //$NON-NLS-1$
		assertTrue(add(B.class, "m5", int.class)); //$NON-NLS-1$
	}

	private boolean add(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes)
			throws Throwable {
		final Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
		final Annotation[] annotations = method.getAnnotations();
		return guard.add(annotations.length == 0 ? null : annotations[0], method);
	}

	private static class A {

		protected void m1() {
		}

		private void m2() {
		}

		@OnActionCallback(ridgetId = "A.m3")
		protected void m3() {
		}

		@OnActionCallback(ridgetId = "A.m4")
		protected void m4() {
		}

		@OnActionCallback(ridgetId = "A.m5")
		protected void m5() {
		}

	}

	private static class B extends A {

		@Override
		public void m1() {
		}

		private void m2() {
		}

		@Override
		@OnActionCallback(ridgetId = "A.m3")
		protected void m3() {
		}

		@Override
		@OnActionCallback(ridgetId = "B.m4")
		protected void m4() {
		}

		@OnActionCallback(ridgetId = "A.m5")
		protected void m5(final int i) {
		}

	}

}
