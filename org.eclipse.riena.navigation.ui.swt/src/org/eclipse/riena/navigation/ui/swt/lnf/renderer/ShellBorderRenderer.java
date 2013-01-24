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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import org.eclipse.swt.graphics.Color;

import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.renderer.DialogBorderRenderer;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Renderer of the border of the (undecorated (no OS-border, no OS-titlebar))
 * shell.
 */
public class ShellBorderRenderer extends DialogBorderRenderer {

	@Override
	protected Boolean hideOsBorder() {
		return LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.SHELL_HIDE_OS_BORDER);
	}

	@Override
	protected Color getInnerBorderColorBottom() {
		return getLnfColor(LnfKeyConstants.TITLELESS_SHELL_INNER_BORDER_BOTTOM_RIGHT_COLOR);
	}

	@Override
	protected Color getInnerBorderColorRight() {
		return getLnfColor(LnfKeyConstants.TITLELESS_SHELL_INNER_BORDER_BOTTOM_RIGHT_COLOR);
	}

	@Override
	protected Color getInnerBorderColorTop() {
		return getLnfColor(LnfKeyConstants.TITLELESS_SHELL_INNER_BORDER_TOP_LEFT_COLOR);
	}

	@Override
	protected Color getInnerBorderColorLeft() {
		return getLnfColor(LnfKeyConstants.TITLELESS_SHELL_INNER_BORDER_TOP_LEFT_COLOR);
	}

	@Override
	protected Color getBorderColorBottom() {
		return getLnfColor(LnfKeyConstants.TITLELESS_SHELL_BORDER_BOTTOM_RIGHT_COLOR);
	}

	@Override
	protected Color getBorderColorRight() {
		return getLnfColor(LnfKeyConstants.TITLELESS_SHELL_BORDER_BOTTOM_RIGHT_COLOR);
	}

	@Override
	protected Color getBorderColorTop() {
		return getLnfColor(LnfKeyConstants.TITLELESS_SHELL_BORDER_TOP_LEFT_COLOR);
	}

	@Override
	protected Color getBorderColorLeft() {
		return getLnfColor(LnfKeyConstants.TITLELESS_SHELL_BORDER_TOP_LEFT_COLOR);
	}

	/**
	 * Returns the width of the border (including padding)
	 * 
	 * @return border width
	 */
	public int getCompleteBorderWidth() {

		int width = getBorderWidth();

		final RienaDefaultLnf lnf = LnfManager.getLnf();
		final Integer padding = lnf.getIntegerSetting(LnfKeyConstants.TITLELESS_SHELL_PADDING);
		if (padding != null) {
			width += padding;
		}

		return width;

	}

}
