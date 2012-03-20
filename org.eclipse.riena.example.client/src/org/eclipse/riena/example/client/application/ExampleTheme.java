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
package org.eclipse.riena.example.client.application;

import org.eclipse.swt.SWT;

import org.eclipse.riena.ui.swt.lnf.ColorLnfResource;
import org.eclipse.riena.ui.swt.lnf.FontLnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfCustomizer;
import org.eclipse.riena.ui.swt.lnf.ImageLnfResource;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultTheme;

/**
 * Theme of the Look and Feel (Lnf) of the example application.
 */
public class ExampleTheme extends RienaDefaultTheme {

	@Override
	public void customizeLnf(final ILnfCustomizer lnf) {
		super.customizeLnf(lnf);

		customizeColors(lnf);
		customizeImages(lnf);
		customizeFonts(lnf);
		customizeSettings(lnf);
	}

	private void customizeColors(final ILnfCustomizer lnf) {

		lnf.putLnfResource(LnfKeyConstants.TITLELESS_SHELL_FOREGROUND, getPrimaryForeground());

		lnf.putLnfResource(LnfKeyConstants.MODULE_ITEM_TOOLTIP_FOREGROUND, new ColorLnfResource(64, 0, 64));

		lnf.putLnfResource(LnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_FOREGROUND, new ColorLnfResource(64, 0, 64));

		lnf.putLnfResource(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BACKGROUND_START_COLOR, new ColorLnfResource(234,
				231, 158));
		lnf.putLnfResource(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BACKGROUND_END_COLOR, new ColorLnfResource(225,
				220, 114));
		lnf.putLnfResource(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BORDER_COLOR, new ColorLnfResource(171, 171, 174));
		lnf.putLnfResource(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BACKGROUND_START_COLOR, new ColorLnfResource(222,
				224, 240));
		lnf.putLnfResource(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BACKGROUND_END_COLOR, new ColorLnfResource(186,
				193, 225));
		lnf.putLnfResource(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BORDER_COLOR, new ColorLnfResource(151, 150, 180));
		lnf.putLnfResource(LnfKeyConstants.MODULE_GROUP_ACTIVE_BORDER_COLOR, new ColorLnfResource(255, 200, 15));

		lnf.putLnfResource("Text.background", new ColorLnfResource(255, 255, 235)); //$NON-NLS-1$
	}

	private void customizeImages(final ILnfCustomizer lnf) {
		String imagePath = "exampleBackground.png"; //$NON-NLS-1$ 
		lnf.putLnfResource(LnfKeyConstants.TITLELESS_SHELL_BACKGROUND_IMAGE, new ImageLnfResource(imagePath));
		imagePath = "exampleRIENA_Logo_RGB.png"; //$NON-NLS-1$ 
		lnf.putLnfResource(LnfKeyConstants.TITLELESS_SHELL_LOGO, new ImageLnfResource(imagePath));

		imagePath = "ledred.png"; //$NON-NLS-1$ 
		lnf.putLnfResource(LnfKeyConstants.SUB_MODULE_TREE_DOCUMENT_LEAF_ICON, new ImageLnfResource(imagePath));
		imagePath = "folder_favorite.png"; //$NON-NLS-1$ 
		lnf.putLnfResource(LnfKeyConstants.SUB_MODULE_TREE_FOLDER_CLOSED_ICON, new ImageLnfResource(imagePath));
		lnf.putLnfResource(LnfKeyConstants.SUB_MODULE_TREE_FOLDER_OPEN_ICON, new ImageLnfResource(imagePath));
		lnf.putLnfResource(LnfKeyConstants.WORKAREA_TREE_FOLDER_OPEN_ICON, new ImageLnfResource(imagePath));
		lnf.putLnfResource(LnfKeyConstants.WORKAREA_TREE_FOLDER_CLOSED_ICON, new ImageLnfResource(imagePath));
	}

	private void customizeFonts(final ILnfCustomizer lnf) {
		lnf.putLnfResource(LnfKeyConstants.TITLELESS_SHELL_FONT, new FontLnfResource("Arial", 12, SWT.BOLD)); //$NON-NLS-1$

		lnf.putLnfResource(LnfKeyConstants.DIALOG_FONT, new FontLnfResource("Arial", 11, SWT.BOLD)); //$NON-NLS-1$

		lnf.putLnfResource(LnfKeyConstants.SUB_APPLICATION_SWITCHER_FONT, new FontLnfResource("Arial", 11, SWT.BOLD)); //$NON-NLS-1$

		lnf.putLnfResource(LnfKeyConstants.MODULE_ITEM_TOOLTIP_FONT, new FontLnfResource("Arial", 11, SWT.BOLD)); //$NON-NLS-1$

		lnf.putLnfResource(LnfKeyConstants.SUB_MODULE_ITEM_FONT, new FontLnfResource("Arial", 11, SWT.BOLD)); //$NON-NLS-1$

		lnf.putLnfResource(LnfKeyConstants.EMBEDDED_TITLEBAR_FONT, new FontLnfResource("Arial", 11, SWT.BOLD)); //$NON-NLS-1$
	}

	private void customizeSettings(final ILnfCustomizer lnf) {
		lnf.putLnfSetting(LnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_POSITION, SWT.TOP);
		lnf.putLnfSetting(LnfKeyConstants.TITLELESS_SHELL_VERTICAL_LOGO_MARGIN, 7);
		lnf.putLnfSetting(LnfKeyConstants.TITLELESS_SHELL_HORIZONTAL_LOGO_POSITION, SWT.CENTER);

		lnf.putLnfSetting(LnfKeyConstants.SUB_APPLICATION_SWITCHER_HORIZONTAL_TAB_POSITION, SWT.LEFT);
		lnf.putLnfSetting(LnfKeyConstants.SUB_APPLICATION_SWITCHER_TAB_SHOW_ICON, true);

		lnf.putLnfSetting(LnfKeyConstants.MARKER_SUPPORT_ID, "defaultMarkerSupport"); //$NON-NLS-1$
	}

	@Override
	protected ColorLnfResource getPrimaryForeground() {
		return new ColorLnfResource(64, 0, 64);
	}

	@Override
	protected ColorLnfResource getPrimaryBackground() {
		return new ColorLnfResource(255, 255, 215);
	}

	@Override
	protected FontLnfResource getPrimaryFont() {
		return new FontLnfResource("Arial", 9, SWT.NORMAL); //$NON-NLS-1$
	}

	@Override
	protected boolean hideOsBorder() {
		return false;
	}

}
