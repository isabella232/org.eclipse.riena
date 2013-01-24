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
package org.eclipse.riena.demo.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer {

	private String custno;
	private String lastName;
	private String firstName;
	private Address address;
	private Address workAddress;
	private String emailAddress;
	private Date birthDate;
	private float salary;
	private List<Contract> contracts;

	public Customer() {
		address = new Address();
		contracts = new ArrayList<Contract>();
		lastName = ""; //$NON-NLS-1$
		firstName = ""; //$NON-NLS-1$
		custno = ""; //$NON-NLS-1$
		emailAddress = ""; //$NON-NLS-1$
		birthDate = null;
		salary = 0;
	}

	public String getCustno() {
		return custno;
	}

	public void setCustno(final String custno) {
		this.custno = custno;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}

	/**
	 * @return the workAddress
	 */
	public Address getWorkAddress() {
		return workAddress;
	}

	/**
	 * @param workAddress
	 *            the workAddress to set
	 */
	public void setWorkAddress(final Address workAddress) {
		this.workAddress = workAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(final float salary) {
		this.salary = salary;
	}

	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(final List<Contract> contracts) {
		this.contracts = contracts;
	}

	public void addContract(final Contract contract) {
		contracts.add(contract);
	}

}
