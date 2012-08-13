package org.eclipse.riena.ui.ridgets.javafx.swtjavafx.uibinding;

import javafx.scene.Node;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.javafx.utils.JavaFxBindingPropertyLocator;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

public class SwtJavaFxBindingPropertyLocator implements IBindingPropertyLocator {

	private static SwtJavaFxBindingPropertyLocator locator;
	private final SWTBindingPropertyLocator swtLocator;
	private final JavaFxBindingPropertyLocator javaFxLocator;

	private SwtJavaFxBindingPropertyLocator() {
		// prevent instantiation
		swtLocator = SWTBindingPropertyLocator.getInstance();
		javaFxLocator = JavaFxBindingPropertyLocator.getInstance();
	}

	/**
	 * Returns an instance of this class.
	 * 
	 * @return
	 */
	public static SwtJavaFxBindingPropertyLocator getInstance() {
		if (locator == null) {
			locator = new SwtJavaFxBindingPropertyLocator();
		}
		return locator;
	}

	@Override
	public String locateBindingProperty(Object uiControl) {
		if (uiControl instanceof Node) {
			return javaFxLocator.locateBindingProperty(uiControl);
		} else {
			return swtLocator.locateBindingProperty(uiControl);
		}
	}

	public void setBindingProperty(final Object uiControl, final String id) {
		if (uiControl instanceof Node) {
			javaFxLocator.setBindingProperty(uiControl, id);
		} else {
			swtLocator.setBindingProperty(uiControl, id);
		}
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

}
