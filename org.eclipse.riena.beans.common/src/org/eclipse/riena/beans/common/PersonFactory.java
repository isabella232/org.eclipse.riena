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
package org.eclipse.riena.beans.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a collection of persons.
 */
public final class PersonFactory {

	private PersonFactory() {
		// prevent instantation
	}

	/**
	 * Create a collection of persons.
	 */
	public static List<Person> createPersonList() {
		final List<Person> newList = new ArrayList<Person>();

		Person person = new Person("Doe", "John"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setEyeColor(1);
		person.setHasDog(true);
		newList.add(person);

		person = new Person("Jackson", "Janet"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setEyeColor(1);
		person.setGender(Person.FEMALE);
		person.setHasCat(true);
		newList.add(person);

		person = new Person("Jackson", "Jermaine"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setEyeColor(1);
		person.setHasFish(true);
		newList.add(person);

		person = new Person("Jackson", "John"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setEyeColor(3);
		person.setHasDog(true);
		person.setHasCat(true);
		person.setHasFish(true);
		newList.add(person);

		person = new Person("JJ Jr. Shabadoo", "Joey"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setEyeColor(3);
		newList.add(person);

		person = new Person("Johnson", "Jack"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setEyeColor(2);
		newList.add(person);

		person = new Person("Johnson", "Jane"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setEyeColor(3);
		person.setGender(Person.FEMALE);
		newList.add(person);

		person = new Person("Zappa", "Frank"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setEyeColor(2);
		newList.add(person);

		return newList;
	}

}
