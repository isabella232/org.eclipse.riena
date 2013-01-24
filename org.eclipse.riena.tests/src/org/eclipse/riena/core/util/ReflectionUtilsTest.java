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

import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Test the ReflectionUtils class.
 */
@NonUITestCase
public class ReflectionUtilsTest extends RienaTestCase {

	/**
	 * Test creating an instance #1
	 */
	public void testNewInstance1() {
		final Object o = ReflectionUtils.newInstance("java.lang.String", "Hello!");
		assertTrue(o instanceof String);
		assertEquals(o, "Hello!");
	}

	/**
	 * Test creating an instance #2
	 */
	public void testNewInstance2() {
		final Object o = ReflectionUtils.newInstance("java.lang.String");
		assertTrue(o instanceof String);
		assertEquals(o, "");
	}

	/**
	 * Test creating an instance #3
	 */
	public void testNewInstance3() {
		final Object o = ReflectionUtils.newInstance("java.awt.Dimension", Integer.valueOf(2), Integer.valueOf(2));
		assertTrue(o instanceof Dimension);
		final Dimension dim = (Dimension) o;
		assertEquals(new Dimension(2, 2), dim);
	}

	/**
	 * Test creating an instance #4
	 */
	public void testNewInstance4() {
		final String s = ReflectionUtils.newInstance(String.class, "Hello!");
		assertEquals(s, "Hello!");
	}

	public void testNewInstance5() {
		final MockClass s = ReflectionUtils.newInstance(MockClass.class, (String) null);
		assertNull(s.getName());
	}

	/**
	 * Test creating an hidden instance #1
	 * */
	public void testNewInstanceHidden1() {
		try {
			// This does not work
			ReflectionUtils.newInstance(ReflectionUtilsHiddenConstructor.class, "something");
			fail("That should not work!");
		} catch (final ReflectionFailure f) {
			Nop.reason("Expected!");
		}
		final Object o = ReflectionUtils.newInstanceHidden(ReflectionUtilsHiddenConstructor.class, "something");
		assertEquals("something", o.toString());
	}

	/**
	 * Test creating an hidden instance #2
	 * */
	public void testNewInstanceHidden2() {
		final Object o = ReflectionUtils
				.newInstanceHidden(ReflectionUtilsHiddenConstructor.class.getName(), "anything");
		assertEquals("anything", o.toString());
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws MalformedURLException
	 * 
	 * @throws Throwable
	 */
	public void testInvokeHidden() throws MalformedURLException {

		final URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		final URL url = new URL("file:c:\\");
		ReflectionUtils.invokeHidden(sysloader, "addURL", url);
	}

	public void testInvokeWithNull() {
		final String str = "Hello";
		assertFalse((Boolean) ReflectionUtils.invoke(str, "equals", (Object) null));
	}

	public void testInvokeWithNoObjectParameter() {
		final String str = "Hello";
		assertFalse((Boolean) ReflectionUtils.invoke(str, "equalsIgnoreCase", (String) null));
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws MalformedURLException
	 * 
	 * @throws Throwable
	 */
	public void testInvokeHiddenWithMalformedURLException() {

		final Object object = new Thrower();
		try {
			ReflectionUtils.invokeHidden(object, "setUrl", MalformedURLException.class, "filez://murks.at");
			fail("MalformedURLException expected");
		} catch (final MalformedURLException expected) {
			ok("MalformedURLException expected");
		}
	}

	/**
	 * Tests the method {@code invokeHidden} with primitive and no-primitive
	 * types as arguments,
	 */
	public void testInvokeHiddenWithPrimitves() {

		ReflectionUtils.invokeHidden(TestClass.class, "setIntegerObject", 1);
		final Integer outInteger = ReflectionUtils.invokeHidden(TestClass.class, "getIntegerObject");
		assertEquals(1, outInteger.intValue());

		ReflectionUtils.invokeHidden(TestClass.class, "setIntPrimitive", 23);
		final int outInt = ReflectionUtils.invokeHidden(TestClass.class, "getIntPrimitive");
		assertEquals(23, outInt);

	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testInvokeHiddenClass() {

		ReflectionUtils.invokeHidden(TestClass.class, "setString", new Object[] { "hihi" });
		final String out = (String) ReflectionUtils.invokeHidden(TestClass.class, "getString");
		assertEquals(out, "hihi");
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testInvokeHiddenClassWithIOException() {

		try {
			ReflectionUtils.invokeHidden(Thrower.class, "throwIOException", IOException.class);
			fail();
		} catch (final IOException e) {
			assertEquals("Yippie!", e.getMessage());
		}
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenInstance() {

		final String s = "Hallo";
		ReflectionUtils.setHidden(s, "count", Integer.valueOf(2));
		final Integer count = (Integer) ReflectionUtils.getHidden(s, "count");
		assertEquals(2, count.intValue());
		assertEquals(s, "Ha");
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenClass() {

		ReflectionUtils.setHidden(TestClass.class, "string", "hallo");
		final String out = (String) ReflectionUtils.getHidden(TestClass.class, "string");
		assertEquals(out, "hallo");
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenDeepField() {

		final TestTestClass ttc = new TestTestClass("Hallo");
		ReflectionUtils.setHidden(ttc, "privateString", "hurz");
		final String hurz = (String) ReflectionUtils.getHidden(ttc, "privateString");
		assertEquals("hurz", hurz);
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenDeepClassField() throws Throwable {

		ReflectionUtils.setHidden(TestTestClass.class, "string", "hallo");
		final String out = (String) ReflectionUtils.getHidden(TestTestClass.class, "string");
		assertEquals(out, "hallo");
	}

	/**
	 * Nomen est Omen!
	 */
	@SuppressWarnings("unchecked")
	public void testNewLazyInstanceByClass() {
		// TODO warning suppression. Ignoring FindBugs problem about writing
		// static field. This test class uses some bad practices to test the
		// evil stuff done by the class ReflectionUtils that itself should be
		// used only in other tests.
		HaeshMaep.created = false;
		final Map<String, String> map = ReflectionUtils.newLazyInstance(Map.class, HaeshMaep.class);
		assertFalse(HaeshMaep.created);
		map.put("hi", "there");
		assertTrue(HaeshMaep.created);
	}

	/**
	 * Nomen est Omen!
	 */
	@SuppressWarnings("unchecked")
	public void testNewLazyInstanceByString() {
		// TODO warning suppression. Ignoring FindBugs problem about writing
		// static field. This test class uses some bad practices to test the
		// evil stuff done by the class ReflectionUtils that itself should be
		// used only in other tests.
		HaeshMaep.created = false;
		final Map<String, String> map = ReflectionUtils.newLazyInstance(Map.class, HaeshMaep.class.getName());
		assertFalse(HaeshMaep.created);
		map.put("hi", "there");
		assertTrue(HaeshMaep.created);
	}

	// Ignoring Checkstyle warning that this class should be final since it
	// has a subclass (below).
	private static class TestClass {

		@SuppressWarnings("unused")
		private final String privateString;
		private static String string;
		private static Integer integerObject;
		private static int intPrimitive;

		private TestClass(final String str) {
			privateString = str;
		}

		@SuppressWarnings("unused")
		private static void setString(final String string) {
			TestClass.string = string;
		}

		@SuppressWarnings("unused")
		private static String getString() {
			return string;
		}

		@SuppressWarnings("unused")
		private static void setIntegerObject(final Integer integerObject) {
			TestClass.integerObject = integerObject;
		}

		@SuppressWarnings("unused")
		private static Integer getIntegerObject() {
			return integerObject;
		}

		@SuppressWarnings("unused")
		private static void setIntPrimitive(final int intPrimitive) {
			TestClass.intPrimitive = intPrimitive;
		}

		@SuppressWarnings("unused")
		private static int getIntPrimitive() {
			return intPrimitive;
		}

	}

	private static final class TestTestClass extends TestClass {
		private static String stringString;

		private TestTestClass(final String str) {
			super(str);
		}

		@SuppressWarnings("unused")
		private static void setString(final String string) {
			TestTestClass.stringString = string;
		}

		@SuppressWarnings("unused")
		private static String getString() {
			return stringString;
		}
	}

	private final static class Thrower {

		private Thrower() {
		}

		@SuppressWarnings("unused")
		private void setUrl(final String url) throws MalformedURLException {
			new URL(url);
		}

		@SuppressWarnings("unused")
		private static String throwIOException() throws IOException {
			throw new IOException("Yippie!");
		}
	}

	// TODO warning suppression. Ignoring FindBugs problem about HaeshMaep
	// being public. Making it package protected causes a test failure.
	public static class HaeshMaep<K, V> extends HashMap<K, V> {

		private static final long serialVersionUID = 1L;
		protected static boolean created;

		/**
		 * 
		 */
		public HaeshMaep() {
			super();
			// TODO warning suppression. Ignoring FindBugs problem about writing
			// static field. This test class uses some bad practices to test the
			// evil stuff done by the class ReflectionUtils that itself should be
			// used only in other tests.
			created = true;
		}

	}

	public final static class MockClass {
		private final String name;

		public MockClass(final String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}