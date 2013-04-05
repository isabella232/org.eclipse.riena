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

import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.core.marker.IMarker;

/**
 * This interface can <i>extend</i> existing markers (e.g. OutputMarker,
 * MandatoryMarker) for special cases to return other background and/or
 * foreground colors.
 * 
 * @since 3.0
 */
public interface ICustomMarker extends IMarker {

	/**
	 * Returns the background color of this marker.
	 * <p>
	 * If this method return {@code null}, the default color of the this kind of
	 * marker will be used.
	 * 
	 * @param markable
	 *            an object that can has markers
	 * @return background color
	 */
	Object getBackground(IMarkable markable);

	/**
	 * Returns the foreground color of this marker.
	 * <p>
	 * If this method return {@code null}, the default color of the this kind of
	 * marker will be used.
	 * 
	 * @param markable
	 *            an object that can has markers
	 * @return foreground color
	 */
	Object getForeground(IMarkable markable);

}
