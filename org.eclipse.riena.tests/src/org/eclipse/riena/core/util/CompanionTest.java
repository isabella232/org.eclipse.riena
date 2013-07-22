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

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 *
 */
@NonUITestCase
public class CompanionTest extends RienaTestCase {

	public void testSinglenessOfACompanionObjectsPrivateConstructor() {
		final TestClass1 companion1 = Companion.per(TestClass1.class);
		final TestClass1 companion2 = Companion.per(TestClass1.class);
		assertTrue(companion1 == companion2);
	}

	public void testSinglenessOfACompanionObjectsProtectedConstructor() {
		final TestClass2 companion1 = Companion.per(TestClass2.class);
		final TestClass2 companion2 = Companion.per(TestClass2.class);
		assertTrue(companion1 == companion2);
	}

	public void testSinglenessOfACompanionObjectsPublicConstructor() {
		final TestClass3 companion1 = Companion.per(TestClass3.class);
		final TestClass3 companion2 = Companion.per(TestClass3.class);
		assertTrue(companion1 == companion2);
	}

	@SuppressWarnings("unused")
	private static final class TestClass1 {
		private TestClass1() {
		}
	}

	@SuppressWarnings("unused")
	private static class TestClass2 {
		protected TestClass2() {
		}
	}

	@SuppressWarnings("unused")
	private static class TestClass3 {
		public TestClass3() {
		}
	}
}
