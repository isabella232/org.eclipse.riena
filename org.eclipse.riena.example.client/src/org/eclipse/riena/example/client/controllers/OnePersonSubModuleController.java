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
package org.eclipse.riena.example.client.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.INumericTextRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.validation.MaxLength;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * This controller displays the data of one person.
 */
public class OnePersonSubModuleController extends SubModuleController {

	private static List<String> countries;
	private static int personCounter;
	private Person person;
	private INumericTextRidget customerNumber;
	private ITextRidget lastName;
	private ITextRidget firstName;
	private IDateTextRidget birthday;
	private ITextRidget birthplace;
	private ITextRidget street;
	private IComboRidget country;
	private INumericTextRidget postalcode;
	private ITextRidget town;
	private ISingleChoiceRidget gender;

	public OnePersonSubModuleController() {
		this(null);
	}

	public OnePersonSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * Creates and initializes one person.
	 * 
	 * @return person
	 */
	private void createPerson() {
		person = new Person("Mustermann", "Erika"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setNumber(personCounter);
		person.setBirthday("12.08.1964"); //$NON-NLS-1$
		person.setBirthplace("Berlin"); //$NON-NLS-1$
		person.setGender(Person.FEMALE);
		person.getAddress().setStreetAndNumber("Heidestraße 17"); //$NON-NLS-1$
		person.getAddress().setCountry(Locale.GERMANY.getCountry());
		person.getAddress().setPostalCode(81739);
		person.getAddress().setTown("München"); //$NON-NLS-1$
	}

	/**
	 * Returns a list of countries.
	 * 
	 * @return countries
	 */
	public synchronized List<String> getCountries() {

		if (countries == null) {
			countries = new ArrayList<String>();
			countries.add(Locale.FRANCE.getCountry());
			countries.add(Locale.CANADA.getCountry());
			countries.add(Locale.GERMANY.getCountry());
			countries.add(Locale.ITALY.getCountry());
			countries.add(Locale.UK.getCountry());
			countries.add(Locale.US.getCountry());
			final String defCountry = Locale.getDefault().getCountry();
			if (!countries.contains(defCountry)) {
				countries.add(defCountry);
			}
			// countries = Arrays.asList(Locale.getISOCountries());
		}

		return countries;

	}

	private void updateTitle() {
		String label = person.toString();
		if (person.getNumber() > 0) {
			label = "#" + person.getNumber() + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
		}
		getNavigationNode().setLabel(label);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		customerNumber = getRidget(INumericTextRidget.class, "customerNumber"); //$NON-NLS-1$
		customerNumber.addMarker(new OutputMarker());
		customerNumber.setGrouping(false);
		lastName = getRidget(ITextRidget.class, "lastName"); //$NON-NLS-1$
		lastName.setMandatory(true);
		firstName = getRidget(ITextRidget.class, "firstName"); //$NON-NLS-1$
		firstName.setMandatory(true);
		birthday = getRidget(IDateTextRidget.class, "birthday"); //$NON-NLS-1$
		birthday.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		birthplace = getRidget(ITextRidget.class, "birthplace"); //$NON-NLS-1$
		gender = getRidget(ISingleChoiceRidget.class, "gender"); //$NON-NLS-1$
		street = getRidget(ITextRidget.class, "street"); //$NON-NLS-1$
		country = getRidget(IComboRidget.class, "country"); //$NON-NLS-1$
		postalcode = getRidget(INumericTextRidget.class, "postalCode"); //$NON-NLS-1$
		postalcode.setGrouping(false);
		postalcode.addValidationRule(new MaxLength(5), ValidationTime.ON_UI_CONTROL_EDIT);
		town = getRidget(ITextRidget.class, "town"); //$NON-NLS-1$
		final IActionRidget show = getRidget(IActionRidget.class, "show"); //$NON-NLS-1$
		final IActionRidget next = getRidget(IActionRidget.class, "next"); //$NON-NLS-1$
		final IActionRidget jumpBack = getRidget(IActionRidget.class, "jumpBack"); //$NON-NLS-1$

		createPerson();
		person.addPropertyChangeListener(new NameChangeListener());

		customerNumber.bindToModel(person, "number"); //$NON-NLS-1$
		lastName.bindToModel(person, "lastname"); //$NON-NLS-1$
		firstName.bindToModel(person, "firstname"); //$NON-NLS-1$
		birthday.bindToModel(person, "birthday"); //$NON-NLS-1$
		birthplace.bindToModel(person, "birthplace"); //$NON-NLS-1$
		final List<String> genders = new ArrayList<String>(2);
		genders.add(Person.FEMALE);
		genders.add(Person.MALE);
		gender.bindToModel(genders, genders, person, "gender"); //$NON-NLS-1$
		street.bindToModel(person.getAddress(), "streetAndNumber"); //$NON-NLS-1$
		country.bindToModel(this, "countries", String.class, null, person.getAddress(), "country"); //$NON-NLS-1$ //$NON-NLS-2$
		postalcode.bindToModel(person.getAddress(), "postalCode"); //$NON-NLS-1$
		town.bindToModel(person.getAddress(), "town"); //$NON-NLS-1$
		show.addListener(new ShowActionListener());
		next.addListener(new NextActionListener());
		jumpBack.addListener(new JumpBackActionListener());

		getNavigationNode().addListener(new SubModuleNodeListener() {
			@Override
			public void afterActivated(final ISubModuleNode source) {
				super.afterActivated(source);
				jumpBack.setEnabled(getNavigationNode().isJumpTarget());
			}
		});

		updateAllRidgetsFromModel();
		updateTitle();

	}

	/**
	 * Displays the properties of the person.
	 */
	private class ShowActionListener implements IActionListener {

		public void callback() {

			System.out.println("lastname: " + person.getLastname()); //$NON-NLS-1$
			System.out.println("firstname: " + person.getFirstname()); //$NON-NLS-1$
			System.out.println("birthday: " + person.getBirthday()); //$NON-NLS-1$
			System.out.println("birthplace: " + person.getBirthplace()); //$NON-NLS-1$
			System.out.println("gender: " + person.getGender()); //$NON-NLS-1$
			System.out.println("streetAndNumber: " + person.getAddress().getStreetAndNumber()); //$NON-NLS-1$
			System.out.println("country: " + person.getAddress().getCountry()); //$NON-NLS-1$
			System.out.println("postalCode: " + person.getAddress().getPostalCode()); //$NON-NLS-1$
			System.out.println("town: " + person.getAddress().getTown()); //$NON-NLS-1$

		}

	}

	/**
	 * Adds another sub module for new person
	 */
	private class NextActionListener implements IActionListener {

		public void callback() {

			final ISubModuleNode nextSubModuleNode = (ISubModuleNode) createNextPersonSubModule();
			final IModuleNode parent = (IModuleNode) getNavigationNode().getParent();
			parent.addChild(nextSubModuleNode);

		}

	}

	/**
	 * Jumps back to the previous sub-module.
	 */
	private class JumpBackActionListener implements IActionListener {

		public void callback() {
			getNavigationNode().jumpBack();
		}

	}

	private class NameChangeListener implements PropertyChangeListener {

		public void propertyChange(final PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(Person.PROPERTY_LASTNAME)
					|| evt.getPropertyName().equals(Person.PROPERTY_FIRSTNAME)) {
				updateTitle();
			}
		}

	}

	protected INavigationNode<?> createNextPersonSubModule() {
		final ISubModuleNode nextSubModuleNode = new SubModuleNode(new NavigationNodeId("onePerson", Integer //$NON-NLS-1$
				.toString(personCounter++)), "nextPersonLabel"); //$NON-NLS-1$
		nextSubModuleNode.setIcon("person.gif"); //$NON-NLS-1$
		WorkareaManager
				.getInstance()
				.registerDefinition(nextSubModuleNode, OnePersonSubModuleController.class,
						"org.eclipse.riena.example.client.views.OnePersonSubModuleView").setRequiredPreparation(true); //$NON-NLS-1$
		return nextSubModuleNode;
	}

}
