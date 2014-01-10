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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import org.eclipse.swt.graphics.Color;

import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Renderer of the border around a module group.
 * 
 * @since 3.0
 */
public class ModuleGroupBorderRenderer extends EmbeddedBorderRenderer {

	@Override
	protected Color getBorderColor() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		Color borderColor = lnf.getColor(LnfKeyConstants.MODULE_GROUP_PASSIVE_BORDER_COLOR);
		if (isActive()) {
			borderColor = lnf.getColor(LnfKeyConstants.MODULE_GROUP_ACTIVE_BORDER_COLOR);
		}
		if (!isEnabled()) {
			borderColor = lnf.getColor(LnfKeyConstants.MODULE_GROUP_DISABLED_BORDER_COLOR);
		}
		if (borderColor == null) {
			borderColor = super.getBorderColor();
		}
		return borderColor;
	}

}
