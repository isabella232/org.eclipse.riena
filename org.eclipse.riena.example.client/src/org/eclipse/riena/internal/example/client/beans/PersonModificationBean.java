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
package org.eclipse.riena.internal.example.client.beans;

import org.eclipse.riena.beans.common.Person;

/**
 * This beans allows editing of a {@link Person} without immediately changing
 * it. Modifications to the first and last name are stored in the bean and are
 * only applied to the Person after invoking {@link #update()}.
 */
public final class PersonModificationBean {

	private Person person;

	private String first;
	private String last;

	public String getFirstName() {
		return person == null ? "" : person.getFirstname(); //$NON-NLS-1$
	}

	public void setFirstName(final String first) {
		this.first = first;
	}

	public String getLastName() {
		return person == null ? "" : person.getLastname(); //$NON-NLS-1$
	}

	public void setLastName(final String last) {
		this.last = last;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(final Person person) {
		this.person = person;
		first = getFirstName();
		last = getLastName();
	}

	public void update() {
		if (person != null) {
			person.setFirstname(first);
			person.setLastname(last);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PersonModificationBean other = (PersonModificationBean) obj;
		if (person == null) {
			if (other.person != null) {
				return false;
			}
		} else if (!person.equals(other.person)) {
			return false;
		}
		return true;
	}

}
