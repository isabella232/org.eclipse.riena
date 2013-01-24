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

import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the {@code Literal} class.
 */
@NonUITestCase
public class LiteralTest extends TestCase {

	public void testCreateLiteralMap() {
		final Map<String, Integer> kids = Literal.map("Emil", 2).map("Anja", 5);
		assertEquals(2, kids.size());
		assertEquals(2, (int) kids.get("Emil"));
		assertEquals(5, (int) kids.get("Anja"));
	}

	public void testCreateLiteralList() {
		final List<String> kids = Literal.list("Emil").list("Anja");
		assertEquals(2, kids.size());
		assertEquals("Emil", kids.get(0));
		assertEquals("Anja", kids.get(1));
	}

	public void testCreateLiteralSet() {
		final Set<String> kids = Literal.set("Emil").set("Anja");
		assertEquals(2, kids.size());
		assertTrue(kids.contains("Emil"));
		assertTrue(kids.contains("Anja"));
	}

}
