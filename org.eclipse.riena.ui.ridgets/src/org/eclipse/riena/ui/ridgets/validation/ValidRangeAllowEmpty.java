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
package org.eclipse.riena.ui.ridgets.validation;

import java.util.Locale;

import org.eclipse.core.runtime.IStatus;

/**
 * A range check rule for a Number, which accepts empty values without flagging
 * an error.
 * <p>
 * The empty value can be <tt>null</tt> or empty string.
 * 
 * @see ValidRange
 * @since 2.0
 */
public class ValidRangeAllowEmpty extends ValidRange {

	/**
	 * Constructs a range check rule for the default locate, with the range set
	 * to (0, 0).
	 */
	public ValidRangeAllowEmpty() {
		super();
	}

	/**
	 * Constructs a range check rule for the default locale, with the range set
	 * to (min, max).
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * 
	 * @throws some_kind_of_runtime_exception
	 *             if <tt>min &gt;= max</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter is <tt>null</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter <tt>min</tt> and <tt>max</tt> do not belong to
	 *             the same class.
	 */
	public ValidRangeAllowEmpty(final Number min, final Number max) {
		super(min, max);
	}

	/**
	 * Constructs a range check rule for the given locale, with the ragne set to
	 * (min, max).
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @param locale
	 *            the Locale to use for number formatting; never null.
	 * 
	 * @throws some_kind_of_runtime_exception
	 *             if <tt>min &gt;= max</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter is <tt>null</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter <tt>min</tt> and <tt>max</tt> do not belong to
	 *             the same class.
	 */
	public ValidRangeAllowEmpty(final Number min, final Number max, final Locale locale) {
		super(min, max, locale);
	}

	@Override
	public IStatus validate(final Object value) {
		if (value == null) {
			return ValidationRuleStatus.ok();
		}
		if (value instanceof String) {
			if (((String) value).length() == 0) {
				return ValidationRuleStatus.ok();
			}
		}
		return super.validate(value);
	}
}
