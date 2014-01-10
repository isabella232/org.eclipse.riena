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
 * This filter rule adds a validator to an UI element (i.g. ridget or navigation
 * node).
 */
public interface IUIFilterRuleValidator extends IUIFilterRule {

	/**
	 * Returns the validator of this IUIFilterRuleValidator.
	 * 
	 * @return validator
	 */
	IValidator getValidator();

	/**
	 * Returns the time of validation.
	 * 
	 * @return validation time
	 */
	ValidationTime getValidationTime();

}
