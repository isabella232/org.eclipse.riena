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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.beans.common.TestBean;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 *
 */
public abstract class AbstractLabelRidgetTest extends AbstractSWTRidgetTest {

	private final static String PLUGIN_ID = "org.eclipse.riena.tests:";
	private final static String ICON_ECLIPSE = PLUGIN_ID + "/icons/eclipse.gif";

	private final static String LABEL = "testlabel";
	private final static String LABEL2 = "testlabel2";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		getRidget().setText(LABEL);
	}

	@Override
	protected ILabelRidget getRidget() {
		return (ILabelRidget) super.getRidget();
	}

	@Override
	protected abstract ILabelRidget createRidget();

	protected abstract String getText(Object widget);

	protected abstract void setText(Object widget, String text);

	protected abstract Image getImage(Object widget);

	protected abstract void setImage(Object widget, Image image);

	protected abstract Class<? extends ILabelRidget> getRidgetClass();

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(getRidgetClass(), mapper.getRidgetClass(getWidget()));
	}

	/**
	 * Test method get/setIcon().
	 */
	public final void testSetIcon() {

		final ILabelRidget ridget = getRidget();

		ridget.setIcon(ICON_ECLIPSE);

		assertEquals(ICON_ECLIPSE, ridget.getIcon());
		assertNotNull(getImage(getWidget()));

		ridget.setIcon(null);

		assertNull(ridget.getIcon());
		assertNull(getImage(getWidget()));

		Widget label = createWidget(getShell());
		final Image labelImage = label.getDisplay().getSystemImage(SWT.ICON_INFORMATION);
		setImage(label, labelImage);
		ILabelRidget labelRidget = createRidget();
		// binding doesn't remove image of label, because the icon of the ridget is null and the method #setIcon wasn't called yet.
		labelRidget.setUIControl(label);
		assertSame(labelImage, getImage(label));

		labelRidget.setIcon(null);
		assertNull(labelRidget.getIcon());
		assertNull(getImage(label));

		labelRidget.setIcon(ICON_ECLIPSE);
		assertEquals(ICON_ECLIPSE, labelRidget.getIcon());
		assertNotNull(getImage(label));
		assertNotSame(labelImage, getImage(label));

		label = createWidget(getShell());
		setImage(label, labelImage);
		labelRidget = createRidget();
		labelRidget.setIcon(ICON_ECLIPSE);
		// binding replaces image of label, because the icon of the ridget is not null.
		labelRidget.setUIControl(label);
		assertNotNull(getImage(label));
		assertNotSame(labelImage, getImage(label));

	}

	/**
	 * Test method get/setText.
	 */
	public final void testSetText() throws Exception {
		final ILabelRidget ridget = getRidget();
		final Object widget = ridget.getUIControl();

		ridget.setText(LABEL2);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL2, getText(widget));

		ridget.setText("");

		assertEquals("", ridget.getText());
		assertEquals("", getText(widget));

		try {
			ridget.setText(null);
			fail();
		} catch (final IllegalArgumentException iae) {
			ok();
		}
	}

	/**
	 * Test method updateFromModel().
	 */
	public void testUpdateFromModel() throws Exception {
		final ILabelRidget ridget = getRidget();
		final Object widget = ridget.getUIControl();

		final TestBean bean = new TestBean();
		bean.setProperty("NewLabel");

		ridget.bindToModel(bean, TestBean.PROPERTY);

		assertEquals("NewLabel", bean.getProperty());
		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, getText(widget));

		ridget.updateFromModel();

		assertEquals("NewLabel", bean.getProperty());
		assertEquals("NewLabel", ridget.getText());
		assertEquals("NewLabel", getText(widget));
	}

	/**
	 * For LabelRidgets, ensure that updates are made to the model from the
	 * ridget.
	 */
	public void testUpdateFromRidget() {
		final ILabelRidget ridget = getRidget();
		final Object widget = ridget.getUIControl();

		final TestBean bean = new TestBean();
		bean.setProperty("NewLabel");

		ridget.bindToModel(bean, TestBean.PROPERTY);
		ridget.updateFromModel();

		assertEquals("NewLabel", bean.getProperty());
		assertEquals("NewLabel", ridget.getText());
		assertEquals("NewLabel", getText(widget));

		ridget.setText(LABEL);

		assertEquals(LABEL, bean.getProperty());
		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, getText(widget));
	}

	/**
	 * For LabelRidgets, ensure that <b>no</b> updates are made to the model
	 * from the control.
	 */
	public void testUpdateFromControl() {
		final ILabelRidget ridget = getRidget();
		final Object widget = ridget.getUIControl();

		final TestBean bean = new TestBean();
		bean.setProperty("NewLabel");

		ridget.bindToModel(bean, TestBean.PROPERTY);
		ridget.updateFromModel();

		assertEquals("NewLabel", bean.getProperty());
		assertEquals("NewLabel", ridget.getText());
		assertEquals("NewLabel", getText(widget));

		setText(widget, LABEL);

		assertEquals("NewLabel", bean.getProperty());
		assertEquals("NewLabel", ridget.getText());
		assertEquals(LABEL, getText(widget));
	}

	public void testUpdateFromRidgetOnRebind() throws Exception {
		final ILabelRidget ridget = getRidget();
		Object widget = ridget.getUIControl();

		final TestBean bean = new TestBean();
		bean.setProperty("NewLabel");

		ridget.bindToModel(bean, TestBean.PROPERTY);
		ridget.updateFromModel();

		assertEquals("NewLabel", ridget.getText());
		assertEquals("NewLabel", getText(widget));

		ridget.setUIControl(null);

		assertEquals("NewLabel", ridget.getText());
		assertEquals("NewLabel", getText(widget));

		ridget.setText(LABEL);

		assertEquals(LABEL, ridget.getText());
		assertEquals("NewLabel", getText(widget));

		widget = createWidget(getShell());
		ridget.setUIControl(widget);

		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, getText(widget));

	}

	/**
	 * Test for problem report #536.
	 */
	public void testDontReadValueInConstructor() throws Exception {
		final ILabelRidget ridget = getRidget();

		final IObservableValue observableValue = new AbstractObservableValue() {
			@Override
			protected Object doGetValue() {
				return "TestText";
			}

			public Object getValueType() {
				return String.class;
			}
		};
		ridget.bindToModel(observableValue);

		assertEquals(LABEL, ridget.getText());

		ridget.updateFromModel();

		assertEquals("TestText", ridget.getText());
	}

	/**
	 * Test method get/setIconLocation().
	 */
	public void testSetIconLocation() throws Exception {
		final ILabelRidget ridget = getRidget();
		final Object widget = ridget.getUIControl();

		assertNull(ridget.getIconLocation());
		assertNull(getImage(widget));

		final URL url = new URL("http://www.compeople.de/assets/compeople-logo.gif");
		ridget.setIconLocation(url);

		assertEquals(url, ridget.getIconLocation());
		assertNotNull(getImage(widget));

		ridget.setIconLocation(null);

		assertNull(ridget.getIconLocation());
		assertNull(getImage(widget));
	}

	/**
	 * Test method setUIControl().
	 */
	public void testSetUIControl() throws Exception {
		final ILabelRidget ridget = getRidget();

		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, getText(getWidget()));

		ridget.setUIControl(null);

		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, getText(getWidget()));

		ridget.setText(LABEL2);
		ridget.setUIControl(getWidget());

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL2, getText(getWidget()));
	}

	/**
	 * Tests the method {@code initText}
	 */
	public void testInitText() {

		final ILabelRidget ridget = getRidget();
		final Object widget = ridget.getUIControl();

		ReflectionUtils.setHidden(ridget, "textAlreadyInitialized", false);
		ReflectionUtils.setHidden(ridget, "text", null);
		setText(widget, "Hello!");

		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {});
		assertEquals("Hello!", ridget.getText());
		assertEquals("Hello!", getText(widget));
		assertTrue((Boolean) ReflectionUtils.getHidden(ridget, "textAlreadyInitialized"));

		setText(widget, "World");
		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {});
		assertEquals("Hello!", ridget.getText());
		assertEquals("World", getText(widget));

	}

	/**
	 * Tests the <i>private</i> method {@code hasChanged.}
	 * 
	 * @throws MalformedURLException
	 */
	public void testHasChanged() throws MalformedURLException {

		final ILabelRidget ridget = getRidget();

		final URL url1 = new URL("file:/a");
		final URL url2 = new URL("file:/b");
		boolean ret = ReflectionUtils.invokeHidden(ridget, "hasChanged", url1, url2);
		assertTrue(ret);
		ret = ReflectionUtils.invokeHidden(ridget, "hasChanged", url1, url1);
		assertFalse(ret);
		ret = ReflectionUtils.invokeHidden(ridget, "hasChanged", url1, (URL) null);
		assertTrue(ret);
		ret = ReflectionUtils.invokeHidden(ridget, "hasChanged", (URL) null, url2);
		assertTrue(ret);
		ret = ReflectionUtils.invokeHidden(ridget, "hasChanged", (URL) null, (URL) null);
		assertFalse(ret);

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
