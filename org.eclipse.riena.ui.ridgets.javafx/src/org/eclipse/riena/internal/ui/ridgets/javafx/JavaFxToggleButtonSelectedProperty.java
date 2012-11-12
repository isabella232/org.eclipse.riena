package org.eclipse.riena.internal.ui.ridgets.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;

import org.eclipse.core.databinding.property.INativePropertyListener;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.SimplePropertyEvent;
import org.eclipse.core.databinding.property.value.SimpleValueProperty;
import org.eclipse.core.runtime.Assert;

/**
 * This class overrides the basic implementations of the abstract class SimpleValueProperty. 
 * It is intended to maintain the property of a ToggleButton object, that indicates the selection status. 
 */

public class JavaFxToggleButtonSelectedProperty extends SimpleValueProperty {

	/**
	 * Returns the type {@link Boolean} because that is the type of the selection
	 * property of the {@link ToggleButton}.
	 * 
	 * @return Returns always the type {@link Boolean}.
	 * 
	 */
	@Override
	public Class<Boolean> getValueType() {
		return Boolean.TYPE;
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Source has to be an instance of {@link ToggleButton}.
	 * 
	 * @return Returns a boolean value that indicates if the source object is 
	 * selected.
	 */
	@Override
	protected Boolean doGetValue(Object source) {
		Assert.isLegal(source instanceof ToggleButton);
		return ((ToggleButton) source).isSelected();
	}

	
	/** 
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Source has to be an instance of {@link ToggleButton}.
	 *
	 **/
	@Override
	protected void doSetValue(Object source, Object value) {
		Assert.isLegal(source instanceof ToggleButton);
		if (value == null) {
			value = Boolean.FALSE;
		}
		boolean selected = ((Boolean) value).booleanValue();
		((ToggleButton) source).setSelected(selected);
	}

	@Override
	public INativePropertyListener adaptListener(
			ISimplePropertyListener listener) {
		return new ToggleButtonListener(this, listener);
	}

	private class ToggleButtonListener implements INativePropertyListener,
			ChangeListener<Boolean> {

		private final IProperty property;
		private final ISimplePropertyListener listener;

		/**
		 * @param property
		 * @param listener
		 */
		public ToggleButtonListener(IProperty property,
				ISimplePropertyListener listener) {
			this.property = property;
			this.listener = listener;
		}

		/** 
		 * {@inheritDoc}
		 *
		 * <p>
		 * Source has to be an instance of {@link ToggleButton}.
		 */
		@Override
		public void addTo(Object source) {
			Assert.isLegal(source instanceof ToggleButton);
			((ToggleButton) source).selectedProperty().addListener(this);
		}

		/** 
		 * {@inheritDoc}
		 *
		 * <p>
		 * Source has to be an instance of {@link ToggleButton}.
		 **/
		@Override
		public void removeFrom(Object source) {
			Assert.isLegal(source instanceof ToggleButton);
			((ToggleButton) source).selectedProperty().removeListener(this);
		}

		/** 
		 * <p>
		 * Informs the listeners about property-changes. 
		 * 
		 */
		@Override
		public void changed(ObservableValue<? extends Boolean> observable,
				Boolean oldValue, Boolean newValue) {
			listener.handleEvent(new SimplePropertyEvent(
					SimplePropertyEvent.CHANGE, new Object(), property, null));
		}

	}

}
