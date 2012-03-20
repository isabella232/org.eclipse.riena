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
package org.eclipse.riena.internal.demo.client;

import java.util.List;

import org.eclipse.riena.demo.common.Customer;
import org.eclipse.riena.demo.common.ICustomerService;

public class RemoteClientTest {

	public void bind(final ICustomerService customerService) {
		System.out.println("remoteclienttest: customer service bound"); //$NON-NLS-1$
		final List<Customer> searchResult = customerService.search("M"); //$NON-NLS-1$
		for (final Customer c : searchResult) {
			System.out.println(c.getLastName() + ", " + c.getFirstName()); //$NON-NLS-1$
		}
	}

	public void unbind(final ICustomerService customerService) {
		System.out.println("remoteclientest: customer service unbound"); //$NON-NLS-1$

	}

}
