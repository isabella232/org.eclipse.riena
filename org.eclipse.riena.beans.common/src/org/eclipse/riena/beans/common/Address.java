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

import java.util.Locale;

/**
 * A bean with an address of a person.
 */
public class Address extends AbstractBean {

	/**
	 * Property name of the first name property ("streetAndNumber").
	 */
	public static final String PROPERTY_STREET = "streetAndNumber"; //$NON-NLS-1$
	/**
	 * Property name of the country property ("country").
	 */
	public static final String PROPERTY_COUNTRY = "country"; //$NON-NLS-1$
	/**
	 * Property name of the postal code property ("postalCode").
	 */
	public static final String PROPERTY_POSTAL_CODE = "postalCode"; //$NON-NLS-1$
	/**
	 * Property name of the town property ("town").
	 */
	public static final String PROPERTY_TOWN = "town"; //$NON-NLS-1$

	private String streetAndNumber;
	private String country;
	private Integer postalCode;
	private String town;

	public Address() {
		streetAndNumber = ""; //$NON-NLS-1$
		country = Locale.getDefault().getCountry();
		postalCode = 0;
		town = ""; //$NON-NLS-1$
	}

	public void setStreetAndNumber(final String streetAndNumber) {
		final Object oldValue = this.streetAndNumber;
		this.streetAndNumber = streetAndNumber;
		firePropertyChanged(PROPERTY_STREET, oldValue, streetAndNumber);
	}

	public String getStreetAndNumber() {
		return streetAndNumber;
	}

	public void setCountry(final String country) {
		final Object oldValue = this.country;
		this.country = country;
		firePropertyChanged(PROPERTY_COUNTRY, oldValue, country);
	}

	public String getCountry() {
		return country;
	}

	public void setPostalCode(final int postalCode) {
		final Object oldValue = this.postalCode;
		this.postalCode = postalCode;
		firePropertyChanged(PROPERTY_POSTAL_CODE, oldValue, postalCode);
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setTown(final String town) {
		final Object oldValue = this.town;
		this.town = town;
		firePropertyChanged(PROPERTY_TOWN, oldValue, town);
	}

	public String getTown() {
		return town;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
		result = prime * result + ((streetAndNumber == null) ? 0 : streetAndNumber.hashCode());
		result = prime * result + ((town == null) ? 0 : town.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		final Address other = (Address) obj;
		if (country == null) {
			if (other.country != null) {
				return false;
			}
		} else if (!country.equals(other.country)) {
			return false;
		}
		if (postalCode == null) {
			if (other.postalCode != null) {
				return false;
			}
		} else if (!postalCode.equals(other.postalCode)) {
			return false;
		}
		if (streetAndNumber == null) {
			if (other.streetAndNumber != null) {
				return false;
			}
		} else if (!streetAndNumber.equals(other.streetAndNumber)) {
			return false;
		}
		if (town == null) {
			if (other.town != null) {
				return false;
			}
		} else if (!town.equals(other.town)) {
			return false;
		}
		return true;
	}

}
