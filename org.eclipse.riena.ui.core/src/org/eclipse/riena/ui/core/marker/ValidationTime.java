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
package org.eclipse.riena.ui.core.marker;

/**
 * This enum is used when adding a {link IValidator} to a ridget, to specify when the validator will be evaluated.
 * 
 * @see {@link IEditableRidget#addValidationRule(IValidator, ValidationTime)
 */
public enum ValidationTime {

	/**
	 * Hint to evaluate an {@link IValidator} "on edit".
	 * <p>
	 * This happens after the user has changed a value in the widget and before copying the new value into the ridget. On edit validations may block (i.e.
	 * abort) the change, thus resetting the widget value.
	 * 
	 * @see IValidationRuleStatus
	 */
	ON_UI_CONTROL_EDIT,
	/**
	 * Hint to evaluate an {@link IValidator} "on update".
	 * <p>
	 * This happens while copying the current ridget value into the model. Several ways may trigger an update, such as the widget loosing the focus or the user
	 * triggering an update.
	 */
	ON_UPDATE_TO_MODEL,
	/**
	 * Hint to evaluate an {@link IValidator} "after update".
	 * <p>
	 * This happens after the current ridget value was copied into the model. The value will be written into the model, even if it does not pass the validators,
	 * set for <tt>AFTER_UPDATE_TO_MODEL</tt>.
	 * 
	 * @since 4.0
	 */
	AFTER_UPDATE_TO_MODEL

}
