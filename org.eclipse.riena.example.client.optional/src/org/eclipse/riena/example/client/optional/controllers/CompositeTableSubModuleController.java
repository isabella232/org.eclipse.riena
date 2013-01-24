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
package org.eclipse.riena.example.client.optional.controllers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.example.client.optional.views.CompositeTableSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRowRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.optional.ICompositeTableRidget;

/**
 * Controller for the {@link CompositeTableSubModuleView} example.
 */
public class CompositeTableSubModuleController extends SubModuleController {

	public static class RowRidget extends AbstractCompositeRidget implements IRowRidget {
		private Person rowData;

		private static final String[] GENDER = { Person.FEMALE, Person.MALE };

		public void setData(final Object rowData) {
			this.rowData = (Person) rowData;
		}

		@Override
		public void configureRidgets() {
			final ITextRidget txtFirst = getRidget(ITextRidget.class, "first"); //$NON-NLS-1$
			txtFirst.bindToModel(rowData, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();

			final ITextRidget txtLast = getRidget(ITextRidget.class, "last"); //$NON-NLS-1$
			txtLast.bindToModel(rowData, Person.PROPERTY_LASTNAME);
			txtLast.updateFromModel();

			final ISingleChoiceRidget gender = getRidget(ISingleChoiceRidget.class, "gender"); //$NON-NLS-1$
			gender.bindToModel(Arrays.asList(GENDER), (List<String>) null, rowData, Person.PROPERTY_GENDER);
			gender.updateFromModel();

			final IMultipleChoiceRidget pets = getRidget(IMultipleChoiceRidget.class, "pets"); //$NON-NLS-1$
			pets.bindToModel(Arrays.asList(Person.Pets.values()), (List<String>) null, rowData, Person.PROPERTY_PETS);
			pets.updateFromModel();
		}
	}

	private final List<Person> input = PersonFactory.createPersonList();

	public CompositeTableSubModuleController() {
		this(null);
	}

	public CompositeTableSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	@Override
	public void configureRidgets() {
		final ICompositeTableRidget table = getRidget(ICompositeTableRidget.class, "table"); //$NON-NLS-1$
		final IActionRidget buttonAdd = getRidget(IActionRidget.class, "buttonAdd"); //$NON-NLS-1$
		final IActionRidget buttonDelete = getRidget(IActionRidget.class, "buttonDelete"); //$NON-NLS-1$
		final IActionRidget buttonDump = getRidget(IActionRidget.class, "buttonDump"); //$NON-NLS-1$

		table.bindToModel(new WritableList(input, Person.class), Person.class, RowRidget.class);
		table.updateFromModel();
		table.setComparator(0, new Comparator<Object>() {
			public int compare(final Object o1, final Object o2) {
				final Person p1 = (Person) o1;
				final Person p2 = (Person) o2;
				int result = p1.getLastname().compareTo(p2.getLastname());
				if (result == 0) {
					result = p1.getFirstname().compareTo(p2.getFirstname());
				}
				return result;
			}
		});
		table.setComparator(1, new Comparator<Object>() {
			public int compare(final Object o1, final Object o2) {
				final Person p1 = (Person) o1;
				final Person p2 = (Person) o2;
				return p1.getGender().compareTo(p2.getGender());
			}
		});
		table.setSortedColumn(0);

		buttonAdd.setText("&Add"); //$NON-NLS-1$
		buttonAdd.addListener(new IActionListener() {
			private int i = 0;

			public void callback() {
				i++;
				final Person person = new Person("Doe #" + i, "John"); //$NON-NLS-1$ //$NON-NLS-2$
				person.setHasCat(true);
				input.add(person);
				table.updateFromModel();
				table.setSelection(person);
			}
		});

		buttonDelete.setText("&Delete"); //$NON-NLS-1$
		buttonDelete.addListener(new IActionListener() {
			public void callback() {
				final Person person = (Person) table.getSingleSelectionObservable().getValue();
				input.remove(person);
				table.updateFromModel();
			}
		});

		buttonDump.setText("&Console dump"); //$NON-NLS-1$
		buttonDump.addListener(new IActionListener() {
			public void callback() {
				System.out.println("\nPersons:"); //$NON-NLS-1$
				for (final Person p : input) {
					System.out.println(p);
				}
			}
		});

		final IObservableValue viewerSelection = table.getSingleSelectionObservable();
		final IObservableValue hasSelection = new ComputedValue(Boolean.TYPE) {
			@Override
			protected Object calculate() {
				return Boolean.valueOf(viewerSelection.getValue() != null);
			}
		};
		final DataBindingContext dbc = new DataBindingContext();
		bindEnablementToValue(dbc, buttonDelete, hasSelection);
	}

	// helping methods
	// ////////////////

	private void bindEnablementToValue(final DataBindingContext dbc, final IRidget ridget, final IObservableValue value) {
		dbc.bindValue(BeansObservables.observeValue(ridget, IRidget.PROPERTY_ENABLED), value, null, null);
	}

}
