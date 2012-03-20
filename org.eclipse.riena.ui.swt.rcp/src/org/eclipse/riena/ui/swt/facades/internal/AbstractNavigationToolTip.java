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
package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 *
 */
public abstract class AbstractNavigationToolTip extends DefaultToolTip {

	public AbstractNavigationToolTip(final Control control) {
		super(control);
	}

	protected abstract String getToolTipText(final Event event);

	protected abstract Integer getLnfDelay(final RienaDefaultLnf lnf);

	protected abstract Font getLnfFont(final RienaDefaultLnf lnf);

	protected abstract Color getLnfBackground(final RienaDefaultLnf lnf);

	protected abstract Color getLnfForeground(final RienaDefaultLnf lnf);

	@Override
	protected Composite createToolTipContentArea(final Event event, final Composite parent) {
		final CLabel label = new CLabel(parent, getStyle(event));

		final Color fgColor = getForegroundColor(event);
		final Color bgColor = getBackgroundColor(event);
		final Font font = getFont(event);

		if (fgColor != null) {
			label.setForeground(fgColor);
		}

		if (bgColor != null) {
			label.setBackground(bgColor);
		}

		if (font != null) {
			label.setFont(font);
		}

		label.setText(getToolTipText(event));

		return label;
	}

	@Override
	protected boolean shouldCreateToolTip(final Event event) {
		final boolean should = super.shouldCreateToolTip(event);

		if (should) {
			initLookAndFeel();
		}

		return should;
	}

	/**
	 * Initializes the look (color and font) and feel (popup delay) of the tool
	 * tip. Uses the settings of the look and feel.
	 */
	private void initLookAndFeel() {
		final RienaDefaultLnf lnf = LnfManager.getLnf();

		final Integer delay = getLnfDelay(lnf);
		if (delay != null) {
			setPopupDelay(delay);
		}
		Color color = getLnfForeground(lnf);
		if (color != null) {
			setForegroundColor(color);
		}
		color = getLnfBackground(lnf);
		if (color != null) {
			setBackgroundColor(color);
		}
		final Font font = getLnfFont(lnf);
		if (font != null) {
			setFont(font);
		}
	}

}
