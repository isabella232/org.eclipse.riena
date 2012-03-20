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

import org.eclipse.core.databinding.BindingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests of the class {@link ActionRidget}.
 */
public class ActionRidgetTest extends AbstractSWTRidgetTest {

	private final static String PLUGIN_ID = "org.eclipse.riena.tests:";
	private final static String ICON_ECLIPSE = PLUGIN_ID + "/icons/eclipse.gif";

	private final static String LABEL = "testlabel";
	private final static String LABEL2 = "testlabel2";

	@Override
	protected Button createWidget(final Composite parent) {
		return new Button(parent, SWT.PUSH);
	}

	@Override
	protected IActionRidget createRidget() {
		return new ActionRidget();
	}

	@Override
	protected Button getWidget() {
		return (Button) super.getWidget();
	}

	@Override
	protected IActionRidget getRidget() {
		return (IActionRidget) super.getRidget();
	}

	@Override
	public void testSetFocusable() {
		// TODO: This test (super.testSetFocusable()) fails on our build machine!
		// Opened bug #268509 to track it!
	}

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(ActionRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testSetUIControl() {
		final IActionRidget ridget = getRidget();

		ridget.setUIControl(null);
		assertNull(ridget.getUIControl());

		ridget.setUIControl(getWidget());
		assertSame(getWidget(), ridget.getUIControl());
	}

	public void testSetUIControlInvalid() {
		final IActionRidget ridget = getRidget();

		try {
			ridget.setUIControl(getShell());
			fail();
		} catch (final BindingException bex) {
			ok();
		}
	}

	public void testAddListenerInvalid() {
		try {
			getRidget().addListener(null);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	public void testAddListener() {
		final Button control = getWidget();
		final IActionRidget ridget = getRidget();

		final FTActionListener listener1 = new FTActionListener();
		final FTActionListener listener2 = new FTActionListener();

		ridget.addListener(listener1);
		ridget.addListener(listener2);
		// listener2 will not be added again
		// if the same instance is already added
		ridget.addListener(listener2);

		UITestHelper.fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(1, listener2.getCount());

		ridget.removeListener(listener1);
		UITestHelper.fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeListener(listener2);
		UITestHelper.fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeListener(listener2);
		UITestHelper.fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());
	}

	public final void testSetText() throws Exception {
		final IActionRidget ridget = getRidget();
		final Button control = getWidget();

		ridget.setText("");

		assertEquals("", ridget.getText());
		assertEquals("", control.getText());

		try {
			ridget.setText(null);
			fail();
		} catch (final IllegalArgumentException iae) {
			ok();
		}

		ridget.setText(LABEL);

		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, control.getText());

		ridget.setUIControl(null);
		ridget.setText(LABEL2);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL, control.getText());

		ridget.setUIControl(control);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL2, control.getText());
	}

	/**
	 * Test method get/setIcon().
	 */
	public final void testSetIcon() {

		final IActionRidget ridget = getRidget();
		final Button control = (Button) ridget.getUIControl();

		ridget.setIcon(ICON_ECLIPSE);

		assertEquals(ICON_ECLIPSE, ridget.getIcon());
		assertNotNull(control.getImage());

		ridget.setIcon(null);

		assertNull(ridget.getIcon());
		assertNull(control.getImage());

		Button button = createWidget(getShell());
		final Image buttonImage = button.getDisplay().getSystemImage(SWT.ICON_INFORMATION);
		button.setImage(buttonImage);
		IActionRidget buttonRidget = createRidget();
		// binding doesn't remove image of button, because the icon of the ridget is null and the method #setIcon wasn't called yet.
		buttonRidget.setUIControl(button);
		assertSame(buttonImage, button.getImage());

		buttonRidget.setIcon(null);
		assertNull(buttonRidget.getIcon());
		assertNull(button.getImage());

		buttonRidget.setIcon(ICON_ECLIPSE);
		assertEquals(ICON_ECLIPSE, buttonRidget.getIcon());
		assertNotNull(button.getImage());
		assertNotSame(buttonImage, button.getImage());

		button = createWidget(getShell());
		button.setImage(buttonImage);
		buttonRidget = createRidget();
		buttonRidget.setIcon(ICON_ECLIPSE);
		// binding replaces image of button, because the icon of the ridget is not null.
		buttonRidget.setUIControl(button);
		assertNotNull(button.getImage());
		assertNotSame(buttonImage, button.getImage());

	}

	/**
	 * Tests the method {@code initText}
	 */
	public void testInitText() {
		final IActionRidget ridget = getRidget();
		final Button control = (Button) ridget.getUIControl();

		ReflectionUtils.setHidden(ridget, "textAlreadyInitialized", false);
		ReflectionUtils.setHidden(ridget, "text", null);
		control.setText("Hello!");

		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {});
		assertEquals("Hello!", ridget.getText());
		assertEquals("Hello!", control.getText());
		assertTrue((Boolean) ReflectionUtils.getHidden(ridget, "textAlreadyInitialized"));

		control.setText("World");
		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {});
		assertEquals("Hello!", ridget.getText());
		assertEquals("World", control.getText());
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

	public void testFireAction() {
		final IActionRidget ridget = getRidget();
		final FTActionListener listener1 = new FTActionListener();
		final FTActionListener listener2 = new FTActionListener();

		ridget.addListener(listener1);

		ridget.fireAction();
		ridget.fireAction();
		assertEquals(2, listener1.getCount());

		ridget.addListener(listener2);
		ridget.fireAction();
		assertEquals(3, listener1.getCount());
		assertEquals(1, listener2.getCount());
	}

}
