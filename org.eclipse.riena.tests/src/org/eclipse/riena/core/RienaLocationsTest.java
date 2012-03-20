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
package org.eclipse.riena.core;

import java.io.File;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 * Test the {@code RienaLocations} class.
 */
@NonUITestCase
public class RienaLocationsTest extends TestCase {

	public void testGetDataArea() {
		final File dataArea = RienaLocations.getDataArea();
		assertTrue(dataArea.isDirectory());
		final File expected = new File(Platform.getInstanceLocation().getURL().getFile());
		assertEquals(expected, dataArea);
	}

	public void testGetDataAreaForBundle() {
		final File dataArea = RienaLocations.getDataArea(Activator.getDefault().getBundle());
		assertTrue(dataArea.isDirectory());
		assertEquals(new File(new File(Platform.getInstanceLocation().getURL().getFile()), Activator.getDefault()
				.getBundle().getSymbolicName()), dataArea);
	}

	public void testGetUserArea() {
		assertEquals(System.getProperty("user.home"), RienaLocations.getUserArea().toString());
	}
}
