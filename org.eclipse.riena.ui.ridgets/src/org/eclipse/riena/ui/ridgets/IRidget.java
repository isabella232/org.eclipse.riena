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
package org.eclipse.riena.ui.ridgets;

import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;

import org.eclipse.riena.ui.ridgets.listener.IFocusListener;

/**
 * A UI-toolkit independent Ridget (Riena widget) wrapping and extending an UI-control / widget. Offers an API to modify a UI-control that is not specific for
 * the UI-toolkit to which the UI-control belongs (e.g. SWT or Swing).
 */
public interface IRidget {

	/**
	 * The name of the PropertyChangeEvent that will be fired when the ridget's tooltip is changed ({@value} ).
	 */
	String PROPERTY_TOOLTIP = "tooltip"; //$NON-NLS-1$

	/**
	 * The name of the PropertyChangeEvent that will be fired when the enablement state of this ridget is changed ("enabled").
	 */
	String PROPERTY_ENABLED = "enabled"; //$NON-NLS-1$

	/**
	 * The name of the PropertyChangeEvent that is fired when the actual visibility of the Ridget changes while the Ridget is bound to a widget - either the
	 * visibility of the Ridget itself or the visibility of a parent of the associated widget.
	 * 
	 * @see IRidget#isVisible()
	 */
	String PROPERTY_SHOWING = "showing"; //$NON-NLS-1$

	/**
	 * Adds a {@link IFocusListener} for receiving focus events from this ridget.
	 * <p>
	 * Adding the same listener several times has to effect.
	 * 
	 * @param listener
	 *            the listener to be added (non-null)
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void addFocusListener(IFocusListener listener);

	/**
	 * Adds a PropertyChangeListener for all properties of the Ridget. Through PropertyChangeEvents the Ridget allows listening for property changes of the
	 * UI-control in a UI-toolkit independent way.
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be added (non-null)
	 * @throws RuntimeException
	 *             if propertyChangeListener is null
	 */
	void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Adds a PropertyChangeListener for a specific property of the Ridget. Through PropertyChangeEvents the Ridget allows listening for property changes of
	 * specific properties of the UI-control in a UI-toolkit independent way, e.g. the property "text" of a text field.
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @param propertyName
	 *            The name of the property to listen on (may be null to listen to all properties)
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be added (non null)
	 * @throws RuntimeException
	 *             if propertyChangeListener is null
	 */
	void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

	/**
	 * Returns the IController for this ridget.
	 * 
	 * @return an IController instance; never null.
	 * @since 3.0
	 */
	IRidgetContainer getController();

	/**
	 * Returns the ID of the ridget.
	 * 
	 * @return ID of this ridget
	 */
	String getID();

	/**
	 * Returns the text that is shown in the tool tip of this Ridget.
	 * 
	 * @return the text of tool tip for this ridget.
	 */
	String getToolTipText();

	/**
	 * Getter to access the UI-toolkit specific UI-control. Allows modifications of the UI-control that are not supported by the Ridgets API.
	 * 
	 * @return The bound UI-control or null, if no control is bound.
	 */
	Object getUIControl();

	/**
	 * Returns if this ridget is the focus owner.
	 * 
	 * @return <code>true</code> if this ridget is the focus owner; <code>false</code> otherwise
	 */
	boolean hasFocus();

	/**
	 * Returns whether the ridget is enabled or disabled.
	 * 
	 * @return Indicates whether the ridget is enabled.
	 */
	boolean isEnabled();

	/**
	 * Returns if the ridget can gain the focus or not.
	 * 
	 * @return true if the ridget can gain the focus; otherwise false
	 */
	boolean isFocusable();

	/**
	 * Returns whether the ridget is visible or invisible.
	 * 
	 * @return Indicates whether the ridget is visible.
	 */
	boolean isVisible();

	/**
	 * Removes the specified focus listener so that it no longer receives focus events from this ridget.
	 * 
	 * @param listener
	 *            the focus listener to be removed
	 * @throws RuntimeException
	 *             if listener is null
	 */
	void removeFocusListener(IFocusListener listener);

	/**
	 * Removes a PropertyChangeListener for all properties of the Ridget.
	 * 
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be removed (non null)
	 * @throws RuntimeException
	 *             if propertyChangeListener is null
	 */
	void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);

	/**
	 * Removes a PropertyChangeListener for a specific property of the Ridget.
	 * 
	 * @param propertyName
	 *            The name of the property to listen on (may be null to remove the listener from all properties)
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be removed (non null)
	 * @throws RuntimeException
	 *             if propertyChangeListener is null
	 */
	void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener);

	/**
	 * Requests that this ridget get the input focus.<br>
	 * Precondition is, that the ridget, which should receive the focus is visible.
	 */
	void requestFocus();

	/**
	 * Set the controller instance holding this ridget.
	 * 
	 * @param controller
	 *            a {@link IRidgetContainer} instance.
	 * @since 3.0
	 */
	void setController(IRidgetContainer controller);

	/**
	 * Sets whether the ridget is enabled.
	 * 
	 * @param enabled
	 *            The new enabled state.
	 */
	void setEnabled(boolean enabled);

	/**
	 * Sets if the ridget can gain the focus or not.
	 * 
	 * @param focusable
	 *            true if the ridgetS can gain the focus; otherwise false
	 */
	void setFocusable(boolean focusable);

	/**
	 * Sets the text that is shown in the tool tip of this Ridget.
	 * 
	 * @param toolTipText
	 *            The text of tool tip to set. May be {@code null} to turn off the tool tip.
	 */
	void setToolTipText(String toolTipText);

	/**
	 * Setter to be internally used by the view to bind and unbind the Ridgets.
	 * 
	 * @param uiControl
	 *            The wrapped UI-control to which the Ridget is bound or null to unbind the Ridget.
	 */
	void setUIControl(Object uiControl);

	/**
	 * Sets whether the ridget is visible.
	 * 
	 * @param visible
	 *            The new visibility state.
	 */
	void setVisible(boolean visible);

	/**
	 * For value based ridgets triggers an update from the model value to the Ridget value when a default binding is used.
	 * 
	 * <p>
	 * For an example binding IValueRidget see:
	 * </p>
	 * <ul>
	 * <li>{@link #bindToModel(IObservableValue)}</li>
	 * <li>{@link #bindToModel(Object, String)}</li>
	 * </ul>
	 * 
	 * <p>
	 * For an example binding ITableRidget see:
	 * </p>
	 * <ul>
	 * <li>{@link #bindToModel(IObservableList, Class, String[], String[])}</li>
	 * <li>{@link #bindToModel(Object, String, Class, String[], String[])}</li>
	 * </ul>
	 */
	void updateFromModel();

	/**
	 * Sets whether the method {@code updateAllRidgetsFromModel()} of the class {@code SubModuleController} should ignore binding errors. If the method should
	 * ignore the binding errors, the binding exceptions will be caught in the method {@code updateAllRidgetsFromModel()} and logged.
	 * 
	 * @param {@code true} if the binding error should be ignored; {@code false} if the binding errors should be forwarded.
	 * 
	 * @since 4.0
	 */
	void setIgnoreBindingError(boolean ignore);

	/**
	 * Returns whether the method {@code updateAllRidgetsFromModel()} of the class {@code SubModuleController} should ignore binding errors. If the method
	 * should ignore the binding errors, the binding exceptions will be caught in the method {@code updateAllRidgetsFromModel()} and logged.
	 * 
	 * @return {@code true} if the binding error should be ignored; {@code false} if the binding errors should be forwarded.
	 * 
	 * @since 4.0
	 */
	boolean isIgnoreBindingError();

	/**
	 * Adds a menu item to the ridget.
	 * 
	 * @param menuItemText
	 *            The text of the menu item to be added.
	 * @return the menu item rigdet.
	 * @since 5.0
	 * 
	 */
	IMenuItemRidget addMenuItem(String menuItemText);

	/**
	 * Adds a menu item to the ridget.
	 * 
	 * @param menuItemText
	 *            The text of the menu item to be added.
	 * @param iconName
	 *            The name of the icon for the menu item to be added.
	 * @return the menu item rigdet.
	 * @since 5.0
	 * 
	 */
	IMenuItemRidget addMenuItem(String menuItemText, String iconName);

	/**
	 * Removes a specific menu item from the contextmenu of the ridget.
	 * 
	 * @param menuItemText
	 *            The text of the menu item to be removed.
	 * @since 5.0
	 */
	void removeMenuItem(String menuItemText);

	/**
	 * Removes a specific menu item from the contextmenu of the ridget.
	 * 
	 * @param menuItemRidget
	 *            The menu item to be removed.
	 * @since 5.0
	 */
	void removeMenuItem(IMenuItemRidget menuItemRidget);

	/**
	 * Returns a menu item was added before.
	 * 
	 * @return Menu item of the index.
	 * @since 5.0
	 */
	IMenuItemRidget getMenuItem(int index);

	/**
	 * Returns count of menu items.
	 * 
	 * @return Count of the menu items.
	 * @since 5.0
	 */
	int getMenuItemCount();
}
