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
package org.eclipse.riena.core.util;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests of the class {@link StringMatcher}.
 */
@NonUITestCase
public class StringMatcherTest extends TestCase {

	/**
	 * Tests the method {@code StringMatcher}
	 */
	public void testMatch() {

		assertFalse(new StringMatcher("").match(null));
		assertTrue(new StringMatcher("").match(""));
		assertTrue(new StringMatcher("*world!").match("Hello world!"));
		assertTrue(new StringMatcher("Hello*").match("Hello world!"));
		assertTrue(new StringMatcher("*world*").match("Hello world!"));
		assertTrue(new StringMatcher("H*ll* w*rld!").match("Hello world!"));
		assertTrue(new StringMatcher("H?ll? w?rld!").match("Hello world!"));
		assertTrue(new StringMatcher("*").match(""));
		assertFalse(new StringMatcher("A").match(""));
		assertFalse(new StringMatcher("Hello").match("Hello world!"));
		assertTrue(new StringMatcher("*Hello*").match("Hello world!"));

	}

}
