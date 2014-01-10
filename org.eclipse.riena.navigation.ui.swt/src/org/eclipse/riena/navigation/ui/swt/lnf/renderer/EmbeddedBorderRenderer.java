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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Renderer of an embedded border.
 */
public class EmbeddedBorderRenderer extends AbstractLnfRenderer {

	private final static int BORDER_WIDTH = 2;
	private boolean active;

	/**
	 * @param value
	 *            is ignored
	 * 
	 * @see org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(final GC gc, final Object value) {

		super.paint(gc, value);

		// Border
		final Color borderColor = getBorderColor();
		gc.setForeground(borderColor);
		// -outer
		// --top
		int x = getBounds().x + BORDER_WIDTH;
		int y = getBounds().y;
		int w = getWidth() - BORDER_WIDTH * 2;
		gc.drawLine(x, y, x + w, y);
		// --bottom
		y = getBounds().y + getHeight();
		gc.drawLine(x, y, x + w, y);
		// --left
		x = getBounds().x;
		y = getBounds().y + BORDER_WIDTH;
		int h = getHeight() - BORDER_WIDTH * 2;
		gc.drawLine(x, y, x, y + h);
		// --right
		x = getBounds().x + getWidth();
		gc.drawLine(x, y, x, y + h);

		// -inner
		// --top
		x = getBounds().x + 1;
		y = getBounds().y + 1;
		w = getWidth() - 2;
		gc.drawLine(x, y, x + w, y);
		// --bottom
		y = getBounds().y + getHeight() - 1;
		gc.drawLine(x, y, x + w, y);
		// --left
		x = getBounds().x + 1;
		y = getBounds().y + 1;
		h = getHeight() - 2;
		gc.drawLine(x, y, x, y + h);
		// --right
		x = getBounds().x + getWidth() - 1;
		gc.drawLine(x, y, x, y + h);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		// nothing to do
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	private int getHeight() {
		return getBounds().height - 1;
	}

	private int getWidth() {
		return getBounds().width - 1;
	}

	/**
	 * Computes the size inside the given outer border.
	 * 
	 * @param outerBounds
	 *            bounds of the outer border
	 * 
	 * @return bounds of the inner border
	 */
	public Rectangle computeInnerBounds(final Rectangle outerBounds) {
		return new Rectangle(outerBounds.x + BORDER_WIDTH, outerBounds.y + BORDER_WIDTH, outerBounds.width
				- BORDER_WIDTH * 2, outerBounds.height - BORDER_WIDTH * 2);
	}

	/**
	 * Computes the size outside the given inner border.
	 * 
	 * @param innerBounds
	 *            bounds of the inner border
	 * 
	 * @return bounds of the outer border
	 */
	public Rectangle computeOuterBounds(final Rectangle innerBounds) {
		return new Rectangle(innerBounds.x - BORDER_WIDTH, innerBounds.y - BORDER_WIDTH, innerBounds.width
				+ BORDER_WIDTH * 2, innerBounds.height + BORDER_WIDTH * 2);
	}

	/**
	 * Computes the outside height of the border.
	 * 
	 * @param innerHeight
	 *            the inner height of the border
	 * 
	 * @return outer border height
	 */
	public int computeOuterHeight(final int innerHeight) {
		return innerHeight + BORDER_WIDTH * 2;
	}

	/**
	 * Computes the outside width of the border.
	 * 
	 * @param innerWidth
	 *            the inner width of the border
	 * 
	 * @return outer border width
	 */
	public int computeOuterWidth(final int innerWidth) {
		return innerWidth + BORDER_WIDTH * 2;
	}

	/**
	 * Returns the width of the border.
	 * 
	 * @return border width
	 */
	public int getBorderWidth() {
		return BORDER_WIDTH;
	}

	/**
	 * Returns the color of the border according if it's active, passive or
	 * disable.
	 * 
	 * @return border color
	 * @since 3.0
	 */
	protected Color getBorderColor() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();
		Color borderColor = lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_PASSIVE_BORDER_COLOR);
		if (isActive()) {
			borderColor = lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_ACTIVE_BORDER_COLOR);
		}
		if (!isEnabled()) {
			borderColor = lnf.getColor(LnfKeyConstants.EMBEDDED_TITLEBAR_DISABLED_BORDER_COLOR);
		}
		return borderColor;
	}

}
