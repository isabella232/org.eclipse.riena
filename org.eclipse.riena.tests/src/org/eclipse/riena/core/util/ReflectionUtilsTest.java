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

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Test the ReflectionUtils class.
 */
@NonUITestCase
public class ReflectionUtilsTest extends RienaTestCase {

	/**
	 * Test creating an instance #1
	 */
	public void testNewInstance1() {
		final Object o = ReflectionUtils.newInstance("java.lang.String", "Hello!"); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(o instanceof String);
		assertEquals(o, "Hello!"); //$NON-NLS-1$
	}

	/**
	 * Test creating an instance #2
	 */
	public void testNewInstance2() {
		final Object o = ReflectionUtils.newInstance("java.lang.String"); //$NON-NLS-1$
		assertTrue(o instanceof String);
		assertEquals(o, ""); //$NON-NLS-1$
	}

	/**
	 * Test creating an instance #3
	 */
	public void testNewInstance3() {
		final Object o = ReflectionUtils.newInstance("java.awt.Dimension", Integer.valueOf(2), Integer.valueOf(2)); //$NON-NLS-1$
		assertTrue(o instanceof Dimension);
		final Dimension dim = (Dimension) o;
		assertEquals(new Dimension(2, 2), dim);
	}

	/**
	 * Test creating an instance #4
	 */
	public void testNewInstance4() {
		final String s = ReflectionUtils.newInstance(String.class, "Hello!"); //$NON-NLS-1$
		assertEquals(s, "Hello!"); //$NON-NLS-1$
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
			ReflectionUtils.newInstance(ReflectionUtilsHiddenConstructor.class, "something"); //$NON-NLS-1$
			fail("That should not work!"); //$NON-NLS-1$
		} catch (final ReflectionFailure f) {
			Nop.reason("Expected!"); //$NON-NLS-1$
		}
		final Object o = ReflectionUtils.newInstanceHidden(ReflectionUtilsHiddenConstructor.class, "something"); //$NON-NLS-1$
		assertEquals("something", o.toString()); //$NON-NLS-1$
	}

	/**
	 * Test creating an hidden instance #2
	 * */
	public void testNewInstanceHidden2() {
		final Object o = ReflectionUtils.newInstanceHidden(ReflectionUtilsHiddenConstructor.class.getName(), "anything"); //$NON-NLS-1$
		assertEquals("anything", o.toString()); //$NON-NLS-1$
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
		final URL url = new URL("file:c:\\"); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(sysloader, "addURL", url); //$NON-NLS-1$
	}

	public void testInvokeWithNull() {
		final String str = "Hello"; //$NON-NLS-1$
		assertFalse((Boolean) ReflectionUtils.invoke(str, "equals", (Object) null)); //$NON-NLS-1$
	}

	public void testInvokeWithNoObjectParameter() {
		final String str = "Hello"; //$NON-NLS-1$
		assertFalse((Boolean) ReflectionUtils.invoke(str, "equalsIgnoreCase", (String) null)); //$NON-NLS-1$
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
			ReflectionUtils.invokeHidden(object, "setUrl", MalformedURLException.class, "filez://murks.at"); //$NON-NLS-1$ //$NON-NLS-2$
			fail("MalformedURLException expected"); //$NON-NLS-1$
		} catch (final MalformedURLException expected) {
			ok("MalformedURLException expected"); //$NON-NLS-1$
		}
	}

	/**
	 * Tests the method {@code invokeHidden} with primitive and no-primitive types as arguments,
	 */
	public void testInvokeHiddenWithPrimitves() {

		ReflectionUtils.invokeHidden(TestClass.class, "setIntegerObject", 1); //$NON-NLS-1$
		final Integer outInteger = ReflectionUtils.invokeHidden(TestClass.class, "getIntegerObject"); //$NON-NLS-1$
		assertEquals(1, outInteger.intValue());

		ReflectionUtils.invokeHidden(TestClass.class, "setIntPrimitive", 23); //$NON-NLS-1$
		final int outInt = ReflectionUtils.invokeHidden(TestClass.class, "getIntPrimitive"); //$NON-NLS-1$
		assertEquals(23, outInt);

	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testInvokeHiddenClass() {

		ReflectionUtils.invokeHidden(TestClass.class, "setString", new Object[] { "hihi" }); //$NON-NLS-1$ //$NON-NLS-2$
		final String out = (String) ReflectionUtils.invokeHidden(TestClass.class, "getString"); //$NON-NLS-1$
		assertEquals(out, "hihi"); //$NON-NLS-1$
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testInvokeHiddenClassWithIOException() {

		try {
			ReflectionUtils.invokeHidden(Thrower.class, "throwIOException", IOException.class); //$NON-NLS-1$
			fail();
		} catch (final IOException e) {
			assertEquals("Yippie!", e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenInstance() {

		final TestClass testInstance = new TestClass("Hello"); //$NON-NLS-1$
		ReflectionUtils.setHidden(testInstance, "privateString", "Hi"); //$NON-NLS-1$ //$NON-NLS-2$
		final String string = (String) ReflectionUtils.getHidden(testInstance, "privateString"); //$NON-NLS-1$
		assertEquals("Hi", string); //$NON-NLS-1$

		TestClass.setIntPrimitive(1);
		int intValue = TestClass.getIntPrimitive();
		assertEquals(1, intValue);

		ReflectionUtils.setHidden(testInstance, "intPrimitive", Integer.valueOf(2)); //$NON-NLS-1$
		intValue = TestClass.getIntPrimitive();
		assertEquals(2, intValue);

		TestClass.setIntPrimitive(3);
		intValue = (Integer) ReflectionUtils.getHidden(testInstance, "intPrimitive"); //$NON-NLS-1$
		assertEquals(3, intValue);

	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenClass() {

		ReflectionUtils.setHidden(TestClass.class, "string", "hallo"); //$NON-NLS-1$ //$NON-NLS-2$
		final String out = (String) ReflectionUtils.getHidden(TestClass.class, "string"); //$NON-NLS-1$
		assertEquals(out, "hallo"); //$NON-NLS-1$
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenDeepField() {

		final TestTestClass ttc = new TestTestClass("Hallo"); //$NON-NLS-1$
		ReflectionUtils.setHidden(ttc, "privateString", "hurz"); //$NON-NLS-1$ //$NON-NLS-2$
		final String hurz = (String) ReflectionUtils.getHidden(ttc, "privateString"); //$NON-NLS-1$
		assertEquals("hurz", hurz); //$NON-NLS-1$
	}

	/**
	 * Nomen est omen!
	 * 
	 * @throws Throwable
	 */
	public void testSetGetHiddenDeepClassField() throws Throwable {

		ReflectionUtils.setHidden(TestTestClass.class, "string", "hallo"); //$NON-NLS-1$ //$NON-NLS-2$
		final String out = (String) ReflectionUtils.getHidden(TestTestClass.class, "string"); //$NON-NLS-1$
		assertEquals(out, "hallo"); //$NON-NLS-1$
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
		map.put("hi", "there"); //$NON-NLS-1$ //$NON-NLS-2$
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
		map.put("hi", "there"); //$NON-NLS-1$ //$NON-NLS-2$
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
			throw new IOException("Yippie!"); //$NON-NLS-1$
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