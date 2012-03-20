/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.marker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * Standard implementation for the {@link IMarkable} interface, can be used by
 * other objects to delegate
 */
public class Markable implements IMarkable {

	private final List<IMarker> markers;

	/**
	 * Create new Instance and initialize
	 */
	public Markable() {
		super();
		markers = new ArrayList<IMarker>();
	}

	/**
	 * @see org.eclipse.riena.core.marker.IMarkable#addMarker(org.eclipse.riena.core.marker.IMarker)
	 */
	public void addMarker(final IMarker marker) {

		Assert.isNotNull(marker);

		if (markers.contains(marker)) {
			return;
		}

		if (marker.isUnique()) {
			final Collection<? extends IMarker> markersOfType = getMarkersOfType(marker.getClass());
			boolean unique = false;
			for (final IMarker m : markersOfType) {
				if (m.isUnique()) {
					unique = true;
					break;
				}
			}
			if (!unique) {
				markers.add(marker);
			}
		} else {
			markers.add(marker);
		}
	}

	/**
	 * @see org.eclipse.riena.core.marker.IMarkable#getMarkers()
	 */
	public Collection<? extends IMarker> getMarkers() {
		return markers;
	}

	/**
	 * @see org.eclipse.riena.core.marker.IMarkable#getMarkersOfType(java.lang.Class)
	 */
	public <T extends IMarker> Collection<T> getMarkersOfType(final Class<T> type) {
		return getMarkersOfType(getMarkers(), type);
	}

	/**
	 * @see org.eclipse.riena.core.marker.IMarkable#removeAllMarkers()
	 */
	public void removeAllMarkers() {
		markers.clear();
	}

	/**
	 * @see org.eclipse.riena.core.marker.IMarkable#removeMarker(org.eclipse.riena.core.marker.IMarker)
	 * @since 3.0
	 */
	public boolean removeMarker(final IMarker marker) {
		return markers.remove(marker);
	}

	@SuppressWarnings("unchecked")
	public static <T extends IMarker> Collection<T> getMarkersOfType(final Collection<? extends IMarker> markerSet,
			final Class<T> type) {

		if ((type == null) || (markerSet == null)) {
			return Collections.emptyList();
		}

		List<T> typedMarkerList = null;
		for (final IMarker marker : markerSet) {
			if (type.isAssignableFrom(marker.getClass())) {
				if (typedMarkerList == null) {
					typedMarkerList = new ArrayList<T>();
				}
				typedMarkerList.add((T) marker);
			}
		}
		if (typedMarkerList == null) {
			return Collections.emptyList();
		} else {
			return typedMarkerList;
		}
	}
}
