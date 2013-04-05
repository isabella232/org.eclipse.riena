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

import java.util.Collection;

/**
 * An interface to combine all markable objects, e.g. Ridget and Controller.
 * This interfaces describes the minimal capabilities of markable objects
 */

public interface IMarkable {

	/**
	 * Adds a marker to the markable object.
	 * 
	 * @param marker
	 *            the marker to add.
	 */
	void addMarker(IMarker marker);

	/**
	 * Removes a marker from the markable object.
	 * 
	 * @param marker
	 *            the marker to remove.
	 * @return true, if marker is removed, else false.
	 * 
	 * @since 3.0
	 */
	boolean removeMarker(IMarker marker);

	/**
	 * Removes all marker from the markable object.
	 */
	void removeAllMarkers();

	/**
	 * Returns all markers corresponding to a certain type.
	 * 
	 * @param type
	 *            the type of markers.
	 * @return markers corresponding to a certain type.
	 */
	<T extends IMarker> Collection<T> getMarkersOfType(Class<T> type);

	/**
	 * Returns the markers of the markable object
	 * 
	 * @return The markers of the markable object.
	 */
	Collection<? extends IMarker> getMarkers();
}
