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
package org.eclipse.riena.navigation.ui.swt.presentation;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Test of the class <code>SwtViewId</code>.
 */
@NonUITestCase
public class SwtViewIdTest extends RienaTestCase {

	/**
	 * Tests the constructor <code>SwtViewId(String,String)</code>
	 */
	public void testSwtViewIdStringString() {

		final SwtViewId viewId = new SwtViewId("a", "b");
		assertEquals("a", viewId.getId());
		assertEquals("b", viewId.getSecondary());

	}

	/**
	 * Tests the constructor <code>SwtViewId(String)</code>
	 */
	public void testSwtViewIdString() {

		final SwtViewId viewId = new SwtViewId("a:b");
		assertEquals("a", viewId.getId());
		assertEquals("b", viewId.getSecondary());

		try {
			new SwtViewId(null);
			fail("Exception expected");
		} catch (final RuntimeException e) {
			ok("Exception expected");
		}

		try {
			new SwtViewId("ab");
			fail("Exception expected");
		} catch (final RuntimeException e) {
			ok("Exception expected");
		}

		try {
			new SwtViewId(null);
			fail("Exception expected");
		} catch (final RuntimeException e) {
			ok("Exception expected");
		}

	}

	/**
	 * Tests the method <code>getCompound()</code>.
	 */
	public void testGetCompoundId() {

		final SwtViewId viewId = new SwtViewId("a", "b");
		assertEquals("a:b", viewId.getCompoundId());

	}

}
