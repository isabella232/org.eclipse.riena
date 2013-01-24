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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IChoiceRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.ChoiceComposite;

/**
 * Tests for the class {@link MultipleChoiceRidget}.
 */
public final class MultipleChoiceRidgetTest extends MarkableRidgetTest {

	private OptionProvider optionProvider;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		optionProvider = new OptionProvider();

		getRidget().bindToModel(optionProvider, "options", optionProvider, "selectedOptions");
		getRidget().updateFromModel();
	}

	@Override
	protected Control createWidget(final Composite parent) {
		return new ChoiceComposite(parent, SWT.NONE, true);
	}

	@Override
	protected IRidget createRidget() {
		return new MultipleChoiceRidget();
	}

	@Override
	protected ChoiceComposite getWidget() {
		return (ChoiceComposite) super.getWidget();
	}

	@Override
	protected IMultipleChoiceRidget getRidget() {
		return (IMultipleChoiceRidget) super.getRidget();
	}

	// testing methods
	// ////////////////

	public void testHasFocus() throws Exception {
		final IMultipleChoiceRidget choiceRidget = getRidget();
		assertNotNull(choiceRidget);

		final ChoiceComposite widget = getWidget();
		assertNotNull(widget);

		assertFalse(choiceRidget.hasFocus());
		getRidget().setUIControl(widget);
		widget.getChildren()[0].setFocus();
		assertTrue(getRidget().hasFocus());
	}

	/**
	 * Test that the control is mapped to the expected ridget.
	 */
	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(MultipleChoiceRidget.class, mapper.getRidgetClass(getWidget()));
	}

	/**
	 * Test method getUIControl().
	 */
	public void testGetUIControl() throws Exception {
		final IMultipleChoiceRidget ridget = getRidget();
		final ChoiceComposite control = getWidget();

		assertEquals(control, ridget.getUIControl());
	}

	/**
	 * Test method getObservableSelectionList().
	 */
	public void testGetObservableSelectionList() throws Exception {
		final IMultipleChoiceRidget ridget = getRidget();

		assertNotNull(ridget.getObservableSelectionList());
	}

	/**
	 * Test method updateFromModel().
	 */
	public void testUpdateFromModel() throws Exception {
		final IMultipleChoiceRidget ridget = getRidget();
		final ChoiceComposite control = getWidget();
		optionProvider.setOptions(Arrays.asList("a", "b", "c", "d"));
		optionProvider.setSelectedOptions(Arrays.asList("c", "d"));

		ridget.updateFromModel();

		final int oldSize = optionProvider.getOptions().size();
		assertEquals(oldSize, ridget.getObservableList().size());
		assertTrue(ridget.getObservableList().containsAll(optionProvider.getOptions()));
		assertEquals(optionProvider.getSelectedOptions().size(), ridget.getObservableSelectionList().size());
		assertTrue(optionProvider.getSelectedOptions().containsAll(ridget.getObservableSelectionList()));
		assertEquals(optionProvider.getSelectedOptions(), getSelectedControlValues(control));

		optionProvider.remove(0);

		assertEquals(oldSize, ridget.getObservableList().size());

		ridget.updateFromModel();

		assertEquals(oldSize - 1, ridget.getObservableList().size());
	}

	/**
	 * Test that updateFromModel() fires events.
	 */
	public void testUpdateFromModelFiresEvents() throws Exception {
		final IMultipleChoiceRidget ridget = getRidget();
		final String element0 = optionProvider.getOptions().get(0);
		final String element1 = optionProvider.getOptions().get(1);
		final List<String> selection1 = Arrays.asList(new String[] { element0 });
		final List<String> selection2 = Arrays.asList(new String[] { element0, element1 });

		ridget.setSelection(selection2);
		assertEquals(2, ridget.getSelection().size());

		final List<PropertyChangeEvent> events = newEventCatchingList(ridget, IChoiceRidget.PROPERTY_SELECTION);
		optionProvider.setSelectedOptions(selection1);

		assertEquals(0, events.size());

		ridget.updateFromModel();

		assertEquals(1, events.size());
		final PropertyChangeEvent event0 = events.get(0);
		assertEquals(IChoiceRidget.PROPERTY_SELECTION, event0.getPropertyName());
		assertEquals(2, ((List<?>) event0.getOldValue()).size());
		assertEquals(1, ((List<?>) event0.getNewValue()).size());

		optionProvider.setSelectedOptions(selection1);
		ridget.updateFromModel();

		assertEquals(1, events.size());

		optionProvider.setSelectedOptions(null);
		ridget.updateFromModel();

		assertEquals(2, events.size());
		final PropertyChangeEvent event1 = events.get(1);
		assertEquals(IChoiceRidget.PROPERTY_SELECTION, event0.getPropertyName());
		assertEquals(1, ((List<?>) event1.getOldValue()).size());
		assertEquals(0, ((List<?>) event1.getNewValue()).size());
	}

	public void testSetSelection() {
		final IMultipleChoiceRidget ridget = getRidget();
		final String element0 = optionProvider.getOptions().get(0);
		final String element1 = optionProvider.getOptions().get(1);
		final List<String> selection1 = Arrays.asList(new String[] { element0 });
		final List<String> selection2 = Arrays.asList(new String[] { element0, element1 });

		ridget.setSelection(selection1);

		assertEquals(1, ridget.getSelection().size());
		assertSame(element0, ridget.getSelection().get(0));
		assertEquals(1, optionProvider.getSelectedOptions().size());
		assertSame(element0, optionProvider.getSelectedOptions().get(0));

		ridget.setSelection(selection2);

		assertEquals(2, ridget.getSelection().size());
		assertSame(element0, ridget.getSelection().get(0));
		assertSame(element1, ridget.getSelection().get(1));
		assertEquals(2, optionProvider.getSelectedOptions().size());
		assertSame(element0, optionProvider.getSelectedOptions().get(0));
		assertSame(element1, optionProvider.getSelectedOptions().get(1));

		ridget.setSelection(null);

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, optionProvider.getSelectedOptions().size());

		try {
			final Object unknownObject = new Object();
			ridget.setSelection(Arrays.asList(unknownObject));
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	/**
	 * As per Bug 304733
	 */
	public void testClearSelectionWhenSelectionIsRemovedFromModel() {
		final IMultipleChoiceRidget ridget = getRidget();

		final String optionA = optionProvider.getOptions().get(0);
		final String optionB = optionProvider.getOptions().get(1);
		ridget.setSelection(Arrays.asList(optionA, optionB));

		assertSame(optionA, ridget.getSelection().get(0));
		assertSame(optionB, ridget.getSelection().get(1));
		assertSame(optionA, optionProvider.getSelectedOptions().get(0));
		assertSame(optionB, optionProvider.getSelectedOptions().get(1));

		optionProvider.getOptions().remove(0);
		ridget.updateFromModel();

		assertSame(optionB, ridget.getSelection().get(0));
		assertSame(optionB, optionProvider.getSelectedOptions().get(0));
	}

	public void testSelectionFiresEvents() {
		final IMultipleChoiceRidget ridget = getRidget();
		final String element0 = optionProvider.getOptions().get(0);
		final String element1 = optionProvider.getOptions().get(1);
		final List<String> selection2 = Arrays.asList(new String[] { element0, element1 });
		ridget.setSelection(null);

		expectPropertyChangeEvent(IChoiceRidget.PROPERTY_SELECTION, Collections.EMPTY_LIST, selection2);
		ridget.setSelection(selection2);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setSelection(selection2);
		verifyPropertyChangeEvents();

		expectPropertyChangeEvent(IChoiceRidget.PROPERTY_SELECTION, selection2, Collections.EMPTY_LIST);
		ridget.setSelection(null);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setSelection(null);
		verifyPropertyChangeEvents();
	}

	public void testSelectionUpdatesControl() {
		final IMultipleChoiceRidget ridget = getRidget();
		final ChoiceComposite control = getWidget();
		final String element0 = optionProvider.getOptions().get(0);
		final String element1 = optionProvider.getOptions().get(1);
		final List<String> selection2 = Arrays.asList(new String[] { element0, element1 });

		ridget.setSelection(selection2);

		assertEquals(2, getSelectionCount(control));
		assertTrue(getSelectedControlValues(control).containsAll(selection2));

		ridget.setSelection(null);

		assertEquals(0, getSelectionCount(control));
	}

	public void testUpdateFromModelUpdatesControl() {
		final IMultipleChoiceRidget ridget = getRidget();
		final ChoiceComposite control = getWidget();
		final String element0 = optionProvider.getOptions().get(0);
		final String element1 = optionProvider.getOptions().get(1);
		final List<String> selection2 = Arrays.asList(new String[] { element0, element1 });

		optionProvider.setSelectedOptions(selection2);
		ridget.updateFromModel();

		assertEquals(2, getSelectionCount(control));
		assertTrue(getSelectedControlValues(control).containsAll(selection2));

		optionProvider.setSelectedOptions(null);
		ridget.updateFromModel();

		assertEquals(0, getSelectionCount(control));
	}

	public void testUserSetSelection() throws Exception {
		final IMultipleChoiceRidget ridget = getRidget();
		final ChoiceComposite control = getWidget();
		optionProvider.setSelectedOptions(new ArrayList<String>());
		ridget.updateFromModel();
		assertTrue("Initially no option selected in model", optionProvider.getSelectedOptions().isEmpty());
		assertEquals("Initially no option selected in ridget", 0, ridget.getSelection().size());

		final List<PropertyChangeEvent> events = newEventCatchingList(ridget, IChoiceRidget.PROPERTY_SELECTION);

		// focus and select first check box
		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), " ");

		assertEquals("Option successfully selected in model", 1, optionProvider.getSelectedOptions().size());
		assertEquals("Option successfully selected in ridget", 1, ridget.getSelection().size());
		assertEquals("PropertyChangedEvent fired", 1, events.size());
		assertEquals("PropertyChangedEvent fired", events.get(0), IChoiceRidget.PROPERTY_SELECTION,
				Collections.EMPTY_LIST, Arrays.asList(optionProvider.getOptions().get(0)));
	}

	/**
	 * Test method addPropertyChangeListener(), removePropertyChangeListener().
	 */
	public void testAddRemovePropertyChangeListener() throws Exception {
		final IMultipleChoiceRidget ridget = getRidget();
		final TestPropertyChangeListener l = new TestPropertyChangeListener();
		ridget.addPropertyChangeListener(l);

		assertEquals(optionProvider.getSelectedOptions(), ridget.getSelection());

		ridget.setSelection(Arrays.asList(optionProvider.getOptions().get(1)));

		assertEquals(ridget.getSelection(), optionProvider.getSelectedOptions());
		assertEquals(1, l.eventCounter);

		ridget.removePropertyChangeListener(l);

		ridget.setSelection(Arrays.asList(optionProvider.getOptions().get(0)));

		assertEquals(ridget.getSelection(), optionProvider.getSelectedOptions());
		assertEquals(1, l.eventCounter);
	}

	/**
	 * Test method bindToModel() using labels.
	 */
	public void testBindToModelUsingLabels() throws Exception {
		final IMultipleChoiceRidget ridget = getRidget();
		final Composite control = getWidget();
		optionProvider = new OptionProvider();

		ridget.bindToModel(optionProvider.getOptions(), optionProvider.getOptionLabels(), optionProvider,
				"selectedOptions");
		ridget.updateFromModel();

		final Object[] labels = optionProvider.getOptionLabels().toArray();
		final Control[] children = control.getChildren();
		assertEquals(labels.length, children.length);
		for (int i = 0; i < labels.length; i++) {
			final String label = (String) labels[i];
			final String caption = ((Button) children[i]).getText();
			assertEquals(label, caption);
		}
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testDisableMandatoryMarkers() throws Exception {
		final IMultipleChoiceRidget ridget = getRidget();

		final MandatoryMarker mandatoryMarker = new MandatoryMarker();

		optionProvider.setSelectedOptions(new ArrayList<String>());
		ridget.updateFromModel();
		ridget.addMarker(mandatoryMarker);

		assertFalse(mandatoryMarker.isDisabled());

		ridget.setSelection(Arrays.asList(optionProvider.getOptions().get(1)));

		assertTrue(mandatoryMarker.isDisabled());

		ridget.setSelection(Collections.EMPTY_LIST);

		assertFalse(mandatoryMarker.isDisabled());

		// marker should be enabled when selecting something that does not exist
		Exception expectedEx = null;
		try {
			ridget.setSelection(Arrays.asList(new Object()));
		} catch (final RuntimeException ex) {
			expectedEx = ex;
		}

		assertNotNull("IllegalArgumentException expected", expectedEx);
		assertFalse(mandatoryMarker.isDisabled());
	}

	/**
	 * Tests that the isDisabledMandatoryMarker true when we have a selection.
	 */
	@Override
	public void testIsDisableMandatoryMarker() {
		final IMultipleChoiceRidget ridget = getRidget();

		optionProvider.setSelectedOptions(null);
		ridget.updateFromModel();

		assertFalse(ridget.isDisableMandatoryMarker());

		final List<String> newSelection = new ArrayList<String>();
		newSelection.add(optionProvider.getOptions().get(1));
		ridget.setSelection(newSelection);

		assertTrue(ridget.isDisableMandatoryMarker());

		ridget.setSelection(null);

		assertFalse(ridget.isDisableMandatoryMarker());
	}

	/**
	 * Tests that colors from the ChoiceComposite are applied to children.
	 */
	public void testColorsAreAppliedToChildren() {
		final Shell shell = getShell();
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, true);
		final Color colorRed = shell.getDisplay().getSystemColor(SWT.COLOR_RED);
		final Color colorGreen = shell.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		control.setForeground(colorGreen);
		control.setBackground(colorRed);
		getRidget().setUIControl(control);

		final Control[] children = control.getChildren();
		assertTrue(children.length > 0);
		for (final Control child : children) {
			assertEquals("wrong foreground on " + child, colorGreen, child.getForeground());
			assertEquals("wrong background on " + child, colorRed, child.getBackground());
		}
	}

	/**
	 * Tests that enablement from the ChoiceComposite is applied to children.
	 */
	public void testEnablementIsAppliedToChildren() {
		final Shell shell = getShell();
		final ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, true);

		assertTrue(control.isEnabled());

		getRidget().setEnabled(false);
		getRidget().setUIControl(control);

		assertFalse(control.isEnabled());
		final Control[] children = control.getChildren();
		assertTrue(children.length > 0);
		for (final Control child : children) {
			assertFalse("control should be disabled: " + child, child.isEnabled());
		}
	}

	/**
	 * Test validation of the bindToModel(...) method.
	 */
	public void testBindToModelWithObservables() {
		final IMultipleChoiceRidget ridget = getRidget();

		try {
			ridget.bindToModel(null, PojoObservables.observeList(Realm.getDefault(), optionProvider, "selectedOptions"));
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
		try {
			ridget.bindToModel(PojoObservables.observeList(Realm.getDefault(), optionProvider, "options"), null);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	/**
	 * Test validation of the bindToModel(...) method.
	 */
	public void testBindToModelWithBeans() {
		final IMultipleChoiceRidget ridget = getRidget();

		try {
			ridget.bindToModel(null, "options", optionProvider, "selectedOptions");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
		try {
			ridget.bindToModel(optionProvider, null, optionProvider, "selectedOptions");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
		try {
			ridget.bindToModel(optionProvider, "options", null, "selectedOptions");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
		try {
			ridget.bindToModel(optionProvider, "options", optionProvider, null);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	/**
	 * Test validation of the bindToModel(...) method.
	 */
	public void testBindToModelWithOptionLabelList() {
		final IMultipleChoiceRidget ridget = getRidget();

		try {
			ridget.bindToModel(null, optionProvider.getOptionLabels(), optionProvider, "selectedOptions");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
		try {
			ridget.bindToModel(optionProvider.getOptions(), (List<String>) null, optionProvider, "selectedOptions");
			ok();
		} catch (final RuntimeException rex) {
			fail();
		}
		try {
			ridget.bindToModel(optionProvider.getOptions(), new ArrayList<String>(), optionProvider, "selectedOptions");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
		try {
			ridget.bindToModel(optionProvider.getOptions(), optionProvider.getOptionLabels(), null, "selectedOptions");
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
		try {
			ridget.bindToModel(optionProvider.getOptions(), optionProvider.getOptionLabels(), optionProvider, null);
			fail();
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	public void testOutputCannotBeChangedFromUI() {
		final IMultipleChoiceRidget ridget = getRidget();
		final Button button1 = (Button) getWidget().getChildren()[0];
		final Button button2 = (Button) getWidget().getChildren()[1];
		final Button button3 = (Button) getWidget().getChildren()[2];

		assertTrue(button1.getSelection());
		assertTrue(button2.getSelection());
		assertFalse(button3.getSelection());
		assertEquals(2, ridget.getSelection().size());
		assertTrue(ridget.getSelection().contains("Option A"));
		assertTrue(ridget.getSelection().contains("Option B"));

		ridget.setOutputOnly(true);
		button1.setFocus();
		UITestHelper.sendString(button1.getDisplay(), " ");
		button3.setFocus();
		UITestHelper.sendString(button3.getDisplay(), " ");

		assertTrue(button1.getSelection());
		assertTrue(button2.getSelection());
		assertFalse(button3.getSelection());
		assertEquals(2, ridget.getSelection().size());
		assertTrue(ridget.getSelection().contains("Option A"));
		assertTrue(ridget.getSelection().contains("Option B"));

		ridget.setOutputOnly(false);
		button1.setFocus();
		UITestHelper.sendString(button1.getDisplay(), " ");
		button3.setFocus();
		UITestHelper.sendString(button3.getDisplay(), " ");

		assertFalse(button1.getSelection());
		assertTrue(button2.getSelection());
		assertTrue(button3.getSelection());
		assertEquals(2, ridget.getSelection().size());
		assertTrue(ridget.getSelection().contains("Option B"));
		assertTrue(ridget.getSelection().contains("Option C"));
	}

	/**
	 * Tests that changing the selected state via
	 * {@link IToggleButtonRidget#setSelected(boolean) does not select the
	 * control, when the ridget is disabled.
	 */
	public void testDisabledRidgetDoesNotCheckControlOnRidgetSelection() {
		final IMultipleChoiceRidget ridget = getRidget();
		final ChoiceComposite control = getWidget();

		ridget.setSelection(Arrays.asList("Option A"));

		assertEquals("Option A", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option A", ridget.getSelection().get(0));
		assertEquals("Option A", getSelectedControlValues(control).get(0));

		ridget.setEnabled(false);

		assertEquals("Option A", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option A", ridget.getSelection().get(0));
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			assertEquals(0, getSelectionCount(control));
		} else {
			assertEquals("Option A", getSelectedControlValues(control).get(0));
		}

		ridget.setSelection(Arrays.asList("Option B"));

		assertEquals("Option B", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option B", ridget.getSelection().get(0));
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			assertEquals(0, getSelectionCount(control));
		} else {
			assertEquals("Option B", getSelectedControlValues(control).get(0));
		}

		ridget.setEnabled(true);

		assertEquals("Option B", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option B", ridget.getSelection().get(0));
		assertEquals("Option B", getSelectedControlValues(control).get(0));
	}

	/**
	 * Tests that changing the selected state via a bound model, does not select
	 * the control, when the ridget is disabled.
	 */
	public void testDisabledRidgetDoesNotCheckControlOnModelSelection() {
		final IMultipleChoiceRidget ridget = getRidget();
		final ChoiceComposite control = getWidget();

		ridget.setEnabled(false);
		optionProvider.setSelectedOptions(Arrays.asList("Option A"));
		ridget.updateFromModel();

		assertEquals("Option A", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option A", ridget.getSelection().get(0));
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			assertEquals(0, getSelectionCount(control));
		} else {
			assertEquals("Option A", getSelectedControlValues(control).get(0));
		}

		optionProvider.setSelectedOptions(Arrays.asList("Option B"));
		ridget.updateFromModel();

		assertEquals("Option B", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option B", ridget.getSelection().get(0));
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			assertEquals(0, getSelectionCount(control));
		} else {
			assertEquals("Option B", getSelectedControlValues(control).get(0));
		}

		ridget.setEnabled(true);

		assertEquals("Option B", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option B", ridget.getSelection().get(0));
		assertEquals("Option B", getSelectedControlValues(control).get(0));
	}

	/**
	 * Tests that disabling the ridget does not fire 'selected' events, even
	 * though the control is modified.
	 */
	public void testDisabledDoesNotFireSelected() {
		final IMultipleChoiceRidget ridget = getRidget();
		ridget.setEnabled(true);
		ridget.setSelection(Arrays.asList("Option A", "Option B"));

		ridget.addPropertyChangeListener(ISingleChoiceRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				fail("Unexpected property change event: " + evt);
			}
		});

		ridget.setEnabled(false);

		ridget.setEnabled(true);
	}

	/**
	 * Tests that the disabled state is applied to a new control when set into
	 * the ridget.
	 */
	public void testDisableAndClearOnBind() {
		final IMultipleChoiceRidget ridget = getRidget();
		final ChoiceComposite control = getWidget();

		ridget.setUIControl(null);
		ridget.setEnabled(false);
		ridget.setSelection(Arrays.asList("Option B"));
		ridget.setUIControl(control);

		assertFalse(control.isEnabled());
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			assertEquals(0, getSelectionCount(control));
		} else {
			assertEquals(1, getSelectionCount(control));
			assertEquals("Option B", getSelectedControlValues(control).get(0));
		}

		ridget.setEnabled(true);

		assertTrue(control.isEnabled());
		assertEquals(1, getSelectionCount(control));
		assertEquals("Option B", getSelectedControlValues(control).get(0));
	}

	public void testAddSelectionListener() {

		final IMultipleChoiceRidget ridget = getRidget();

		try {
			ridget.addSelectionListener(null);
			fail();
		} catch (final RuntimeException npe) {
			ok();
		}

		final TestSelectionListener selectionListener = new TestSelectionListener();
		ridget.addSelectionListener(selectionListener);

		final String element0 = optionProvider.getOptions().get(0);
		final String element1 = optionProvider.getOptions().get(1);
		final List<String> selection1 = Arrays.asList(new String[] { element0 });
		final List<String> selection2 = Arrays.asList(new String[] { element0, element1 });

		ridget.setSelection(selection1);

		assertEquals(1, ridget.getSelection().size());
		assertSame(element0, ridget.getSelection().get(0));
		assertEquals(1, optionProvider.getSelectedOptions().size());
		assertSame(element0, optionProvider.getSelectedOptions().get(0));
		assertEquals(1, selectionListener.getCount());

		ridget.setSelection(selection2);

		assertEquals(2, ridget.getSelection().size());
		assertSame(element0, ridget.getSelection().get(0));
		assertSame(element1, ridget.getSelection().get(1));
		assertEquals(2, optionProvider.getSelectedOptions().size());
		assertSame(element0, optionProvider.getSelectedOptions().get(0));
		assertSame(element1, optionProvider.getSelectedOptions().get(1));
		assertEquals(2, selectionListener.getCount());

		ridget.removeSelectionListener(selectionListener);
		ridget.setSelection(null);
		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, optionProvider.getSelectedOptions().size());
		assertEquals(2, selectionListener.getCount());
	}

	/**
	 * As per Bug 321927
	 */
	public void testToggleDisabledWhenOutputOnly() {
		final IMultipleChoiceRidget ridget = getRidget();
		final ChoiceComposite control = getWidget();
		final Button btnFirst = (Button) control.getChildren()[0];
		final String first = optionProvider.getOptions().get(0);
		ridget.setSelection(Arrays.asList(first));

		assertTrue(btnFirst.getSelection());

		ridget.setOutputOnly(true);
		ridget.setEnabled(false);
		ridget.setEnabled(true);
		for (final Control child : control.getChildren()) {
			final Button button = (Button) child;
			if (button == btnFirst) {
				assertTrue(btnFirst.isEnabled());
				assertTrue(btnFirst.getSelection());
			} else {
				assertFalse(button.isEnabled());
				assertFalse(button.getSelection());
			}
		}
	}

	/**
	 * As per Bug 321927 - test setSelection() and output only := true
	 */
	public void testSelectionWithOutputOnly() {
		final IMultipleChoiceRidget ridget = getRidget();
		final ChoiceComposite control = getWidget();
		final String first = optionProvider.getOptions().get(0);
		final String second = optionProvider.getOptions().get(1);
		ridget.setSelection(Arrays.asList(first));

		assertEquals(1, ridget.getSelection().size());
		assertEquals(first, ridget.getSelection().get(0));

		ridget.setOutputOnly(true);
		ridget.setSelection(Arrays.asList(second));

		assertEquals(1, ridget.getSelection().size());
		assertEquals(second, ridget.getSelection().get(0));
		final Button btnSecond = (Button) control.getChildren()[1];
		for (final Control child : control.getChildren()) {
			final Button button = (Button) child;
			if (button == btnSecond) {
				assertTrue(btnSecond.isEnabled());
				assertTrue(btnSecond.getSelection());
			} else {
				assertFalse(button.isEnabled());
				assertFalse(button.getSelection());
			}
		}
	}

	// helping methods
	// ////////////////

	private static List<PropertyChangeEvent> newEventCatchingList(final IRidget ridget, final String eventName) {
		final List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();
		// note: somehow expectPropertyChangeEvent(..) and
		// verifyPropertyChangeEvents() did not work properly here

		ridget.addPropertyChangeListener(eventName, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				events.add(evt);
			}
		});
		return events;
	}

	private void assertEquals(final String message, final PropertyChangeEvent evt, final String name,
			final Object oldValue, final Object newValue) {
		assertEquals(message + " / event name", evt.getPropertyName(), name);
		assertEquals(message + " / event old Value", evt.getOldValue(), oldValue);
		assertEquals(message + " / event new value", evt.getNewValue(), newValue);
	}

	private List<Object> getSelectedControlValues(final ChoiceComposite control) {
		final List<Object> result = new ArrayList<Object>();
		for (final Control child : control.getChildren()) {
			final Button button = (Button) child;
			if (button.getSelection()) {
				result.add(button.getData());
			}
		}
		return result;
	}

	private int getSelectionCount(final ChoiceComposite control) {
		return getSelectedControlValues(control).size();
	}

	// helping classes
	// ////////////////

	private static final class OptionProvider {

		private List<String> options = new ArrayList<String>(Arrays.asList("Option A", "Option B", "Option C",
				"Option D", "Option E", "Option F"));
		private List<String> optionLabels = new ArrayList<String>(Arrays.asList("Option label A", "Option label B",
				"Option label C", "Option label D", "Option label E", "Option label F"));
		private List<String> selectedOptions = Arrays.asList(options.get(0), options.get(1));

		public List<String> getOptions() {
			return options;
		}

		public List<String> getSelectedOptions() {
			return selectedOptions;
		}

		public void setOptions(final List<String> options) {
			this.options = new ArrayList<String>(options);
			optionLabels = null;
			selectedOptions = new ArrayList<String>();
		}

		public void setSelectedOptions(final List<String> selectedOptions) {
			this.selectedOptions = selectedOptions;
		}

		public List<String> getOptionLabels() {
			return optionLabels;
		}

		public void remove(final int index) {
			options.remove(index);
			if (optionLabels != null) {
				optionLabels.remove(index);
			}
		}
	}

	private static final class TestPropertyChangeListener implements PropertyChangeListener {
		private int eventCounter = 0;

		public void propertyChange(final PropertyChangeEvent evt) {
			eventCounter++;
		}
	}

}
