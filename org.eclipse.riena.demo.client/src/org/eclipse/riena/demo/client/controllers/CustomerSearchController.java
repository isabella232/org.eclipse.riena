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
package org.eclipse.riena.demo.client.controllers;

import java.util.Date;

import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.demo.client.model.SearchBean;
import org.eclipse.riena.demo.client.model.SearchResult;
import org.eclipse.riena.demo.common.Customer;
import org.eclipse.riena.demo.common.ICustomerService;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.annotation.OnActionCallback;
import org.eclipse.riena.ui.ridgets.annotation.OnDoubleClick;
import org.eclipse.riena.ui.ridgets.swt.DateColumnFormatter;

/**
 * customer search view controller
 */
public class CustomerSearchController extends SubModuleController {

	private ICustomerService customerDemoService;

	@InjectService(useRanking = true)
	public void bind(final ICustomerService customerDemoService) {
		this.customerDemoService = customerDemoService;
	}

	public void unbind(final ICustomerService customerDemoService) {
		this.customerDemoService = null;
	}

	private final SearchBean customerSearchBean = new SearchBean();
	private final SearchResult result = new SearchResult();

	@Override
	public void configureRidgets() {
		final ITextRidget suchName = getRidget(ITextRidget.class, "searchLastName"); //$NON-NLS-1$
		suchName.bindToModel(customerSearchBean, "lastName"); //$NON-NLS-1$
		suchName.setMandatory(true);

		getRidget(ILabelRidget.class, "hits").bindToModel(result, "hits"); //$NON-NLS-1$ //$NON-NLS-2$

		final ITableRidget kunden = getRidget(ITableRidget.class, "result"); //$NON-NLS-1$
		final String[] columnNames = { "Lastname", "Firstname", "Birthdate", "Street", "City" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		final String[] propertyNames = { "lastName", "firstName", "birthDate", "address.street", "address.city" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		kunden.bindToModel(result, "customers", Customer.class, propertyNames, columnNames); //$NON-NLS-1$

		kunden.setColumnFormatter(2, new DateColumnFormatter("dd.MM.yyyy") { //$NON-NLS-1$

					@Override
					protected Date getDate(final Object element) {
						return ((Customer) element).getBirthDate();
					}
				});
	}

	@OnActionCallback(ridgetId = "search")
	protected void search() {
		result.setCustomers(null);
		getRidget(ITableRidget.class, "result").updateFromModel(); //$NON-NLS-1$

		result.setCustomers(customerDemoService.search(customerSearchBean.getLastName()));

		getRidget(ITableRidget.class, "result").updateFromModel(); //$NON-NLS-1$
		getRidget(ILabelRidget.class, "hits").updateFromModel(); //$NON-NLS-1$
		updateAllRidgetsFromModel();
	}

	@OnActionCallback(ridgetId = "new")
	protected void newCustomer() {
		getNavigationNode().navigate(new NavigationNodeId("riena.demo.client.CustomerRecord")); //$NON-NLS-1$
	}

	@OnActionCallback(ridgetId = "open")
	@OnDoubleClick(ridgetId = "result")
	protected void openSelectedCustomer() {
		final ITableRidget kunden = getRidget(ITableRidget.class, "result"); //$NON-NLS-1$
		final int selectionIndex = kunden.getSelectionIndex();
		if (selectionIndex >= 0) {
			getNavigationNode().navigate(new NavigationNodeId("riena.demo.client.CustomerRecord", result.getCustomers().get(selectionIndex).getEmailAddress()), //$NON-NLS-1$
					new NavigationArgument(result.getCustomers().get(selectionIndex)));
		}
	}
}
