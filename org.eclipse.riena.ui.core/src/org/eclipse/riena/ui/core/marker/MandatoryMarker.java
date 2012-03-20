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
package org.eclipse.riena.ui.core.marker;

import org.eclipse.riena.core.marker.AbstractMarker;

/**
 * Marks an adapter, resp. its associated UI control, as mandatory.
 */
public class MandatoryMarker extends AbstractMarker implements IIconizableMarker {

	/**
	 * Attribute holding the disabled state.
	 */
	private static final String ATTRIBUTE_DISABLED = "disabled"; //$NON-NLS-1$
	public static final String MARKER_KEY = "MandatoryMarker"; //$NON-NLS-1$

	public MandatoryMarker() {
		super();
	}

	public MandatoryMarker(final boolean unique) {
		super(unique);
	}

	/**
	 * @see org.eclipse.riena.ui.core.marker.IIconizableMarker#getIconConfigurationKey()
	 */
	public String getIconConfigurationKey() {
		return MARKER_KEY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.core.marker.IIconizableMarker#getPositionOfMarker()
	 */
	public MarkerPosition getPositionOfMarker() {
		return MarkerPosition.BOTTOM_LEFT;
	}

	/**
	 * Returns the disabled state.
	 * 
	 * @return true, if the marker is disabled, false otherwise.
	 */
	public boolean isDisabled() {
		final Object attributeDisabled = getAttribute(ATTRIBUTE_DISABLED);
		return (attributeDisabled != null && attributeDisabled.equals(Boolean.TRUE));
	}

	/**
	 * Sets the disabled state.
	 * 
	 * @param disabled
	 *            The new disabled state: true, to disable the marker; false, to
	 *            enable it.
	 */
	public void setDisabled(final boolean disabled) {
		if (disabled) {
			setAttribute(ATTRIBUTE_DISABLED, Boolean.TRUE);
		} else {
			setAttribute(ATTRIBUTE_DISABLED, null);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 1.2
	 */
	public boolean isVisible() {
		return !isDisabled();
	}

}
