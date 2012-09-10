package org.eclipse.riena.ui.ridgets.javafx;

import java.beans.PropertyChangeSupport;
import java.util.Iterator;

import javafx.scene.control.Button;
import javafx.scene.control.Control;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractActionRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;

public class JavaFxMarkerSupport extends JavaFxBasicMarkerSupport {

	private final static String CSS_MANADORY = "mandatory";
	private final static String CSS_OUTPUT_ONLY = "output-only";
	private final static String CSS_MANADORY_OUTPUT_ONLY = "mandatory-output-only";
	private final static String CSS_ERROR = "error";

	public JavaFxMarkerSupport() {
		super();
	}

	public JavaFxMarkerSupport(final IBasicMarkableRidget ridget,
			final PropertyChangeSupport propertyChangeSupport) {
		super(ridget, propertyChangeSupport);
	}

	private void updateOutput(final Control control) {
		if (getRidget().isOutputOnly() && getRidget().isEnabled()) {
			clearMandatory(control);
			clearOutput(control);
			addOutput(control);
		} else {
			clearOutput(control);
		}
	}

	private void addOutput(final Control control) {
		if (isMandatory(getRidget()) && !isHidden(MandatoryMarker.class)) {
			addCssClass(control, CSS_MANADORY_OUTPUT_ONLY);
		} else {
			addCssClass(control, CSS_OUTPUT_ONLY);
		}
		control.setFocusTraversable(false);
	}

	private void updateMandatory(final Control control) {
		if (isMandatory(getRidget()) && !getRidget().isOutputOnly()
				&& getRidget().isEnabled() && !isHidden(MandatoryMarker.class)) {
			addMandatory(control);
		} else {
			clearMandatory(control);
		}
	}

	private void updateError(final Control control) {
		if (getRidget().isErrorMarked() && getRidget().isEnabled()
				&& getRidget().isVisible() && !isHidden(ErrorMarker.class)) {
			if (!(isButton(control) && getRidget().isOutputOnly())) {
				addError(control);
			} else {
				clearError(control);
			}
		} else {
			clearError(control);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link MarkerSupport#getRidget()}
	 */
	@Override
	protected IMarkableRidget getRidget() {
		return (IMarkableRidget) super.getRidget();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Copy of {@link MarkerSupport#updateUIControl()}
	 */
	@Override
	protected void updateUIControl(final Control control) {
		super.updateUIControl(control);
		updateOutput(control);
		updateMandatory(control);
		updateError(control);
		// updateNegative(control);
	}

	/**
	 * Copy of {@link MarkerSupport#isHidden()}
	 */
	private boolean isHidden(final Class<? extends IMarker> type) {
		for (final Class<IMarker> marker : getHiddenMarkerTypes()) {
			if (marker.isAssignableFrom(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Copy of {@link MarkerSupport#isMandatory()}
	 */
	private boolean isMandatory(final IMarkableRidget ridget) {
		boolean result = false;
		final Iterator<MandatoryMarker> iter = getRidget().getMarkersOfType(
				MandatoryMarker.class).iterator();
		while (!result && iter.hasNext()) {
			result = !iter.next().isDisabled();
		}
		return result;
	}

	protected void clearMandatory(final Control control) {
		control.getStyleClass().remove(CSS_MANADORY);
		control.getStyleClass().remove(CSS_MANADORY_OUTPUT_ONLY);
	}

	protected void clearOutput(final Control control) {
		control.getStyleClass().remove(CSS_OUTPUT_ONLY);
		control.getStyleClass().remove(CSS_MANADORY_OUTPUT_ONLY);
		control.setFocusTraversable(getRidget().isFocusable());
	}

	protected void addCssClass(final Control control, String cssClass) {
		control.getStyleClass().add(cssClass);
	}

	protected void addMandatory(final Control control) {
		control.getStyleClass().add(CSS_MANADORY);
	}

	protected void addError(final Control control) {
		control.getStyleClass().add(CSS_ERROR);
	}

	protected void clearError(final Control control) {
		control.getStyleClass().remove(CSS_ERROR);
	}

	private boolean isButton(final Control control) {
		return control instanceof Button
				|| getRidget() instanceof AbstractActionRidget;
	}

}
