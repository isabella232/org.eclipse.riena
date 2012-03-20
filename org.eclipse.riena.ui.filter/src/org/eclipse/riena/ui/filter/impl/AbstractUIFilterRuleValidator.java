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
package org.eclipse.riena.ui.filter.impl;

import org.eclipse.core.databinding.validation.IValidator;

import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.filter.IUIFilterRuleValidator;

/**
 * Filter rule for with validator.
 */
public abstract class AbstractUIFilterRuleValidator implements IUIFilterRuleValidator {
	private IValidator validator;

	private ValidationTime validationTime;

	/**
	 * Create a new filter rule for validation.
	 */
	public AbstractUIFilterRuleValidator() {
	}

	/**
	 * Create a new filter rule with the given validator.
	 * 
	 * @param validator
	 *            validator to set
	 * @param validationTime
	 *            time of validation
	 */
	public AbstractUIFilterRuleValidator(final IValidator validator, final ValidationTime validationTime) {
		this.validator = validator;
		this.validationTime = validationTime;
	}

	public IValidator getValidator() {
		return validator;
	}

	public ValidationTime getValidationTime() {
		return validationTime;
	}

	public void setValidator(final IValidator validator) {
		this.validator = validator;
	}

	public void setValidationTime(final ValidationTime validationTime) {
		this.validationTime = validationTime;
	}

}
