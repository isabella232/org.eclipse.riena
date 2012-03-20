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
package org.eclipse.riena.internal.navigation.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Utility class for working with {@link CoolBar}s.
 */
public final class CoolbarUtils {

	/**
	 * Initializes the given cool bar.<br>
	 * E.g. sets the background of the cool bar and if the cool bar is empty
	 * adds necessary cool item with the height 1.
	 * 
	 * @param coolBar
	 *            cool bar
	 * @return the first cool item of the given cool bar
	 */
	public static CoolItem initCoolBar(final CoolBar coolBar, final Font font) {
		if (coolBar.getItemCount() == 0) {
			final CoolItem coolItem = new CoolItem(coolBar, SWT.DROP_DOWN);
			final ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
			toolBar.setFont(font);
			coolItem.setControl(toolBar);
			// sets the default size of an empty menu bar or tool bar
			coolItem.setSize(new Point(0, 1));
		} else {
			final CoolItem[] items = coolBar.getItems();
			for (final CoolItem coolItem : items) {
				final Control ciControl = coolItem.getControl();
				if (updateFont(ciControl, font)) {
					updateToolbarSize(coolItem);
				}
			}
		}

		coolBar.setBackground(getCoolbarBackground());
		coolBar.setBackgroundMode(SWT.INHERIT_FORCE);
		coolBar.setLocked(true);

		return coolBar.getItem(0);
	}

	/**
	 * Return the coolbar / menubar background color according to the
	 * look-and-feel.
	 */
	private static Color getCoolbarBackground() {
		return LnfManager.getLnf().getColor(LnfKeyConstants.COOLBAR_BACKGROUND);
	}

	/**
	 * Set newFont as the font for the given control, if not already set.
	 * 
	 * @param control
	 *            a Control; may be null
	 * @param font
	 *            a Font; may be null.
	 * @return true, if the font was changed, false otherwise.
	 */
	private static boolean updateFont(final Control control, final Font newFont) {
		boolean result = false;
		if (control != null) {
			final Font oldFont = control.getFont();
			if (newFont != oldFont || (newFont != null && !newFont.equals(oldFont))) {
				control.setFont(newFont);
				result = true;
			}
		}
		return result;
	}

	private static void updateToolbarSize(final CoolItem coolItem) {
		final Control control = coolItem.getControl();
		if (control instanceof ToolBar) {
			final ToolBar toolBar = (ToolBar) control;
			for (final ToolItem item : toolBar.getItems()) {
				final String text = item.getText();
				// workaround for SWT issue: ToolItem size is not recomputed after
				// chaning the font, must set a different text than the one in
				// the item to trigger a recomputation.
				item.setText(""); //$NON-NLS-1$
				item.setText(text);
			}

			toolBar.pack();
			// see ToolBarContributionItem.updateToolbarSize(...)
			final Point newSize = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			final Point preferredSize = coolItem.computeSize(newSize.x, newSize.y);
			coolItem.setPreferredSize(preferredSize);
			coolItem.setMinimumSize(newSize);
			coolItem.setSize(preferredSize);
		}
	}

	private CoolbarUtils() {
		// prevent instantiation
	}
}
