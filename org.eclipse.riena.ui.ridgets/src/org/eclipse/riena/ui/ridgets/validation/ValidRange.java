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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import com.ibm.icu.text.NumberFormat;

import org.osgi.service.log.LogService;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.conversion.NumberToStringConverter;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.log.Logger;
import org.eclipse.osgi.util.NLS;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.ArraysUtil;
import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.ui.ridgets.nls.Messages;

/**
 * A range check rule for a Number.
 * <p>
 * Checks if a given string could be safely converted to a given number type and if the input is in a given range. A value of <tt>null</tt> or an empty String
 * is treated as zero.
 * <p>
 * This rule does not support partial correctness checking.
 * 
 * @see ValidRangeAllowEmpty
 */
public class ValidRange extends ValidDecimal implements IExecutableExtension {

	private Number min;
	private Number max;

	private final IConverter converter;

	/**
	 * Constructs a range check rule for the default locate, with the range set to (0, 0).
	 */
	public ValidRange() {
		this(0, 0);
	}

	/**
	 * Constructs a range check rule for the default locale, with the range set to (min, max).
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
	 *             if a parameter <tt>min</tt> and <tt>max</tt> do not belong to the same class.
	 */
	public ValidRange(final Number min, final Number max) {
		this(min, max, Locale.getDefault());
	}

	/**
	 * Constructs a range check rule for the given locale, with the range set to (min, max).
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
	 *             if a parameter <tt>min</tt> and <tt>max</tt> do not belong to the same class.
	 */
	public ValidRange(final Number min, final Number max, final Locale locale) {
		this(min, max, locale, null);
	}

	/**
	 * Constructs a range check rule for the given locale, with the range set to (min, max).
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @param converter
	 *            a IConverter capable of converting the range min and max numbers to a string (only used for the error reporting)
	 * 
	 * @throws some_kind_of_runtime_exception
	 *             if <tt>min &gt;= max</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter is <tt>null</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter <tt>min</tt> and <tt>max</tt> do not belong to the same class.
	 * 
	 * @since 4.0
	 */
	public ValidRange(final Number min, final Number max, final IConverter converter) {
		this(min, max, Locale.getDefault(), converter);
	}

	/**
	 * Constructs a range check rule for the given locale, with the range set to (min, max).
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @param locale
	 *            the Locale to use for number formatting; never null.
	 * @param converter
	 *            a IConverter capable of converting the range min and max numbers to a string (only used for the error reporting)
	 * 
	 * @throws some_kind_of_runtime_exception
	 *             if <tt>min &gt;= max</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter is <tt>null</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter <tt>min</tt> and <tt>max</tt> do not belong to the same class.
	 * 
	 * @since 4.0
	 */
	public ValidRange(final Number min, final Number max, final Locale locale, final IConverter converter) {
		this(min, max, locale, converter, DEFAULT_NUMBER_OF_FRACTION_DIGITS, DEFAULT_MAX_LENGTH);
	}

	/**
	 * Constructs a range check rule for the given locale, with the range set to (min, max).
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @param locale
	 *            the Locale to use for number formatting; never null.
	 * @param converter
	 *            a IConverter capable of converting the range min and max numbers to a string (only used for the error reporting)
	 * @param numberOfFractionDigits
	 *            number of fraction digits.
	 * @param maxLength
	 *            number of integer digits.
	 * 
	 * @throws some_kind_of_runtime_exception
	 *             if <tt>min &gt;= max</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter is <tt>null</tt>.
	 * @throws some_kind_of_runtime_exception
	 *             if a parameter <tt>min</tt> and <tt>max</tt> do not belong to the same class.
	 * 
	 * @since 4.0
	 */
	public ValidRange(final Number min, final Number max, final Locale locale, final IConverter converter, final int numberOfFractionDigits, final int maxLength) {
		super(true, numberOfFractionDigits, maxLength, true, locale);
		Assert.isNotNull(min, "parameter min must not be null"); //$NON-NLS-1$
		Assert.isNotNull(max, "parameter max must not be null"); //$NON-NLS-1$
		Assert.isLegal(min.getClass().equals(max.getClass()), "min and max must be of the same class. (min =  " //$NON-NLS-1$
				+ min.getClass().getName() + ", max = " + max.getClass().getName()); //$NON-NLS-1$		
		this.min = min;
		this.max = max;
		Assert.isLegal(toBigDecimal(this.min).compareTo(toBigDecimal(this.max)) <= 0, "min " + this.min + " must be smaller or equal max " //$NON-NLS-1$ //$NON-NLS-2$
				+ this.max);
		this.converter = converter;
	}

	/**
	 * Returns an IStatus instance; never null.
	 * 
	 * @param value
	 *            a <tt>String</tt> instance or <tt>null</tt>, where <tt>null</tt> is treated as zero.
	 * 
	 * @see org.eclipse.riena.ui.ridgets.validation.ValidDecimal#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(final Object value) {
		final IStatus validDecimalStatus = super.validate(value);
		if (!validDecimalStatus.isOK()) {
			return validDecimalStatus;
		}
		Assert.isLegal(value == null || value instanceof String);
		BigDecimal currentValue = BigDecimal.ZERO;
		if (value != null) {
			final String string = Utils.removeWhitespace((String) value);
			if (string.length() > 0) {
				final DecimalFormat format = getFormat();
				synchronized (format) { // format not thread safe!
					format.setParseBigDecimal(true);
					try {
						currentValue = (BigDecimal) format.parse(string);
					} catch (final ParseException pex) {
						// should never occur, as super.validate(Object) will
						// make this method return earlier
						final Logger logger = Log4r.getLogger(ValidRange.class);
						final String message = NLS.bind(Messages.ValidRange_error_cannotParse, string);
						logger.log(LogService.LOG_ERROR, message, pex);
						return ValidationRuleStatus.error(true, message);
					}
				}
			}
		}
		if (validateRange(currentValue)) {
			final String message = NLS.bind(Messages.ValidRange_error_outOfRange, new Object[] { convert(currentValue), convert(min), convert(max) });
			return ValidationRuleStatus.error(true, message);
		}
		return ValidationRuleStatus.ok();
	}

	private String convert(final Number number) {
		if (converter != null) {
			return (String) converter.convert(number);
		}
		final NumberFormat numberInstance = NumberFormat.getNumberInstance(getLocale());
		numberInstance.setGroupingUsed(isGroupingInMessage());
		return (String) NumberToStringConverter.fromBigDecimal(numberInstance).convert(toBigDecimal(number));
	}

	/**
	 * @since 3.0
	 */
	protected boolean validateRange(final BigDecimal value) {
		return value.compareTo(toBigDecimal(min, value)) < 0 || value.compareTo(toBigDecimal(max, value)) > 0;
	}

	/**
	 * Convert a number to BigDecimal.
	 * 
	 * @param number
	 *            a number.
	 * 
	 * @return a BigDecimal instance.
	 */
	protected BigDecimal toBigDecimal(final Number number) {
		return toBigDecimal(number, 0);
	}

	/**
	 * Convert a number to BigDecimal.
	 * 
	 * @param number
	 *            a number.
	 * 
	 * @param value
	 *            the value used for the precision of the number to convert.
	 * 
	 * @return a BigDecimal instance.
	 * @since 3.0
	 */
	protected BigDecimal toBigDecimal(final Number number, final BigDecimal value) {
		return toBigDecimal(number, value.precision());
	}

	/**
	 * Convert a number to BigDecimal.
	 * 
	 * @param number
	 *            a number.
	 * 
	 * @param precision
	 *            The number of digits to be used for an operation. A value of 0 indicates that unlimited precision (as many digits as are required) will be
	 *            used. Note that leading zeros (in the coefficient of a number) are never significant.
	 * 
	 * @return a BigDecimal instance.
	 * @since 3.0
	 */
	protected BigDecimal toBigDecimal(final Number number, final int precision) {
		if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		} else if (number instanceof BigInteger) {
			return new BigDecimal((BigInteger) number);
		} else if (number instanceof Float || number instanceof Double) {
			return new BigDecimal(number.doubleValue(), new MathContext(precision));
		} else {
			return new BigDecimal(number.longValue());
		}
	}

	/**
	 * This method is called on a newly constructed extension for validation. After creating a new instance of {@code ValidRange} this method is called to
	 * initialize the instance. The arguments for initialization are in the parameter {@code data}. Is the data a string the arguments are separated with ','.
	 * The order of the arguments in data is equivalent to the order of the parameters of one of the constructors.<br>
	 * If data has more than two arguments. The last arguments are used to set the Local for this validator.
	 * 
	 * 
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 *      java.lang.Object)
	 * @see org.eclipse.riena.ui.ridgets.validation.ValidDecimal#setLocale(java.lang.String[])
	 */
	@Override
	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data) throws CoreException {

		if (data instanceof String) {
			final String[] args = PropertiesUtils.asArray(data);
			if (args.length > 0) {
				this.min = new BigDecimal(args[0]);
			}
			if (args.length > 1) {
				this.max = new BigDecimal(args[1]);
			}
			if (args.length > 2) {
				final String[] localArgs = ArraysUtil.copyRange(args, 2, args.length);
				setLocale(localArgs);
			}
		}

	}

}
