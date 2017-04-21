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
package org.eclipse.riena.internal.sample.app.server;

import java.security.AccessControlException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.riena.core.exception.MurphysLawFailure;
import org.eclipse.riena.sample.app.common.model.Address;
import org.eclipse.riena.sample.app.common.model.BankData;
import org.eclipse.riena.sample.app.common.model.Birth;
import org.eclipse.riena.sample.app.common.model.Customer;
import org.eclipse.riena.sample.app.common.model.CustomersPermission;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.sample.app.common.model.ICustomers;
import org.eclipse.riena.sample.app.common.model.Offer;
import org.eclipse.riena.security.common.authorization.Sentinel;

/**
 * Customers Service Class that is exposed as Webservice. It implements
 * ICustomers (the customer maintaince interface) and ICustomerSearch (the
 * interface for search on the customer data)
 */
public class Customers implements ICustomers, ICustomerSearch {

	private final Map<Integer, Customer> customers;
	private final Map<Integer, Set<Offer>> offers;
	private int nextUniqueCustomerNumber;

	public Customers() {

		customers = new HashMap<Integer, Customer>();
		offers = new HashMap<Integer, Set<Offer>>();
		nextUniqueCustomerNumber = 1;

		initializeCustomers();
		initializeOffers();
	}

	/**
	 * @see org.eclipse.riena.sample.app.common.model.ICustomers#getNextUniqueCustomerNumber()
	 */
	public Integer getNextUniqueCustomerNumber() {

		return nextUniqueCustomerNumber++;
	}

	/**
	 * @see org.eclipse.riena.sample.app.common.model.ICustomers#store(org.eclipse.riena.sample.app.common.model.Customer)
	 */
	public void store(final Customer customer) {
		final SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			sm.checkPermission(new CustomersPermission("riena.sample", "store")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		storeInternal(customer);
	}

	/**
	 * This method is also used internally to store customers. No security is
	 * checked.
	 * 
	 * @param customer
	 */
	private void storeInternal(final Customer customer) {
		customer.setId(customer.getCustomerNumber());
		customers.put(customer.getCustomerNumber(), customer);

	}

	/**
	 * @see org.eclipse.riena.sample.app.common.model.ICustomerSearch#findCustomer(org.eclipse.riena.sample.app.common.model.Customer)
	 */
	public Customer[] findCustomer(final Customer searchedCustomer) {
		final List<Customer> l = new ArrayList<Customer>();

		for (final Customer c : customers.values()) {
			if (isIdentical(c, searchedCustomer)) {
				l.add(c);
			}
		}

		return l.toArray(new Customer[l.size()]);
	}

	public Customer[] findCustomerWithPermission(final Customer searchedCustomer) {
		if (!Sentinel.checkAccess(new CustomersPermission("riena.sample", "find"))) { //$NON-NLS-1$ //$NON-NLS-2$
			throw new AccessControlException("no rights for current user for this operation"); //$NON-NLS-1$
		}
		return findCustomer(searchedCustomer);
	}

	/**
	 * @see org.eclipse.riena.sample.app.common.model.ICustomers#getOffers(java.lang.Integer)
	 */
	public Offer[] getOffers(final Integer customerNumber) {

		final Set<Offer> customerOffers = offers.get(customerNumber);
		if (customerOffers == null) {
			return new Offer[0];
		} else {
			return customerOffers.toArray(new Offer[customerOffers.size()]);
		}
	}

	private boolean isIdentical(final Customer customer, final Customer searchedCustomer) {

		if (searchedCustomer.getCustomerNumber() != null
				&& !searchedCustomer.getCustomerNumber().equals(customer.getCustomerNumber())) {
			return false;
		}

		if (!contains(customer.getLastName(), searchedCustomer.getLastName())) {
			return false;
		}

		if (!contains(customer.getFirstName(), searchedCustomer.getFirstName())) {
			return false;
		}

		return true;
	}

	private boolean contains(final String original, final String other) {

		if (other == null || other.equals("")) { //$NON-NLS-1$
			return true;
		}

		return original.toUpperCase().contains(other.toUpperCase());
	}

	private void initializeCustomers() {

		Customer customer = new Customer();
		customer.setFirstName("Han"); //$NON-NLS-1$
		customer.setLastName("Solo"); //$NON-NLS-1$
		Address address = new Address();
		address.setCity("Frankfurt am Main"); //$NON-NLS-1$
		address.setStreet("Am Main 233"); //$NON-NLS-1$
		address.setZipCode("61236"); //$NON-NLS-1$
		address.setCountry("Germany"); //$NON-NLS-1$
		customer.setAddress(address);

		customer.setBirth(new Birth());
		final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy"); //$NON-NLS-1$
		try {
			customer.getBirth().setBirthDay(format.parse("01.04.1962")); //$NON-NLS-1$
		} catch (final ParseException e) {
			throw new MurphysLawFailure("Parsing date failed", e); //$NON-NLS-1$
		}
		customer.getBirth().setBirthPlace("Frankfurt"); //$NON-NLS-1$
		customer.setBankData(new ArrayList<BankData>());
		initializeCustomerNumber(customer);

		customer = new Customer();
		customer.setFirstName("Luke"); //$NON-NLS-1$
		customer.setLastName("Skywalker"); //$NON-NLS-1$
		address = new Address();
		address.setCity("Washington"); //$NON-NLS-1$
		address.setStreet("Any Road 845"); //$NON-NLS-1$
		address.setZipCode("98123898"); //$NON-NLS-1$
		address.setCountry("USA"); //$NON-NLS-1$
		customer.setAddress(address);
		customer.setBirth(new Birth());
		try {
			customer.getBirth().setBirthDay(format.parse("01.04.1963")); //$NON-NLS-1$
		} catch (final ParseException e) {
			throw new MurphysLawFailure("Parsing date failed", e); //$NON-NLS-1$
		}
		customer.getBirth().setBirthPlace("Frankfurt"); //$NON-NLS-1$
		customer.setBankData(new ArrayList<BankData>());
		initializeCustomerNumber(customer);

		customer = new Customer();
		customer.setFirstName("Frodo"); //$NON-NLS-1$
		customer.setLastName("Baggins"); //$NON-NLS-1$
		address = new Address();
		address.setCity("Hanau"); //$NON-NLS-1$
		address.setStreet("Grüner Weg 3"); //$NON-NLS-1$
		address.setZipCode("62342"); //$NON-NLS-1$
		address.setCountry("Germany"); //$NON-NLS-1$
		customer.setAddress(address);
		customer.setBirth(new Birth());
		try {
			customer.getBirth().setBirthDay(format.parse("01.04.1964")); //$NON-NLS-1$
		} catch (final ParseException e) {
			throw new MurphysLawFailure("Parsing date failed", e); //$NON-NLS-1$
		}
		customer.getBirth().setBirthPlace("Frankfurt"); //$NON-NLS-1$
		customer.setBankData(new ArrayList<BankData>());
		initializeCustomerNumber(customer);
	}

	private void initializeOffers() {

		Offer offer = new Offer(1, 1);
		addOffer(offer);
		offer = new Offer(1, 2);
		addOffer(offer);
	}

	private void addOffer(final Offer offer) {

		Set<Offer> customerOffers = offers.get(offer.getCustomerNumber());
		if (customerOffers == null) {
			customerOffers = new HashSet<Offer>();
			offers.put(offer.getCustomerNumber(), customerOffers);
		}
		customerOffers.add(offer);
	}

	private void initializeCustomerNumber(final Customer customer) {

		customer.setCustomerNumber(getNextUniqueCustomerNumber());
		storeInternal(customer);
	}
}
