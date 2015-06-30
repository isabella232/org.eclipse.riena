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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.BooleanBean;
import org.eclipse.riena.core.marker.AbstractMarker;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.swt.AbstractRidgetController;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * Tests of the class <code>ToggleButtonRidget</code>.
 * 
 */
public class ToggleButtonRidgetTest extends AbstractSWTRidgetTest {

	private final static String ICON_ECLIPSE = "eclipse.gif"; //$NON-NLS-1$
	private final static String LABEL = "testlabel"; //$NON-NLS-1$
	private final static String LABEL2 = "testlabel2"; //$NON-NLS-1$

	@Override
	protected IToggleButtonRidget createRidget() {
		return new ToggleButtonRidget();
	}

	@Override
	protected Button createWidget(final Composite parent) {
		return new Button(parent, SWT.CHECK);
	}

	@Override
	protected IToggleButtonRidget getRidget() {
		return (IToggleButtonRidget) super.getRidget();
	}

	@Override
	protected Button getWidget() {
		return (Button) super.getWidget();
	}

	public void testEnableDisableModelSynchronization() throws Exception {
		final BooleanBean model = new BooleanBean(false);
		final IToggleButtonRidget r = getRidget();
		r.bindToModel(model, "value");
		r.updateFromModel();
		final Button w = getWidget();

		assertFalse(model.isValue());
		assertFalse(r.isSelected());
		assertFalse(w.getSelection());

		w.setSelection(true);
		fireSelection(w);
		assertTrue(model.isValue());
		assertTrue(r.isSelected());
		assertTrue(w.getSelection());

		r.setEnabled(false);
		model.setValue(false);
		r.updateFromModel();
		assertFalse(model.isValue());
		assertFalse(r.isSelected());
		assertFalse(r.isEnabled());
		assertFalse(w.getSelection());

		r.setEnabled(true);
		r.updateFromModel();
		assertFalse(model.isValue());
		assertFalse(r.isSelected());
		assertFalse(w.getSelection());

		w.setSelection(true);
		fireSelection(w);
		assertTrue(model.isValue());
		assertTrue(r.isSelected());
		assertTrue(w.getSelection());
	}

	public void testRidgetMapping() {
		final Shell shell = getShell();

		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();

		final Button buttonToggle = new Button(shell, SWT.TOGGLE);
		assertSame(ToggleButtonRidget.class, mapper.getRidgetClass(buttonToggle));

		final Button buttonCheck = new Button(shell, SWT.CHECK);
		assertSame(ToggleButtonRidget.class, mapper.getRidgetClass(buttonCheck));

		final Button buttonPush = new Button(shell, SWT.PUSH);
		assertNotSame(ToggleButtonRidget.class, mapper.getRidgetClass(buttonPush));

		final Button aButton = new Button(shell, SWT.NONE);
		assertNotSame(ToggleButtonRidget.class, mapper.getRidgetClass(aButton));
	}

	public void testSetUIControl() throws Exception {
		final IToggleButtonRidget ridget = getRidget();
		final Button button = getWidget();

		assertSame(button, ridget.getUIControl());
	}

	public void testSetSelected() throws Exception {
		final IToggleButtonRidget ridget = getRidget();
		final Button button = getWidget();
		final BooleanTestPojo model = new BooleanTestPojo();
		final IObservableValue modelOV = PojoObservables.observeValue(model, "selected"); //$NON-NLS-1$
		ridget.bindToModel(modelOV);

		ridget.setSelected(true);

		assertTrue(ridget.isSelected());
		assertTrue(button.getSelection());

		ridget.setSelected(false);

		assertFalse(ridget.isSelected());
		assertFalse(button.getSelection());
	}

	public void testIsSelected() throws Exception {
		final IToggleButtonRidget ridget = getRidget();

		final BooleanTestPojo model = new BooleanTestPojo();
		model.setSelected(true);
		final IObservableValue modelOV = PojoObservables.observeValue(model, "selected"); //$NON-NLS-1$
		ridget.bindToModel(modelOV);
		ridget.updateFromModel();

		assertTrue(ridget.isSelected());
	}

	public void testBindToModelIObservableValue() throws Exception {
		final IToggleButtonRidget ridget = getRidget();

		final BooleanTestPojo model = new BooleanTestPojo();
		model.setSelected(true);
		final IObservableValue modelOV = PojoObservables.observeValue(model, "selected"); //$NON-NLS-1$
		ridget.bindToModel(modelOV);

		assertNotNull(BeansObservables.observeValue(ridget, IToggleButtonRidget.PROPERTY_SELECTED));
		assertEquals(boolean.class, BeansObservables.observeValue(ridget, IToggleButtonRidget.PROPERTY_SELECTED).getValueType());
		assertFalse(ridget.isSelected());

		ridget.updateFromModel();

		assertTrue(ridget.isSelected());
	}

	public void testBindToModelPropertyName() throws Exception {
		final IToggleButtonRidget ridget = getRidget();

		final BooleanTestPojo model = new BooleanTestPojo();
		model.setSelected(true);
		ridget.bindToModel(model, "selected"); //$NON-NLS-1$

		assertNotNull(BeansObservables.observeValue(ridget, IToggleButtonRidget.PROPERTY_SELECTED));
		assertEquals(boolean.class, BeansObservables.observeValue(ridget, IToggleButtonRidget.PROPERTY_SELECTED).getValueType());
		assertFalse(ridget.isSelected());

		ridget.updateFromModel();

		assertTrue(ridget.isSelected());
	}

	public void testUpdateFromModel() throws Exception {
		final IToggleButtonRidget ridget = getRidget();
		final Button button = getWidget();

		final BooleanTestPojo model = new BooleanTestPojo();
		model.setSelected(true);
		ridget.bindToModel(model, "selected"); //$NON-NLS-1$
		ridget.updateFromModel();

		assertTrue(button.getSelection());

		model.setSelected(false);
		ridget.updateFromModel();
		assertFalse(button.getSelection());
	}

	public void testActionListener() {
		final IToggleButtonRidget ridget = getRidget();

		ridget.setSelected(false);

		final FTActionListener listener = new FTActionListener();
		ridget.addListener(listener);
		ridget.setSelected(true);

		assertEquals(1, listener.getCount());

		ridget.setSelected(true);

		assertEquals(1, listener.getCount());

		ridget.setSelected(false);

		assertEquals(2, listener.getCount());

		ridget.removeListener(listener);
		ridget.setSelected(true);

		assertEquals(2, listener.getCount());

		try {
			ridget.addListener(null);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	public final void testSetText() throws Exception {
		final IToggleButtonRidget ridget = getRidget();
		final Button button = getWidget();

		ridget.setText(""); //$NON-NLS-1$

		assertEquals("", ridget.getText()); //$NON-NLS-1$
		assertEquals("", button.getText()); //$NON-NLS-1$

		try {
			ridget.setText(null);
			fail();
		} catch (final IllegalArgumentException iae) {
			ok();
		}

		ridget.setText(LABEL);

		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, button.getText());

		ridget.setUIControl(null);
		ridget.setText(LABEL2);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL, button.getText());

		ridget.setUIControl(button);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL2, button.getText());
	}

	/**
	 * Test method get/setIcon().
	 */
	public final void testSetIcon() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

		ridget.setIcon(ICON_ECLIPSE);

		assertEquals(ICON_ECLIPSE, ridget.getIcon());
		assertNotNull(control.getImage());

		ridget.setIcon(null);

		assertNull(ridget.getIcon());
		assertNull(control.getImage());

		ridget.setIcon("nonsense"); //$NON-NLS-1$

		final Image missingImage = ImageStore.getInstance().getMissingImage();
		assertEquals("nonsense", ridget.getIcon()); //$NON-NLS-1$
		assertEquals(missingImage, control.getImage());

		Button button = createWidget(getShell());
		final Image buttonImage = button.getDisplay().getSystemImage(SWT.ICON_INFORMATION);
		button.setImage(buttonImage);
		IToggleButtonRidget buttonRidget = createRidget();
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
	public final void testInitText() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

		ReflectionUtils.setHidden(ridget, "textAlreadyInitialized", false); //$NON-NLS-1$
		ReflectionUtils.setHidden(ridget, "text", null); //$NON-NLS-1$
		control.setText("Hello!"); //$NON-NLS-1$

		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {}); //$NON-NLS-1$
		assertEquals("Hello!", ridget.getText()); //$NON-NLS-1$
		assertEquals("Hello!", control.getText()); //$NON-NLS-1$
		assertTrue((Boolean) ReflectionUtils.getHidden(ridget, "textAlreadyInitialized")); //$NON-NLS-1$

		control.setText("World"); //$NON-NLS-1$
		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {}); //$NON-NLS-1$
		assertEquals("Hello!", ridget.getText()); //$NON-NLS-1$
		assertEquals("World", control.getText()); //$NON-NLS-1$

	}

	/**
	 * Tests that control is hidden when the ToggleButtonRidget is "output only".
	 */
	public void testOutputRidgetNotVisible() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

		assertFalse(ridget.isOutputOnly());
		assertTrue(control.isVisible());

		ridget.setOutputOnly(true);

		assertTrue(ridget.isOutputOnly());
		assertTrue(control.isVisible());

		ridget.setOutputOnly(false);

		assertFalse(ridget.isOutputOnly());
		assertTrue(control.isVisible());

		ridget.setOutputOnly(true);
		ridget.setVisible(true);

		assertTrue(ridget.isOutputOnly());
		assertTrue(ridget.isVisible());
		assertTrue(control.isVisible());

		ridget.setVisible(false);
		ridget.setOutputOnly(false);

		assertFalse(ridget.isOutputOnly());
		assertFalse(ridget.isVisible());
		assertFalse(control.isVisible());

		ridget.setVisible(true);
		ridget.setOutputOnly(false);

		assertFalse(ridget.isOutputOnly());
		assertTrue(ridget.isVisible());
		assertTrue(control.isVisible());

		ridget.setVisible(false);
		ridget.setOutputOnly(true);

		assertTrue(ridget.isOutputOnly());
		assertFalse(ridget.isVisible());
		assertFalse(control.isVisible());

		ridget.setVisible(true);
		ridget.setOutputOnly(true);
		ridget.setVisible(false);
		ridget.setVisible(true);

		assertTrue(ridget.isOutputOnly());
		assertTrue(ridget.isVisible());
	}

	/**
	 * Tests that changing the selected state via {@link IToggleButtonRidget#setSelected(boolean) does not select the control, when the ridget is disabled.
	 */
	public void testDisabledRidgetDoesNotCheckControlOnRidgetSelection() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();
		final BooleanTestPojo model = new BooleanTestPojo();
		ridget.bindToModel(model, "selected"); //$NON-NLS-1$

		ridget.setSelected(false);
		ridget.setEnabled(false);

		assertFalse(model.isSelected());
		assertFalse(ridget.isSelected());
		assertFalse(control.getSelection());

		ridget.setSelected(true);

		assertTrue(model.isSelected());
		assertTrue(ridget.isSelected());
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			assertFalse(control.getSelection());
		} else {
			assertTrue(control.getSelection());
		}

		ridget.setEnabled(true);

		assertTrue(model.isSelected());
		assertTrue(ridget.isSelected());
		assertTrue(control.getSelection());
	}

	/**
	 * Tests that changing the selected state via a bound model, does not select the control, when the ridget is disabled.
	 */
	public void testDisabledRidgetDoesNotCheckControlOnModelSelection() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();
		final BooleanTestPojo model = new BooleanTestPojo();
		ridget.bindToModel(model, "selected"); //$NON-NLS-1$
		ridget.setEnabled(false);

		model.setSelected(false);
		ridget.updateFromModel();

		assertFalse(model.isSelected());
		assertFalse(ridget.isSelected());
		assertFalse(control.getSelection());

		model.setSelected(true);
		ridget.updateFromModel();

		assertTrue(model.isSelected());
		assertTrue(ridget.isSelected());
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			assertFalse(control.getSelection());
		} else {
			assertTrue(control.getSelection());
		}

		ridget.setEnabled(true);

		assertTrue(model.isSelected());
		assertTrue(ridget.isSelected());
		assertTrue(control.getSelection());
	}

	/**
	 * Tests that disabling the ridget unselects the checkbox button, even when no model is bound to the ridget.
	 */
	public void testDisableRidgetRemovesSelection() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

		ridget.setEnabled(true);
		ridget.setSelected(true);

		assertTrue(ridget.isEnabled());
		assertTrue(ridget.isSelected());
		assertTrue(control.isEnabled());
		assertTrue(control.getSelection());

		ridget.setEnabled(false);

		assertFalse(ridget.isEnabled());
		assertTrue(ridget.isSelected());
		assertFalse(control.isEnabled());
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			assertFalse(control.getSelection());
		} else {
			assertTrue(control.getSelection());
		}

		ridget.setEnabled(true);

		assertTrue(ridget.isEnabled());
		assertTrue(ridget.isSelected());
		assertTrue(control.isEnabled());
		assertTrue(control.getSelection());
	}

	/**
	 * Tests that disabling the ridget does not fire 'selected' events, even though the control is modified.
	 */
	public void testDisabledDoesNotFireSelected() {
		final IToggleButtonRidget ridget = getRidget();
		ridget.setEnabled(true);
		ridget.setSelected(true);

		ridget.addPropertyChangeListener(IToggleButtonRidget.PROPERTY_SELECTED, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				fail("Unexpected property change event: " + evt); //$NON-NLS-1$
			}
		});

		ridget.setEnabled(false);

		ridget.setEnabled(true);
	}

	/**
	 * Tests that the disabled state is applied to a new control when set into the ridget.
	 */
	public void testDisableAndClearOnBind() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

		ridget.setUIControl(null);
		ridget.setEnabled(false);
		ridget.setSelected(true);
		ridget.setUIControl(control);

		assertFalse(control.isEnabled());
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			assertFalse(control.getSelection());
		} else {
			assertTrue(control.getSelection());
		}

		ridget.setEnabled(true);

		assertTrue(control.isEnabled());
		assertTrue(control.getSelection());
	}

	public void testFireAction() {
		final IToggleButtonRidget ridget = getRidget();
		final FTActionListener listener1 = new FTActionListener();
		final FTActionListener listener2 = new FTActionListener();

		ridget.addListener(listener1);

		assertEquals(0, listener1.getCount());

		final BooleanTestPojo model = new BooleanTestPojo();
		model.setSelected(true);
		ridget.bindToModel(PojoObservables.observeValue(model, "selected")); //$NON-NLS-1$
		ridget.updateFromModel();

		assertTrue(ridget.isSelected());
		assertEquals(1, listener1.getCount());

		ridget.fireAction();
		ridget.fireAction();

		assertEquals(3, listener1.getCount());

		ridget.addListener(listener2);
		ridget.fireAction();

		assertEquals(4, listener1.getCount());
		assertEquals(1, listener2.getCount());
		assertFalse(ridget.isSelected());
	}

	/**
	 * As per Bug 321927
	 */
	public void testOutputOnlyWidgetsAreDisabledWhenNotSelected() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

		setEnabledOutputSelected(ridget, false, false, false);

		assertEquals(false, ridget.isEnabled());
		assertEquals(false, control.isEnabled());

		setEnabledOutputSelected(ridget, false, false, true);

		assertEquals(false, ridget.isEnabled());
		assertEquals(false, control.isEnabled());

		setEnabledOutputSelected(ridget, false, false, false);

		assertEquals(false, ridget.isEnabled());
		assertEquals(false, control.isEnabled());

		setEnabledOutputSelected(ridget, false, false, true);

		assertEquals(false, ridget.isEnabled());
		assertEquals(false, control.isEnabled());

		setEnabledOutputSelected(ridget, true, true, false);

		assertEquals(false, ridget.isEnabled());
		assertEquals(false, control.isEnabled());

		setEnabledOutputSelected(ridget, true, true, true);

		assertEquals(true, ridget.isEnabled());
		assertEquals(false, control.isEnabled());

		setEnabledOutputSelected(ridget, true, false, false);

		assertEquals(true, ridget.isEnabled());
		assertEquals(true, control.isEnabled());

		setEnabledOutputSelected(ridget, true, false, true);

		assertEquals(true, ridget.isEnabled());
		assertEquals(true, control.isEnabled());
	}

	/**
	 * As per Bug 271762
	 */
	public void testClickOnOutputOnlyCheckboxDoesNotChangeState() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

		ridget.setEnabled(true);
		ridget.setOutputOnly(false);
		ridget.setSelected(true);
		ridget.setOutputOnly(true);

		assertTrue(ridget.isSelected());
		assertTrue(control.getSelection());

		control.setSelection(false);
		fireSelection(control);

		assertTrue(ridget.isSelected());
		assertTrue(control.getSelection());

		ridget.setOutputOnly(false);
		control.setSelection(false);
		fireSelection(control);

		assertFalse(ridget.isSelected());
		assertFalse(control.getSelection());
	}

	/**
	 * As per Bug 321935
	 */
	public void testSetSelectedOnOutputOnlyCheckboxChangesState() {
		final IToggleButtonRidget ridget = getRidget();

		ridget.setOutputOnly(false);
		ridget.setSelected(false);

		assertFalse(ridget.isSelected());

		ridget.setOutputOnly(true);
		ridget.setSelected(true);

		assertTrue(ridget.isSelected());
	}

	/**
	 * As per Bug 323429
	 */
	public void testSetMandatory() {
		final IToggleButtonRidget ridget = getRidget();

		assertFalse(ridget.isMandatory());

		ridget.setMandatory(true);

		assertTrue(ridget.isMandatory());

		ridget.setMandatory(false);

		assertFalse(ridget.isMandatory());
	}

	/**
	 * As per Bug 323429
	 */
	public void testIsDisableMandatoryMarker() {
		final IToggleButtonRidget ridget = getRidget();
		ridget.setEnabled(true);
		ridget.setMandatory(true);

		assertFalse(ridget.isDisableMandatoryMarker());

		ridget.setSelected(true);

		assertTrue(ridget.isDisableMandatoryMarker());

		ridget.setSelected(false);

		assertFalse(ridget.isDisableMandatoryMarker());
	}

	/**
	 * As per Bug 323429
	 */
	public void testDisabledMarkerUpdatesWithTwoRidgetsViaSetSelected() {
		final IToggleButtonRidget ridget1 = getRidget();
		final Button check2 = new Button(getShell(), SWT.CHECK);
		final IToggleButtonRidget ridget2 = new ToggleButtonRidget();
		ridget2.setUIControl(check2);

		ridget1.setSelected(false);
		ridget1.setEnabled(true);
		ridget1.setMandatory(true);

		ridget2.setSelected(false);
		ridget2.setEnabled(true);
		ridget2.setMandatory(true);

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget1.setSelected(true);

		assertTrue(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertTrue(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget1.setSelected(false);

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget2.setSelected(true);

		assertTrue(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertTrue(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget2.setSelected(false);

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
	}

	/**
	 * As per Bug 323429
	 */
	public void testDisabledMarkerUpdatesWithTwoRidgetsViaUISelection() {
		final IToggleButtonRidget ridget1 = getRidget();
		final Button check1 = (Button) ridget1.getUIControl();
		final Button check2 = new Button(getShell(), SWT.CHECK);
		final IToggleButtonRidget ridget2 = new ToggleButtonRidget();
		ridget2.setUIControl(check2);

		ridget1.setSelected(false);
		ridget1.setEnabled(true);
		ridget1.setMandatory(true);

		ridget2.setSelected(false);
		ridget2.setEnabled(true);
		ridget2.setMandatory(true);

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		check1.setSelection(true);
		fireSelection(check1);

		assertTrue(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertTrue(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		check1.setSelection(false);
		fireSelection(check1);

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		check2.setSelection(true);
		fireSelection(check2);

		assertTrue(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertTrue(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		check2.setSelection(false);
		fireSelection(check2);

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
	}

	/**
	 * As per Bug 323429
	 */
	public void testIsDisableMandatoryMarkerWithOtherCheckbox() {
		final IToggleButtonRidget ridget = getRidget();
		final Button check2 = new Button(getShell(), SWT.CHECK);
		ridget.setEnabled(true);
		ridget.setMandatory(true);

		assertFalse(ridget.isDisableMandatoryMarker());
		assertFalse(check2.getSelection());

		check2.setSelection(true);

		assertTrue(ridget.isDisableMandatoryMarker());

		check2.setSelection(false);

		assertFalse(ridget.isDisableMandatoryMarker());

		check2.setSelection(true);
		check2.setEnabled(false);

		assertFalse(ridget.isDisableMandatoryMarker());
	}

	public void testMandatoryMarkerWithTwoRidgetsOnBindingOnSharedView() {
		final IToggleButtonRidget ridget1 = getRidget();
		final Button check2 = new Button(getShell(), SWT.CHECK);
		final IToggleButtonRidget ridget2 = new ToggleButtonRidget();
		ridget2.setUIControl(check2);
		final IController controller1 = new AbstractRidgetController() {

			@Override
			public void configureRidgets() {

			}
		};
		final IController controller2 = new AbstractRidgetController() {

			@Override
			public void configureRidgets() {

			}
		};

		ridget1.setController(controller1);
		ridget1.setSelected(false);
		ridget1.setEnabled(true);
		ridget1.setMandatory(true);

		ridget2.setController(controller2);
		ridget2.setSelected(false);
		ridget2.setEnabled(true);
		ridget2.setMandatory(true);

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget1.setSelected(true);
		ridget1.updateMarkers();

		assertTrue(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget1.setSelected(false);
		ridget1.updateMarkers();

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget2.setSelected(true);
		ridget2.updateMarkers();

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertTrue(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget2.setSelected(false);
		ridget2.updateMarkers();

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget1.setSelected(true);
		ridget2.setSelected(true);
		ridget1.updateMarkers();
		ridget2.updateMarkers();

		assertTrue(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertTrue(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget1.setController(controller2);

		ridget1.setSelected(true);
		ridget2.setSelected(false);
		ridget1.updateMarkers();

		assertTrue(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertTrue(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());

		ridget1.setSelected(false);
		ridget1.updateMarkers();

		assertFalse(ridget1.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
		assertFalse(ridget2.getMarkersOfType(MandatoryMarker.class).iterator().next().isDisabled());
	}

	/**
	 * As per Bug 323429
	 */
	public void testIsDisableMandatoryMarkerWithOtherButtons() {
		final IToggleButtonRidget ridget = getRidget();
		final Button radio = new Button(getShell(), SWT.RADIO);
		final Button toggle = new Button(getShell(), SWT.TOGGLE);
		ridget.setEnabled(true);
		ridget.setMandatory(true);

		assertFalse(ridget.isDisableMandatoryMarker());
		assertFalse(radio.getSelection());
		assertFalse(toggle.getSelection());

		radio.setSelection(true);

		assertFalse(ridget.isDisableMandatoryMarker());
		assertTrue(radio.getSelection());
		assertFalse(toggle.getSelection());

		toggle.setSelection(true);

		assertFalse(ridget.isDisableMandatoryMarker());
		assertTrue(radio.getSelection());
		assertTrue(toggle.getSelection());
	}

	public void testSetOutputOnly() {
		final IToggleButtonRidget ridget = createRidget();
		final Button control = createWidget(getShell());

		ridget.setOutputOnly(true);

		assertTrue(ridget.isOutputOnly());
		assertTrue(control.isEnabled());

		ridget.setUIControl(control);
		ridget.addMarker(new AbstractMarker() {
		});

		assertTrue(ridget.isOutputOnly());
		assertFalse(control.isEnabled());
	}

	public void testSingleChoiceRadioButtons() throws Exception {
		// setting up test chase: we need two RadioButtons with the same parent
		// those buttons have to be bound and the ridgets must be
		// available in the controller
		// not using injectRidgets of the BindingManager to keep the test as capsuled as possible
		final IRidgetContainer controller = new DummyContainer();

		final Button button = new Button(getShell(), SWT.RADIO);
		final IToggleButtonRidget ridget = new ToggleButtonRidget();
		ridget.setUIControl(button);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(button, "radio1"); //$NON-NLS-1$
		ridget.setController(controller);
		controller.addRidget("radio1", ridget); //$NON-NLS-1$

		final Button button2 = new Button(getShell(), SWT.RADIO);
		final IToggleButtonRidget ridget2 = new ToggleButtonRidget();
		ridget2.setUIControl(button2);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(button2, "radio2"); //$NON-NLS-1$
		ridget2.setController(controller);
		controller.addRidget("radio2", ridget2); //$NON-NLS-1$

		ridget2.setSelected(true);
		assertFalse(ridget.isSelected());
		assertTrue(ridget2.isSelected());
		assertFalse(button.getSelection());
		assertTrue(button2.getSelection());

		ridget.setSelected(true);
		assertTrue(ridget.isSelected());
		assertFalse(ridget2.isSelected());
		assertTrue(button.getSelection());
		assertFalse(button2.getSelection());
	}

	// helping methods
	//////////////////

	private void fireSelection(final Button control) {
		final Event event = new Event();
		event.type = SWT.Selection;
		event.widget = control;
		event.display = control.getDisplay();
		control.notifyListeners(SWT.Selection, event); // fire a selection on the control
	}

	private void setEnabledOutputSelected(final IToggleButtonRidget ridget, final boolean enabled, final boolean output, final boolean selected) {
		ridget.setEnabled(enabled);
		ridget.setOutputOnly(output);
		ridget.setSelected(selected);
	}

	// helping classes
	// ////////////////

	/**
	 * An object holding one boolean.
	 */
	private static class BooleanTestPojo {

		private boolean selected;

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(final boolean selected) {
			this.selected = selected;
		}
	}

	private static final class DummyContainer implements IRidgetContainer {
		private final Map<String, IRidget> ridgets = new HashMap<String, IRidget>();

		public void addRidget(final String id, final IRidget ridget) {
			ridgets.put(id, ridget);
		}

		public boolean removeRidget(final String id) {
			return false;
		}

		public void configureRidgets() {
			// nothing
		}

		public <R extends IRidget> R getRidget(final String id) {
			return (R) ridgets.get(id);
		}

		public <R extends IRidget> R getRidget(final Class<R> ridgetClazz, final String id) {
			return getRidget(id);
		}

		public Collection<? extends IRidget> getRidgets() {
			return ridgets.values();
		}

		public boolean isConfigured() {
			return false;
		}

		public void setConfigured(final boolean configured) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#setStatuslineToShowMarkerMessages(org.eclipse.riena.ui.ridgets.IStatuslineRidget)
		 */
		public void setStatuslineToShowMarkerMessages(final IStatuslineRidget statuslineToShowMarkerMessages) {
			// TODO Auto-generated method stub

		}
	}

}
