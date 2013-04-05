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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Class containing a customer
 */
public class Customer {

	public final static String PROPERTY_CUSTOMER_NUMBER = "customerNumber"; //$NON-NLS-1$
	public final static String PROPERTY_FORM = "form"; //$NON-NLS-1$
	public final static String PROPERTY_TITLE = "title"; //$NON-NLS-1$
	public final static String PROPERTY_FIRST_NAME = "firstName"; //$NON-NLS-1$
	public final static String PROPERTY_LAST_NAME = "lastName"; //$NON-NLS-1$
	public final static String PROPERTY_ADDRESS = "address"; //$NON-NLS-1$
	public final static String PROPERTY_BIRTH = "birth"; //$NON-NLS-1$
	public final static String PROPERTY_PHONE_PRIVATE = "phonePrivate"; //$NON-NLS-1$
	public final static String PROPERTY_PHONE_BUSINESS = "phoneBusiness"; //$NON-NLS-1$
	public final static String PROPERTY_PHONE_MOBILE = "phoneMobile"; //$NON-NLS-1$
	public final static String PROPERTY_FAX = "fax"; //$NON-NLS-1$
	public final static String PROPERTY_EMAIL = "email"; //$NON-NLS-1$
	public final static String PROPERTY_BANK_DATA = "bankData"; //$NON-NLS-1$

	/**
	 * db4o - id
	 */
	private long id = -1;

	public void setId(final long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	private transient PropertyChangeSupport propertyChangeSupport;

	private Integer customerNumber;
	private String form;
	private String title;
	private String firstName;
	private String lastName;
	private Address address;
	private Birth birth;
	private String phonePrivate;
	private String phoneBusiness;
	private String phoneMobile;
	private String fax;
	private String email;
	private List<BankData> bankData;

	// private List<Offer> offers;

	/**
	 * Creates an empty customer
	 */
	public Customer() {

		super();

		propertyChangeSupport = new PropertyChangeSupport(this);
		address = new Address();
		birth = new Birth();
		// offers = new ArrayList<Offer>();
		bankData = new ArrayList<BankData>();
		firstName = ""; //$NON-NLS-1$
		lastName = ""; //$NON-NLS-1$
	}

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	protected void firePropertyChanged(final String propertyName, final Object oldValue, final Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Returns the address of the customer
	 * 
	 * @return address of the customer
	 */
	public Address getAddress() {

		return address;

	}

	/**
	 * Sets the address of the customer
	 * 
	 * @param address
	 *            of the customer
	 */
	public void setAddress(final Address address) {
		final Address old = this.getAddress();
		this.address = address;
		firePropertyChanged(PROPERTY_ADDRESS, old, address);

	}

	/**
	 * Returns the birth of the customer
	 * 
	 * @return birth of the customer
	 */
	public Birth getBirth() {

		return birth;

	}

	/**
	 * Sets the birth of the customer
	 * 
	 * @param birth
	 *            the birth to set
	 */
	public void setBirth(final Birth birth) {
		final Birth old = getBirth();
		this.birth = birth;
		firePropertyChanged(PROPERTY_BIRTH, old, birth);

	}

	/**
	 * Returns the first name of the customer
	 * 
	 * @return first name of the customer
	 */
	public String getFirstName() {

		return firstName;

	}

	/**
	 * Sets the first name of the customer
	 * 
	 * @param firstName
	 *            the first name to set
	 */
	public void setFirstName(final String firstName) {
		final String old = this.getFirstName();
		this.firstName = firstName;
		firePropertyChanged(PROPERTY_FIRST_NAME, old, firstName);

	}

	/**
	 * Returns the form of the customer
	 * 
	 * @return form of the customer
	 */
	public String getForm() {

		return form;

	}

	/**
	 * Sets the form of the customer
	 * 
	 * @param form
	 *            the form to set.
	 */
	public void setForm(final String form) {
		final String old = getForm();
		this.form = form;
		firePropertyChanged(PROPERTY_FORM, old, form);

	}

	/**
	 * Returns the last name of the customer
	 * 
	 * @return last name of the customer
	 */
	public String getLastName() {

		return lastName;

	}

	/**
	 * Sets the last name of the customer
	 * 
	 * @param lastName
	 *            the last name to set
	 */
	public void setLastName(final String lastName) {
		final String old = getLastName();
		this.lastName = lastName;
		firePropertyChanged(PROPERTY_LAST_NAME, old, lastName);

	}

	/**
	 * Returns the title of the customer
	 * 
	 * @return title of the customer
	 */
	public String getTitle() {

		return title;

	}

	/**
	 * Set the title of the customer
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title) {
		final String old = getTitle();
		this.title = title;
		firePropertyChanged(PROPERTY_TITLE, old, title);

	}

	/**
	 * Returns the fax number of the customer
	 * 
	 * @return fax number
	 */
	public String getFax() {

		return fax;

	}

	/**
	 * Sets the given fax number
	 * 
	 * @param fax
	 *            the fax number to set
	 */
	public void setFax(final String fax) {
		final String old = getFax();
		this.fax = fax;
		firePropertyChanged(PROPERTY_FAX, old, fax);

	}

	/**
	 * Returns the business phone number of the customer
	 * 
	 * @return business phone number
	 */
	public String getPhoneBusiness() {

		return phoneBusiness;

	}

	/**
	 * Sets the given business phone number
	 * 
	 * @param phoneBusiness
	 *            the phone number to set
	 */
	public void setPhoneBusiness(final String phoneBusiness) {
		final String old = getPhoneBusiness();
		this.phoneBusiness = phoneBusiness;
		firePropertyChanged(PROPERTY_PHONE_BUSINESS, old, phoneBusiness);

	}

	/**
	 * Returns the mobile phone number of the customer
	 * 
	 * @return mobile phone number
	 */
	public String getPhoneMobile() {

		return phoneMobile;

	}

	/**
	 * Sets the given mobile phone number
	 * 
	 * @param phoneMobile
	 *            the phone number to set
	 */
	public void setPhoneMobile(final String phoneMobile) {
		final String old = getPhoneMobile();
		this.phoneMobile = phoneMobile;
		firePropertyChanged(PROPERTY_PHONE_MOBILE, old, phoneMobile);

	}

	/**
	 * Returns the private phone number of the customer
	 * 
	 * @return private phone number
	 */
	public String getPhonePrivate() {

		return phonePrivate;

	}

	/**
	 * Sets the given private phone number
	 * 
	 * @param phonePrivate
	 *            the phone number to set
	 */
	public void setPhonePrivate(final String phonePrivate) {
		final String old = getPhonePrivate();
		this.phonePrivate = phonePrivate;
		firePropertyChanged(PROPERTY_PHONE_PRIVATE, old, phonePrivate);

	}

	/**
	 * Returns the eMail address of the customer
	 * 
	 * @return eMail address
	 */
	public String getEmail() {

		return email;

	}

	/**
	 * Sets the given eMail address
	 * 
	 * @param email
	 *            the eMail address to set
	 */
	public void setEmail(final String email) {
		final String old = getEmail();
		this.email = email;
		firePropertyChanged(PROPERTY_EMAIL, old, email);

	}

	/**
	 * @return the customerNumber.
	 */
	public Integer getCustomerNumber() {

		return customerNumber;

	}

	/**
	 * @param customerNumber
	 *            The customerNumber to set.
	 */
	public void setCustomerNumber(final Integer customerNumber) {

		final Integer old = getCustomerNumber();
		this.customerNumber = customerNumber;
		firePropertyChanged(PROPERTY_CUSTOMER_NUMBER, old, customerNumber);
	}

	/**
	 * Compares two customers by customerNumber.
	 * 
	 * @param obj
	 *            The reference object with which to compare.
	 * @return <code>true</code> if the argument is a Customer with the same
	 *         customerNumber (other than <code>null</code>);<code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(final Object obj) {

		if (obj instanceof Customer) {
			final Customer customer = (Customer) obj;
			if (customerNumber != null && customerNumber.equals(customer.getCustomerNumber())) {
				return true;
			}
		}
		return false;

	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		if (customerNumber != null) {
			return customerNumber.hashCode();
		}
		return 0;

	}

	/**
	 * Returns a list with all bank data
	 * 
	 * @return list of bank data
	 */
	public List<BankData> getBankData() {

		return bankData;

	}

	/**
	 * Sets the given list of bank data
	 * 
	 * @param bankData
	 *            the list of bank data to set
	 */
	public void setBankData(final List<BankData> bankData) {
		final List<BankData> old = getBankData();
		this.bankData = bankData;
		firePropertyChanged(PROPERTY_BANK_DATA, old, bankData);

	}

	@Override
	public String toString() {
		return getFullCustomerName() + " --> customer number=" + this.getCustomerNumber(); //$NON-NLS-1$
	}

	public String getFullCustomerName() {

		final StringBuilder builder = new StringBuilder();

		if (getLastName() != null) {
			builder.append(getLastName());
			builder.append(", "); //$NON-NLS-1$
		}
		builder.append(getFirstName());

		return builder.toString();
	}
}
