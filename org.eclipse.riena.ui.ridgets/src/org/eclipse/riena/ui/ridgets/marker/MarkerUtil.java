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
package org.eclipse.riena.ui.ridgets.marker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;

/**
 * Utility class for working with markers.
 * 
 * @since 5.0
 * 
 */
public class MarkerUtil {

	/**
	 * Collects the markers from the given container's children ridgets. Only visible and enabled ridgets will be considered. Markers of ridgets, which are not
	 * visible or not enabled will be not collected.
	 * <p>
	 * Note: The resulting list may contain disabled or invisible markers.
	 * 
	 * @param ridgetContainer
	 *            the {@link IRidgetContainer}, whose children ridgets will be scanned for markers
	 * @return A collection of the markers, set to the visible and enabled children ridgets.
	 */
	public static List<IMarker> getRidgetMarkers(final IRidgetContainer ridgetContainer) {
		final List<IMarker> combinedMarkers = new ArrayList<IMarker>();
		addRidgetMarkers(ridgetContainer, combinedMarkers);
		return combinedMarkers;
	}

	private static void addRidgetMarkers(final IRidget ridget, final List<IMarker> combinedMarkers) {
		if (ridget instanceof IBasicMarkableRidget && ((IBasicMarkableRidget) ridget).isVisible() && ((IBasicMarkableRidget) ridget).isEnabled()) {
			addRidgetMarkers((IBasicMarkableRidget) ridget, combinedMarkers);
		} else if (ridget instanceof IRidgetContainer) {
			addRidgetMarkers((IRidgetContainer) ridget, combinedMarkers);
		}
	}

	private static void addRidgetMarkers(final IBasicMarkableRidget ridget, final List<IMarker> combinedMarkers) {
		combinedMarkers.addAll(getNotHiddenMarkers(ridget));
	}

	private static List<? extends IMarker> getNotHiddenMarkers(final IBasicMarkableRidget ridget) {
		final Collection<? extends IMarker> markers = ridget.getMarkers();
		final List<? extends IMarker> notHiddenMarkers = new ArrayList<IMarker>(markers);
		final Set<Class<IMarker>> hiddenTypes = ridget.getHiddenMarkerTypes();
		for (final Class<IMarker> hiddenType : hiddenTypes) {
			for (final IMarker marker : markers) {
				if (hiddenType.isAssignableFrom(marker.getClass())) {
					notHiddenMarkers.remove(marker);
				}
			}
		}
		return notHiddenMarkers;
	}

	private static void addRidgetMarkers(final IRidgetContainer ridgetContainer, final List<IMarker> combinedMarkers) {
		for (final IRidget ridget : ridgetContainer.getRidgets()) {
			addRidgetMarkers(ridget, combinedMarkers);
		}
	}
}
