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

import org.eclipse.riena.core.marker.AbstractMarker;
import org.eclipse.riena.core.marker.IMarker;

/**
 * Marks a Ridget, resp. its associated UI control, as error. The Marker can
 * also be used for navigation nodes.
 */
public class ErrorMarker extends AbstractMarker implements IIconizableMarker {

	public static final String MARKER_KEY = "ErrorMarker"; //$NON-NLS-1$

	/**
	 * Create a 'unique' error marker instance.
	 */
	public ErrorMarker() {
		super(true);
	}

	/**
	 * Create an error marker instance.
	 * 
	 * @param unique
	 *            true to create a 'unique' marker; false otherwise. See
	 *            {@link AbstractMarker} more information.
	 * @since 1.2
	 */
	protected ErrorMarker(final boolean unique) {
		super(unique);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code ErrorMarker.MARKER_KEY}
	 */
	public String getIconConfigurationKey() {
		return MARKER_KEY;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code MarkerPosition.BOTTOM_RIGHT}
	 */
	public MarkerPosition getPositionOfMarker() {
		return MarkerPosition.BOTTOM_RIGHT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code true}
	 * 
	 * @since 1.2
	 */
	public boolean isVisible() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code IMarker.Priority.VERY_HIGH}
	 */
	@Override
	public Priority getPriority() {
		return IMarker.Priority.VERY_HIGH;
	}

}
