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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Provides access to a set of shared colors. The colors can be accessed using
 * one of the predefined string constants.
 * 
 * @see Activator#getSharedColor(Display, String)
 */
public final class SharedColors {

	/**
	 * @deprecated - since v2.1 the error marker is flashed. A special
	 *             background color is no longer used.
	 */
	@Deprecated
	public static final String COLOR_FLASH_ERROR = "COLOR_FLASH_ERROR"; //$NON-NLS-1$

	public static final String COLOR_MANDATORY = "COLOR_MANDATORY"; //$NON-NLS-1$
	public static final String COLOR_MANDATORY_OUTPUT = "COLOR_MANDATORY_OUTPUT"; //$NON-NLS-1$
	public static final String COLOR_NEGATIVE = "COLOR_NEGATIVE"; //$NON-NLS-1$
	public static final String COLOR_OUTPUT = "COLOR_OUTPUT"; //$NON-NLS-1$

	private final Map<String, Color> sharedColors = new HashMap<String, Color>();

	SharedColors(final Display display) {
		put(display, COLOR_MANDATORY, new RGB(255, 255, 175));
		put(display, COLOR_OUTPUT, new RGB(231, 233, 245));
		put(display, COLOR_MANDATORY_OUTPUT, new RGB(255, 249, 216));
		put(display, COLOR_FLASH_ERROR, new RGB(250, 190, 190));
		put(display, COLOR_NEGATIVE, new RGB(255, 0, 0));
	}

	private void put(final Display display, final String name, final RGB rgb) {
		sharedColors.put(name, new Color(display, rgb));
	}

	Color getSharedColor(final String colorKey) {
		Assert.isLegal(!sharedColors.isEmpty(), "Shared colors are disposed"); //$NON-NLS-1$
		final Color result = sharedColors.get(colorKey);
		Assert.isNotNull(result, "Could not find color with key: " + colorKey); //$NON-NLS-1$
		return result;
	}

	void dispose() {
		for (final Color color : sharedColors.values()) {
			color.dispose();
		}
		sharedColors.clear();
	}

}
