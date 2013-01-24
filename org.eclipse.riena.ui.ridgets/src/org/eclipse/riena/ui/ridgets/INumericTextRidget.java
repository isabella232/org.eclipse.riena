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
package org.eclipse.riena.ui.ridgets;

/**
 * Ridget for a numeric text field.
 * <p>
 * On the view-side we have to use the appropriate control-creation method of
 * the UIControlsFactory (see UIControlsFactory.createTextNumeric()).
 * <p>
 * The ridget uses strings internally. It can be bound to any model that is
 * supported by the JFace databinding, provided the bounds value can be
 * converted to a string representing a number. Model values have to conform to
 * signage setting of the ridget.
 */
public interface INumericTextRidget extends ITextRidget {

	/**
	 * Property name of the singed property ("signed").
	 */
	String PROPERTY_SIGNED = "signed"; //$NON-NLS-1$

	/**
	 * Property name of the max length property ("maxLength").
	 * 
	 * @since 2.0
	 */
	String PROPERTY_MAXLENGTH = "maxLength"; //$NON-NLS-1$

	/**
	 * Max length configuration value for unbounded input length.
	 * 
	 * <pre/>
	 * Usage:
	 * setMaxLength(INumericTextRidget.MAX_LENGTH_UNBOUNDED )
	 * @since 4.0
	 */
	int MAX_LENGTH_UNBOUNDED = -1;

	/**
	 * Returns the maximum number of decimal digits (excluding separators and
	 * the minus sign). May be -1 if no limit is set.
	 * 
	 * @return the number of decimal digits
	 * @since 2.0
	 */
	int getMaxLength();

	/**
	 * When true, empty values ("", null) will be converted to zero (i.e. "0" or
	 * "0,00"). Consult {@link #setConvertEmptyToZero(boolean)} for details.
	 * <p>
	 * The default setting for this option is false.
	 * 
	 * @since 3.0
	 */
	boolean isConvertEmptyToZero();

	/**
	 * Indicates whether grouping is used to separate thousands.
	 * <p>
	 * The default setting for this option is true.
	 * 
	 * @return Indicates whether grouping is used to separate thousands.
	 */
	boolean isGrouping();

	/**
	 * Indicates if negative values should be marked.
	 * <p>
	 * The default setting for this option is true.
	 * 
	 * @return true if negative values should be marked
	 */
	boolean isMarkNegative();

	/**
	 * Indicates whether negative values are allowed.
	 * <p>
	 * The default setting for this option is true.
	 * 
	 * @return Indicates whether negative values are allowed.
	 */
	boolean isSigned();

	/**
	 * When true, empty values ("", null) will be converted to zero (i.e. "0" or
	 * "0,00"). This works as follows:
	 * <ul>
	 * <li>when an empty value is passed via {@link #setText(String)} or
	 * {@link #updateFromModel()} the text control will display zero but
	 * {@link #getText()} returns {@code ""}</li>
	 * <li>when an empty value is entered by the user it is converted to zero.
	 * The text control will display zero and {@link #getText()} will return
	 * zero</li>
	 * </ul>
	 * <p>
	 * The default setting for this option is false.
	 * 
	 * @since 3.0
	 */
	void setConvertEmptyToZero(boolean nullAsZero);

	/**
	 * Sets whether grouping is used to separate thousands. This will use the
	 * grouping separator of the default locale.
	 * <p>
	 * The default setting for this option is true.
	 * 
	 * @param useGrouping
	 *            The new grouping state.
	 */
	void setGrouping(boolean useGrouping);

	/**
	 * Sets whether negative values should be marked.
	 * <p>
	 * The default setting for this option is true.
	 * 
	 * @param mustBeMarked
	 *            whether to mark or not a negative value
	 */
	void setMarkNegative(boolean mustBeMarked);

	/**
	 * Sets the number of allowed decimal digits, that it , the number of digits
	 * before the comma separator. Note that the grouping separators ( i.e. the
	 * dots between "1.034.235.123" ) do not count towards reaching this limit.
	 * <p>
	 * Note that {@link #setText(String)} and {@link #updateFromModel()} will
	 * throw a RuntimeException when the number of decimal digits is exceeded.
	 * 
	 * @param maxLength
	 *            a value greater than 0 or -1 if no limit is should be set
	 * @since 2.0
	 */
	void setMaxLength(int maxLength);

	/**
	 * Sets whether negative values are allowed.
	 * <p>
	 * Note that {@link #setText(String)} and {@link #updateFromModel()} will
	 * throw a RuntimeException with negative values after
	 * {@code setSigned(false)} has been called.
	 * <p>
	 * The default setting for this option is true.
	 * 
	 * @param signed
	 *            The new signed state.
	 */
	void setSigned(boolean signed);
}
