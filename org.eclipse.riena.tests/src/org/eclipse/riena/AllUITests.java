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

import junit.framework.Test;
import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonGatherableTestCase;
import org.eclipse.riena.core.test.collect.TestCollector;
import org.eclipse.riena.core.test.collect.UITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 * Tests all test cases within this bundle related to UI.
 */
@NonGatherableTestCase("This is not a ´TestCase´!")
public class AllUITests extends TestCase {

	@SuppressWarnings("unchecked")
	public static Test suite() {
		return TestCollector.createTestSuiteWithJUnit3And4(Activator.getDefault().getBundle(), null, UITestCase.class);
	}

}
