/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.marker;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.MessageMarker;

/**
 * Tests of the class {@link AbstractMessageMarkerViewer}.
 */
@NonUITestCase
public class AbstractMessageMarkerViewerTest extends TestCase {

	/**
	 * Tests the method {@code constructMessage}.
	 */
	public void testConstructMessage() {
		final List<IMessageMarker> messageMarkers = new ArrayList<IMessageMarker>();

		String message = AbstractMessageMarkerViewer.constructMessage(messageMarkers, "*");
		assertEquals("", message);

		messageMarkers.add(new MessageMarker("one"));
		message = AbstractMessageMarkerViewer.constructMessage(messageMarkers, "*");
		assertEquals("one", message);

		messageMarkers.add(new MessageMarker("  Two  "));
		message = AbstractMessageMarkerViewer.constructMessage(messageMarkers, "*");
		assertEquals("one*  Two", message);

		messageMarkers.add(new MessageMarker("THREE"));
		message = AbstractMessageMarkerViewer.constructMessage(messageMarkers, "*");
		assertEquals("one*  Two  *THREE", message);

		messageMarkers.add(new MessageMarker("  "));
		message = AbstractMessageMarkerViewer.constructMessage(messageMarkers, "*");
		assertEquals("one*  Two  *THREE*", message);

	}

}
