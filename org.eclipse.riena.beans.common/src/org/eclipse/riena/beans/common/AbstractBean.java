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
package org.eclipse.riena.beans.common;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;

/**
 * Base class for beans
 */
public abstract class AbstractBean {
	protected Set<PropertyChangeListener> propertyChangeListeners;

	/**
	 * constructor.
	 */
	public AbstractBean() {
		propertyChangeListeners = new HashSet<PropertyChangeListener>();
	}

	/**
	 * Adds a PropertyChangeListener for all properties.
	 * <p>
	 * Adding the same listener several times has no effect.
	 * 
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be added (non-null)
	 * @throws RuntimeException
	 *             if propertyChangeListener is null
	 */
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		Assert.isNotNull(listener);
		propertyChangeListeners.add(listener);
	}

	/**
	 * Removes a PropertyChangeListener from this class.
	 * 
	 * @param propertyChangeListener
	 *            The PropertyChangeListener to be removed (non null)
	 * @throws RuntimeException
	 *             if propertyChangeListener is null
	 */
	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		Assert.isNotNull(listener);
		propertyChangeListeners.remove(listener);
	}

	// protected methods
	////////////////////

	protected void removeAllPropertyChangeListeners() {
		propertyChangeListeners.clear();
	}

	protected void firePropertyChanged(final String aProperty, final Object oldValue, final Object newValue) {
		firePropertyChanged(new PropertyChangeEvent(this, aProperty, oldValue, newValue));
	}

	protected void firePropertyChanged(final PropertyChangeEvent event) {
		// prepare for comodification
		final List<PropertyChangeListener> tmpListeners = new ArrayList<PropertyChangeListener>(propertyChangeListeners);
		for (final PropertyChangeListener listener : tmpListeners) {
			listener.propertyChange(event);
		}

	}

	/**
	 * @return Anwers <code>true</code> if a listener exists otherwise
	 *         <code>false</code>
	 */
	protected boolean hasListener() {
		return (propertyChangeListeners.size() > 0);
	}
}
