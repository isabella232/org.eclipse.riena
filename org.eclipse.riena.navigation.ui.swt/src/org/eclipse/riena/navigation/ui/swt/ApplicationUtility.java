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
package org.eclipse.riena.navigation.ui.swt;

import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentationFactory;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Utility class for Application manipulation.
 * 
 * @since 4.0
 */
public final class ApplicationUtility {

	private ApplicationUtility() {
	}

	/**
	 * Sets the visibility of the navigation, if the Property
	 * {@link LnfKeyConstants#NAVIGATION_FAST_VIEW} is set to true.
	 * 
	 * @param visible
	 *            the visibility of the navigation
	 */
	public static void setNavigationVisible(final boolean visible) {
		TitlelessStackPresentationFactory.getActiveTitlelessStackPresentation().setNavigationVisible(visible);
	}

	/**
	 * Returns the visibility of the navigation.
	 * 
	 * @return the visibility of the navigation. Always false if the property
	 *         {@link LnfKeyConstants#NAVIGATION_FAST_VIEW} is set to false.
	 */
	public static boolean isNavigationVisible() {
		return TitlelessStackPresentationFactory.getActiveTitlelessStackPresentation().isNavigationVisible();
	}

	/**
	 * Returns whether the navigation is a fast view.
	 * 
	 * @return true if {@link LnfKeyConstants#NAVIGATION_FAST_VIEW} is true,
	 *         false otherwise
	 */
	public static boolean isNavigationFastViewEnabled() {
		return LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.NAVIGATION_FAST_VIEW);
	}
}
