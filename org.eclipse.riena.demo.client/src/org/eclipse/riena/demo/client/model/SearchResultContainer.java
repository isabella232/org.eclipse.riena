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
package org.eclipse.riena.demo.client.model;

import java.util.List;

import org.eclipse.riena.beans.common.AbstractBean;
import org.eclipse.riena.demo.common.Customer;

/**
 * SearchResultContainer
 */
public class SearchResultContainer extends AbstractBean {

	private List<Customer> customerList;

	/**
	 * @return the customerOverview.
	 */
	public List<Customer> getCustomerList() {
		return customerList;
	}

	/**
	 * @param customerList
	 *            The customerRecordOverview to set.
	 */
	public void setCustomerList(final List<Customer> kunden) {
		this.customerList = kunden;
	}

}