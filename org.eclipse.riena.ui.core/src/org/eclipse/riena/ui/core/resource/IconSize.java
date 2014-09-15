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
package org.eclipse.riena.ui.core.resource;

/**
 * Constants for icon sizes.
 */
public class IconSize {

	/**
	 * None special size.
	 */
	public static final IconSize NONE = new IconSize("", 0, 0); //$NON-NLS-1$

	/** Size a (16x16). */
	public static final IconSize A16 = new IconSize("a", 16, 16); //$NON-NLS-1$

	/** Size b (22x22). */
	public static final IconSize B22 = new IconSize("b", 22, 22); //$NON-NLS-1$

	/** Size c (32x32). */
	public static final IconSize C32 = new IconSize("c", 32, 32); //$NON-NLS-1$

	/** Size d (48x48). */
	public static final IconSize D48 = new IconSize("d", 48, 48); //$NON-NLS-1$

	/** Size e (64x64). */
	public static final IconSize E64 = new IconSize("e", 64, 64); //$NON-NLS-1$

	/** Size f (128x128). */
	public static final IconSize F128 = new IconSize("f", 128, 128); //$NON-NLS-1$

	private final String defaultMapping;
	private final int width;
	private final int height;

	private IconSize(final String defaultMapping, final int width, final int height) {
		this.defaultMapping = defaultMapping;
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the mapping of this {@code IconSize}.
	 * 
	 * @return the filename character the icon size is mapped to
	 */
	public String getDefaultMapping() {
		return defaultMapping;
	}

	@Override
	public String toString() {
		return getDefaultMapping();
	}

	/**
	 * Returns the width of this icon size.
	 * 
	 * @since 6.0
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of this icon size.
	 * 
	 * @since 6.0
	 */
	public int getHeight() {
		return height;
	}

}
