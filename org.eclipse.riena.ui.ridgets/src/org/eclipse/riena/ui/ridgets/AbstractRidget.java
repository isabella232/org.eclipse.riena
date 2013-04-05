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
import java.beans.PropertyChangeSupport;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.ui.ridgets.listener.FocusEvent;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;

/**
 * Superclass for ridgets with property change support.
 */
public abstract class AbstractRidget implements IRidget {

	/**
	 * @since 3.0
	 */
	public static final String COMMAND_UPDATE = "update"; //$NON-NLS-1$

	public final static String PROPERTY_RIDGET = "ridget"; //$NON-NLS-1$

	protected PropertyChangeSupport propertyChangeSupport;
	protected boolean savedVisibleState = true;

	private final ListenerList<IFocusListener> focusListeners;
	private IRidgetContainer controller;
	private boolean ignoreBindingError;
	private boolean retryRequestFocus;

	/**
	 * Constructor.
	 */
	public AbstractRidget() {
		propertyChangeSupport = new PropertyChangeSupport(this);
		focusListeners = new ListenerList<IFocusListener>(IFocusListener.class);
	}

	/**
	 * Checks that the given uiControl is assignable to the the given type.
	 * 
	 * @param uiControl
	 *            a uiControl, may be null
	 * @param type
	 *            a class instance (non-null)
	 * @throws BindingException
	 *             if the uiControl is not of the given type
	 * @since 4.0
	 */
	protected void checkType(final Object uiControl, final Class<?> type) {
		if ((uiControl != null) && !(type.isAssignableFrom(uiControl.getClass()))) {
			final String expectedClassName = type.getSimpleName();
			final String controlClassName = uiControl.getClass().getSimpleName();
			throw new BindingException("uiControl of  must be a " + expectedClassName + " but was a " //$NON-NLS-1$ //$NON-NLS-2$
					+ controlClassName);
		}
	}

	public void addFocusListener(final IFocusListener listener) {
		focusListeners.add(listener);
	}

	public void addPropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
		addPropertyChangeListener(null, propertyChangeListener);
	}

	public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener propertyChangeListener) {
		Assert.isNotNull(propertyChangeListener);
		if (!hasListener(propertyName, propertyChangeListener)) {
			if (propertyName == null) {
				propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
			} else {
				propertyChangeSupport.addPropertyChangeListener(propertyName, propertyChangeListener);
			}
		}
	}

	/**
	 * Notifies all listeners that the ridget has gained the focus.
	 * 
	 * @since 3.0
	 */
	public final void fireFocusGained() {
		final FocusEvent event = new FocusEvent(null, this);
		for (final IFocusListener focusListener : focusListeners.getListeners()) {
			focusListener.focusGained(event);
		}
	}

	/**
	 * Notifies all listeners that the ridget has lost the focus.
	 * 
	 * @since 3.0
	 */
	public final void fireFocusLost() {
		final FocusEvent event = new FocusEvent(this, null);
		for (final IFocusListener focusListener : focusListeners.getListeners()) {
			focusListener.focusLost(event);
		}
	}

	/**
	 * @since 3.0
	 */
	public IRidgetContainer getController() {
		return controller;
	}

	public void removeFocusListener(final IFocusListener listener) {
		focusListeners.remove(listener);
	}

	public void removePropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
		removePropertyChangeListener(null, propertyChangeListener);
	}

	public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener propertyChangeListener) {
		Assert.isNotNull(propertyChangeListener);
		if (propertyName == null) {
			propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
		} else {
			propertyChangeSupport.removePropertyChangeListener(propertyName, propertyChangeListener);
		}
	}

	/**
	 * @since 3.0
	 */
	public void setController(final IRidgetContainer controller) {
		Assert.isNotNull(controller);
		this.controller = controller;
	}

	public void updateFromModel() {
		// Do nothing by default
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 3.0
	 */
	public void forceMarkerUpdate() {
		firePropertyChange(COMMAND_UPDATE, false, true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	public void setIgnoreBindingError(final boolean ignore) {
		ignoreBindingError = ignore;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 4.0
	 */
	public boolean isIgnoreBindingError() {
		return ignoreBindingError;
	}

	// protected methods
	////////////////////

	/**
	 * Notifies all listeners that the ridget has gained the focus.
	 * 
	 * @param event
	 *            the FocusEvent
	 * @deprecated use {@link #fireFocusGained()}
	 */
	@Deprecated
	protected final void fireFocusGained(final FocusEvent event) {
		for (final IFocusListener focusListener : focusListeners.getListeners()) {
			focusListener.focusGained(event);
		}
	}

	/**
	 * Notifies all listeners that the ridget has lost the focus.
	 * 
	 * @param event
	 *            the FocusEvent
	 * @deprecated use {@link #fireFocusLost()}
	 */
	@Deprecated
	protected final void fireFocusLost(final FocusEvent event) {
		for (final IFocusListener focusListener : focusListeners.getListeners()) {
			focusListener.focusLost(event);
		}
	}

	/**
	 * Notifies all listeners about a changed property. No event is fired if old and new are equal and non-null.
	 * 
	 * @param propertyName
	 *            The name of the property that was changed.
	 * @param oldValue
	 *            The old value of the property.
	 * @param newValue
	 *            The new value of the property.
	 */
	protected final void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Notifies all listeners about a changed property. No event is fired if old and new are equal and non-null.
	 * 
	 * @param propertyName
	 *            The name of the property that was changed.
	 * @param oldValue
	 *            The old value of the property.
	 * @param newValue
	 *            The new value of the property.
	 */
	protected final void firePropertyChange(final String propertyName, final int oldValue, final int newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	/**
	 * Notifies all listeners about a changed property. No event is fired if old and new are equal and non-null.
	 * 
	 * @param propertyName
	 *            The name of the property that was changed.
	 * @param oldValue
	 *            The old value of the property.
	 * @param newValue
	 *            The new value of the property.
	 */
	protected final void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	// helping methods
	//////////////////

	private boolean hasListener(final String propertyName, final Object listener) {
		boolean result = false;
		final PropertyChangeListener[] listeners = propertyChangeSupport.getPropertyChangeListeners(propertyName);
		for (int i = 0; !result && i < listeners.length; i++) {
			result = (listeners[i] == listener);
		}
		return result;
	}

	/**
	 * Returns <code>true</code> if retryFocusRequest has been set otherwise <code>false</code>
	 * 
	 * @return the retryRequestFocus flag
	 * @since 4.0
	 */
	public boolean isRetryRequestFocus() {
		return retryRequestFocus;
	}

	/**
	 * Marks this ridget, that a call to requestFocus() failed, because e.g. the parent is disabled. Afterwards this flag can be checked to restore the focus.
	 * 
	 * @since 4.0
	 */
	public void setRetryRequestFocus(final boolean retryRequestFocus) {
		this.retryRequestFocus = retryRequestFocus;
	}

	/**
	 * @since 5.0
	 */
	public IMenuItemRidget addMenuItem(final String itemText) {
		throw new UnsupportedOperationException("Context menu is unsupported for " + this.toString()); //$NON-NLS-1$
	}

	/**
	 * @since 5.0
	 */
	public IMenuItemRidget addMenuItem(final String itemText, final String iconName) {
		throw new UnsupportedOperationException("Context menu is unsupported for " + this.toString()); //$NON-NLS-1$
	}

	/**
	 * @since 5.0
	 */
	public void removeMenuItem(final String menuItemText) {
		throw new UnsupportedOperationException("Context menu is unsupported for " + this.toString()); //$NON-NLS-1$
	}

	/**
	 * @since 5.0
	 */
	public void removeMenuItem(final IMenuItemRidget menuItemRidget) {
		throw new UnsupportedOperationException("Context menu is unsupported for " + this.toString()); //$NON-NLS-1$
	}

	/**
	 * @since 5.0
	 */
	public IMenuItemRidget getMenuItem(final int index) {
		throw new UnsupportedOperationException("Context menu is unsupported for " + this.toString()); //$NON-NLS-1$
	}

	/**
	 * @since 5.0
	 */
	public int getMenuItemCount() {
		throw new UnsupportedOperationException("Context menu is unsupported for " + this.toString()); //$NON-NLS-1$
	}
}
