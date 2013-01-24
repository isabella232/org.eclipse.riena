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
package org.eclipse.riena.ui.ridgets.swt;

import java.util.Collection;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IEditableRidget;
import org.eclipse.riena.ui.ridgets.ValueBindingSupport;
import org.eclipse.riena.ui.ridgets.validation.IValidationCallback;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;

/**
 * Abstract implementation of an {@link IEditableRidget} for SWT.
 */
public abstract class AbstractEditableRidget extends AbstractValueRidget implements IEditableRidget {

	/**
	 * The 'error' background color is shown for this duration of time, in
	 * miliseconds ({@value} ).
	 * 
	 * @deprecated - flashing is now delegated to {@link AbstractMarkerSupport}
	 */
	@Deprecated
	protected static final int FLASH_DURATION_MS = 300;

	/**
	 * Returns the given status object, without the ERROR_BLOCK_WITH_FLASH
	 * status code which is downgraded to ERROR_ALLOW_WITH_MESSAGE. Other status
	 * codes are not modified.
	 * 
	 * @since 2.0
	 */
	protected static final IStatus suppressBlock(final IStatus... statuses) {
		final IStatus status = ValidationRuleStatus.join(statuses);
		IStatus result;
		if (status.getCode() == ValidationRuleStatus.ERROR_BLOCK_WITH_FLASH) {
			final int newCode = ValidationRuleStatus.ERROR_ALLOW_WITH_MESSAGE;
			result = new Status(status.getSeverity(), status.getPlugin(), newCode, status.getMessage(),
					status.getException());
		} else {
			result = status;
		}
		return result;
	}

	public void addValidationRule(final IValidator validationRule, final ValidationTime validationTime) {
		Assert.isNotNull(validationRule);
		Assert.isNotNull(validationTime);
		getValueBindingSupport().addValidationRule(validationRule, validationTime);
	}

	public IConverter getUIControlToModelConverter() {
		return getValueBindingSupport().getUIControlToModelConverter();
	}

	public Collection<IValidator> getValidationRules() {
		return getValueBindingSupport().getValidationRules();
	}

	public void removeValidationRule(final IValidator validationRule) {
		getValueBindingSupport().removeValidationRule(validationRule);
	}

	public void setUIControlToModelConverter(final IConverter converter) {
		getValueBindingSupport().setUIControlToModelConverter(converter);
	}

	public void addValidationMessage(final IMessageMarker messageMarker, final IValidator validationRule) {
		getValueBindingSupport().addValidationMessage(messageMarker, validationRule);
	}

	public void addValidationMessage(final IMessageMarker messageMarker) {
		getValueBindingSupport().addValidationMessage(messageMarker);
	}

	public void addValidationMessage(final String message, final IValidator validationRule) {
		getValueBindingSupport().addValidationMessage(message, validationRule);
	}

	public void addValidationMessage(final String message) {
		getValueBindingSupport().addValidationMessage(message);
	}

	public void removeValidationMessage(final IMessageMarker messageMarker, final IValidator validationRule) {
		getValueBindingSupport().removeValidationMessage(messageMarker, validationRule);
	}

	public void removeValidationMessage(final IMessageMarker messageMarker) {
		getValueBindingSupport().removeValidationMessage(messageMarker);
	}

	public void removeValidationMessage(final String message, final IValidator validationRule) {
		getValueBindingSupport().removeValidationMessage(message, validationRule);
	}

	public void removeValidationMessage(final String message) {
		getValueBindingSupport().removeValidationMessage(message);
	}

	// protected methods
	////////////////////

	/**
	 * Validates the given {@code value} against ALL validators.
	 * 
	 * @param value
	 *            the value to validate
	 * @param IValidationCallback
	 *            a callback object; may be null
	 * @return an IStatus with the most severe validation result; IStatus.isOK()
	 *         indicates valdiation success or failure.
	 * @see ValidationCallback
	 * @since 2.0
	 */
	protected final IStatus checkAllRules(final Object value, final IValidationCallback callback) {
		final ValueBindingSupport vbs = getValueBindingSupport();
		final ValidatorCollection allValidators = vbs.getAllValidators();
		return allValidators.validate(value, callback);
	}

	/**
	 * Validates the given {@code value} against all 'on edit' validators.
	 * 
	 * @param value
	 *            the value to validate
	 * @param IValidationCallback
	 *            a callback object; may be null
	 * @return an IStatus with the most severe validation result; IStatus.isOK()
	 *         indicates validation success or failure.
	 * @see ValidationCallback
	 * @since 2.0
	 */
	protected final IStatus checkOnEditRules(final Object value, final IValidationCallback callback) {
		final ValueBindingSupport vbs = getValueBindingSupport();
		final ValidatorCollection onEditValidators = vbs.getOnEditValidators();
		return onEditValidators.validate(value, callback);
	}

	/**
	 * Validates the given {@code value} against all 'on update' validators.
	 * 
	 * @param value
	 *            the value to validate
	 * @param IValidationCallback
	 *            a callback object; may be null
	 * @return an IStatus with the most severe validation result; IStatus.isOK()
	 *         indicates valdiation success or failure.
	 * @see ValidationCallback
	 * @since 2.0
	 */
	protected final IStatus checkOnUpdateRules(final Object value, final IValidationCallback callback) {
		final ValueBindingSupport vbs = getValueBindingSupport();
		final ValidatorCollection afterGetValidators = vbs.getAfterGetValidators();
		return afterGetValidators.validate(value, callback);
	}

	// helping classes
	//////////////////

	/**
	 * Subclasses invoking the checkXXX methods of
	 * {@link AbstractEditableRidget} may use instances of this class to update
	 * the valdiation state of the ridget.
	 * 
	 * @since 2.0
	 */
	protected final class ValidationCallback implements IValidationCallback {

		private final boolean allowBlock;

		/**
		 * Create a new instance of this class.
		 * 
		 * @param allowBlock
		 *            false will downgrade 'block' status codes to 'allow with
		 *            message' -- see
		 *            {@link AbstractEditableRidget#suppressBlock(IStatus...)}
		 */
		public ValidationCallback(final boolean allowBlock) {
			this.allowBlock = allowBlock;
		}

		public void validationRuleChecked(final IValidator validationRule, final IStatus status) {
			final IStatus myStatus = allowBlock ? status : suppressBlock(status);
			getValueBindingSupport().updateValidationStatus(validationRule, myStatus);
		}

		public void validationResult(final IStatus status) {
			final IStatus myStatus = allowBlock ? status : suppressBlock(status);
			if (!myStatus.isOK() && myStatus.getCode() == ValidationRuleStatus.ERROR_BLOCK_WITH_FLASH) {
				flash();
			}
			/*
			 * VBS only tracks aggregate changes of onUpdate-validation rules.
			 * So when the aggregate status of onEdit rules has changed, it will
			 * go undetected unless we do this:
			 */
			getValueBindingSupport().updateValidationStatus(status);
		}
	}

}
