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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.swt.lnf.ILnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * This listener paints the shell (the border of the shell).
 * 
 * @since 5.0
 */
public class ShellPaintListener implements PaintListener {

	public void paintControl(final PaintEvent e) {
		onPaint(e);
	}

	/**
	 * Paints the border of the (titleless) shell.
	 * 
	 * @param e
	 *            event
	 */
	private void onPaint(final PaintEvent e) {
		if (e.getSource() instanceof Control) {
			final Control shell = (Control) e.getSource();

			final Rectangle shellBounds = shell.getBounds();
			final Rectangle bounds = new Rectangle(0, 0, shellBounds.width, shellBounds.height);

			final ILnfRenderer borderRenderer = LnfManager.getLnf().getRenderer(LnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
			borderRenderer.setBounds(bounds);
			// TODO [ev] gc is sometimes disposed -- looks like a RAP bug, adding a workaround, need to file bug
			if (!e.gc.isDisposed()) {
				borderRenderer.paint(e.gc, null);
			}
		}
	}

}