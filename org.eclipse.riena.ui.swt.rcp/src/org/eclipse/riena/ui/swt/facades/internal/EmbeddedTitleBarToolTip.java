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
package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;

import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.renderer.EmbeddedTitlebarRenderer;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * ToolTip for {@link EmbeddedTitleBar}s. The ToolTip is only displayed if the
 * title of the {@link EmbeddedTitleBar} is clipped.
 */
public class EmbeddedTitleBarToolTip extends AbstractNavigationToolTip {

	private final EmbeddedTitleBar embeddedTitleBar;

	/**
	 * Creates a tooltip on a {@link EmbeddedTitleBar}. The ToolTip is only
	 * displayed if the title of the {@link EmbeddedTitleBar} is clipped.
	 * 
	 * @param parent
	 *            the {@link EmbeddedTitleBar} instance
	 */
	public EmbeddedTitleBarToolTip(final EmbeddedTitleBar parent) {
		super(parent);
		this.embeddedTitleBar = parent;
		setShift(new Point(0, 0));
	}

	@Override
	public Point getLocation(final Point tipSize, final Event event) {
		final GC gc = new GC(embeddedTitleBar);
		try {
			final Rectangle bounds = getLnfTitlebarRenderer().computeTextBounds(gc);
			final Point location = embeddedTitleBar.toDisplay(bounds.x, 0);
			return location;
		} finally {
			gc.dispose();
		}
	}

	// protected methods
	////////////////////

	@Override
	protected boolean shouldCreateToolTip(final Event event) {
		boolean should = super.shouldCreateToolTip(event);

		if (should) {
			if (embeddedTitleBar.getToolTipText() != null) {
				return true;
			}
			should = embeddedTitleBar.isTextClipped();
		}

		return should;
	}

	@Override
	protected String getToolTipText(final Event event) {
		String toolTipText = embeddedTitleBar.getToolTipText();
		if (toolTipText == null) {
			toolTipText = embeddedTitleBar.getTitle();
		}
		return toolTipText;
	}

	@Override
	protected Integer getLnfDelay(final RienaDefaultLnf lnf) {
		return lnf.getIntegerSetting(LnfKeyConstants.MODULE_ITEM_TOOLTIP_POPUP_DELAY);
	}

	@Override
	protected Font getLnfFont(final RienaDefaultLnf lnf) {
		return lnf.getFont(LnfKeyConstants.MODULE_ITEM_TOOLTIP_FONT);
	}

	@Override
	protected Color getLnfBackground(final RienaDefaultLnf lnf) {
		return lnf.getColor(LnfKeyConstants.MODULE_ITEM_TOOLTIP_BACKGROUND);
	}

	@Override
	protected Color getLnfForeground(final RienaDefaultLnf lnf) {
		return lnf.getColor(LnfKeyConstants.MODULE_ITEM_TOOLTIP_FOREGROUND);
	}

	// helping methods
	//////////////////

	/**
	 * Returns the renderer of the title bar.
	 */
	private EmbeddedTitlebarRenderer getLnfTitlebarRenderer() {
		EmbeddedTitlebarRenderer renderer = (EmbeddedTitlebarRenderer) LnfManager.getLnf().getRenderer(
				LnfKeyConstants.SUB_MODULE_VIEW_TITLEBAR_RENDERER);
		if (renderer == null) {
			renderer = new EmbeddedTitlebarRenderer();
		}
		return renderer;
	}

}
