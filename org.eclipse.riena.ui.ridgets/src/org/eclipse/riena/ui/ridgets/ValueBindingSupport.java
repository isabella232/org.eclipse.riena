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
package org.eclipse.riena.ui.ridgets;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.MessageMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.databinding.RidgetUpdateValueStrategy;
import org.eclipse.riena.ui.ridgets.marker.ValidationMessageMarker;
import org.eclipse.riena.ui.ridgets.validation.IValidationCallback;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;

/**
 * Helper class for Ridgets to delegate their value binding issues to.
 */
public class ValueBindingSupport {

	/**
	 * This rule fails if the ridget is marked with an error marker
	 */
	private final IValidator noErrorsRule = new IValidator() {
		public IStatus validate(final Object value) {
			final boolean isOk = markable.getMarkersOfType(ErrorMarker.class).isEmpty();
			return isOk ? Status.OK_STATUS : Status.CANCEL_STATUS;
		}

		@Override
		public String toString() {
			return "NO_ERRORS_RULE"; //$NON-NLS-1$
		}
	};

	private final ValidatorCollection afterGetValidators;
	private final ValidatorCollection onEditValidators;

	private Map<IValidator, Set<ValidationMessageMarker>> rule2messages;
	private Map<IValidator, IStatus> rule2status;
	private Map<IValidator, ErrorMessageMarker> rule2error;

	private DataBindingContext context;
	private IObservableValue targetOV;
	private IObservableValue modelOV;
	private String valuePropertyName;
	private Object valueHolder;

	private Binding modelBinding;
	private IConverter uiControlToModelConverter;
	private IConverter modelToUIControlConverter;
	private AggregateValidationStatus validationStatus;
	private IMarkable markable;

	public ValueBindingSupport(final IObservableValue target) {
		bindToTarget(target);
		afterGetValidators = new ValidatorCollection();
		onEditValidators = new ValidatorCollection();
	}

	public ValueBindingSupport(final IObservableValue target, final IObservableValue model) {
		this(target);
		bindToModel(model);
	}

	public IConverter getUIControlToModelConverter() {
		return uiControlToModelConverter;
	}

	public void setUIControlToModelConverter(final IConverter uiControlToModelConverter) {
		this.uiControlToModelConverter = uiControlToModelConverter;
	}

	public IConverter getModelToUIControlConverter() {
		return modelToUIControlConverter;
	}

	public void setModelToUIControlConverter(final IConverter modelToUIControlConverter) {
		this.modelToUIControlConverter = modelToUIControlConverter;
	}

	public Collection<IValidator> getValidationRules() {
		final List<IValidator> allValidationRules = new ArrayList<IValidator>(onEditValidators.getValidators());
		allValidationRules.addAll(afterGetValidators.getValidators());
		return allValidationRules;
	}

	/**
	 * Return all validation rules kept by this instance.
	 * 
	 * @return a ValidatorCollection with all rules; never null; may be empty.
	 * 
	 * @since 1.2
	 */
	public ValidatorCollection getAllValidators() {
		final ValidatorCollection result = new ValidatorCollection();
		for (final IValidator validationRule : onEditValidators) {
			result.add(validationRule);
		}
		for (final IValidator validationRule : afterGetValidators) {
			result.add(validationRule);
		}
		return result;
	}

	/**
	 * Return all 'on edit' validation rules kept by this instance.
	 * 
	 * @return a ValidatorCollection; never null; may be empty.
	 */
	public ValidatorCollection getOnEditValidators() {
		return onEditValidators;
	}

	/**
	 * Return all 'on update' validation rules kept by this instance.
	 * 
	 * @return a ValidatorCollection; never null; may be empty.
	 */
	public ValidatorCollection getAfterGetValidators() {
		return afterGetValidators;
	}

	/**
	 * Adds a validation rule.
	 * 
	 * @param validationRule
	 *            The validation rule to add (non null)
	 * @param validationTime
	 *            a value specifying when to evalute the validationRule (non
	 *            null)
	 * @return true, if the onEditValidators were changed, false otherwise
	 * @see #getOnEditValidators()
	 * @throws RuntimeException
	 *             if validationRule is null, or an unsupported ValidationTime
	 *             is used
	 */
	public boolean addValidationRule(final IValidator validationRule, final ValidationTime validationTime) {
		Assert.isNotNull(validationRule);
		if (validationTime == ValidationTime.ON_UI_CONTROL_EDIT) {
			onEditValidators.add(validationRule);
			return true;
		} else if (validationTime == ValidationTime.ON_UPDATE_TO_MODEL) {
			afterGetValidators.add(validationRule);
			return false;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Removes a validation rule.
	 * 
	 * @param validationRule
	 *            The validation rule to remove
	 * @return true, if the onEditValidators were changed, false otherwise
	 * @see #getOnEditValidators()
	 */
	public boolean removeValidationRule(final IValidator validationRule) {
		if (validationRule == null) {
			return false;
		}
		removeErrorMarker(validationRule);
		removeMessages(validationRule);
		clearStatus(validationRule);
		// first remove in the list of afterGetValidators
		afterGetValidators.remove(validationRule);
		// if it is in the list of On_edit validators, also remove and return true
		if (onEditValidators.contains(validationRule)) {
			onEditValidators.remove(validationRule);
			return true;
		} else {
			return false;
		}
	}

	public void bindToTarget(final IObservableValue observableValue) {
		targetOV = observableValue;
		rebindToModel();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IValueRidget#bindToModel(org.eclipse.core
	 *      .databinding.observable.value.IObservableValue)
	 */
	public void bindToModel(final IObservableValue observableValue) {
		this.valuePropertyName = null;
		this.valueHolder = null;
		modelOV = observableValue;
		rebindToModel();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IValueRidget#bindToModel(java.lang.Object,
	 *      java.lang.String)
	 */
	public void bindToModel(final Object valueHolder, final String valuePropertyName) {
		this.valueHolder = valueHolder;
		this.valuePropertyName = valuePropertyName;
		if (isBean(valueHolder.getClass())) {
			modelOV = BeansObservables.observeValue(valueHolder, valuePropertyName);
		} else {
			modelOV = PojoObservables.observeValue(valueHolder, valuePropertyName);
		}
		rebindToModel();
	}

	/**
	 * Binds (first time or again) the model to the control.
	 */
	public void rebindToModel() {
		if (modelOV == null || targetOV == null) {
			return;
		}
		final UpdateValueStrategy uiControlToModelStrategy = new RidgetUpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE);
		final UpdateValueStrategy modelToUIControlStrategy = new RidgetUpdateValueStrategy(
				UpdateValueStrategy.POLICY_ON_REQUEST);
		uiControlToModelStrategy.setAfterGetValidator(afterGetValidators);
		if (uiControlToModelConverter != null) {
			if ((targetOV.getValueType() == uiControlToModelConverter.getFromType())
					&& (modelOV.getValueType() == uiControlToModelConverter.getToType())) {
				uiControlToModelStrategy.setConverter(uiControlToModelConverter);
			}
		}
		if (modelToUIControlConverter != null) {
			if ((targetOV.getValueType() == modelToUIControlConverter.getToType())
					&& (modelOV.getValueType() == modelToUIControlConverter.getFromType())) {
				modelToUIControlStrategy.setConverter(modelToUIControlConverter);
			}
		}

		if (modelBinding != null) {
			// MUST dispose previous binding, otherwise code like this:
			// ridget.bind(modelA);
			// ridget.bind(modelB);
			// causes the ridget to be bound to two models and ui changes are
			// synched with both! 
			modelBinding.dispose();
		}
		if (validationStatus != null) {
			// must dispose old instance, see performance Bug 327684
			validationStatus.dispose();
		}

		modelBinding = getContext().bindValue(targetOV, modelOV, uiControlToModelStrategy, modelToUIControlStrategy);
		validationStatus = new AggregateValidationStatus(getContext().getBindings(),
				AggregateValidationStatus.MAX_SEVERITY);
		validationStatus.addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(final ValueChangeEvent event) {
				final IStatus newStatus = (IStatus) ((ComputedValue) event.getSource()).getValue();
				updateValidationMessages(newStatus);
			}

			private void updateValidationMessages(final IStatus newStatus) {
				if (targetOV != null) {
					final Object value = targetOV.getValue();
					updateValidationStatus(noErrorsRule, newStatus);
					for (final IValidator rule : getAfterGetValidators()) {
						updateValidationStatus(rule, rule.validate(value));
					}
				}
			}
		});
	}

	public IObservableValue getModelObservable() {
		return modelOV;
	}

	public Binding getModelBinding() {
		return modelBinding;
	}

	/**
	 * Get the value property name that has been specified with the
	 * <i>bindToModel</i> method.
	 * 
	 * @return the value property name
	 * 
	 * @since 4.0
	 */
	public String getValuePropertyName() {
		return valuePropertyName;
	}

	public void setMarkable(final IMarkable markable) {
		this.markable = markable;
	}

	public void updateFromModel() {
		if (valueHolder != null) {
			if (!isBean(valueHolder.getClass()) && isNestedProperty(valuePropertyName)) {
				bindToModel(valueHolder, valuePropertyName);
			}
		}
		if (modelBinding != null) {
			modelBinding.updateModelToTarget();
		}
	}

	/**
	 * Returns whether the given name for the property is a conjunction of
	 * properties. The nested properties must be separated with a dot (e.g.
	 * "parent.name").
	 * 
	 * @param propertyName
	 *            the property name
	 * @return {@code true} if the property name contains nested properties;
	 *         otherwise {@code false}
	 */
	private boolean isNestedProperty(final String propertyName) {
		return StringUtils.isGiven(propertyName) && (propertyName.indexOf('.') != -1);
	}

	public void updateFromTarget() {
		if (modelBinding != null) {
			modelBinding.updateTargetToModel();
		}
	}

	public DataBindingContext getContext() {
		if (context == null) {
			context = new DataBindingContext();
		}
		return context;
	}

	public void addValidationMessage(final String message) {
		addValidationMessage(message, noErrorsRule);
	}

	public void addValidationMessage(final IMessageMarker messageMarker) {
		addValidationMessage(messageMarker, noErrorsRule);
	}

	public void addValidationMessage(final String message, final IValidator validationRule) {
		addValidationMessage(new MessageMarker(message), validationRule);
	}

	public void addValidationMessage(final IMessageMarker messageMarker, final IValidator validationRule) {
		Assert.isNotNull(messageMarker, "messageMarker cannot be null"); //$NON-NLS-1$
		Assert.isNotNull(validationRule, "validationRule cannot be null"); //$NON-NLS-1$
		final ValidationMessageMarker validationMessageMarker = new ValidationMessageMarker(messageMarker,
				validationRule);
		if (rule2messages == null) {
			rule2messages = new HashMap<IValidator, Set<ValidationMessageMarker>>();
		}
		Set<ValidationMessageMarker> messages = rule2messages.get(validationRule);
		if (messages == null) {
			messages = new HashSet<ValidationMessageMarker>();
			rule2messages.put(validationRule, messages);
		}
		messages.add(validationMessageMarker);
		updateValidationMessageMarker(validationRule);
	}

	public void removeValidationMessage(final String message) {
		removeValidationMessage(message, noErrorsRule);
	}

	public void removeValidationMessage(final IMessageMarker messageMarker) {
		removeValidationMessage(messageMarker, noErrorsRule);
	}

	public void removeValidationMessage(final String message, final IValidator validationRule) {
		removeValidationMessage(new MessageMarker(message), validationRule);
	}

	public void removeValidationMessage(final IMessageMarker messageMarker, final IValidator validationRule) {
		final ValidationMessageMarker validationMessageMarker = new ValidationMessageMarker(messageMarker,
				validationRule);
		if (rule2messages != null) {
			final Set<ValidationMessageMarker> messages = rule2messages.get(validationRule);
			messages.remove(validationMessageMarker);
			markable.removeMarker(validationMessageMarker);
			if (messages.isEmpty()) {
				rule2messages.remove(validationRule);
			}
		}
	}

	/**
	 * Updates the aggregate error status (i.e. the sum of all rules). This will
	 * update the error state and messages attached to the aggregate error
	 * status -- see {@link #addValidationMessage(String)}.
	 * <p>
	 * Implementation note: This should be invoked when the aggregate state of
	 * 'on_edit' rules has changed, since the status of such rules is not
	 * 
	 * @param status
	 *            an IStatus instance with the aggregate status; never null
	 * @see IValidationCallback
	 * @since 1.2
	 */
	public void updateValidationStatus(final IStatus status) {
		updateValidationStatus(noErrorsRule, status);
	}

	/**
	 * Update the status of a specific rule. This will update the error state
	 * and messages attached to that rule -- see
	 * {@link #addValidationMessage(String, IValidator)}.
	 * 
	 * @param validationRule
	 *            an IValidator instance; never null
	 * @param status
	 *            an IStatus instance; never null
	 * @see IValidationCallback
	 * @since 1.2
	 */
	public void updateValidationStatus(final IValidator validationRule, final IStatus status) {
		// trace("updating rule " + validationRule + " with " + status);
		storeStatus(validationRule, status);
		if (!status.isOK()) {
			addErrorMarker(validationRule, status);
			addMessages(validationRule);
		} else {
			removeErrorMarker(validationRule);
			removeMessages(validationRule);
		}
	}

	// helping methods
	//////////////////

	private void addErrorMarker(final IValidator validationRule, final IStatus status) {
		if (isBlocked(validationRule, status) || validationRule == noErrorsRule) {
			return;
		}
		if (rule2error == null) {
			rule2error = new HashMap<IValidator, ErrorMessageMarker>();
		}
		ErrorMessageMarker errorMarker = rule2error.get(validationRule);
		if (errorMarker == null) {
			errorMarker = new ErrorMessageMarker(status.getMessage());
			rule2error.put(validationRule, errorMarker);
		} else {
			errorMarker.setMessage(status.getMessage());
		}
		markable.addMarker(errorMarker);
		// trace("+EM " + errorMarker + " " + markable.getMarkers().size());
	}

	private void addMessages(final IValidator validationRule) {
		if (rule2messages != null && rule2messages.containsKey(validationRule)) {
			final Set<ValidationMessageMarker> messages = rule2messages.get(validationRule);
			for (final ValidationMessageMarker message : messages) {
				// trace("+VMM " + message);
				markable.addMarker(message);
			}
		}
	}

	private void clearStatus(final IValidator validationRule) {
		if (rule2status != null) {
			rule2status.remove(validationRule);
		}
	}

	private IStatus getStatus(final IValidator validationRule) {
		return rule2status != null ? rule2status.get(validationRule) : null;
	}

	private boolean isBean(final Class<?> clazz) {
		boolean result;
		try {
			// next line throws NoSuchMethodException, if no matching method found
			clazz.getMethod("addPropertyChangeListener", PropertyChangeListener.class); //$NON-NLS-1$
			result = true; // have bean
		} catch (final NoSuchMethodException e) {
			result = false; // have pojo
		}
		return result;
	}

	private boolean isBlocked(final IValidator validationRule, final IStatus status) {
		return status.getCode() == ValidationRuleStatus.ERROR_BLOCK_WITH_FLASH
				&& onEditValidators.contains(validationRule);
	}

	private void storeStatus(final IValidator validationRule, final IStatus status) {
		if (rule2status == null) {
			rule2status = new HashMap<IValidator, IStatus>();
		}
		rule2status.put(validationRule, status);
	}

	private void removeErrorMarker(final IValidator validationRule) {
		if (rule2error != null) {
			final ErrorMessageMarker errorMarker = rule2error.remove(validationRule);
			if (errorMarker != null) {
				markable.removeMarker(errorMarker);
				// trace("-EM " + errorMarker + " " + (size - 1));
			}
		}
	}

	private void removeMessages(final IValidator validationRule) {
		if (rule2messages != null && rule2messages.containsKey(validationRule)) {
			final Set<ValidationMessageMarker> messages = rule2messages.get(validationRule);
			for (final ValidationMessageMarker message : messages) {
				markable.removeMarker(message);
				// trace("-VMM " + message);
			}
		}
	}

	private void updateValidationMessageMarker(final IValidator validationRule) {
		final IStatus status = getStatus(validationRule);
		if (status != null) {
			updateValidationStatus(validationRule, status);
		}
	}

	//	private void trace(String message) {
	//		System.out.println(message); 
	//	}

}
