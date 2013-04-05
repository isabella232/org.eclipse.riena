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
package org.eclipse.riena.internal.core.test.collect.testpackage;

import static org.junit.Assert.*;

import org.junit.Test;

import org.eclipse.riena.internal.core.test.collect.TestCollector;
import org.eclipse.riena.internal.core.test.collect.TestCollectorTest;

/**
 * A dummy JUnit4 test case which should be found by the {@link TestCollector} <br/>
 * This class is related to the {@link TestCollectorTest}
 */
public class JUnit4DummyTest {

	/**
	 * We need at least one real test since this class will also be run together with all other tests by the real {@link TestCollector} <br/>
	 * (avoid "there are no tests" error)
	 */
	@Test
	public void testSuccess() throws Exception {
		assertTrue("everything is just fine", true); //$NON-NLS-1$
	}
}
