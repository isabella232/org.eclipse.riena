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
package org.eclipse.riena.demo.client.model;

import java.util.List;

import org.eclipse.riena.demo.common.Customer;

public class SearchResult {

	private List<Customer> customers;

	public void setCustomers(final List<Customer> customers) {
		this.customers = customers;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public int getHits() {
		if (customers == null) {
			return 0;
		}
		return customers.size();
	}

}
