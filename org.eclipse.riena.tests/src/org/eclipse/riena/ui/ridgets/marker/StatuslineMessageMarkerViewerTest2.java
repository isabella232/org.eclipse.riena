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
package org.eclipse.riena.ui.ridgets.marker;

import java.util.ArrayList;
import java.util.Collection;

import org.easymock.EasyMock;

import org.eclipse.riena.core.test.RienaTestCase;
import org.eclipse.riena.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;

/**
 * Pure Unittest for the {@link StatuslineMessageMarkerViewer} that doesn't use
 * swt-controls for testing.
 */
@NonUITestCase
public class StatuslineMessageMarkerViewerTest2 extends RienaTestCase {

	private static final String MESSAGE = "message";
	private StatuslineMessageMarkerViewerUnderTest viewer;
	private IStatuslineRidget statuslineRidget;
	private boolean hasRidgetFocus = true;
	private IMarkableRidget markableRidget;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		statuslineRidget = EasyMock.createNiceMock(IStatuslineRidget.class);
		viewer = new StatuslineMessageMarkerViewerUnderTest(statuslineRidget);

		markableRidget = EasyMock.createNiceMock(IMarkableRidget.class);
		final Collection<ErrorMessageMarker> marker = new ArrayList<ErrorMessageMarker>();
		marker.add(new ErrorMessageMarker(MESSAGE));
		EasyMock.expect(markableRidget.hasFocus()).andReturn(hasRidgetFocus).anyTimes();
		EasyMock.expect(markableRidget.getMarkersOfType(ErrorMessageMarker.class)).andReturn(marker).anyTimes();
		EasyMock.replay(markableRidget);
	}

	public void testShowMessagesWithFocus() throws Exception {
		statuslineRidget.error(MESSAGE);
		EasyMock.replay(statuslineRidget);

		viewer.showMessages(markableRidget);
		EasyMock.verify(statuslineRidget);
	}

	public void testShowMessagesWithoutFocus() throws Exception {
		hasRidgetFocus = false;
		EasyMock.replay(statuslineRidget);

		viewer.showMessages(markableRidget);
		EasyMock.verify(statuslineRidget);
	}

	private class StatuslineMessageMarkerViewerUnderTest extends StatuslineMessageMarkerViewer {
		public StatuslineMessageMarkerViewerUnderTest(final IStatuslineRidget statuslineRidget) {
			super(statuslineRidget);
		}

		@Override
		public void showMessages(final IBasicMarkableRidget markableRidget) {
			super.showMessages(markableRidget);
		}
	}

}
