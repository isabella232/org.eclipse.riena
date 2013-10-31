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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;

import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractEditableRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;

/**
 * Ridget for {@link DateTime} widgets.
 */
public class DateTimeRidget extends AbstractEditableRidget implements IDateTimeRidget {

	/**
	 * This property is used by the databinding to sync ridget and model. It is
	 * always fired before its sibling {@link IDateTimeRidget#PROPERTY_DATE} to
	 * ensure that the model is updated before any listeners try accessing it.
	 * <p>
	 * This property is not API. Do not use in client code.
	 */
	private static final String PROPERTY_DATE_INTERNAL = "dateInternal"; //$NON-NLS-1$

	private Date date;
	private DataBindingContext dbc;
	private Binding controlBinding;

	@Override
	protected void checkUIControl(final Object uiControl) {
		checkType(uiControl, DateTime.class);
	}

	@Override
	protected void bindUIControl() {
		final DateTime control = getUIControl();
		if (control != null) {
			dbc = new DataBindingContext();
			final IObservableValue timeObservable;
			final IObservableValue dateObservable;
			final Date nonNullDate = getNonNullDate(getDate());
			if (isTimeControl(control)) {
				// it is a time widget
				timeObservable = WidgetProperties.selection().observe(control);
				timeObservable.setValue(nonNullDate);
				dateObservable = new WritableValue(timeObservable.getRealm(), nonNullDate, Date.class);
			} else {
				// it is  date/calendar widget
				dateObservable = WidgetProperties.selection().observe(control);
				dateObservable.setValue(nonNullDate);
				timeObservable = new WritableValue(dateObservable.getRealm(), nonNullDate, Date.class);
			}
			controlBinding = dbc.bindValue(new DateAndTimeObservableWithNullConversion(dateObservable, timeObservable),
					getRidgetObservable(), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE)
							.setAfterGetValidator(new EditRulesValidator()), new UpdateValueStrategy(
							UpdateValueStrategy.POLICY_ON_REQUEST));
		}
	}

	@Override
	protected void unbindUIControl() {
		super.unbindUIControl();
		if (dbc != null) {
			dbc.dispose();
			dbc = null;
		}
	}

	@Override
	protected final IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, PROPERTY_DATE_INTERNAL);
	}

	@Override
	public DateTime getUIControl() {
		return (DateTime) super.getUIControl();
	}

	@Override
	public void bindToModel(final IObservableValue observableValue) {
		unbindUIControl();

		Assert.isNotNull(observableValue);
		super.bindToModel(observableValue);

		bindUIControl();
	}

	@Override
	public void bindToModel(final Object valueHolder, final String valuePropertyName) {
		if (AbstractSWTRidget.isBean(valueHolder.getClass())) {
			bindToModel(BeansObservables.observeValue(valueHolder, valuePropertyName));
		} else {
			bindToModel(PojoObservables.observeValue(valueHolder, valuePropertyName));
		}
	}

	public Date getDate() {
		return date;
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final Date getDateInternal() {
		return getDate();
	}

	public String getText() {
		final Date date = getDate();
		return date != null ? SimpleDateFormat.getInstance().format(date) : ""; //$NON-NLS-1$
	}

	public boolean isDirectWriting() {
		return true;
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && !isOutputOnly();
	}

	public boolean revalidate() {
		final Date date = getDate();
		final IStatus onUpdate = checkOnUpdateRules(date, new ValidationCallback(false));
		if (onUpdate.isOK()) {
			getValueBindingSupport().updateFromTarget();
		}
		return !isErrorMarked();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation note: since the underlying DateTime widget cannot be
	 * empty, a {@code null} date value will cause the widget to show the
	 * 'empty' date, but getDate() will correctly return null.
	 * <p>
	 * Invoking this method will copy the given date into the ridget and the
	 * widget regardless of the validation outcome. If the date does not pass
	 * validation the error marker will be set and the date will <b>not</b> be
	 * copied into the model. If validation passes the date will be copied into
	 * the model as well.
	 * <p>
	 * Because of limitations of the underlying SWT {@link DateTime} widget, the
	 * timestamp will be formatted according to the date/time format of the
	 * operating system. See <a href="http://bugs.eclipse.org/248075">Bug
	 * #248075</a>.
	 */
	public void setDate(final Date date) {
		if (isChanged(this.date, date)) {
			final Object oldDate = this.date;
			// date is mutable, store / send out copies to be safe
			this.date = date == null ? null : new Date(date.getTime());
			final Date newDate = date == null ? null : new Date(date.getTime());
			firePropertyChange(PROPERTY_DATE_INTERNAL, oldDate, newDate);
			firePropertyChange(IDateTimeRidget.PROPERTY_DATE, oldDate, newDate);
			if (controlBinding != null) {
				controlBinding.updateModelToTarget(); // update widget
			}
			final IStatus status = checkAllRules(date, new ValidationCallback(false));
			if (status.isOK()) {
				getValueBindingSupport().updateFromTarget();
			}
		}
	}

	/**
	 * This method is not API. Do not use in client code.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public final void setDateInternal(final Date date) {
		setDate(date);
	}

	/** Not supported. */
	public void setDirectWriting(final boolean directWriting) {
		throw new UnsupportedOperationException();
	}

	/** Not supported. */
	public void setInputToUIControlConverter(final IConverter converter) {
		throw new UnsupportedOperationException();
	}

	/** Not supported. */
	public void setText(final String text) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		if (controlBinding != null) {
			controlBinding.updateModelToTarget(); // updateWidget
		}
		checkAllRules(getDate(), new ValidationCallback(false));
	}

	// helping methods
	//////////////////

	/**
	 * Return {@code date} if non-null, otherwise return the 'empty' date value.
	 * 
	 * @return {@code date} or new Date instance
	 */
	private Date getNonNullDate(final Date date) {
		return date != null ? date : new Date(0);
	}

	private boolean isChanged(final Date date1, final Date date2) {
		if (date1 == date2) {
			return false;
		}
		return date1 != null ? !date1.equals(date2) : !date2.equals(date1);
	}

	private boolean isTimeControl(final DateTime control) {
		return (control.getStyle() & SWT.TIME) != 0;
	}

	// helping classes
	//////////////////

	/**
	 * DateAndTimeObservable that handles doSetValue(null) gracefully.
	 */
	private final class DateAndTimeObservableWithNullConversion extends DateAndTimeObservableValue {
		public DateAndTimeObservableWithNullConversion(final IObservableValue dateObservable,
				final IObservableValue timeObservable) {
			super(dateObservable, timeObservable);
		}

		@Override
		protected void doSetValue(final Object value) {
			super.doSetValue(getNonNullDate((Date) value));
		}
	}

	/**
	 * Validator that delegates to the 'on edit' validators for this ridget.
	 */
	private final class EditRulesValidator implements IValidator {
		public IStatus validate(final Object value) {
			return checkOnEditRules(value, new ValidationCallback(true));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.ITextRidget#setMultilineIgnoreEnterKey(boolean)
	 */
	@Override
	public void setMultilineIgnoreEnterKey(final boolean multilineIgnoreEnterKey) {
		// this setting has no effect on date text fields
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.ITextRidget#isMultilineIgnoreEnterKey()
	 */
	@Override
	public boolean isMultilineIgnoreEnterKey() {
		// this setting has no effect on date text fields
		return false;
	}

}
