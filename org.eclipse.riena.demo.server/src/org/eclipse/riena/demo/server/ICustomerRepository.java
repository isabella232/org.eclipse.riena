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
package org.eclipse.riena.demo.server;

import java.util.List;

import org.eclipse.riena.demo.common.Customer;

/**
 * ICustomerRepository 
 */

/**
 *
 */
public interface ICustomerRepository {

	List<Customer> search(String lastName);

	void store(Customer customer);

	/**
	 * @param emailAddress
	 * @return
	 */
	Customer findCustomerWithEmailAddress(String emailAddress);
}
