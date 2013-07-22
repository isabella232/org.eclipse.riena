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
package org.eclipse.riena.core.injector.extension;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.internal.core.injector.extension.ExtensionPointId;

/**
 *
 */
@NonUITestCase
public class ExtensionPointIdTest extends RienaTestCase {

	public void testCreateEmptyExtensionPoint1() {
		final ExtensionPointId id = new ExtensionPointId();
		try {
			id.normalize(IData.class);
		} catch (final IllegalStateException e) {
			Nop.reason("expected");
		}
	}

	public void testCreateEmptyExtensionPoint2() {
		final ExtensionPointId id = new ExtensionPointId(null);
		try {
			id.normalize(IData.class);
		} catch (final IllegalStateException e) {
			Nop.reason("expected");
		}
	}

	public void testCreateEmptyExtensionPoint3() {
		final ExtensionPointId id = new ExtensionPointId("");
		try {
			id.normalize(IData.class);
		} catch (final IllegalStateException e) {
			Nop.reason("expected");
		}
	}

	public void testExtensionPointWithOneFQId() {
		final ExtensionPointId id = new ExtensionPointId("id.one");
		id.normalize(IData.class);
		String result = "";
		for (final String str : id.compatibleIds()) {
			result += str;
		}
		assertEquals("id.one", result);
	}

	public void testExtensionPointWithTwoFQId() {
		final ExtensionPointId id = new ExtensionPointId("id.one,id.two");
		id.normalize(IData.class);
		String result = "";
		for (final String str : id.compatibleIds()) {
			result += str;
		}
		assertEquals("id.oneid.two", result);
	}

	public void testExtensionPointWithTthreeFQId() {
		final ExtensionPointId id = new ExtensionPointId("id.one,id.two,id.three");
		id.normalize(IData.class);
		String result = "";
		for (final String str : id.compatibleIds()) {
			result += str;
		}
		assertEquals("id.oneid.twoid.three", result);
	}

	public void testExtensionPointWithOneSimpleId() {
		final ExtensionPointId id = new ExtensionPointId("one");
		id.normalize(IData.class);
		String result = "";
		for (final String str : id.compatibleIds()) {
			result += str;
		}
		final String symbolicName = getContext().getBundle().getSymbolicName();
		assertEquals(symbolicName + ".one", result);
	}

	public void testExtensionPointWithTwoSimpleId() {
		final ExtensionPointId id = new ExtensionPointId("one,two");
		id.normalize(IData.class);
		String result = "";
		for (final String str : id.compatibleIds()) {
			result += str;
		}
		final String symbolicName = getContext().getBundle().getSymbolicName();
		assertEquals(symbolicName + ".one" + symbolicName + ".two", result);
	}

	public void testExtensionPointWithThreeSimpleId() {
		final ExtensionPointId id = new ExtensionPointId("one,two,three");
		id.normalize(IData.class);
		String result = "";
		for (final String str : id.compatibleIds()) {
			result += str;
		}
		final String symbolicName = getContext().getBundle().getSymbolicName();
		assertEquals(symbolicName + ".one" + symbolicName + ".two" + symbolicName + ".three", result);
	}

	public void testExtensionPointWithMixedId() {
		final ExtensionPointId id = new ExtensionPointId("id.one,two,id.three");
		id.normalize(IData.class);
		String result = "";
		for (final String str : id.compatibleIds()) {
			result += str;
		}
		final String symbolicName = getContext().getBundle().getSymbolicName();
		assertEquals("id.one" + symbolicName + ".two" + "id.three", result);
	}

	public void testFailBecauseOfNoNormalize() {
		final ExtensionPointId id = new ExtensionPointId("id.one,two,id.three");
		try {
			id.compatibleIds();
		} catch (final IllegalStateException e) {
			Nop.reason("ok");
		}
	}
}
