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
package org.eclipse.riena.ui.filter.extension;

import org.eclipse.core.databinding.validation.IValidator;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * The rule to add a validator or to a ridget.
 */
@ExtensionInterface
public interface IRuleValidatorRidget {

	/**
	 * Returns the ID of a ridget, to which the validator of the rule will be
	 * added.
	 * 
	 * @return ID of ridget
	 */
	String getRidgetId();

	/**
	 * Returns a {@code String} that specifies when to evaluate the validator.
	 * 
	 * @return "onUIControlEdit" or "onUpdateToModel"
	 */
	String getValidationTime();

	/**
	 * Returns the a new instance of validator.
	 * 
	 * @return validator which will be added to a Ridget
	 */
	IValidator getValidator();

}
