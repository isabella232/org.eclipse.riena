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
package org.eclipse.riena.ui.filter;

import org.eclipse.core.databinding.validation.IValidator;

import org.eclipse.riena.ui.core.marker.ValidationTime;

/**
 * This filter rule adds a validator to a Ridget.
 */
public interface IUIFilterRuleValidatorRidget extends IUIFilterRuleValidator {

	/**
	 * Sets the ID of the ridget, to which the validator of the rule will be
	 * added.
	 * 
	 * @param id
	 *            ID of the ridget
	 */
	void setId(String id);

	/**
	 * Sets the validator of this rule.
	 * 
	 * @param validator
	 *            validator which will be added to a Ridget
	 */
	void setValidator(IValidator validator);

	/**
	 * Sets the time of validation.
	 * 
	 * @param validationTime
	 *            time of validation
	 */
	void setValidationTime(ValidationTime validationTime);

}
