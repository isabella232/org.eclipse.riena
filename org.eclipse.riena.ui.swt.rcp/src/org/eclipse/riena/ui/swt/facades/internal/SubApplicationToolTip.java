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

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationItem;
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationSwitcherWidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Tool tip for sub-applications.
 */
public class SubApplicationToolTip extends AbstractNavigationToolTip {

	private static final int DEFAULT_TOOLTIP_SHIFT_X = 10;
	private static final int DEFAULT_TOOLTIP_SHIFT_Y = 10;

	private final SubApplicationSwitcherWidget control;

	/**
	 * Creates new instance which add TooltipSupport to the switcher of
	 * sub-applications. For every tab/item another tool tip can be displayed.
	 * 
	 * @param parent
	 *            the switcher for sub-applications on whose action the tool tip
	 *            is shown
	 */
	public SubApplicationToolTip(final SubApplicationSwitcherWidget control) {
		super(control);
		Assert.isNotNull(control);
		this.control = control;
		setShift(new Point(DEFAULT_TOOLTIP_SHIFT_X, DEFAULT_TOOLTIP_SHIFT_Y));
	}

	@Override
	protected String getToolTipText(final Event event) {
		final SubApplicationItem item = control.getItem(new Point(event.x, event.y));
		return item != null ? item.getToolTipText() : null;
	}

	@Override
	protected boolean shouldCreateToolTip(final Event event) {
		if (super.shouldCreateToolTip(event)) {
			return StringUtils.isGiven(getToolTipText(event));
		}
		return false;
	}

	@Override
	protected Integer getLnfDelay(final RienaDefaultLnf lnf) {
		return lnf.getIntegerSetting(LnfKeyConstants.SUB_APPLICATION_ITEM_TOOLTIP_POPUP_DELAY);
	}

	@Override
	protected Font getLnfFont(final RienaDefaultLnf lnf) {
		return lnf.getFont(LnfKeyConstants.SUB_APPLICATION_ITEM_TOOLTIP_FONT);
	}

	@Override
	protected Color getLnfBackground(final RienaDefaultLnf lnf) {
		return lnf.getColor(LnfKeyConstants.SUB_APPLICATION_ITEM_TOOLTIP_BACKGROUND);
	}

	@Override
	protected Color getLnfForeground(final RienaDefaultLnf lnf) {
		return lnf.getColor(LnfKeyConstants.SUB_APPLICATION_ITEM_TOOLTIP_FOREGROUND);
	}

}
