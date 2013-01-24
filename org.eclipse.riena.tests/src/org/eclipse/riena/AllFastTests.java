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
package org.eclipse.riena;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.riena.internal.core.test.collect.NonGatherableTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.core.test.collect.TestCollector;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 * Tests all 'fast' test cases within this bundle.
 * <p>
 * All test cases taking more than 10 seconds are skipped. It removes about 20% of the tests that take 80% of the time. Currently this suite runs in 75 seconds.
 * You should not rely solely on the results of this suite, however it should be useful if you want to run many tests quite frequently.
 */
@NonGatherableTestCase("This is not a ´TestCase´!")
public class AllFastTests extends TestCase {

	private static Set<String> longTests;

	static {
		longTests = new HashSet<String>();
		longTests.add("DateTextRidgetTest");
		longTests.add("DecimalTextRidgetTest");
		longTests.add("NumericTextRidgetTest");
		longTests.add("TextRidgetTest2");
		longTests.add("DateTimeRidgetTest");
		// longTests.add("TreeRidgetTest2");
		// longTests.add("TreeTableRidgetTest");
		longTests.add("CheckTestConstraintsTest");
	};

	@SuppressWarnings("unchecked")
	public static Test suite() {
		final TestSuite collected = TestCollector
				.createTestSuiteWithJUnit3And4(Activator.getDefault().getBundle(), null, UITestCase.class, NonUITestCase.class);
		final Enumeration<Test> tests = collected.tests();
		final TestSuite result = new TestSuite();
		while (tests.hasMoreElements()) {
			final Test test = tests.nextElement();
			if (!longTests.contains(getTestName(test))) {
				result.addTest(test);
			} else {
				System.err.println("...skipping long test: " + test.toString());
			}
		}
		return result;
	}

	// helping methods
	//////////////////

	private static String getTestName(final Test test) {
		String result = test.toString();
		final int lastDot = result.lastIndexOf('.');
		if (lastDot != -1) {
			result = result.substring(lastDot + 1);
		}
		return result;
	}
}
