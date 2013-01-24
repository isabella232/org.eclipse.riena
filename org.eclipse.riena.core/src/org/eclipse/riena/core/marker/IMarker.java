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
package org.eclipse.riena.core.marker;

import java.util.Map;

/**
 * A <code>IMarker</code> is used to mark a ridget, resp. its associated UI
 * control. Implementations supported by Riena are {@link MandatoryMarker
 * MandatoryMarker}{@link ErrorMarker ErrorMarker}.
 */
public interface IMarker {

	/**
	 * The priority of a marker is used to guarantee that the marker with the
	 * highest priority is visible even if (e.g.) a node has more than one
	 * marker.
	 * 
	 * @since 1.2
	 */
	enum Priority {
		VERY_LOW, LOW, NORMAL, HIGH, VERY_HIGH;
	}

	/**
	 * Returns the attribute with the given <code>name</code>, or
	 * <code>null</code> if it does not exist.
	 * 
	 * @param name
	 *            the name of attribute.
	 * 
	 * @return the attribute with the given <code>name</code>.
	 * 
	 * @pre name != null
	 */
	Object getAttribute(String name);

	/**
	 * Returns the attribute with the given <code>name</code>, or the
	 * <code>defaultValue</code> if it does not exist.
	 * 
	 * @param name
	 *            the name of attribute.
	 * @param defaultValue
	 *            the value that will be returned not attribute with the given
	 *            name was found.
	 * 
	 * @return the attribute with the given <code>name</code>.
	 * 
	 * @pre name != null
	 */
	Object getAttribute(String name, Object defaultValue);

	/**
	 * @return a Map mapping the attributes' names to their values.
	 */
	Map<String, ?> getAttributes();

	/**
	 * Sets the <code>value</code> of the attribute with the given
	 * <code>name</code>.
	 * 
	 * @param name
	 *            the name of the attribute.
	 * @param value
	 *            the value of the attribute.
	 * 
	 * @pre name != null
	 */
	void setAttribute(String name, Object value);

	/**
	 * Adds an object listening for changes of the markers attributes.
	 * 
	 * @param listener
	 *            The new listener.
	 */
	void addAttributeChangeListener(IMarkerAttributeChangeListener listener);

	/**
	 * Removes an object listening for changes of the markers attributes.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	void removeAttributeChangeListener(IMarkerAttributeChangeListener listener);

	/**
	 * Returns {@code true} if marker is created by a filter, {@code false}
	 * otherwise. If marker is unique, only one unique marker of this marker
	 * type is allowed.
	 * 
	 * @return {@code true} if marker is created by a filter; otherwise
	 *         {@code false}
	 */
	boolean isUnique();

	/**
	 * Returns the priority of this marker.
	 * 
	 * @return priority of marker
	 * 
	 * @since 1.2
	 */
	Priority getPriority();

}
