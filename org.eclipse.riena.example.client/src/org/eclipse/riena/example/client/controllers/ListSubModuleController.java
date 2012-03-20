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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.beans.common.PersonManager;
import org.eclipse.riena.beans.common.TypedComparator;
import org.eclipse.riena.example.client.views.TextSubModuleView;
import org.eclipse.riena.internal.example.client.beans.PersonModificationBean;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.annotation.OnActionCallback;
import org.eclipse.riena.ui.ridgets.annotation.OnClick;
import org.eclipse.riena.ui.ridgets.annotation.OnDoubleClick;
import org.eclipse.riena.ui.ridgets.annotation.OnPropertyChange;
import org.eclipse.riena.ui.ridgets.listener.ClickEvent;

/**
 * Controller for the {@link TextSubModuleView} example.
 */
public class ListSubModuleController extends SubModuleController {

	/** Manages a collection of persons. */
	private final PersonManager manager;
	/** Holds editable data for a person. */
	private final PersonModificationBean value;
	private ITableRidget listPersons;
	private ITextRidget textFirst;
	private ITextRidget textLast;

	private IToggleButtonRidget buttonSort;

	/** sort count */
	private int count = 0;

	public ListSubModuleController() {
		this(null);
	}

	public ListSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
		manager = new PersonManager(PersonFactory.createPersonList());
		manager.setSelectedPerson(manager.getPersons().iterator().next());
		value = new PersonModificationBean();
	}

	/**
	 * Binds and updates the ridgets.
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		listPersons = getRidget("listPersons"); //$NON-NLS-1$
		listPersons.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		listPersons.setComparator(0, new TypedComparator<String>());
		listPersons.setSortedColumn(0);
		listPersons.bindToModel(manager, "persons", Person.class, new String[] { "listEntry" }, null); //$NON-NLS-1$ //$NON-NLS-2$
		listPersons.updateFromModel();

		listPersons.bindSingleSelectionToModel(manager, PersonManager.PROPERTY_SELECTED_PERSON);

		textFirst = getRidget("textFirst"); //$NON-NLS-1$
		textFirst.bindToModel(value, "firstName"); //$NON-NLS-1$
		textFirst.updateFromModel();
		textLast = getRidget("textLast"); //$NON-NLS-1$
		textLast.bindToModel(value, "lastName"); //$NON-NLS-1$
		textLast.updateFromModel();

		buttonSort = getRidget("buttonSort"); //$NON-NLS-1$
		buttonSort.setText("Sort ascending"); //$NON-NLS-1$
		buttonSort.setSelected(true);
		listPersons.setSortedAscending(buttonSort.isSelected());

		final IActionRidget buttonAdd = getRidget("buttonAdd"); //$NON-NLS-1$
		buttonAdd.setText("&Add"); //$NON-NLS-1$

		final IActionRidget buttonRemove = getRidget("buttonRemove"); //$NON-NLS-1$
		buttonRemove.setText("&Remove"); //$NON-NLS-1$

		final IActionRidget buttonSave = getRidget("buttonSave"); //$NON-NLS-1$
		buttonSave.setText("&Save"); //$NON-NLS-1$

		final IObservableValue viewerSelection = listPersons.getSingleSelectionObservable();
		final IObservableValue hasSelection = new ComputedValue(Boolean.TYPE) {
			@Override
			protected Object calculate() {
				return Boolean.valueOf(viewerSelection.getValue() != null);
			}
		};
		final DataBindingContext dbc = new DataBindingContext();
		bindEnablementToValue(dbc, buttonRemove, hasSelection);
		bindEnablementToValue(dbc, buttonSave, hasSelection);

		if (getNavigationNode().getNavigationArgument() != null) {
			setValuesFromNavigation();
		}

	}

	@OnPropertyChange(ridgetId = "listPersons", propertyName = ITableRidget.PROPERTY_SELECTION)
	public void selectionChanged() {
		value.setPerson(manager.getSelectedPerson());
		textFirst.updateFromModel();
		textLast.updateFromModel();
	}

	@OnActionCallback(ridgetId = "buttonSort")
	public void sortAction() {
		final boolean ascending = buttonSort.isSelected();
		listPersons.setSortedAscending(ascending);
	}

	@OnDoubleClick(ridgetId = "listPersons")
	@OnActionCallback(ridgetId = "buttonSort")
	public void dumpDoubleClickedItem() {
		System.out.println("List of selected item(s): " + listPersons.getSelection()); //$NON-NLS-1$
	}

	@OnClick(ridgetId = "listPersons")
	public void dumpClickedItem(final ClickEvent clickEvent) {
		System.out.println("Clicked item: " + listPersons.getSelection()); //$NON-NLS-1$
	}

	@OnActionCallback(ridgetId = "buttonAdd")
	public void addAction() {

		final Person newPerson = new Person("Average", "Joe #" + ++count); //$NON-NLS-1$ //$NON-NLS-2$
		manager.getPersons().add(newPerson);
		listPersons.updateFromModel();
		manager.setSelectedPerson(newPerson);
		listPersons.updateSingleSelectionFromModel();
	}

	@OnActionCallback(ridgetId = "buttonRemove")
	public void removeAction() {
		final Person selPerson = manager.getSelectedPerson();
		if (selPerson != null) {
			manager.getPersons().remove(selPerson);
			listPersons.updateFromModel();
			manager.setSelectedPerson(null);
		}
	}

	@OnActionCallback(ridgetId = "buttonSave")
	public void saveAction() {
		value.update();
		listPersons.updateFromModel();
	}

	private void setValuesFromNavigation() {
		final NavigationArgument navigationArgument = getNavigationNode().getNavigationArgument();
		if (navigationArgument.getParameter() instanceof Integer) {
			final int modelIndex = (Integer) navigationArgument.getParameter();
			listPersons.setSelection(modelIndex);
		}
	}

	private void bindEnablementToValue(final DataBindingContext dbc, final IRidget ridget, final IObservableValue value) {
		dbc.bindValue(BeansObservables.observeValue(ridget, IRidget.PROPERTY_ENABLED), value, null, null);
	}

}
