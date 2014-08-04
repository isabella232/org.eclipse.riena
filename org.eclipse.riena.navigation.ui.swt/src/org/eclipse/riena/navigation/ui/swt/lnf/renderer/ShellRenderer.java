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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.renderer.AbstractTitleBarRenderer;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Renderer of the buttons (min.,max. and close) and the title of the shell.
 */
public class ShellRenderer extends AbstractTitleBarRenderer {

	/**
	 * Margin between left shell border and title or title and button.
	 */
	private final static int TITLE_MARGIN = SwtUtilities.convertXToDpiTruncate(5);

	private final String[] btnImageKeys = new String[] { LnfKeyConstants.TITLELESS_SHELL_CLOSE_ICON, LnfKeyConstants.TITLELESS_SHELL_MAX_ICON,
			LnfKeyConstants.TITLELESS_SHELL_MIN_ICON, LnfKeyConstants.TITLELESS_SHELL_RESTORE_ICON };
	private final String[] btnHoverSelectedImageKeys = new String[] { LnfKeyConstants.TITLELESS_SHELL_CLOSE_HOVER_SELECTED_ICON,
			LnfKeyConstants.TITLELESS_SHELL_MAX_HOVER_SELECTED_ICON, LnfKeyConstants.TITLELESS_SHELL_MIN_HOVER_SELECTED_ICON,
			LnfKeyConstants.TITLELESS_SHELL_RESTORE_HOVER_SELECTED_ICON };
	private final String[] btnHoverImageKeys = new String[] { LnfKeyConstants.TITLELESS_SHELL_CLOSE_HOVER_ICON, LnfKeyConstants.TITLELESS_SHELL_MAX_HOVER_ICON,
			LnfKeyConstants.TITLELESS_SHELL_MIN_HOVER_ICON, LnfKeyConstants.TITLELESS_SHELL_RESTORE_HOVER_ICON };
	private final String[] btnInactiveImageKeys = new String[] { LnfKeyConstants.TITLELESS_SHELL_CLOSE_INACTIVE_ICON,
			LnfKeyConstants.TITLELESS_SHELL_MAX_INACTIVE_ICON, LnfKeyConstants.TITLELESS_SHELL_MIN_INACTIVE_ICON,
			LnfKeyConstants.TITLELESS_SHELL_RESTORE_INACTIVE_ICON };

	@Override
	protected void paintButton(final GC gc, final int btnIndex) {

		if (!LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.SHELL_HIDE_OS_BORDER)) {
			return;
		}

		super.paintButton(gc, btnIndex);

	}

	@Override
	protected Rectangle paintTitle(final GC gc) {

		if (!LnfManager.getLnf().getBooleanSetting(LnfKeyConstants.SHELL_HIDE_OS_BORDER)) {
			return new Rectangle(0, 0, 0, 0);
		}

		final String title = getTitleText();
		if (StringUtils.isEmpty(title)) {
			return new Rectangle(0, 0, 0, 0);
		}

		Color fgColor = LnfManager.getLnf().getColor(LnfKeyConstants.TITLELESS_SHELL_FOREGROUND);
		if (!isActive()) {
			fgColor = LnfManager.getLnf().getColor(LnfKeyConstants.TITLELESS_SHELL_PASSIVE_FOREGROUND);
		}
		if (fgColor == null) {
			return new Rectangle(0, 0, 0, 0);
		}
		gc.setForeground(fgColor);

		final Font font = LnfManager.getLnf().getFont(LnfKeyConstants.TITLELESS_SHELL_FONT);
		gc.setFont(font);

		final int textHeight = gc.getFontMetrics().getHeight();
		int y = getBounds().height / 2 - textHeight / 2;
		y -= 2;

		int x = getBounds().x + getBounds().width;
		for (int i = 0; i < getButtonsBounds().length; i++) {
			if (getButtonsBounds()[i].x > 0) {
				x = Math.min(x, getButtonsBounds()[i].x);
			}
		}
		final int textWidth = SwtUtilities.calcTextWidth(gc, title);
		switch (getHorizontalLogoPosition()) {
		case SWT.LEFT:
			x = TITLE_MARGIN;
			break;
		case SWT.CENTER:
			x = getBounds().width / 2 - textWidth / 2;
			break;
		default: // SWT.RIGHT
			x -= TITLE_MARGIN;
			x -= textWidth;
			break;
		}

		gc.drawText(title, x, y, true);
		return new Rectangle(x, y, textWidth, textHeight);

	}

	/**
	 * Returns the text, that will be displayed in the title bar of the shell.
	 * <p>
	 * The default implementation returns the text of the SWT shell.
	 * 
	 * @return text of title bar
	 * @since 5.0
	 */
	protected String getTitleText() {
		final String title = getShell().getText();
		return title;
	}

	@Override
	protected Rectangle paintImage(final GC gc) {
		return new Rectangle(0, 0, 0, 0);
	}

	/**
	 * Returns the horizontal position of the text inside the shell.
	 * 
	 * @return horizontal position (SWT.LEFT, SWT.CENTER, SWT.RIGHT)
	 */
	private int getHorizontalLogoPosition() {

		Integer hPos = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_TEXT_POSITION);
		if (hPos == null) {
			hPos = SWT.LEFT;
		}
		return hPos;

	}

	@Override
	protected String[] getBtnHoverImageKeys() {
		return btnHoverImageKeys;
	}

	@Override
	protected String[] getBtnHoverSelectedImageKeys() {
		return btnHoverSelectedImageKeys;
	}

	@Override
	protected String[] getBtnImageKeys() {
		return btnImageKeys;
	}

	@Override
	protected String[] getBtnInactiveImageKeys() {
		return btnInactiveImageKeys;
	}

	@Override
	protected void paintBackground(final GC gc) {
		//		gc.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.TITLELESS_SHELL_BACKGROUND));
		//		Image logo = getBackgroundImage();
		//		if (logo != null) {
		//			int y = logo.getBounds().height;
		//			int h = getBounds().height - y;
		//			gc.fillRectangle(0, y, getBounds().width, h);
		//		}
	}

}
