/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets;

import java.util.Set;

import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.core.marker.IMarker;

/**
 * A Ridget with a basic support for markers.
 */
public interface IBasicMarkableRidget extends IRidget, IMarkable, IClickableRidget {

	/**
	 * The name of the PropertyChangeEvent that will be fired if a marker was added or removed ("marker").
	 */
	String PROPERTY_MARKER = "marker"; //$NON-NLS-1$

	/**
	 * The name of the PropertyChangeEvent that will be fired if a marker is hidden or shown ("markerHiding").
	 * 
	 * @since 3.0
	 */
	String PROPERTY_MARKER_HIDING = "markerHiding"; //$NON-NLS-1$

	/**
	 * Returns all hidden marker types.
	 * 
	 * @return the set of currently hidden marker types; never null; may be empty
	 * 
	 * @since 3.0
	 */
	Set<Class<IMarker>> getHiddenMarkerTypes();

	/**
	 * Hide markers of the given type. Hidden markers of a matching type shall be ignored in the UI (i.e. no feedback is shown by the ridget), but are not
	 * removed from the ridget.
	 * <p>
	 * Initially the set of hidden markers is empty. When calling this method the {@code type}-argument is added to the set.
	 * 
	 * @param types
	 *            the type of markers to hide. The matching includes sublasses
	 * @return the set of currently hidden marker types (including type)
	 * 
	 * @since 3.0
	 */
	Set<Class<IMarker>> hideMarkersOfType(Class<? extends IMarker>... types);

	/**
	 * Show markers of the given type. Hidden markers of a matching type shall be ignored in the UI (i.e. not feedback is shown by the ridget), but are not
	 * removed from the ridget.
	 * <p>
	 * Initially the set of hidden markers is empty. When calling this method the {@code type}-argument is removed from the set.
	 * 
	 * @param types
	 *            the type of markers to show (unhide). The matching includes subclasses
	 * @return the set of currently hidden marker types (including type).
	 * 
	 * @since 3.0
	 */
	Set<Class<IMarker>> showMarkersOfType(Class<? extends IMarker>... types);

	/**
	 * Controls whether the border decoration will draw the border around the visible control area (does not work for all control types) or around the control
	 * bounds. Set this flag if you are sure that the appropriate widget will be supported by the border decoration.
	 * 
	 * @since 5.0
	 */
	boolean decorateVisibleControlArea();

}
