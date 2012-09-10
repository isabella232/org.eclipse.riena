package org.eclipse.riena.ui.ridgets.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.eclipse.core.runtime.Assert;

public class JavaFxFocusManager implements ChangeListener<Boolean> {

	private final AbstractJavaFxRidget ridget;

	/**
	 * Create a new instance.
	 * 
	 * @param ridget
	 *            a ridget instance; never null.
	 */
	JavaFxFocusManager(final AbstractJavaFxRidget ridget) {
		Assert.isNotNull(ridget);
		this.ridget = ridget;
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> observable,
			Boolean oldValue, Boolean focused) {
		if (oldValue != focused) {
			if (focused) {
				focusGained();
			} else {
				focusLost();
			}
		}
	}

	private void focusGained() {
		if (isFocusable()) {
			ridget.fireFocusGained();
		}
	}

	private void focusLost() {
		if (isFocusable()) {
			ridget.fireFocusLost();
		}
	}

	private boolean isFocusable() {
		return isFocusable(ridget);
	}

	private boolean isFocusable(final AbstractJavaFxRidget ridget) {
		Assert.isNotNull(ridget);
		return (ridget.isFocusable() && !ridget.isOutputOnly());
	}

}
