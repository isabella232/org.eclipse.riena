package org.eclipse.riena.ui.ridgets.javafx;

import java.util.Collection;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IEditableRidget;
import org.eclipse.riena.ui.ridgets.ValueBindingSupport;
import org.eclipse.riena.ui.ridgets.swt.AbstractEditableRidget;
import org.eclipse.riena.ui.ridgets.validation.IValidationCallback;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;

public abstract class AbstractJavaFxEditableRidget extends
		AbstractJavaFxValueRidget implements IEditableRidget {

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#getUIControlToModelConverter()}
	 */
	@Override
	public IConverter getUIControlToModelConverter() {
		return getValueBindingSupport().getUIControlToModelConverter();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#setUIControlToModelConverter()}
	 */
	@Override
	public void setUIControlToModelConverter(IConverter converter) {
		getValueBindingSupport().setUIControlToModelConverter(converter);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#getValidationRules()}
	 */
	@Override
	public Collection<IValidator> getValidationRules() {
		return getValueBindingSupport().getValidationRules();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#addValidationRule()}
	 */
	@Override
	public void addValidationRule(IValidator validationRule,
			ValidationTime validationTime) {
		Assert.isNotNull(validationRule);
		Assert.isNotNull(validationTime);
		getValueBindingSupport().addValidationRule(validationRule,
				validationTime);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#removeValidationRule()}
	 */
	@Override
	public void removeValidationRule(IValidator validationRule) {
		getValueBindingSupport().removeValidationRule(validationRule);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#addValidationMessage()}
	 */
	@Override
	public void addValidationMessage(final IMessageMarker messageMarker) {
		getValueBindingSupport().addValidationMessage(messageMarker);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#addValidationMessage()}
	 */
	@Override
	public void addValidationMessage(final String message,
			final IValidator validationRule) {
		getValueBindingSupport().addValidationMessage(message, validationRule);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#addValidationMessage()}
	 */
	@Override
	public void addValidationMessage(final String message) {
		getValueBindingSupport().addValidationMessage(message);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#addValidationMessage()}
	 */
	@Override
	public void addValidationMessage(IMessageMarker messageMarker,
			IValidator validationRule) {
		getValueBindingSupport().removeValidationMessage(messageMarker,
				validationRule);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#addValidationMessage()}
	 */
	@Override
	public void removeValidationMessage(final IMessageMarker messageMarker,
			final IValidator validationRule) {
		getValueBindingSupport().removeValidationMessage(messageMarker,
				validationRule);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#removeValidationMessage()}
	 */
	@Override
	public void removeValidationMessage(final IMessageMarker messageMarker) {
		getValueBindingSupport().removeValidationMessage(messageMarker);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#removeValidationMessage()}
	 */
	@Override
	public void removeValidationMessage(final String message,
			final IValidator validationRule) {
		getValueBindingSupport().removeValidationMessage(message,
				validationRule);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#removeValidationMessage()}
	 */
	@Override
	public void removeValidationMessage(final String message) {
		getValueBindingSupport().removeValidationMessage(message);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#checkOnEditRules()}
	 */
	protected final IStatus checkOnEditRules(final Object value,
			final IValidationCallback callback) {
		final ValueBindingSupport vbs = getValueBindingSupport();
		final ValidatorCollection onEditValidators = vbs.getOnEditValidators();
		return onEditValidators.validate(value, callback);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#checkAllRules()}
	 */
	protected final IStatus checkAllRules(final Object value,
			final IValidationCallback callback) {
		final ValueBindingSupport vbs = getValueBindingSupport();
		final ValidatorCollection allValidators = vbs.getAllValidators();
		return allValidators.validate(value, callback);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget#suppressBlock()}
	 */
	protected static final IStatus suppressBlock(final IStatus... statuses) {
		final IStatus status = ValidationRuleStatus.join(statuses);
		IStatus result;
		if (status.getCode() == ValidationRuleStatus.ERROR_BLOCK_WITH_FLASH) {
			final int newCode = ValidationRuleStatus.ERROR_ALLOW_WITH_MESSAGE;
			result = new Status(status.getSeverity(), status.getPlugin(),
					newCode, status.getMessage(), status.getException());
		} else {
			result = status;
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link AbstractEditableRidget.ValidationCallback}
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

		@Override
		public void validationRuleChecked(final IValidator validationRule,
				final IStatus status) {
			final IStatus myStatus = allowBlock ? status
					: suppressBlock(status);
			getValueBindingSupport().updateValidationStatus(validationRule,
					myStatus);
		}

		@Override
		public void validationResult(final IStatus status) {
			final IStatus myStatus = allowBlock ? status
					: suppressBlock(status);
			if (!myStatus.isOK()
					&& myStatus.getCode() == ValidationRuleStatus.ERROR_BLOCK_WITH_FLASH) {
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
