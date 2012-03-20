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
package org.eclipse.riena.security;

import junit.framework.Test;
import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.IntegrationTestCase;
import org.eclipse.riena.internal.core.test.collect.NonGatherableTestCase;
import org.eclipse.riena.internal.core.test.collect.TestCollector;
import org.eclipse.riena.internal.tests.Activator;

/**
 * Tests all manual test cases within this package.
 */
@NonGatherableTestCase("This is not a ´TestCase´!")
public class AllIntegrationTests extends TestCase {

	@SuppressWarnings("unchecked")
	public static Test suite() {
		return TestCollector.createTestSuiteWith(Activator.getDefault().getBundle(),
				AllIntegrationTests.class.getPackage(), true, IntegrationTestCase.class);
	}

}
