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
package org.eclipse.riena.ui.swt.lnf;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.UIProcessFinishedMarker;

/**
 * Tests of the class {@link FlasherSupportForRenderer}.
 */
@NonUITestCase
public class FlasherSupportForRendererTest extends TestCase {

	/**
	 * Tests the method {@code isProcessMarkerVisible()}.
	 */
	public void testIsProcessMarkerVisible() {

		final ILnfRenderer renderer = new MockRenderer();
		final FlasherSupportForRenderer support = new FlasherSupportForRenderer(renderer, null);

		final Collection<IMarker> markers = new ArrayList<IMarker>();
		renderer.setMarkers(markers);

		assertFalse(support.isProcessMarkerVisible());

		markers.add(new ErrorMarker());
		assertFalse(support.isProcessMarkerVisible());

		final UIProcessFinishedMarker finishedMarker = new UIProcessFinishedMarker();
		markers.add(finishedMarker);
		finishedMarker.setOn(true);
		assertTrue(support.isProcessMarkerVisible());

		finishedMarker.setOn(false);
		assertFalse(support.isProcessMarkerVisible());

	}

	/**
	 * This renderer doesn't paint anything. It only collects the markers.
	 */
	private static class MockRenderer extends AbstractLnfRenderer {

		public void dispose() {
		}

	}

}
