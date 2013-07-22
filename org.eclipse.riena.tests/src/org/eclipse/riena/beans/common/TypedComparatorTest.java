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
package org.eclipse.riena.beans.common;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Test the {@code TypedComparator} class.
 */
@NonUITestCase
public class TypedComparatorTest extends TestCase {

	/**
	 * Tests the method {@code compare()} with <code>null</code> values.
	 */
	public void testNullValues() {

		final TypedComparator<String> tc = new TypedComparator<String>();
		assertEquals(0, tc.compare(null, null));
		assertEquals(1, tc.compare("test", null)); //$NON-NLS-1$
		assertEquals(1, tc.compare("", null)); //$NON-NLS-1$
		assertEquals(-1, tc.compare(null, "test")); //$NON-NLS-1$
		assertEquals(-1, tc.compare(null, "")); //$NON-NLS-1$
	}
}
