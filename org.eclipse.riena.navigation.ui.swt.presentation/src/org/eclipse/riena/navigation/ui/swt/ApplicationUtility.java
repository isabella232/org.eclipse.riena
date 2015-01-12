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
package org.eclipse.riena.navigation.ui.swt;

import org.eclipse.riena.navigation.ui.swt.IApplicationUtility;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.common.TitlelessStackPresentationFactory;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Utility class for Application manipulation.
 * 
 * @since 4.0
 */
public final class ApplicationUtility implements IApplicationUtility {

	/* (non-Javadoc)
	 * @see org.eclipse.riena.navigation.ui.swt.IApplicationUtility#setNavigationVisible(boolean)
	 */
	@Override
	public void setNavigationVisible(final boolean visible) {
		TitlelessStackPresentationFactory.getActiveTitlelessStackPresentation().setNavigationVisible(visible);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.riena.navigation.ui.swt.IApplicationUtility#isNavigationVisible()
	 */
	@Override
	public boolean isNavigationVisible() {
		return TitlelessStackPresentationFactory.getActiveTitlelessStackPresentation().isNavigationVisible();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.riena.navigation.ui.swt.IApplicationUtility#isNavigationFastViewEnabled()
	 */
	@Override
	public boolean isNavigationFastViewEnabled() {
		return LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.NAVIGATION_FAST_VIEW);
	}
}
