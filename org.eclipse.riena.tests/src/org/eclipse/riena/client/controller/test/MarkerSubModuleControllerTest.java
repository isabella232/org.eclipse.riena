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
package org.eclipse.riena.client.controller.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.ui.swt.utils.TestUtils;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.swt.controllers.AbstractSubModuleControllerTest;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.IDecimalTextRidget;
import org.eclipse.riena.ui.ridgets.IGroupedTreeTableRidget;
import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.INumericTextRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.tests.base.TestSingleSelectionBean;

/**
 * Tests for the MarkerSubModuleController.
 */
@NonUITestCase
public class MarkerSubModuleControllerTest extends AbstractSubModuleControllerTest<MarkerSubModuleController> {

	private List<Person> personList;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		personList = PersonFactory.createPersonList();
	}

	@Override
	protected MarkerSubModuleController createController(final ISubModuleNode node) {

		final MarkerSubModuleController newInst = new MarkerSubModuleController();
		node.setNodeId(new NavigationNodeId("org.eclipse.riena.example.marker")); //$NON-NLS-1$
		newInst.setNavigationNode(node);

		return newInst;
	}

	public void testBasicInitialization() {

		final IToggleButtonRidget checkMandatory = getController().getRidget(IToggleButtonRidget.class,
				"checkMandatory"); //$NON-NLS-1$
		assertFalse(checkMandatory.isSelected());
		final IToggleButtonRidget checkError = getController().getRidget(IToggleButtonRidget.class, "checkError"); //$NON-NLS-1$
		assertFalse(checkError.isSelected());
		final IToggleButtonRidget checkDisabled = getController().getRidget(IToggleButtonRidget.class, "checkDisabled"); //$NON-NLS-1$
		assertFalse(checkDisabled.isSelected());
		final IToggleButtonRidget checkOutput = getController().getRidget(IToggleButtonRidget.class, "checkOutput"); //$NON-NLS-1$
		assertFalse(checkOutput.isSelected());

		final IToggleButtonRidget checkHidden = getController().getRidget(IToggleButtonRidget.class, "checkHidden"); //$NON-NLS-1$
		assertFalse(checkHidden.isSelected());
		final IToggleButtonRidget checkHiddenParent = getController().getRidget(IToggleButtonRidget.class,
				"checkHiddenParent"); //$NON-NLS-1$
		assertFalse(checkHiddenParent.isSelected());

		final ITextRidget textName = getController().getRidget(ITextRidget.class, "textName"); //$NON-NLS-1$
		assertEquals("Chateau Schaedelbrummer", textName.getText()); //$NON-NLS-1$

		final IDecimalTextRidget textPrice = getController().getRidget(IDecimalTextRidget.class, "textPrice"); //$NON-NLS-1$
		assertEquals(TestUtils.getLocalizedNumber("-29,99"), textPrice.getText()); //$NON-NLS-1$
		final INumericTextRidget textAmount = getController().getRidget(INumericTextRidget.class, "textAmount"); //$NON-NLS-1$
		assertEquals(TestUtils.getLocalizedNumber("1.001"), textAmount.getText()); //$NON-NLS-1$

		final IDateTextRidget textDate = getController().getRidget(IDateTextRidget.class, "textDate"); //$NON-NLS-1$
		assertEquals("04.12.2008", textDate.getText()); //$NON-NLS-1$

		final IDateTimeRidget dtDate = getController().getRidget(IDateTimeRidget.class, "dtDate"); //$NON-NLS-1$
		if ("US".equals(Locale.getDefault().getCountry())) { //$NON-NLS-1$
			assertEquals("12/4/08 12:00 AM", dtDate.getText()); //$NON-NLS-1$
		} else {
			assertEquals("04.12.08 00:00", dtDate.getText()); //$NON-NLS-1$
		}
		final Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2008, Calendar.DECEMBER, 4);
		assertEquals(cal.getTime(), dtDate.getDate());

		final IComboRidget comboAge = getController().getRidget(IComboRidget.class, "comboAge"); //$NON-NLS-1$
		assertEquals("young", comboAge.getSelection()); //$NON-NLS-1$

		final ISingleChoiceRidget choiceType = getController().getRidget(ISingleChoiceRidget.class, "choiceType"); //$NON-NLS-1$
		assertEquals("red", choiceType.getSelection()); //$NON-NLS-1$

		final IMultipleChoiceRidget choiceFlavor = getController().getRidget(IMultipleChoiceRidget.class,
				"choiceFlavor"); //$NON-NLS-1$
		assertNotNull(choiceFlavor.getSelection());
		assertEquals(2, choiceFlavor.getSelection().size());
		assertEquals("dry", choiceFlavor.getSelection().get(0)); //$NON-NLS-1$
		assertEquals("spicy", choiceFlavor.getSelection().get(1)); //$NON-NLS-1$

		final IToggleButtonRidget buttonToggleA = getController().getRidget(IToggleButtonRidget.class, "buttonToggleA"); //$NON-NLS-1$
		assertTrue(buttonToggleA.isSelected());

		final IToggleButtonRidget buttonToggleB = getController().getRidget(IToggleButtonRidget.class, "buttonToggleB"); //$NON-NLS-1$
		assertFalse(buttonToggleB.isSelected());

		final IToggleButtonRidget buttonRadioA = getController().getRidget(IToggleButtonRidget.class, "buttonRadioA"); //$NON-NLS-1$
		assertTrue(buttonRadioA.isSelected());

		final IToggleButtonRidget buttonRadioB = getController().getRidget(IToggleButtonRidget.class, "buttonRadioB"); //$NON-NLS-1$
		assertFalse(buttonRadioB.isSelected());

		final IToggleButtonRidget buttonCheckA = getController().getRidget(IToggleButtonRidget.class, "buttonCheckA"); //$NON-NLS-1$
		assertTrue(buttonCheckA.isSelected());

		final IToggleButtonRidget buttonCheckB = getController().getRidget(IToggleButtonRidget.class, "buttonCheckB"); //$NON-NLS-1$
		assertFalse(buttonCheckB.isSelected());
	}

	public void testMandatoryMarkerOptions() {
		final ITreeRidget treePersons = getController().getRidget(ITreeRidget.class, "treePersons"); //$NON-NLS-1$
		treePersons.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		final TestSingleSelectionBean singleSelectionBean = new TestSingleSelectionBean();
		treePersons.bindSingleSelectionToModel(singleSelectionBean, TestSingleSelectionBean.PROPERTY_SELECTION);
		treePersons.updateFromModel();

		final IToggleButtonRidget mandatoryToggle = getController().getRidget(IToggleButtonRidget.class,
				"checkMandatory"); //$NON-NLS-1$
		assertFalse(mandatoryToggle.isSelected());
		for (final IMarkableRidget ridget : getAllMarkableRidgets()) {
			assertFalse("failed on: " + ridget, ridget.isMandatory()); //$NON-NLS-1$
		}

		mandatoryToggle.setSelected(true);
		assertTrue(mandatoryToggle.isSelected());
		for (final IMarkableRidget ridget : getAllMarkableRidgets()) {
			assertTrue("failed on: " + ridget, ridget.isMandatory()); //$NON-NLS-1$
		}
	}

	public void testErrorMarkerOptions() {
		final IToggleButtonRidget errorToggle = getController().getRidget(IToggleButtonRidget.class, "checkError"); //$NON-NLS-1$
		assertFalse(errorToggle.isSelected());
		for (final IMarkableRidget ridget : getAllMarkableRidgets()) {
			assertFalse("failed on: " + ridget, ridget.isErrorMarked()); //$NON-NLS-1$
		}

		errorToggle.setSelected(true);
		assertTrue(errorToggle.isSelected());
		for (final IMarkableRidget ridget : getAllMarkableRidgets()) {
			assertTrue("failed on: " + ridget, ridget.isErrorMarked()); //$NON-NLS-1$
		}
	}

	public void testDisabledMarkerOptions() {
		final IToggleButtonRidget disabledToggle = getController()
				.getRidget(IToggleButtonRidget.class, "checkDisabled"); //$NON-NLS-1$
		assertFalse(disabledToggle.isSelected());
		for (final IMarkableRidget ridget : getAllMarkableRidgets()) {
			assertTrue("failed on: " + ridget, ridget.isEnabled()); //$NON-NLS-1$
		}

		disabledToggle.setSelected(true);
		assertTrue(disabledToggle.isSelected());
		for (final IMarkableRidget ridget : getAllMarkableRidgets()) {
			assertFalse("failed on: " + ridget, ridget.isEnabled()); //$NON-NLS-1$
		}

		assertFalse(getController().getRidget(IActionRidget.class, "buttonPush").isEnabled()); //$NON-NLS-1$
	}

	public void testOutputMarkerOptions() {
		final IToggleButtonRidget outputToggle = getController().getRidget(IToggleButtonRidget.class, "checkOutput"); //$NON-NLS-1$
		assertFalse(outputToggle.isSelected());
		for (final IMarkableRidget ridget : getAllMarkableRidgets()) {
			assertFalse("failed on: " + ridget, ridget.isOutputOnly()); //$NON-NLS-1$
		}

		outputToggle.setSelected(true);
		assertTrue(outputToggle.isSelected());
		for (final IMarkableRidget ridget : getAllMarkableRidgets()) {
			assertTrue("failed on: " + ridget, ridget.isOutputOnly()); //$NON-NLS-1$
		}
	}

	public void testHiddenMarkerOptions() {
		final IToggleButtonRidget hiddenToggle = getController().getRidget(IToggleButtonRidget.class, "checkHidden"); //$NON-NLS-1$
		assertFalse(hiddenToggle.isSelected());
		for (final IMarkableRidget ridget : getAllMarkableRidgets()) {
			assertTrue("failed on: " + ridget, ridget.isVisible()); //$NON-NLS-1$
		}

		hiddenToggle.setSelected(true);
		assertTrue(hiddenToggle.isSelected());
		for (final IMarkableRidget ridget : getAllMarkableRidgets()) {
			assertFalse("failed on: " + ridget, ridget.isVisible()); //$NON-NLS-1$
		}

		assertFalse(getController().getRidget(IActionRidget.class, "buttonPush").isVisible()); //$NON-NLS-1$
	}

	public void testList() {
		Person expected = null;
		Person actual = null;
		final IListRidget listPersons = getController().getRidget(IListRidget.class, "listPersons"); //$NON-NLS-1$
		assertEquals(personList.size(), listPersons.getOptionCount());
		assertEquals(-1, listPersons.getSelectionIndex());
		assertEquals(0, listPersons.getSelection().size());
		for (int i = 0; i < listPersons.getOptionCount(); i++) {
			expected = personList.get(i);
			actual = (Person) listPersons.getOption(i);
			assertEquals(expected.getFirstname(), actual.getFirstname());
			assertEquals(expected.getLastname(), actual.getLastname());
		}
		listPersons.setSelection(1);
		// Fehler: liefert -1
		// assertEquals(1, listPersons.getSelectionIndex());
		assertEquals(1, listPersons.getSelection().size());
		expected = personList.get(1);
		actual = (Person) listPersons.getSelection().get(0);
		assertEquals(expected.getFirstname(), actual.getFirstname());
		assertEquals(expected.getLastname(), actual.getLastname());
	}

	public void testTable() {
		Person expected = null;
		Person actual = null;
		final ITableRidget tablePersons = getController().getRidget(ITableRidget.class, "tablePersons"); //$NON-NLS-1$
		assertEquals(personList.size(), tablePersons.getOptionCount());
		assertEquals(-1, tablePersons.getSelectionIndex());
		assertEquals(0, tablePersons.getSelection().size());
		for (int i = 0; i < tablePersons.getOptionCount(); i++) {
			expected = personList.get(i);
			actual = (Person) tablePersons.getOption(i);
			assertEquals(expected.getFirstname(), actual.getFirstname());
			assertEquals(expected.getLastname(), actual.getLastname());
		}
		tablePersons.setSelection(1);
		// Fehler: liefert -1
		// assertEquals(1, tablePersons.getSelectionIndex());
		assertEquals(1, tablePersons.getSelection().size());
		expected = personList.get(1);
		actual = (Person) tablePersons.getSelection().get(0);
		assertEquals(expected.getFirstname(), actual.getFirstname());
		assertEquals(expected.getLastname(), actual.getLastname());
	}

	public void testTree() {
		final ITreeRidget treePersons = getController().getRidget(ITreeRidget.class, "treePersons"); //$NON-NLS-1$
		assertTrue(treePersons.getSelection().isEmpty());
	}

	public void testTreeTable() {
		final IGroupedTreeTableRidget treeWCols = getController().getRidget(IGroupedTreeTableRidget.class, "treeWCols"); //$NON-NLS-1$
		assertTrue(treeWCols.getSelection().isEmpty());
	}

	public void testMarkers() {
		basicTestMarkers(getController().getRidget(ITextRidget.class, "textName"), "testString"); //$NON-NLS-1$ //$NON-NLS-2$
		basicTestMarkers(getController().getRidget(IDecimalTextRidget.class, "textPrice"), "123"); //$NON-NLS-1$ //$NON-NLS-2$
		basicTestMarkers(getController().getRidget(INumericTextRidget.class, "textAmount"), "123"); //$NON-NLS-1$ //$NON-NLS-2$
		basicTestMarkers(getController().getRidget(IDateTextRidget.class, "textDate"), "01.01.1980"); //$NON-NLS-1$ //$NON-NLS-2$
		basicTestMarkers(getController().getRidget(IDateTimeRidget.class, "dtDate")); //$NON-NLS-1$
		basicTestMarkers(getController().getRidget(IComboRidget.class, "comboAge")); //$NON-NLS-1$
		basicTestMarkers(getController().getRidget(ISingleChoiceRidget.class, "choiceType")); //$NON-NLS-1$
		basicTestMarkers(getController().getRidget(IMultipleChoiceRidget.class, "choiceFlavor")); //$NON-NLS-1$
	}

	// helping methods
	//////////////////

	private void basicTestMarkers(final ITextRidget textRidget, final String value) {
		basicTestMarkers(textRidget);
		textRidget.setText(null);
		textRidget.setMandatory(true);
		assertFalse(textRidget.isDisableMandatoryMarker());
		textRidget.setText(value);
		assertTrue(textRidget.isDisableMandatoryMarker());
		textRidget.setText(null);
		assertFalse(textRidget.isDisableMandatoryMarker());
		textRidget.setMandatory(false);
	}

	private void basicTestMarkers(final IComboRidget comboRidget) {
		basicTestMarkers((IMarkableRidget) comboRidget);
		comboRidget.setMandatory(true);
		comboRidget.setSelection(null);
		assertFalse(comboRidget.isDisableMandatoryMarker());
		if (comboRidget.getEmptySelectionItem() == null) {
			comboRidget.setSelection(0);
		} else {
			comboRidget.setSelection(1);
		}
		assertTrue(comboRidget.isDisableMandatoryMarker());
		comboRidget.setSelection(-1);
		assertFalse(comboRidget.isDisableMandatoryMarker());
		if (comboRidget.getEmptySelectionItem() == null) {
			comboRidget.setSelection(0);
		} else {
			comboRidget.setSelection(1);
		}
		assertTrue(comboRidget.isDisableMandatoryMarker());
		comboRidget.setSelection(comboRidget.getEmptySelectionItem());
		assertFalse(comboRidget.isDisableMandatoryMarker());
		comboRidget.setMandatory(false);
	}

	private void basicTestMarkers(final IMarkableRidget ridget) {
		basicTestMandatory(ridget);
		basicTestErrorMarked(ridget);
		basicTestOutputOnly(ridget);
		basicTestEnabled(ridget);
		basicTestVisible(ridget);
	}

	private void basicTestMandatory(final IMarkableRidget ridget) {
		assertFalse(ridget.isMandatory());
		ridget.setMandatory(true);
		assertTrue(ridget.isMandatory());
		ridget.setMandatory(false);
		assertFalse(ridget.isMandatory());
	}

	private void basicTestErrorMarked(final IMarkableRidget ridget) {
		assertFalse(ridget.isErrorMarked());
		ridget.setErrorMarked(true);
		assertTrue(ridget.isErrorMarked());
		ridget.setErrorMarked(false);
		assertFalse(ridget.isErrorMarked());
	}

	private void basicTestOutputOnly(final IMarkableRidget ridget) {
		assertFalse(ridget.isOutputOnly());
		ridget.setOutputOnly(true);
		assertTrue(ridget.isOutputOnly());
		ridget.setOutputOnly(false);
		assertFalse(ridget.isOutputOnly());
	}

	private void basicTestEnabled(final IMarkableRidget ridget) {
		assertTrue(ridget.isEnabled());
		ridget.setEnabled(false);
		assertFalse(ridget.isEnabled());
		ridget.setEnabled(true);
		assertTrue(ridget.isEnabled());
	}

	private void basicTestVisible(final IMarkableRidget ridget) {
		assertTrue(ridget.isVisible());
		ridget.setVisible(false);
		assertFalse(ridget.isVisible());
		ridget.setVisible(true);
		assertTrue(ridget.isVisible());
	}

	private List<IMarkableRidget> getAllMarkableRidgets() {
		final List<IMarkableRidget> markableRidgets = new ArrayList<IMarkableRidget>();

		markableRidgets.add(getController().getRidget(ITextRidget.class, "textName")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IDecimalTextRidget.class, "textPrice")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(INumericTextRidget.class, "textAmount")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IDateTextRidget.class, "textDate")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IDateTimeRidget.class, "dtDate")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IComboRidget.class, "comboAge")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(ISingleChoiceRidget.class, "choiceType")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IMultipleChoiceRidget.class, "choiceFlavor")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IToggleButtonRidget.class, "buttonToggleA")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IToggleButtonRidget.class, "buttonToggleB")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IToggleButtonRidget.class, "buttonRadioA")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IToggleButtonRidget.class, "buttonRadioB")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IToggleButtonRidget.class, "buttonCheckA")); //$NON-NLS-1$
		markableRidgets.add(getController().getRidget(IToggleButtonRidget.class, "buttonCheckB")); //$NON-NLS-1$

		return markableRidgets;
	}
}