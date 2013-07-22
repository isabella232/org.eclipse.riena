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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;

/**
 * Tests of the class {@link Markable}
 */
@NonUITestCase
public class MarkableTest extends TestCase {

	/**
	 * Tests the static method {@code getMarkersOfType}.
	 */
	public void testGetMarkersOfType() {

		assertNotNull(Markable.getMarkersOfType(null, null));
		assertTrue(Markable.getMarkersOfType(null, null).isEmpty());

		final List<IMarker> markers = new ArrayList<IMarker>();
		assertTrue(Markable.getMarkersOfType(markers, null).isEmpty());
		assertTrue(Markable.getMarkersOfType(markers, ErrorMarker.class).isEmpty());

		markers.add(new ErrorMarker());
		assertEquals(1, Markable.getMarkersOfType(markers, ErrorMarker.class).size());
		assertTrue(Markable.getMarkersOfType(markers, DisabledMarker.class).isEmpty());

		markers.add(new ErrorMarker());
		markers.add(new MandatoryMarker());
		assertEquals(2, Markable.getMarkersOfType(markers, ErrorMarker.class).size());
		assertEquals(1, Markable.getMarkersOfType(markers, MandatoryMarker.class).size());
		assertTrue(Markable.getMarkersOfType(markers, DisabledMarker.class).isEmpty());
		assertTrue(Markable.getMarkersOfType(markers, null).isEmpty());

	}

}
