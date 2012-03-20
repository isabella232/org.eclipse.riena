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

import org.easymock.EasyMock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IControlDecoration;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;

/**
 * Test for {@link MarkerSupport} with a shared {@link Widget}
 */
public class MarkerViewSharedTest extends AbstractRidgetSharedTestCase {

	private final IControlDecoration[] decorationMocks = new IControlDecoration[2];
	private int mockIndex = 0;

	@Override
	protected Control createWidget(final Composite parent) {
		return new Text(parent, SWT.NONE);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mockIndex = 0;
	}

	@Override
	protected IRidget createRidget() {
		return new TextRidget() {
			@Override
			protected AbstractMarkerSupport createMarkerSupport() {
				return new MarkerSupport(this, propertyChangeSupport) {

					@Override
					protected IControlDecoration createErrorDecoration(final Control control) {
						decorationMocks[mockIndex] = EasyMock.createNiceMock(IControlDecoration.class);
						return decorationMocks[mockIndex++];
					}
				};
			}
		};
	}

	@Override
	protected TextRidget getRidget1() {
		return (TextRidget) super.getRidget1();
	}

	@Override
	protected TextRidget getRidget2() {
		return (TextRidget) super.getRidget2();
	}

	public void testMandatoryMarker() throws Exception {
		final Color bg = getWidget().getBackground();
		activateRidget1();
		getRidget1().setMandatory(true);
		assertFalse(bg.equals(getWidget().getBackground()));
		activateRidget2();
		assertFalse(getRidget2().isMandatory());
		assertEquals(bg, getWidget().getBackground());
	}

	public void testErrorMarker() throws Exception {
		activateRidget1();
		assertNull(decorationMocks[0]);
		getRidget1().setErrorMarked(true);
		EasyMock.reset(decorationMocks[0]);
		decorationMocks[0].hide();
		EasyMock.expectLastCall().times(1);
		EasyMock.replay(decorationMocks[0]);
		activateRidget2();
		EasyMock.verify(decorationMocks[0]);
		EasyMock.reset(decorationMocks[0]);
		decorationMocks[0].show();
		EasyMock.expectLastCall().times(1);
		EasyMock.replay(decorationMocks[0]);
		activateRidget1();
		EasyMock.verify(decorationMocks[0]);
	}

	public void testOutputMarker() throws Exception {
		final Color bg = getWidget().getBackground();
		activateRidget1();
		getRidget1().setOutputOnly(true);
		assertFalse(bg.equals(getWidget().getBackground()));
		activateRidget2();
		assertFalse(getRidget2().isMandatory());
		assertEquals(bg, getWidget().getBackground());
	}

	public void testVisibleMarker() throws Exception {
		activateRidget1();
		getRidget1().setVisible(false);
		assertFalse(getWidget().isVisible());
		activateRidget2();
		assertTrue(getWidget().isVisible());
	}

	public void testDisabledMarker() throws Exception {
		activateRidget1();
		getRidget1().setEnabled(false);
		assertFalse(getWidget().isEnabled());
		activateRidget2();
		assertTrue(getWidget().isEnabled());
	}

}
