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
package org.eclipse.riena.sample.app.client.helloworld.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.observable.value.WritableValue;

import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.sample.app.client.helloworld.views.CustomerDetailsSubModuleView;
import org.eclipse.riena.sample.app.common.model.Customer;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.workarea.WorkareaManager;

public class CustomerSearchSubModuleController extends SubModuleController {

	private ICustomerSearch service;

	private ITableRidget tableRidget;
	private IActionRidget searchAction;
	private IActionRidget clearAction;
	private IActionRidget openAction;

	private ITextRidget firstNameRidget;
	private ITextRidget lastNameRidget;

	private final ResultContainer searchResult;
	private WritableValue tableSelection;

	private final Customer sample;

	public CustomerSearchSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
		searchResult = new ResultContainer();
		sample = new Customer();
	}

	@InjectService()
	public void bind(final ICustomerSearch service) {
		this.service = service;
	}

	public void unbind(final ICustomerSearch service) {
		if (this.service == service) {
			this.service = null;
		}
	}

	private class SearchCallback implements IActionListener {

		public void callback() {
			searchCustomers();
		}
	}

	protected void searchCustomers() {
		final Customer[] result = service.findCustomer(sample);
		this.searchResult.setList(Arrays.asList(result));
		tableRidget.updateFromModel();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		tableRidget = getRidget("tableRidget"); //$NON-NLS-1$
		searchAction = getRidget("searchAction"); //$NON-NLS-1$
		clearAction = getRidget("clearAction"); //$NON-NLS-1$
		openAction = getRidget("openAction"); //$NON-NLS-1$
		firstNameRidget = getRidget("firstNameRidget"); //$NON-NLS-1$
		lastNameRidget = getRidget("lastNameRidget"); //$NON-NLS-1$
	}

	@Override
	public void afterBind() {
		super.afterBind();
		final String[] columnProperties = new String[] { Customer.PROPERTY_CUSTOMER_NUMBER,
				Customer.PROPERTY_LAST_NAME, Customer.PROPERTY_FIRST_NAME, Customer.PROPERTY_PHONE_BUSINESS };
		tableRidget.bindToModel(searchResult, "list", Customer.class, columnProperties, new String[] { "Number", //$NON-NLS-1$ //$NON-NLS-2$
				"Lastname", "Firstname", "phone" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		tableRidget.updateFromModel();

		tableSelection = new WritableValue();
		tableRidget.bindSingleSelectionToModel(tableSelection);
		searchAction.addListener(new SearchCallback());
		firstNameRidget.bindToModel(sample, "firstName"); //$NON-NLS-1$
		lastNameRidget.bindToModel(sample, "lastName"); //$NON-NLS-1$
		clearAction.addListener(new ClearCallback());
		openAction.addListener(new OpenCallback());
	}

	private class OpenCallback implements IActionListener {
		public void callback() {
			final Object selectedValue = tableSelection.getValue();
			if (selectedValue == null) {
				return;
			}
			if (!(selectedValue instanceof Customer)) {
				throw new RuntimeException("invalid datatype for selected value"); //$NON-NLS-1$
			}
			final Customer selected = (Customer) selectedValue;
			final ISubModuleNode node = getNavigationNode();
			final SubModuleNode cNode = new SubModuleNode(null, selected.getFirstName());
			cNode.setContext(Customer.class.getName(), selected);
			WorkareaManager.getInstance().registerDefinition(cNode, CustomerDetailsSubModuleView.ID);
			node.addChild(cNode);
			cNode.activate();
		}
	}

	private class ClearCallback implements IActionListener {

		public void callback() {
			sample.setCustomerNumber(null);
			sample.setFirstName(""); //$NON-NLS-1$
			sample.setLastName(""); //$NON-NLS-1$
			firstNameRidget.updateFromModel();
			lastNameRidget.updateFromModel();
		}
	}

	public static class ResultContainer {

		private List<Customer> list = new ArrayList<Customer>();

		public List<Customer> getList() {
			return list;
		}

		public void setList(final List<Customer> list) {
			this.list = list;
		}

	}
}
