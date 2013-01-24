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
package org.eclipse.riena.demo.client.validator;

import java.text.DecimalFormatSymbols;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

/**
 * This validator tests if the given value if greater than zero.
 */
public class SalaryMoreThanNull implements IValidator {

	private static final char DECIMAL_SEPARATOR = new DecimalFormatSymbols().getDecimalSeparator();
	private static final char GROUPING_SEPARATOR = new DecimalFormatSymbols().getGroupingSeparator();

	public IStatus validate(final Object value) {
		try {
			String strgValue = ""; //$NON-NLS-1$
			if (value != null) {
				strgValue = value.toString();
				strgValue = strgValue.replace("" + GROUPING_SEPARATOR, ""); //$NON-NLS-1$//$NON-NLS-2$
				strgValue = strgValue.replace(DECIMAL_SEPARATOR, '.');
			}
			final float parseFloat = Float.parseFloat(strgValue);
			if (parseFloat > 0) {
				return ValidationRuleStatus.ok();
			}
			return ValidationRuleStatus.error(false, "salary must be more than 0"); //$NON-NLS-1$
		} catch (final NumberFormatException e) {
			return ValidationRuleStatus.error(false, "invalid value"); //$NON-NLS-1$
		}
	}

}
