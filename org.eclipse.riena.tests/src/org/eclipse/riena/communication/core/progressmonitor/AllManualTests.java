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
package org.eclipse.riena.communication.core.progressmonitor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.riena.core.test.collect.NonGatherableTestCase;

/**
 * 
 */
@NonGatherableTestCase("This is not a ´TestCase´!")
public class AllManualTests extends TestCase {

	public static Test suite() {
		final TestSuite suite = new TestSuite(AllManualTests.class.getName());
		suite.addTestSuite(RemoteProgressMonitorITest.class);
		return suite;
	}
}