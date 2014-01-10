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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@code EmbeddedTitleBarRidget}.
 */
public class EmbeddedTitleBarRidgetTest extends AbstractSWTRidgetTest {

	private final static String PLUGIN_ID = "org.eclipse.riena.tests:";
	private final static String ICON_ECLIPSE = PLUGIN_ID + "/icons/eclipse.gif";

	private final static String LABEL = "testlabel";
	private final static String LABEL2 = "testlabel2";

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		getRidget().setTitle(LABEL);
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#createRidget()
	 */
	@Override
	protected IRidget createRidget() {
		return new EmbeddedTitleBarRidget();
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#getRidget()
	 */
	@Override
	protected EmbeddedTitleBarRidget getRidget() {
		return (EmbeddedTitleBarRidget) super.getRidget();
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#createWidget(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createWidget(final Composite parent) {
		return new EmbeddedTitleBar(parent, SWT.NONE);
	}

	/**
	 * Tests the method {@code setTitle}.
	 */
	public void testSetTitle() {

		final EmbeddedTitleBarRidget ridget = getRidget();
		final EmbeddedTitleBar control = ridget.getUIControl();

		ridget.setTitle(LABEL2);

		assertEquals(LABEL2, ridget.getTitle());
		assertEquals(LABEL2, control.getTitle());

		ridget.setTitle("");

		assertEquals("", control.getTitle());

		ridget.setTitle(null);
		assertNull(ridget.getTitle());
		assertNull(control.getTitle());

	}

	/**
	 * Test method get/setIcon().
	 */
	public void testSetIcon() {

		final EmbeddedTitleBarRidget ridget = getRidget();
		final EmbeddedTitleBar control = ridget.getUIControl();

		ridget.setIcon(ICON_ECLIPSE);

		assertEquals(ICON_ECLIPSE, ridget.getIcon());
		assertNotNull(control.getImage());

		ridget.setIcon(null);

		assertNull(ridget.getIcon());
		assertNull(control.getImage());

	}

	/**
	 * Tests the method {@code checkUIControl}.
	 */
	public void testCheckUIControl() {

		final EmbeddedTitleBarRidget ridget = getRidget();
		final EmbeddedTitleBar control = ridget.getUIControl();

		// no exception expected
		ReflectionUtils.invokeHidden(ridget, "checkUIControl", control);

		final Label label = new Label(control.getParent(), SWT.NONE);
		try {
			ReflectionUtils.invokeHidden(ridget, "checkUIControl", label);
			fail("Missing expected BindingException!");
		} catch (final Exception e) {
			// exception expected
			assertTrue(e.getCause() instanceof BindingException);
		}
		SwtUtilities.dispose(label);

	}

	/**
	 * Tests that markers that are irrelavant for this type of Ridget do not
	 * change the widget.
	 */
	public void testUnsupportedMarkersIgnored() {
		assertMarkerIgnored(new ErrorMarker());
		assertMarkerIgnored(new MandatoryMarker());
		assertMarkerIgnored(new OutputMarker());
		assertMarkerIgnored(new NegativeMarker());
	}

}
