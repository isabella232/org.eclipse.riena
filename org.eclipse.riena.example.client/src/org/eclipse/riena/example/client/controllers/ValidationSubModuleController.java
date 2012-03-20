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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;

import org.eclipse.riena.example.client.views.ValidationSubModuleView;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.marker.StatuslineMessageMarkerViewer;
import org.eclipse.riena.ui.ridgets.marker.TooltipMessageMarkerViewer;
import org.eclipse.riena.ui.ridgets.validation.MaxLength;
import org.eclipse.riena.ui.ridgets.validation.MinLength;
import org.eclipse.riena.ui.ridgets.validation.RequiredField;
import org.eclipse.riena.ui.ridgets.validation.ValidCharacters;
import org.eclipse.riena.ui.ridgets.validation.ValidDate;
import org.eclipse.riena.ui.ridgets.validation.ValidEmailAddress;
import org.eclipse.riena.ui.ridgets.validation.ValidExpression;
import org.eclipse.riena.ui.ridgets.validation.ValidIntermediateDate;
import org.eclipse.riena.ui.ridgets.validation.ValidRange;

/**
 * Controller for the {@link ValidationSubModuleView} example.
 */
public class ValidationSubModuleController extends SubModuleController {

	private static final String DATE_PATTERN = "dd.MM.yyyy"; //$NON-NLS-1$

	public ValidationSubModuleController() {
		this(null);
	}

	public ValidationSubModuleController(final ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * Binds and updates the ridgets.
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		final ITextRidget txtNumbersOnly = getRidget("txtNumbersOnly"); //$NON-NLS-1$
		final ITextRidget txtNumbersOnlyDW = getRidget("txtNumbersOnlyDW"); //$NON-NLS-1$
		final ITextRidget txtCharactersOnly = getRidget("txtCharactersOnly"); //$NON-NLS-1$
		final ITextRidget txtExpression = getRidget("txtExpression"); //$NON-NLS-1$
		final ITextRidget txtLengthLessThan5 = getRidget("txtLengthLessThan5"); //$NON-NLS-1$
		final ITextRidget txtRequiredLowercase = getRidget("txtRequiredLowercase"); //$NON-NLS-1$
		final ITextRidget txtRange18to80 = getRidget("txtRange18to80"); //$NON-NLS-1$
		final ITextRidget txtLength5to10 = getRidget("txtLength5to10"); //$NON-NLS-1$
		final ITextRidget txtDate = getRidget("txtDate"); //$NON-NLS-1$
		final ITextRidget txtEmail = getRidget("txtEmail"); //$NON-NLS-1$

		final ITextRidget lblNumbersOnly = getRidget("lblNumbersOnly"); //$NON-NLS-1$
		final ITextRidget lblNumbersOnlyDW = getRidget("lblNumbersOnlyDW"); //$NON-NLS-1$
		final ITextRidget lblCharactersOnly = getRidget("lblCharactersOnly"); //$NON-NLS-1$
		final ITextRidget lblExpression = getRidget("lblExpression"); //$NON-NLS-1$
		final ITextRidget lblLengthLessThan5 = getRidget("lblLengthLessThan5"); //$NON-NLS-1$
		final ITextRidget lblRequiredLowercase = getRidget("lblRequiredLowercase"); //$NON-NLS-1$
		final ITextRidget lblRange18to80 = getRidget("lblRange18to80"); //$NON-NLS-1$
		final ITextRidget lblLength5to10 = getRidget("lblLength5to10"); //$NON-NLS-1$
		final ITextRidget lblDate = getRidget("lblDate"); //$NON-NLS-1$
		final ITextRidget lblEmail = getRidget("lblEmail"); //$NON-NLS-1$

		makeOutputOnly(lblNumbersOnly, lblNumbersOnlyDW, lblCharactersOnly, lblExpression, lblLengthLessThan5,
				lblRequiredLowercase, lblRange18to80, lblLength5to10, lblDate, lblEmail);

		// on edit validation

		txtNumbersOnly.addValidationRule(new ValidCharacters(ValidCharacters.VALID_NUMBERS),
				ValidationTime.ON_UI_CONTROL_EDIT);
		txtNumbersOnly.addValidationMessage("Only numbers are allowed!"); //$NON-NLS-1$
		txtNumbersOnly.bindToModel(getTextValue(lblNumbersOnly));

		txtNumbersOnlyDW.addValidationRule(new ValidCharacters(ValidCharacters.VALID_NUMBERS),
				ValidationTime.ON_UI_CONTROL_EDIT);
		txtNumbersOnlyDW.addValidationMessage("Only numbers are allowed!"); //$NON-NLS-1$
		txtNumbersOnlyDW.setDirectWriting(true);
		txtNumbersOnlyDW.bindToModel(getTextValue(lblNumbersOnlyDW));

		txtCharactersOnly.addValidationRule(new ValidCharacters(ValidCharacters.VALID_LETTER),
				ValidationTime.ON_UI_CONTROL_EDIT);
		txtCharactersOnly.addValidationMessage("Only characters are allowed!"); //$NON-NLS-1$
		txtCharactersOnly.bindToModel(getTextValue(lblCharactersOnly));

		txtExpression.addValidationRule(new ValidExpression("^PDX[0-9]{2}$"), ValidationTime.ON_UI_CONTROL_EDIT); //$NON-NLS-1$
		txtExpression.addValidationMessage("The text does not match with the expression (PDX##)!"); //$NON-NLS-1$
		txtExpression.bindToModel(getTextValue(lblExpression));
		txtExpression.setText("PDX97"); //$NON-NLS-1$

		txtLengthLessThan5.addValidationRule(new MaxLength(5), ValidationTime.ON_UI_CONTROL_EDIT);
		txtLengthLessThan5.addValidationMessage("The text is longer than 5 characters!"); //$NON-NLS-1$
		txtLengthLessThan5.bindToModel(getTextValue(lblLengthLessThan5));

		txtRequiredLowercase.addValidationRule(new RequiredField(), ValidationTime.ON_UI_CONTROL_EDIT);
		txtRequiredLowercase.addValidationRule(new ValidCharacters(ValidCharacters.VALID_LOWERCASE),
				ValidationTime.ON_UI_CONTROL_EDIT);
		txtRequiredLowercase.addValidationMessage("Only lowercase characters are allowed!"); //$NON-NLS-1$
		txtRequiredLowercase.bindToModel(getTextValue(lblRequiredLowercase));

		// on update validation

		txtRange18to80.addValidationRule(new RequiredField(), ValidationTime.ON_UPDATE_TO_MODEL);
		txtRange18to80.addValidationRule(new ValidRange(18, 80), ValidationTime.ON_UPDATE_TO_MODEL);
		txtRange18to80.bindToModel(getTextValue(lblRange18to80));

		txtLength5to10.addValidationRule(new MinLength(5), ValidationTime.ON_UPDATE_TO_MODEL);
		txtLength5to10.addValidationRule(new MaxLength(10), ValidationTime.ON_UPDATE_TO_MODEL);
		txtLength5to10.bindToModel(getTextValue(lblLength5to10));

		// complex validation

		txtDate.addValidationRule(new ValidIntermediateDate(DATE_PATTERN), ValidationTime.ON_UI_CONTROL_EDIT);
		txtDate.addValidationRule(new ValidDate(DATE_PATTERN), ValidationTime.ON_UPDATE_TO_MODEL);
		txtDate.setText("25.12.2008"); //$NON-NLS-1$
		txtDate.bindToModel(getTextValue(lblDate));

		txtEmail.addValidationRule(new ValidEmailAddress(), ValidationTime.ON_UI_CONTROL_EDIT);
		txtEmail.bindToModel(getTextValue(lblEmail));
		txtEmail.setText("elmer@foo.bar"); //$NON-NLS-1$

		// show validation messages in statusline and tooltip

		final IStatuslineRidget statuslineRidget = getApplicationController().getStatusline();
		final StatuslineMessageMarkerViewer statuslineMessageMarkerViewer = new StatuslineMessageMarkerViewer(
				statuslineRidget);
		final TooltipMessageMarkerViewer tooltipMessageMarkerViewer = new TooltipMessageMarkerViewer();

		final IBasicMarkableRidget[] ridgets = { txtNumbersOnly, txtNumbersOnlyDW, txtCharactersOnly, txtExpression,
				txtLengthLessThan5, txtRequiredLowercase, txtRange18to80, txtLength5to10, txtDate, txtEmail };
		for (final IBasicMarkableRidget ridget : ridgets) {
			statuslineMessageMarkerViewer.addRidget(ridget);
			tooltipMessageMarkerViewer.addRidget(ridget);
		}
	}

	// helping methods
	//////////////////

	private IObservableValue getTextValue(final ITextRidget bean) {
		return BeansObservables.observeValue(bean, ITextRidget.PROPERTY_TEXT);
	}

	/**
	 * Returns the controller of the parent sub-application.
	 * 
	 * @return sub-application controller
	 */
	private ApplicationController getApplicationController() {
		return (ApplicationController) getNavigationNode().getParentOfType(IApplicationNode.class)
				.getNavigationNodeController();
	}

	private void makeOutputOnly(final ITextRidget... ridgets) {
		for (final ITextRidget ridget : ridgets) {
			ridget.setOutputOnly(true);
		}
	}

}
