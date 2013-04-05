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
package org.eclipse.riena.sample.app.common.model;

/**
 * Class containing an address
 */
public class Address implements Cloneable {

	private String street;
	private String zipCode;
	private String city;
	private String country;

	/**
	 * Creates an empty address
	 */
	public Address() {

		super();

	}

	/**
	 * Creates an address and sets the given values
	 * 
	 * @param country
	 *            the country to set
	 * @param zipCode
	 *            the zip code to set
	 * @param city
	 *            the city to set
	 * @param street
	 *            the street to set
	 */
	public Address(final String country, final String zipCode, final String city, final String street) {

		this();

		this.country = country;
		this.zipCode = zipCode;
		this.city = city;
		this.street = street;

	}

	/**
	 * Returns the city
	 * 
	 * @return city
	 */
	public String getCity() {

		return city;

	}

	/**
	 * Sets the given city
	 * 
	 * @param city
	 *            the city to set
	 */
	public void setCity(final String city) {

		this.city = city;

	}

	/**
	 * Returns the street
	 * 
	 * @return street
	 */
	public String getStreet() {

		return street;

	}

	/**
	 * Sets the given street
	 * 
	 * @param street
	 *            the street to set
	 */
	public void setStreet(final String street) {

		this.street = street;

	}

	/**
	 * Returns the zip code
	 * 
	 * @return zip code
	 */
	public String getZipCode() {

		return zipCode;

	}

	/**
	 * Sets the given zip code
	 * 
	 * @param zipCode
	 *            the zip code to set
	 */
	public void setZipCode(final String zipCode) {

		this.zipCode = zipCode;

	}

	/**
	 * Returns the country
	 * 
	 * @return country
	 */
	public String getCountry() {

		return country;

	}

	/**
	 * Sets the given country
	 * 
	 * @param country
	 *            the country to set
	 */
	public void setCountry(final String country) {

		this.country = country;

	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {

		super.clone();
		final Address clone = new Address();
		clone.setCity(getCity());
		clone.setCountry(getCountry());
		clone.setStreet(getStreet());
		clone.setZipCode(getZipCode());

		return clone;

	}

}
