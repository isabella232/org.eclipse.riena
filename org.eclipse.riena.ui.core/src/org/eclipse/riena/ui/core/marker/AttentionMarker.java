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
 * This marker should be used for navigation nodes. This marker should direct
 * the attention of the user to a sub-module. After the user has visited the
 * sub-module the marker can be removed.
 * 
 * @since 1.2
 */
public class AttentionMarker extends AbstractMarker implements IIconizableMarker {

	public static final String MARKER_KEY = "AttentionMarker"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code AttentionMarker.MARKER_KEY}
	 */
	public String getIconConfigurationKey() {
		return MARKER_KEY;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code MarkerPosition.TOP_RIGHT}
	 */
	public MarkerPosition getPositionOfMarker() {
		return MarkerPosition.TOP_RIGHT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@code true}
	 */
	public boolean isVisible() {
		return true;
	}
}
