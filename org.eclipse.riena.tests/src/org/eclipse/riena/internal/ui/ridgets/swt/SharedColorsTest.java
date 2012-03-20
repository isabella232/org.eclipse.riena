/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.core.util.InvocationTargetFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;

/**
 * Test the {@code SharedColors}
 */
@UITestCase
public class SharedColorsTest extends RienaTestCase {

	private SharedColors sharedColors;
	private Display display;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		display = Display.getDefault();
		sharedColors = ReflectionUtils.newInstanceHidden(SharedColors.class, display);
	}

	@Override
	protected void tearDown() throws Exception {
		ReflectionUtils.invokeHidden(sharedColors, "dispose");
		super.tearDown();
	}

	public void testGet() {
		assertEquals(new Color(display, new RGB(250, 190, 190)), getSharedColor(SharedColors.COLOR_FLASH_ERROR));
		assertEquals(new Color(display, new RGB(255, 255, 175)), getSharedColor(SharedColors.COLOR_MANDATORY));
		assertEquals(new Color(display, new RGB(255, 249, 216)), getSharedColor(SharedColors.COLOR_MANDATORY_OUTPUT));
		assertEquals(new Color(display, new RGB(255, 0, 0)), getSharedColor(SharedColors.COLOR_NEGATIVE));
		assertEquals(new Color(display, new RGB(231, 233, 245)), getSharedColor(SharedColors.COLOR_OUTPUT));
	}

	public void testGetFailWrongKey() {
		try {
			getSharedColor("hmm");
			fail();
		} catch (final AssertionFailedException e) {
			ok();
		}
	}

	public void testGetFailDisposed() {
		ReflectionUtils.invokeHidden(sharedColors, "dispose");
		try {
			getSharedColor(SharedColors.COLOR_MANDATORY);
			fail();
		} catch (final IllegalArgumentException e) {
			ok();
		}
	}

	private Color getSharedColor(final String key) {
		try {
			return (Color) ReflectionUtils.invokeHidden(sharedColors, "getSharedColor", key);
		} catch (final InvocationTargetFailure e) {
			throw (RuntimeException) e.getTargetException();
		}
	}
}
