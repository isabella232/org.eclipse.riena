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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.beans.common.PersonManager;
import org.eclipse.riena.example.client.views.ComboSubModuleView;
import org.eclipse.riena.internal.example.client.beans.PersonModificationBean;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * Controller for the {@link ComboSubModuleView} example.
 */
public class ComboSubModuleController extends SubModuleController {

	/** Manages a collection of persons. */
	private final PersonManager manager;
	/** Holds editable data for a person. */
	private PersonModificationBean value;
	private IComboRidget comboOne;
	private ITextRidget textFirst;
	private ITextRidget textLast;

	public ComboSubModuleController() {
		this(null);
	}

	public ComboSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
		manager = new PersonManager(PersonFactory.createPersonList());
		manager.setSelectedPerson(manager.getPersons().iterator().next());
		value = new PersonModificationBean();
	}

	@Override
	public void afterBind() {
		super.afterBind();
		bindModels();
	}

	private void bindModels() {
		comboOne.bindToModel(manager, "persons", String.class, null, manager, "selectedPerson"); //$NON-NLS-1$ //$NON-NLS-2$
		comboOne.updateFromModel();

		textFirst.bindToModel(value, "firstName"); //$NON-NLS-1$
		textFirst.updateFromModel();

		textLast.bindToModel(value, "lastName"); //$NON-NLS-1$
		textLast.updateFromModel();
	}

	@Override
	public void configureRidgets() {

		comboOne = getRidget(IComboRidget.class, "comboOne"); //$NON-NLS-1$

		value.setPerson(manager.getSelectedPerson());

		textFirst = getRidget(ITextRidget.class, "textFirst"); //$NON-NLS-1$
		textLast = getRidget(ITextRidget.class, "textLast"); //$NON-NLS-1$

		if (getNavigationNode().getNavigationArgument() != null
				&& getNavigationNode().getNavigationArgument().getParameter() instanceof PersonModificationBean) { // we came here from a navigation
			setValuesFromNavigation();
		}

		comboOne.addPropertyChangeListener(IComboRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				final Person selectedPerson = (Person) evt.getNewValue();
				value.setPerson(selectedPerson);
				textFirst.updateFromModel();
				textLast.updateFromModel();
			}
		});

		final IActionRidget buttonSave = getRidget(IActionRidget.class, "buttonSave"); //$NON-NLS-1$
		buttonSave.setText("&Save"); //$NON-NLS-1$
		buttonSave.addListener(new IActionListener() {
			public void callback() {
				value.update();
				comboOne.updateFromModel();
			}
		});

		final IToggleButtonRidget buttonSecondValue = getRidget("buttonSecondValue"); //$NON-NLS-1$
		if (buttonSecondValue != null) {
			buttonSecondValue.setText("Always use second person!"); //$NON-NLS-1$
			buttonSecondValue.addListener(new IActionListener() {
				public void callback() {
					if (buttonSecondValue.isSelected()) {
						if (manager.getPersons().size() > 1) {
							final Iterator<Person> iterator = manager.getPersons().iterator();
							iterator.next();
							final Person second = iterator.next();
							manager.setSelectedPerson(second);
						}
						comboOne.setOutputOnly(true);
					} else {
						comboOne.setOutputOnly(false);
					}
					System.out.println("Selected Person: " + manager.getSelectedPerson()); //$NON-NLS-1$
					comboOne.updateFromModel();
				}
			});
		}
		getNavigationNode().addListener(new NavigateSubModuleNavigationNodeListener());
	}

	private void setValuesFromNavigation() {
		final NavigationArgument navigationArgument = getNavigationNode().getNavigationArgument();
		if (navigationArgument != null) {
			value = (PersonModificationBean) navigationArgument.getParameter();
			manager.setSelectedPerson(value.getPerson());
			bindModels();
		}
	}

	private class NavigateSubModuleNavigationNodeListener extends SubModuleNodeListener {
		@Override
		public void afterActivated(final ISubModuleNode source) {
			super.afterActivated(source);
			if (getNavigationNode().getNavigationArgument() != null
					&& getNavigationNode().getNavigationArgument().getParameter() instanceof PersonModificationBean) { // we came here from a navigation
				setValuesFromNavigation();
			}
		}
	}
}
