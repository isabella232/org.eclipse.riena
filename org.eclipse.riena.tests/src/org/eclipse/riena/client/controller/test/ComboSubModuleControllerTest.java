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
package org.eclipse.riena.client.controller.test;

import java.util.List;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.controllers.AbstractSubModuleControllerTest;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Tests for the ComboSubModuleController.
 */
@NonUITestCase
public class ComboSubModuleControllerTest extends AbstractSubModuleControllerTest<ComboSubModuleController> {

	@Override
	protected ComboSubModuleController createController(final ISubModuleNode node) {
		return new ComboSubModuleController(node);
	}

	public void testComboSelection() {
		basicTestComboSelection(0);
		basicTestComboSelection(4);
	}

	public void testNoSelection() {
		final IComboRidget combo = getController().getRidget(IComboRidget.class, "comboOne"); //$NON-NLS-1$
		combo.setSelection(null);
		assertEquals(-1, combo.getSelectionIndex());
		assertEquals(null, combo.getSelection());
	}

	public void testSaveName() {
		final ITextRidget textFirst = getController().getRidget(ITextRidget.class, "textFirst"); //$NON-NLS-1$
		final ITextRidget textLast = getController().getRidget(ITextRidget.class, "textLast"); //$NON-NLS-1$
		final IComboRidget combo = getController().getRidget(IComboRidget.class, "comboOne"); //$NON-NLS-1$
		final IActionRidget saveButton = getController().getRidget(IActionRidget.class, "buttonSave"); //$NON-NLS-1$

		// set new name and save
		final Person newPerson = (Person) combo.getSelection();
		textFirst.setText("Jane"); //$NON-NLS-1$
		textLast.setText("Fonda"); //$NON-NLS-1$

		saveButton.fireAction();

		assertEquals(newPerson, combo.getSelection());
	}

	private void basicTestComboSelection(final int index) {
		final ITextRidget textFirst = getController().getRidget(ITextRidget.class, "textFirst"); //$NON-NLS-1$
		final ITextRidget textLast = getController().getRidget(ITextRidget.class, "textLast"); //$NON-NLS-1$
		final IComboRidget combo = getController().getRidget(IComboRidget.class, "comboOne"); //$NON-NLS-1$
		final List<Person> expected = PersonFactory.createPersonList();

		combo.setSelection(index);

		assertEquals(((Person) combo.getSelection()).getFirstname(), expected.get(index).getFirstname());
		assertEquals(((Person) combo.getSelection()).getLastname(), expected.get(index).getLastname());

		assertEquals(textFirst.getText(), expected.get(index).getFirstname());
		assertEquals(textLast.getText(), expected.get(index).getLastname());
	}
}