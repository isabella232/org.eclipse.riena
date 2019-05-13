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
package org.eclipse.riena.internal.core.test.collect;

import static org.junit.Assert.*;

import org.junit.Test;

import org.eclipse.riena.SelectedTests;
import org.eclipse.riena.core.test.collect.NonUITestCase;

@NonUITestCase
public class SelectedTestsTest {

	private final String emptyString = ""; //$NON-NLS-1$

	@Test
	public void testProcessLineShouldNotBeNull() {
		String testString = ""; //$NON-NLS-1$
		testString = SelectedTests.processLine(testString);
		assertNotNull("return value is null but shouldn't", testString); //$NON-NLS-1$

		testString = null;
		testString = SelectedTests.processLine(testString);
		assertNotNull("return value is null but shouldn't", testString); //$NON-NLS-1$
	}

	@Test
	public void testProcessLineNotEmpty() {
		String javaTestString = "org/eclipse/riena/beans/common/TypedComparatorTest.java,"; //$NON-NLS-1$
		String classTestString = "org/eclipse/riena/beans/common/TypedComparatorTest.class,"; //$NON-NLS-1$

		javaTestString = SelectedTests.processLine(javaTestString);
		assertNotEquals(emptyString, classTestString);

		classTestString = SelectedTests.processLine(classTestString);
		assertNotEquals(emptyString, classTestString);

	}

	@Test
	public void testProcessLine() {
		String javaTestString = "org/eclipse/riena/beans/common/TypedComparatorTest.java"; //$NON-NLS-1$
		String classTestString = "org/eclipse/riena/beans/common/TypedComparatorTest.class"; //$NON-NLS-1$

		javaTestString = SelectedTests.processLine(javaTestString);
		assertEquals("org/eclipse/riena/beans/common/TypedComparatorTest", javaTestString); //$NON-NLS-1$

		classTestString = SelectedTests.processLine(classTestString);
		assertEquals("org/eclipse/riena/beans/common/TypedComparatorTest", javaTestString); //$NON-NLS-1$
	}

	@Test
	public void loadTestCaseFromFilePathReturnsNull() {
		assertNull(SelectedTests.loadTestCaseFromFilePath("")); //$NON-NLS-1$
		assertNull(SelectedTests.loadTestCaseFromFilePath(null));
	}

}
