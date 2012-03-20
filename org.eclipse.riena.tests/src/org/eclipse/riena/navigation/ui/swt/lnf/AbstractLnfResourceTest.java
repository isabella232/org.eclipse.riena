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
package org.eclipse.riena.navigation.ui.swt.lnf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.swt.lnf.AbstractLnfResource;

/**
 * Tests of the class <code>AbstractLnfResource</code>.
 */
@NonUITestCase
public class AbstractLnfResourceTest extends TestCase {

	private MockLnfResource lnfResource;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		lnfResource = new MockLnfResource();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		lnfResource = null;
	}

	/**
	 * Tests the class <code>dispose()</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testDispose() throws Exception {

		final Color color = lnfResource.getResource();
		assertNotNull(color);
		assertFalse(color.isDisposed());

		assertFalse(color.isDisposed());

	}

	/**
	 * Tests the class <code>GetResource()</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetResource() throws Exception {

		final Color color1 = lnfResource.getResource();
		assertNotNull(color1);
		assertFalse(color1.isDisposed());

		color1.dispose();

		final Color color2 = lnfResource.getResource();
		assertNotNull(color2);
		assertFalse(color2.isDisposed());
		assertNotSame(color1, color2);

	}

	public void testThatTheResourceWillNotBeDisposedIfStillInUse() throws IOException {
		final Color color1 = lnfResource.getResource();
		assertFalse(color1.isDisposed());
		assertEquals(1, color1.getRed());

		runOutOfMemory();

		lnfResource.setRed(255);
		final Color color2 = lnfResource.getResource();
		assertEquals(1, color2.getRed());
		assertSame(color1, color2);
	}

	private static class MockLnfResource extends AbstractLnfResource<Color> {

		private int red = 1;

		public void setRed(final int red) {
			this.red = red;
		}

		/**
		 * @see org.eclipse.riena.ui.swt.lnf.ILnfResource#createResource()
		 */
		public Color createResource() {
			return new Color(Display.getDefault(), red, 1, 1);
		}

	}

	private void runOutOfMemory() throws IOException {
		try {
			final OutputStream os = new ByteArrayOutputStream();
			while (true) {
				os.write(new byte[1024 * 1024]);
			}
		} catch (final OutOfMemoryError e) {
			System.gc();
		}
	}
}
