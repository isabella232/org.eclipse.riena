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
 * 
 * 
 */
public interface ICustomerSearch {

	/**
	 * Retrieves customers from the database. For the query the given customer
	 * is use as an example (Query by Example (QBE))
	 * 
	 * @param customer
	 *            customer candidate
	 * @return list of customers
	 */
	Customer[] findCustomer(Customer customer);

	Customer[] findCustomerWithPermission(Customer customer);
}
