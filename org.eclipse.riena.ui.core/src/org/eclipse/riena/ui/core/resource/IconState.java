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
package org.eclipse.riena.ui.core.resource;

/**
 * Constants for icon states.
 */
public final class IconState {

	/** Icon state NORMAL. */
	public static final IconState NORMAL = new IconState(""); //$NON-NLS-1$

	/** Icon state if mouse pointer is of the UI control. */
	public static final IconState HOVER = new IconState("_h_"); //$NON-NLS-1$

	/** Icon state if the UI control is pressed. */
	public static final IconState PRESSED = new IconState("_p_"); //$NON-NLS-1$

	/** Icon state if the UI control is disabled. */
	public static final IconState DISABLED = new IconState("_d_"); //$NON-NLS-1$

	/** Icon state if the UI control is selected. */
	public static final IconState SELECTED = new IconState("_a_"); //$NON-NLS-1$

	/**
	 * Icon state if the UI control is selected and the mouse pointer is of the
	 * UI control.
	 */
	public static final IconState SELECTED_HOVER = new IconState("_ah_"); //$NON-NLS-1$

	/** Icon state if the UI control is selected and disabled. */
	public static final IconState SELECTED_DISABLED = new IconState("_ad_"); //$NON-NLS-1$

	/** Icon state DEFAULT. */
	public static final IconState DEFAULT = new IconState("_s_"); //$NON-NLS-1$

	/** Icon state if the UI control has the focus. */
	public static final IconState HAS_FOCUS = new IconState("_f_"); //$NON-NLS-1$

	/**
	 * Icon state if the UI control has the focus and the mouse pointer is over
	 * the UI control.
	 */
	public static final IconState HOVER_HAS_FOCUS = new IconState("_hf_"); //$NON-NLS-1$

	private final String defaultMapping;

	private IconState(final String defaultMapping) {
		this.defaultMapping = defaultMapping;
	}

	/**
	 * Returns the mapping of this {@code IconState}.
	 * 
	 * @return the filename character the icon state is mapped to
	 */
	public String getDefaultMapping() {
		return defaultMapping;
	}

	@Override
	public String toString() {
		return getDefaultMapping();
	}

}
