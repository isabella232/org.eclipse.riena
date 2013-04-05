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
package org.eclipse.riena.internal.core.test;

import junit.framework.Assert;

import org.eclipse.riena.internal.core.test.TestingTools.TestCaseWrapper;

/**
 *
 */
public class JUnit4Wrapper implements TestCaseWrapper {

	private final RienaTestCaseJUnit4 testCase;

	/**
	 * @param rienaTestCaseJUnit4
	 */
	public JUnit4Wrapper(final RienaTestCaseJUnit4 testCase) {
		this.testCase = testCase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.internal.core.test.TestingTools.TestCaseWrapper#getName()
	 */
	public String getName() {
		return testCase.name.getMethodName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.internal.core.test.TestingTools.TestCaseWrapper#fail(java.lang.String)
	 */
	public void fail(final String string) {
		Assert.fail(string);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.internal.core.test.TestingTools.TestCaseWrapper#assertTrue(boolean)
	 */
	public void assertTrue(final boolean success) {
		Assert.assertTrue(success);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.internal.core.test.TestingTools.TestCaseWrapper#assertNotNull(java.lang.Object)
	 */
	public void assertNotNull(final Object o) {
		Assert.assertNotNull(o);
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
