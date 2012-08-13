package org.eclipse.riena.ui.javafx.utils;

import javafx.scene.Node;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.ui.javafx.Activator;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.osgi.service.log.LogService;

public class JavaFxBindingPropertyLocator implements IBindingPropertyLocator {

	private static JavaFxBindingPropertyLocator locator;

	private JavaFxBindingPropertyLocator() {
		// prevent instantiation
	}

	/**
	 * Returns an instance of this class.
	 * 
	 * @return
	 */
	public static JavaFxBindingPropertyLocator getInstance() {
		if (locator == null) {
			locator = new JavaFxBindingPropertyLocator();
		}
		return locator;
	}

	@Override
	public String locateBindingProperty(final Object uiControl) {
		String result = null;
		if (uiControl instanceof Node) {
			Node javaFxNode = (Node) uiControl;
			result = javaFxNode.getId();
		}
		if (result == null) {
			// TODO (?): IPropertyNameProvider
		}
		return result;
	}

	/**
	 * Returns true if the given UI control has a binding property, false
	 * otherwise.
	 * 
	 * @param uiControl
	 *            UI control; may be null
	 * @since 2.0
	 */
	public boolean hasBindingProperty(final Object uiControl) {
		final String prop = locateBindingProperty(uiControl);
		return !StringUtils.isDeepEmpty(prop);
	}

	/**
	 * Set the ID (binding property) for the given {@code uiControl}.
	 * 
	 * @param uiControl
	 *            an instanceof {@link Node}
	 *            <p>
	 *            (or {@link IPropertyNameProvider} )
	 * @param id
	 *            the binding property; never null; must not be empty. The given
	 *            value will also be assigned to the Ridget that is paired to
	 *            the control.
	 */
	public void setBindingProperty(final Object uiControl, final String id) {
		Assert.isNotNull(id, "The binding property must not be null"); //$NON-NLS-1$
		Assert.isLegal(id.length() > 0,
				"The binding property must not be empty"); //$NON-NLS-1$
		if (uiControl instanceof Node) {
			Node javaFxNode = (Node) uiControl;
			javaFxNode.setId(id);
			// TODO (?): IPropertyNameProvider
		} else {
			final Logger log = Log4r.getLogger(Activator.getDefault(),
					JavaFxBindingPropertyLocator.class);
			final String className = uiControl != null ? uiControl.getClass()
					.getName() : "null"; //$NON-NLS-1$
			final String msg = String
					.format("Failed to set binding property '%s' for %s", id, className); //$NON-NLS-1$
			log.log(LogService.LOG_WARNING, msg);
		}
	}
}
