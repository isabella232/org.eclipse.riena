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
package org.eclipse.riena.sample.app.client.helloworld.controllers;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.sample.app.common.model.Customer;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Controller of the sub module that displays the details of a customer.
 */
public class CustomerDetailsSubModuleController extends SubModuleController {

	public static final String RIDGET_ID_CUSTOMER_NUMBER = "customerNumber"; //$NON-NLS-1$
	public static final String RIDGET_ID_LAST_NAME = "lastName"; //$NON-NLS-1$
	public static final String RIDGET_ID_FIRST_NAME = "firstName"; //$NON-NLS-1$
	public static final String RIDGET_ID_BIRTHPLACE = "birthPlace"; //$NON-NLS-1$
	public static final String RIDGET_ID_SAVE = "save"; //$NON-NLS-1$
	public static final String RIDGET_ID_OPEN_OFFERS = "openOffers"; //$NON-NLS-1$

	public CustomerDetailsSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.ui.controllers.SubModuleController#
	 * configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		super.configureRidgets();

		final Customer customer = getCustomer();

		final ITextRidget customerNumber = getRidget(RIDGET_ID_CUSTOMER_NUMBER);
		customerNumber.setOutputOnly(true);
		customerNumber.bindToModel(customer, "customerNumber"); //$NON-NLS-1$
		customerNumber.updateFromModel();

		final ITextRidget lastName = getRidget(RIDGET_ID_LAST_NAME);
		lastName.bindToModel(customer, "lastName"); //$NON-NLS-1$
		lastName.updateFromModel();

		final ITextRidget firstName = getRidget(RIDGET_ID_FIRST_NAME);
		firstName.bindToModel(customer, "firstName"); //$NON-NLS-1$
		firstName.updateFromModel();

		final ITextRidget birthPlace = getRidget(RIDGET_ID_BIRTHPLACE);
		birthPlace.bindToModel(customer.getBirth(), "birthPlace"); //$NON-NLS-1$
		birthPlace.updateFromModel();

		final IActionRidget saveAction = getRidget(RIDGET_ID_SAVE);
		saveAction.addListener(new SaveCallback());
		saveAction.setText("Save"); //$NON-NLS-1$

		final IActionRidget openOffersAction = getRidget(RIDGET_ID_OPEN_OFFERS);
		openOffersAction.addListener(new OffersCallback());
		openOffersAction.setEnabled(false);
		openOffersAction.setText("Offers"); //$NON-NLS-1$
	}

	private Customer getCustomer() {
		return (Customer) getNavigationNode().getContext(Customer.class.getName());
	}

	//	public ITextRidget getNumberFacade() {
	//		return numberFacade;
	//	}
	//
	//	public void setNumberFacade(ITextRidget numberFacade) {
	//		this.numberFacade = numberFacade;
	//	}
	//
	//	public ITextRidget getNameFacade() {
	//		return nameFacade;
	//	}
	//
	//	public void setNameFacade(ITextRidget nameFacade) {
	//		this.nameFacade = nameFacade;
	//	}
	//
	//	public ITextRidget getFirstnameFacade() {
	//		return firstnameFacade;
	//	}
	//
	//	public void setFirstnameFacade(ITextRidget firstnameFacade) {
	//		this.firstnameFacade = firstnameFacade;
	//	}
	//
	//	public ITextRidget getBirthplaceFacade() {
	//		return birthplaceFacade;
	//	}
	//
	//	public void setBirthplaceFacade(ITextRidget birthplaceFacade) {
	//		this.birthplaceFacade = birthplaceFacade;
	//	}
	//
	//	public IActionRidget getOffersFacade() {
	//		return offersFacade;
	//	}
	//
	//	public void setOffersFacade(IActionRidget offersFacade) {
	//		this.offersFacade = offersFacade;
	//	}
	//
	//	public IActionRidget getSaveFacade() {
	//		return saveFacade;
	//	}
	//
	//	public void setSaveFacade(IActionRidget saveFacade) {
	//		this.saveFacade = saveFacade;
	//	}

	private static class SaveCallback implements IActionListener {

		public void callback() {
			System.out.println("Save is not implemented"); //$NON-NLS-1$
		}

	}

	private static class OffersCallback implements IActionListener {

		public void callback() {
			System.out.println("Offers is not implemented"); //$NON-NLS-1$
		}

	}

}
