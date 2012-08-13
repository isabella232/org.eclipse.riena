package org.eclipse.riena.ui.javafx.utils;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.common.IComplexComponent;

public abstract class JavaFxControlFinder {

	private Pane start;

	public JavaFxControlFinder(final Pane pane) {
		Assert.isNotNull(pane);
		start = pane;
	}

	/**
	 * Visits all controls, starting with the {@code pane} designated in the
	 * constructor and recursively descending into each of it's children.
	 * <p>
	 * This method can only be called once.
	 * 
	 * @throws RuntimeException
	 *             if called more than once
	 */
	public void run() {
		if (start == null) {
			throw new IllegalStateException("cannot run more than once!"); //$NON-NLS-1$
		}
		addUIControls(start);
		start = null;
	}

	/**
	 * Returns true to skip calling {@link #handleBoundControl(Control, String)}
	 * for the given {@control}, false otherwise.
	 * <p>
	 * The default implementation always returns false. Subclasses may override.
	 * 
	 * @param node
	 *            the JavaFX node; never null
	 */
	public boolean skip(final Node node) {
		return false;
	}

	/**
	 * This method is invoked for every visited control.
	 * <p>
	 * Implementors may override, but <b>must</b> call super.
	 * 
	 * @param node
	 *            the JavaFX node; never null
	 */
	public void handleControl(final Node node) {
		if ((node instanceof Pane) && !(node instanceof IComplexComponent)) {
			addUIControls((Pane) node);
		}
	}

	/**
	 * This method is invoked for every bound control that is not skipped (see
	 * {@link #skip(Control)}).
	 * <p>
	 * Bound controls are those controls that have been assigned with a binding
	 * ID.
	 * 
	 * @param node
	 *            the JavaFX node; never null
	 * @param bindingProperty
	 *            the binding ID for that control (a non-null, non-empty String)
	 */
	public abstract void handleBoundControl(Node node, String bindingProperty);

	// protected methods
	// //////////////////

	private void addUIControls(final Pane pane) {
		final JavaFxBindingPropertyLocator locator = JavaFxBindingPropertyLocator
				.getInstance();
		for (final Node control : pane.getChildren()) {
			final String bindingProperty = locator
					.locateBindingProperty(control);
			if (StringUtils.isGiven(bindingProperty) && !skip(control)) {
				handleBoundControl(control, bindingProperty);
			}
			handleControl(control);
		}
	}

}
