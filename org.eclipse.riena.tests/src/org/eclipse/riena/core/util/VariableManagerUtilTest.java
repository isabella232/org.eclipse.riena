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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.internal.variables.StringVariableManager;
import org.eclipse.core.runtime.CoreException;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the <code>ConfigSymbolReplace</code> class.
 */
@SuppressWarnings("restriction")
@NonUITestCase
public class VariableManagerUtilTest extends RienaTestCase {

	private static Field fgManager;
	private static final String WWW_ECLIPSE_ORG = "www.eclipse.org";

	static {
		try {
			fgManager = StringVariableManager.class.getDeclaredField("fgManager");
			fgManager.setAccessible(true);
		} catch (final Throwable e) {
			fail("Could not access field ´fgManager´ from StringVariableManager!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.RienaTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// Need every time a fresh manager
		fgManager.set(null, null);
	}

	public void testReolveNullExpression() throws CoreException {
		assertNull(VariableManagerUtil.substitute(null));
	}

	public void testReolveEmptyExpression() throws CoreException {
		assertEquals("", VariableManagerUtil.substitute(""));
	}

	public void testReolveUnknownVariable() throws CoreException {
		try {
			VariableManagerUtil.substitute("${micky.mouse}");
			fail("Expected CoreException");
		} catch (final CoreException e) {
			ok();
		}
	}

	public void testUpdatedWrongKey() throws CoreException {
		VariableManagerUtil.addVariable("host", "${host");
		assertEquals("${host", VariableManagerUtil.substitute("${host}"));
	}

	public void testUpdatedRecursiveKeyDirectly() {
		try {
			VariableManagerUtil.addVariable("host", "${host}");
			VariableManagerUtil.substitute("${host}");
			fail();
		} catch (final CoreException e) {
			assertTrue(e.getMessage().contains("host"));
		}
	}

	public void testUpdatedRecursiveKeyIndirectly1() {
		try {
			VariableManagerUtil.addVariable("a", "${b}");
			VariableManagerUtil.addVariable("b", "${a}");
			VariableManagerUtil.substitute("${a}");
			fail();
		} catch (final CoreException e) {
			final String problemVariableList = e.getMessage().substring(e.getMessage().indexOf(",") - 1,
					e.getMessage().indexOf(",") + 3);
			assertTrue(problemVariableList.contains("a"));
			assertTrue(problemVariableList.contains("b"));
		}
	}

	public void testUpdatedRecursiveKeyIndirectly2() {
		try {
			VariableManagerUtil.addVariable("a", "${b}");
			VariableManagerUtil.addVariable("b", "${c}");
			VariableManagerUtil.addVariable("c", "${a}");
			VariableManagerUtil.substitute("${a}");
			fail();
		} catch (final CoreException e) {
			final String problemVariableList = e.getMessage().substring(e.getMessage().indexOf(",") - 1,
					e.getMessage().indexOf(",") + 6);
			assertTrue(problemVariableList.contains("a"));
			assertTrue(problemVariableList.contains("b"));
			assertTrue(problemVariableList.contains("c"));
		}
	}

	public void testModifyWrongKey() throws CoreException {
		VariableManagerUtil.addVariable("host", WWW_ECLIPSE_ORG);
		assertEquals("${host", VariableManagerUtil.substitute("${host"));
	}

	public void testModifyEmptyKey() throws CoreException {
		VariableManagerUtil.addVariable("", WWW_ECLIPSE_ORG);
		assertEquals(WWW_ECLIPSE_ORG, VariableManagerUtil.substitute("${}"));
	}

	public void testModifySingle() throws CoreException {
		VariableManagerUtil.addVariable("host", WWW_ECLIPSE_ORG);
		assertEquals(WWW_ECLIPSE_ORG, VariableManagerUtil.substitute("${host}"));
	}

	public void testModifyDoubleSequentialy() throws CoreException {
		VariableManagerUtil.addVariable("host", WWW_ECLIPSE_ORG);
		assertEquals(WWW_ECLIPSE_ORG + WWW_ECLIPSE_ORG, VariableManagerUtil.substitute("${host}${host}"));
	}

	public void testModifyDoubleNested() throws CoreException {
		VariableManagerUtil.addVariable("url", "http://${host}/path");
		VariableManagerUtil.addVariable("host", WWW_ECLIPSE_ORG);
		assertEquals("http://" + WWW_ECLIPSE_ORG + "/path", VariableManagerUtil.substitute("${url}"));
	}

	public void testRemoveVariable() throws CoreException {
		VariableManagerUtil.addVariable("a", "1");
		assertEquals("1", VariableManagerUtil.substitute("${a}"));
		VariableManagerUtil.removeVariable("a");
		try {
			VariableManagerUtil.substitute("${a}");
			fail();
		} catch (final CoreException e) {
			ok();
		}
	}

	public void testAddMultipleVariables() throws CoreException {
		final Map<String, String> toAdd = new HashMap<String, String>();
		toAdd.put("a", "1");
		toAdd.put("b", "2");
		toAdd.put("c", "3");
		VariableManagerUtil.addVariables(toAdd);
		assertEquals("123", VariableManagerUtil.substitute("${a}${b}${c}"));
	}

	public void testAddMultipleVariablesSomeTwiceButOkBecauseTheyAreEqual() throws CoreException {
		Map<String, String> toAdd = new HashMap<String, String>();
		toAdd.put("a", "1");
		toAdd.put("b", "2");
		toAdd.put("c", "3");
		VariableManagerUtil.addVariables(toAdd);
		assertEquals("123", VariableManagerUtil.substitute("${a}${b}${c}"));
		toAdd = new HashMap<String, String>();
		toAdd.put("d", "4");
		toAdd.put("b", "2");
		toAdd.put("e", "5");
		VariableManagerUtil.addVariables(toAdd);
		assertEquals("12345", VariableManagerUtil.substitute("${a}${b}${c}${d}${e}"));
	}

	public void testAddMultipleVariablesSomeTwiceButNotOkBecauseTheyAreNotEqualSoThatItShouldFail()
			throws CoreException {
		Map<String, String> toAdd = new HashMap<String, String>();
		toAdd.put("a", "1");
		toAdd.put("b", "2");
		toAdd.put("c", "3");
		VariableManagerUtil.addVariables(toAdd);
		assertEquals("123", VariableManagerUtil.substitute("${a}${b}${c}"));
		toAdd = new HashMap<String, String>();
		toAdd.put("d", "4");
		toAdd.put("b", "6");
		toAdd.put("e", "5");
		try {
			VariableManagerUtil.addVariables(toAdd);
		} catch (final CoreException e) {
			ok();
		}
	}

}
