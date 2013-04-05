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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.beans.common.TypedBean;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.CComboRidget;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.swt.controllers.AbstractSubModuleControllerTest;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IScaleRidget;
import org.eclipse.riena.ui.ridgets.ISliderRidget;
import org.eclipse.riena.ui.ridgets.ISpinnerRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.ITraverseRidget;

/**
 * Test for most of the existing ridgets.
 */
@NonUITestCase
public class ControllerTestsPlaygroundSubModuleControllerTest extends AbstractSubModuleControllerTest<ControllerTestsPlaygroundSubModuleController> {

	private final List<Person> persons = PersonFactory.createPersonList();

	@Override
	protected ControllerTestsPlaygroundSubModuleController createController(final ISubModuleNode node) {
		final ControllerTestsPlaygroundSubModuleController newInst = new ControllerTestsPlaygroundSubModuleController();
		node.setNodeId(new NavigationNodeId("org.eclipse.riena.example.marker")); //$NON-NLS-1$
		newInst.setNavigationNode(node);
		return newInst;
	}

	public void testScaleSpinner() {
		final ITraverseRidget scale = getController().getRidget(IScaleRidget.class, "celsiusScale"); //$NON-NLS-1$
		final ISpinnerRidget fahrenheitSpinner = getController().getRidget(ISpinnerRidget.class, "fahrenheitSpinner"); //$NON-NLS-1$
		final ISliderRidget kelvinSlider = getController().getRidget(ISliderRidget.class, "kelvinSlider"); //$NON-NLS-1$

		assertEquals(0, scale.getValue());
		assertEquals(32, fahrenheitSpinner.getValue());
		assertEquals(273, kelvinSlider.getValue());

		scale.setValue(5);
		scale.triggerListener();
		assertEquals(5, scale.getValue());
		assertEquals(41, fahrenheitSpinner.getValue());
		assertEquals(278, kelvinSlider.getValue());

		fahrenheitSpinner.setValue(100);
		fahrenheitSpinner.triggerListener();
		assertEquals(100, fahrenheitSpinner.getValue());
		assertEquals(38, scale.getValue());
		assertEquals(311, kelvinSlider.getValue());

		kelvinSlider.setValue(300);
		kelvinSlider.triggerListener();
		assertEquals(300, kelvinSlider.getValue());
		assertEquals(81, fahrenheitSpinner.getValue());
		assertEquals(27, scale.getValue());
	}

	public void testCombo() {
		final IComboRidget comboAge = getController().getRidget(IComboRidget.class, "ageCombo"); //$NON-NLS-1$
		final IComboRidget cComboAge = getController().getRidget(CComboRidget.class, "ageCCombo"); //$NON-NLS-1$
		final ITextRidget comboText = getController().getRidget(ITextRidget.class, "comboTextField"); //$NON-NLS-1$
		final ILabelRidget comboLabel = getController().getRidget(ILabelRidget.class, "comboLabel"); //$NON-NLS-1$
		final IActionRidget addToComboButton = getController().getRidget(IActionRidget.class, "addToComboButton"); //$NON-NLS-1$
		final List<String> ages = new ArrayList<String>(Arrays.asList(new String[] { "<none>", "young", "moderate", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"aged", "old" })); //$NON-NLS-1$ //$NON-NLS-2$

		// test default values
		assertNull(comboAge.getSelection());
		assertNull(cComboAge.getSelection());
		assertEquals("", comboText.getText()); //$NON-NLS-1$
		assertEquals("<none>", comboLabel.getText()); //$NON-NLS-1$

		// test selection of comboAge and the proper update of comboLabel
		comboAge.setSelection(ages.get(1));
		assertNull(cComboAge.getSelection());
		assertEquals(1, comboAge.getSelectionIndex());
		assertEquals(ages.get(1), comboAge.getSelection());
		assertEquals(ages.get(1), comboLabel.getText());

		// test adding a new item via the textField
		comboText.setText("too old"); //$NON-NLS-1$
		addToComboButton.fireAction();
		assertEquals(5, comboAge.getSelectionIndex());
		assertEquals(5, cComboAge.getSelectionIndex());
		assertEquals("too old", comboAge.getSelection()); //$NON-NLS-1$
		assertEquals("too old", cComboAge.getSelection()); //$NON-NLS-1$
		assertEquals("too old", comboLabel.getText()); //$NON-NLS-1$
		assertEquals("", comboText.getText()); //$NON-NLS-1$

		// test selection of cComboAge and the proper update of comboLabel
		cComboAge.setSelection(ages.get(2));
		assertEquals(5, comboAge.getSelectionIndex());
		assertEquals(2, cComboAge.getSelectionIndex());
		assertEquals(ages.get(2), cComboAge.getSelection());
		assertEquals(ages.get(2), comboLabel.getText());

	}

	public void testTable() {
		final ITableRidget table = getController().getRidget(ITableRidget.class, "multiTable"); //$NON-NLS-1$
		final IActionRidget copySelectionButton = getController().getRidget(IActionRidget.class, "copySelectionButton"); //$NON-NLS-1$
		final IListRidget list = getController().getRidget(IListRidget.class, "tableList"); //$NON-NLS-1$
		final IToggleButtonRidget selectAllToggleButton = getController().getRidget(IToggleButtonRidget.class, "toggleButton"); //$NON-NLS-1$

		assertFalse(selectAllToggleButton.isSelected());

		assertEquals(persons, table.getObservableList());

		assertTrue(table.getSelection().isEmpty());

		table.setSelection(2);
		assertEquals(persons.get(2), table.getSelection().get(0));
		assertTrue(selectAllToggleButton.isSelected());

		copySelectionButton.fireAction();
		assertEquals(persons.get(2), list.getObservableList().get(0));

		final int[] selection = { 0, 3, 5 };
		table.setSelection(selection);

		final List<Person> selectedPersons = new ArrayList<Person>();
		selectedPersons.add(persons.get(selection[0]));
		selectedPersons.add(persons.get(selection[1]));
		selectedPersons.add(persons.get(selection[2]));

		assertEquals(selectedPersons, table.getSelection());

		copySelectionButton.fireAction();
		assertEquals(selectedPersons, list.getObservableList());

		assertTrue(selectAllToggleButton.isSelected());

		selectAllToggleButton.fireAction();
		assertFalse(selectAllToggleButton.isSelected());
		assertTrue(table.getSelection().isEmpty());

		selectAllToggleButton.fireAction();
		assertEquals(persons.size(), table.getOptionCount());
	}

	public void testMasterDetails() {
		final IMasterDetailsRidget master = getController().getRidget(IMasterDetailsRidget.class, "master"); //$NON-NLS-1$
		final IActionRidget enableDisableButton = getController().getRidget(IActionRidget.class, "enableDisable"); //$NON-NLS-1$
		assertNull(master.getSelection());

		assertTrue(master.isEnabled());

		enableDisableButton.fireAction();
		assertFalse(master.isEnabled());
		// TODO work in progress
	}

	public void testDateTime() throws Exception {
		final IDateTimeRidget dtDate = getController().getRidget(IDateTimeRidget.class, "dtDate"); //$NON-NLS-1$
		final IDateTimeRidget dtTime = getController().getRidget(IDateTimeRidget.class, "dtTime"); //$NON-NLS-1$
		final IDateTimeRidget dtDateOnly = getController().getRidget(IDateTimeRidget.class, "dtDateOnly"); //$NON-NLS-1$
		final IDateTimeRidget dtTimeOnly = getController().getRidget(IDateTimeRidget.class, "dtTimeOnly"); //$NON-NLS-1$
		final IDateTimeRidget dtCal = getController().getRidget(IDateTimeRidget.class, "dtCal"); //$NON-NLS-1$
		final ITextRidget txt1 = getController().getRidget(ITextRidget.class, "txt1"); //$NON-NLS-1$
		final ITextRidget txt2 = getController().getRidget(ITextRidget.class, "txt2"); //$NON-NLS-1$
		final ITextRidget txt3 = getController().getRidget(ITextRidget.class, "txt3"); //$NON-NLS-1$
		final ITextRidget txt4 = getController().getRidget(ITextRidget.class, "txt4"); //$NON-NLS-1$

		// test if the binding between the TextRidget and the DateTimeRidget works
		final long now = ReflectionUtils.getHidden(getController(), "now"); //$NON-NLS-1$
		final TypedBean<Date> date = new TypedBean<Date>(new Date(now));
		assertEquals(date.getValue(), dtDate.getDate());
		assertEquals(date.getValue(), dtTime.getDate());
		assertEquals(date.getValue(), dtDateOnly.getDate());
		assertEquals(date.getValue(), dtTimeOnly.getDate());
		assertEquals(date.getValue(), dtCal.getDate());

		assertEquals(date.getValue().toString(), txt1.getText());
		assertEquals(date.getValue().toString(), txt2.getText());
		assertEquals(date.getValue().toString(), txt3.getText());
		assertEquals(date.getValue().toString(), txt4.getText());

		// test if the setting of a date works
		final Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.MONTH, 5);
		cal.set(Calendar.YEAR, 2011);
		dtDate.setDate(cal.getTime());
		dtTime.setDate(cal.getTime());
		dtDateOnly.setDate(cal.getTime());
		dtTimeOnly.setDate(cal.getTime());
		dtCal.setDate(cal.getTime());
		assertEquals(cal.getTime().toString(), txt1.getText());
		assertEquals(cal.getTime().toString(), txt2.getText());
		assertEquals(cal.getTime().toString(), txt3.getText());
		assertEquals(cal.getTime().toString(), txt4.getText());

		// test if the setting of a date via the DateTextRidget works
		final IDateTextRidget dateTextRidget = getController().getRidget(IDateTextRidget.class, "dateText"); //$NON-NLS-1$
		final IActionRidget dateTimeButton = getController().getRidget(IActionRidget.class, "dateTimeButton"); //$NON-NLS-1$

		dateTextRidget.setText("31.03.1980"); //$NON-NLS-1$
		assertEquals("31.03.1980", dateTextRidget.getText()); //$NON-NLS-1$
		final DateFormat df = new SimpleDateFormat("dd.MM.yyyy"); //$NON-NLS-1$
		final Date newDate = df.parse("31.03.1980"); //$NON-NLS-1$
		dateTimeButton.fireAction();
		assertEquals(newDate, dtDate.getDate());
		assertEquals(newDate, dtDateOnly.getDate());
		assertEquals(newDate, dtCal.getDate());
	}
}
