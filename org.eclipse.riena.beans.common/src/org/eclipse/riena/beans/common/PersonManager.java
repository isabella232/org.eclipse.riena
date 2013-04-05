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
package org.eclipse.riena.beans.common;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;

/**
 * 
 */
public class PersonManager {

	/**
	 * Property name of the selected person property.
	 */
	public static final String PROPERTY_SELECTED_PERSON = "selectedPerson"; //$NON-NLS-1$

	private final PropertyChangeSupport propertyChangeSupport;
	private Collection<Person> persons;
	private Person selectedPerson;

	/**
	 * constructor.
	 * 
	 * @param persons
	 */
	public PersonManager(final Collection<Person> persons) {

		super();
		propertyChangeSupport = new PropertyChangeSupport(this);
		setPersons(persons);
	}

	public void addPropertyChangeListener(final String p, final PropertyChangeListener l) {

		propertyChangeSupport.addPropertyChangeListener(p, l);
	}

	public void addPropertyChangeListener(final PropertyChangeListener l) {

		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(final String p, final PropertyChangeListener l) {

		propertyChangeSupport.removePropertyChangeListener(p, l);
	}

	public void removePropertyChangeListener(final PropertyChangeListener l) {

		propertyChangeSupport.removePropertyChangeListener(l);
	}

	/**
	 * @return persons
	 */
	public Collection<Person> getPersons() {

		return persons;
	}

	/**
	 * @param persons
	 */
	public void setPersons(final Collection<Person> persons) {

		this.persons = persons;
	}

	/**
	 * @return Returns the selectedPerson.
	 */
	public Person getSelectedPerson() {
		return selectedPerson;
	}

	/**
	 * @param newSelection
	 *            The selectedPerson to set.
	 */
	public void setSelectedPerson(final Person newSelection) {
		final Person oldSelection = this.selectedPerson;
		this.selectedPerson = newSelection;
		propertyChangeSupport.firePropertyChange(PROPERTY_SELECTED_PERSON, oldSelection, newSelection);
	}
}
