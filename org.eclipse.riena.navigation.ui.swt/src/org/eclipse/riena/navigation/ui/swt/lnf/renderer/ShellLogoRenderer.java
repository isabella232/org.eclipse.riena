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

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.ImageStore;

/**
 * This class renders the logo of the application at the correct position.
 */
public class ShellLogoRenderer extends AbstractLnfRenderer {

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ShellLogoRenderer.class);

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC, java.lang.Object)
	 */
	@Override
	public void paint(final GC gc, final Object value) {

		super.paint(gc, value);

		final Image logo = value instanceof Image ? (Image) value : getLogoImage();
		if (logo == null) {
			return;
		}
		final int logoWidth = logo.getBounds().width;
		final int logoHeight = logo.getBounds().height;
		final int hMargin = getHorizontalLogoMargin();
		final int vMargin = getVerticalLogoMargin();

		int x = getBounds().x;
		final Integer hPos = getHorizontalLogoPosition();
		switch (hPos) {
		case SWT.CENTER:
			x = getBounds().width / 2 - logoWidth / 2;
			break;
		case SWT.RIGHT:
			x = getBounds().width - logoWidth - hMargin;
			break;
		default:
			x = hMargin;
			break;
		}
		int y = getBounds().y;
		final Integer vPos = getVerticalLogoPosition();
		switch (vPos) {
		case SWT.CENTER:
			y = getBounds().height / 2 - logoHeight / 2;
			y += vMargin;
			break;
		case SWT.BOTTOM:
			y = getBounds().height - logoHeight - vMargin;
			break;
		default:
			y = vMargin;
			break;
		}

		gc.drawImage(logo, x, y);

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		// nothing to do
	}

	/**
	 * Returns the horizontal margin of the logo image.<br>
	 * Gap between the shell border an the image.
	 * 
	 * @return horizontal margin
	 */
	public static Integer getHorizontalLogoMargin() {

		Integer margin = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_MARGIN);
		if (margin == null) {
			margin = 0;
		}
		return margin;

	}

	/**
	 * Returns the vertical margin of the logo image.<br>
	 * Gap between the shell border an the image.
	 * 
	 * @return horizontal margin
	 */
	private Integer getVerticalLogoMargin() {

		Integer margin = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_MARGIN);
		if (margin == null) {
			margin = 0;
		}
		return margin;

	}

	/**
	 * Returns the horizontal position of the logo inside the shell.
	 * 
	 * @return horizontal position (SWT.LEFT, SWT.CENTER, SWT.RIGHT)
	 */
	private int getHorizontalLogoPosition() {

		Integer hPos = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_POSITION);
		if (hPos == null) {
			hPos = SWT.LEFT;
		}
		return hPos;

	}

	/**
	 * Returns the vertical position of the logo inside the shell.
	 * 
	 * @return horizontal position (SWT.TOP, SWT.CENTER, SWT.BOTTOM)
	 */
	private int getVerticalLogoPosition() {

		Integer hPos = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_POSITION);
		if (hPos == null) {
			hPos = SWT.TOP;
		}
		return hPos;

	}

	/**
	 * Returns the image of the logo.
	 * 
	 * @return logo image or the default missing image, if the logo image of the L&F wasn't found.
	 */
	private Image getLogoImage() {
		Image logoImage = LnfManager.getLnf().getImage(LnfKeyConstants.TITLELESS_SHELL_LOGO);
		if (logoImage == null) {
			final String message = "The image of the logo wasn't found! A dummy image is used."; //$NON-NLS-1$
			LOGGER.log(LogService.LOG_WARNING, message);
			logoImage = ImageStore.getInstance().getMissingImage();
		}
		return logoImage;
	}

}
