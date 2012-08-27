package org.eclipse.riena.ui.ridgets.javafx;

import java.beans.PropertyChangeSupport;

import javafx.scene.control.Control;

import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;

public class JavaFxBasicMarkerSupport extends AbstractMarkerSupport {

	private boolean initialEnabled = true;
	private boolean initialVisible;

	public JavaFxBasicMarkerSupport() {
		super();
		initialVisible = true;
	}

	public JavaFxBasicMarkerSupport(final IBasicMarkableRidget ridget,
			final PropertyChangeSupport propertyChangeSupport) {
		super(ridget, propertyChangeSupport);
		initialVisible = true;
	}

	@Override
	public void updateMarkers() {
		updateUIControl();
	}

	/**
	 * Does nothing. Subclasses may override.
	 * 
	 * @param control
	 *            the control
	 */
	protected void clearAllMarkers(final Control control) {
		clearVisible(control);
	}

	private void clearVisible(final Control control) {
		control.setVisible(initialVisible);
	}

	@Override
	public void bind() {
		if (getUIControl() != null) {
			// save initial state of control
			saveState();
		}
	}

	@Override
	public void unbind() {
		if (getUIControl() != null) {
			// restore initial state of control
			clearAllMarkers(getUIControl());
		}
	}

	protected void saveState() {
		this.initialEnabled = !getUIControl().isDisable();
		this.initialVisible = getUIControl().isVisible();
	}

	public boolean isInitialEnabled() {
		return initialEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Control getUIControl() {
		return (Control) super.getUIControl();
	}

	private void updateUIControl() {
		final Control control = getUIControl();
		if (control != null) {
			updateUIControl(control);
		}
	}

	@Override
	protected void handleMarkerAttributesChanged() {
		updateUIControl();
		super.handleMarkerAttributesChanged();
	}

	protected void updateUIControl(final Control control) {
		updateVisible(control);
		updateDisabled(control);
	}

	protected void updateVisible(final Control control) {
		control.setVisible(!hasHiddenMarkers());
	}

	protected void updateDisabled(final Control control) {
		control.setDisable(!getRidget().isEnabled());
	}

}
