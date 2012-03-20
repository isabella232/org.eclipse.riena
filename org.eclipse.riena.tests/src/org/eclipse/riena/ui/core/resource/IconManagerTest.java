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
package org.eclipse.riena.ui.core.resource;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Tests of the class {@link IconManager}.
 */
@NonUITestCase
public class IconManagerTest extends TestCase {

	/**
	 * Tests the method {@code getIconID(String, IconSize, IconState)};
	 */
	public void testGetIconID() {

		final IIconManager manager = new IconManager();

		String iconID = manager.getIconID(null, null, null);
		assertNull(iconID);

		iconID = manager.getIconID(null, IconSize.B22, IconState.HOVER);
		assertNull(iconID);

		iconID = manager.getIconID("star", IconSize.B22, IconState.HOVER);
		final String expectedID = "star" + IconState.HOVER.getDefaultMapping() + IconSize.B22.getDefaultMapping();
		assertEquals(expectedID, iconID);

	}

	/**
	 * Tests the method {@code getName()}.
	 */
	public void testGetName() {

		final IIconManager manager = new IconManager();
		String name = manager.getName(null);
		assertNull(name);

		name = manager.getName("star");
		assertNull(name);

		String iconID = manager.getIconID("star", null);
		name = manager.getName(iconID);
		assertEquals("star", name);

		iconID = manager.getIconID("star", IconSize.B22, IconState.HOVER);
		name = manager.getName(iconID);
		assertEquals("star", name);

	}

	/**
	 * Tests the method {@code getSize(String)}.
	 */
	public void testGetSize() {

		final IIconManager manager = new IconManager();
		IconSize size = manager.getSize(null);
		assertNull(size);

		size = manager.getSize("star");
		assertNull(size);

		String iconID = manager.getIconID("star", null);
		size = manager.getSize(iconID);
		assertNull(size);

		iconID = manager.getIconID("star", IconSize.B22, IconState.HOVER);
		size = manager.getSize(iconID);
		assertEquals(IconSize.B22, size);

		iconID = manager.getIconID("moon", IconSize.NONE);
		size = manager.getSize(iconID);
		assertEquals(IconSize.NONE, size);

	}

	/**
	 * Tests the method {@code getState(String)}.
	 */
	public void testGetState() {

		final IIconManager manager = new IconManager();
		IconState state = manager.getState(null);
		assertNull(state);

		state = manager.getState("star");
		assertNull(state);

		String iconID = manager.getIconID("star", null);
		state = manager.getState(iconID);
		assertEquals(IconState.NORMAL, state);

		iconID = manager.getIconID("star", IconSize.B22, IconState.HOVER);
		state = manager.getState(iconID);
		assertEquals(IconState.HOVER, state);

		iconID = manager.getIconID("moon", IconSize.NONE);
		state = manager.getState(iconID);
		assertEquals(IconState.NORMAL, state);

	}

	/**
	 * Tests the method {@code hasExtension(String)}.
	 */
	public void testHasExtension() {

		final IIconManager manager = new IconManager();

		String abc = "123";
		assertFalse(manager.hasExtension(abc));

		abc = ".";
		assertFalse(manager.hasExtension(abc));

		abc = "1.";
		assertTrue(manager.hasExtension(abc));

		abc = "1.2";
		assertTrue(manager.hasExtension(abc));

		abc = ".2";
		assertFalse(manager.hasExtension(abc));

	}

}
