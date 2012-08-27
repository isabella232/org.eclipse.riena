package org.eclipse.riena.ui.ridgets.javafx;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.ui.ridgets.IValueRidget;
import org.eclipse.riena.ui.ridgets.ValueBindingSupport;

public abstract class AbstractJavaFxValueRidget extends AbstractJavaFxRidget
		implements IValueRidget {

	private final ValueBindingSupport valueBindingSupport;

	public AbstractJavaFxValueRidget() {
		valueBindingSupport = new ValueBindingSupport(
				this.getRidgetObservable());
		valueBindingSupport.setMarkable(this);
	}

	/**
	 * @return The observable value of the Ridget.
	 */
	protected abstract IObservableValue getRidgetObservable();

	@Override
	public void bindToModel(final IObservableValue observableValue) {
		valueBindingSupport.bindToModel(observableValue);
	}

	@Override
	public void bindToModel(final Object valueHolder,
			final String valuePropertyName) {
		valueBindingSupport.bindToModel(valueHolder, valuePropertyName);
	}

	@Override
	public IConverter getModelToUIControlConverter() {
		return valueBindingSupport.getModelToUIControlConverter();
	}

	@Override
	public void setModelToUIControlConverter(final IConverter converter) {
		valueBindingSupport.setModelToUIControlConverter(converter);
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		valueBindingSupport.updateFromModel();
	}

	/**
	 * @since 4.0
	 */
	public final ValueBindingSupport getValueBindingSupport() {
		return valueBindingSupport;
	}

	/**
	 * @since 4.0
	 */
	@Override
	public String getValuePropertyName() {
		return valueBindingSupport.getValuePropertyName();
	}

}
