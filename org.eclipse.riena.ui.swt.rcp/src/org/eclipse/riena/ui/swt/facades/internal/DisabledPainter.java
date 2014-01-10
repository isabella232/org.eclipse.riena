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
package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * The renderer of the DisabledMarker state. Colors and Alpha value are
 * configurable. See {@link LnfManager} for more details on this. Constants used
 * are:
 * <ul>
 * <li>
 * {@link LnfKeyConstants#DISABLED_MARKER_STANDARD_ALPHA}</li>
 * <li>
 * {@link LnfKeyConstants#DISABLED_BACKGROUND}</li>
 * <li>
 * {@link LnfKeyConstants#DISABLED_MARKER_BACKGROUND}</li>
 * </ul>
 */
public final class DisabledPainter implements PaintListener {
	public void paintControl(final PaintEvent e) {
		final GC gc = e.gc;
		final Control control = (Control) e.widget;
		final Rectangle bounds = control.getBounds();

		// fill basic background to draw over later
		if (!(control instanceof Combo || control instanceof DateTime)) {
			fillDisabledBackground(gc, bounds);
		}

		// common disabled visualization based on a common background color
		final int alpha = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.DISABLED_MARKER_STANDARD_ALPHA);
		final Color color = LnfManager.getLnf().getColor(LnfKeyConstants.DISABLED_MARKER_BACKGROUND);
		gc.setAlpha(alpha);
		gc.setBackground(color);
		if (control instanceof ChoiceComposite) {
			gc.setAlpha(255);
			gc.setBackground(control.getParent().getBackground());
		}
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
	}

	/**
	 * Fills the background of the {@link Control} with
	 * {@link LnfKeyConstants#DISABLED_BACKGROUND}
	 * 
	 */
	private void fillDisabledBackground(final GC gc, final Rectangle bounds) {
		final Color color = LnfManager.getLnf().getColor(LnfKeyConstants.DISABLED_BACKGROUND);
		gc.setBackground(color);
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
	}
}