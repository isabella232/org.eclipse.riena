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
package org.eclipse.riena.core.marker;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;

/**
 * Tests of the class {@link AbstractMarker}.
 */
@NonUITestCase
public class AbstractMarkerTest extends RienaTestCase {

	/**
	 * Test of the constructor {@code AbstractMarker()}.
	 */
	public void testAbstractMarker() {

		final AbstractMarker marker = new DummyMarker();
		assertTrue(marker.isUnique());

	}

	/**
	 * Test of the method {@code toString()}.
	 */
	public void testToString() {

		AbstractMarker marker = new DummyMarker();
		assertEquals("DummyMarker[attributes={unique=true}]", marker.toString()); //$NON-NLS-1$

		marker.setAttribute("att", "val"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("DummyMarker[attributes={unique=true, att=val}]", marker.toString()); //$NON-NLS-1$

		marker = new DummyMarker(false);
		assertEquals("DummyMarker[attributes={}]", marker.toString()); //$NON-NLS-1$

	}

	/**
	 * Test of the method {@code setAttribute}.
	 */
	public void testSetAttribute() {

		final DummyMarker marker = new DummyMarker();
		assertNull(marker.getAttribute("lastname")); //$NON-NLS-1$

		marker.reset();
		marker.setAttribute("lastname", "Gate"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Gate", marker.getAttribute("lastname")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(marker.isChangeFired());

		marker.reset();
		marker.setAttribute("lastname", "Ballmer"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Ballmer", marker.getAttribute("lastname")); //$NON-NLS-1$ //$NON-NLS-2$
		assertTrue(marker.isChangeFired());

		marker.reset();
		marker.setAttribute("lastname", null); //$NON-NLS-1$
		assertNull(marker.getAttribute("lastname")); //$NON-NLS-1$
		assertTrue(marker.isChangeFired());

		marker.reset();
		marker.setAttribute("lastname", 12); //$NON-NLS-1$
		assertEquals(Integer.valueOf(12), marker.getAttribute("lastname")); //$NON-NLS-1$
		assertTrue(marker.isChangeFired());

		marker.reset();
		try {
			marker.setAttribute(null, 12);
			fail("Exception expected"); //$NON-NLS-1$
		} catch (final Exception e) {
			ok("Exception expected"); //$NON-NLS-1$
		}
		assertFalse(marker.isChangeFired());

	}

	/**
	 * Test of the method {@code getAttribute(String, Object)}.
	 */
	public void testGetAttribute() {

		final DummyMarker marker = new DummyMarker();

		assertEquals("unkown", marker.getAttribute("lastname", "unkown")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		marker.setAttribute("lastname", "Gate"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("Gate", marker.getAttribute("lastname", "unkown")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		marker.setAttribute("lastname", null); //$NON-NLS-1$
		assertEquals("unkown", marker.getAttribute("lastname", "unkown")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	}

	private static class DummyMarker extends AbstractMarker implements IMarkerAttributeChangeListener {

		private boolean changeFired;

		public DummyMarker() {
			super();
			init();
		}

		public DummyMarker(final boolean unique) {
			super(unique);
			init();
		}

		private void init() {
			addAttributeChangeListener(this);
			reset();
		}

		public void attributesChanged() {
			changeFired = true;
		}

		public void reset() {
			changeFired = false;
		}

		public boolean isChangeFired() {
			return changeFired;
		}

	}

}
