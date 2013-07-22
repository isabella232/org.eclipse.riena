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
package org.eclipse.riena.core.test;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.TestingTools.TestCaseWrapper;

/**
 * @since 5.0
 */
public class JUnit3Wrapper implements TestCaseWrapper {

	private final RienaTestCase testCase;

	JUnit3Wrapper(final RienaTestCase testCase) {
		this.testCase = testCase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.internal.core.test.TestingTools.TestCaseWrapper#getName()
	 */
	public String getName() {
		return testCase.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.internal.core.test.TestingTools.TestCaseWrapper#fail(java.lang.String)
	 */
	public void fail(final String string) {
		TestCase.fail(string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.internal.core.test.TestingTools.TestCaseWrapper#assertTrue(boolean)
	 */
	public void assertTrue(final boolean success) {
		TestCase.assertTrue(success);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.internal.core.test.TestingTools.TestCaseWrapper#assertNotNull(java.lang.Object)
	 */
	public void assertNotNull(final Object o) {
		TestCase.assertNotNull(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.internal.core.test.TestingTools.TestCaseWrapper#getTestClass()
	 */
	public Class<?> getTestClass() {
		return testCase.getClass();
	}
}
