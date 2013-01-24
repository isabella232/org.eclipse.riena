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
package org.eclipse.riena.ui.core.marker;

/**
 * A property change event that was caused by the change of a property holding
 * markers.
 * 
 * @see org.eclipse.riena.core.marker.IMarker
 */
public interface IMarkerPropertyChangeEvent {

	/**
	 * Indicates whether the event was caused by the change of a markers
	 * property only.
	 * 
	 * @return true if the only cause of the event is the change of a marker
	 *         property, false otherwise.
	 */
	boolean isAttributeRelated();

}
