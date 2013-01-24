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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.core.util.StringMatcher;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 * The matcher compares the ID of this class with the ID of a menu and tool bar
 * items.
 */
public class MenuItemRidgetMatcher extends RidgetMatcher {

	/**
	 * Creates a new instance of {@code MenuItemRidgetMatcher}.
	 * 
	 * @param idPattern
	 *            ID
	 */
	public MenuItemRidgetMatcher(final String idPattern) {
		super(idPattern);
	}

	@Override
	public boolean matches(final Object... args) {

		if ((args == null) || (args.length <= 0)) {
			return false;
		}
		if (!(args[0] instanceof IActionRidget)) {
			return false;
		}

		final IActionRidget ridget = (IActionRidget) args[0];
		final String ridgetId = ridget.getID();
		StringMatcher matcher = new StringMatcher(getMenuItemId());
		if (matcher.match(ridgetId)) {
			return true;
		}
		matcher = new StringMatcher(getToolbarItemId());
		if (matcher.match(ridgetId)) {
			return true;
		}

		return false;

	}

	private String getMenuItemId() {
		if (getId().startsWith(IActionRidget.BASE_ID_MENUACTION)) {
			return getId();
		} else {
			return IActionRidget.BASE_ID_MENUACTION + getId();
		}
	}

	private String getToolbarItemId() {
		if (getId().startsWith(IActionRidget.BASE_ID_TOOLBARACTION)) {
			return getId();
		} else {
			return IActionRidget.BASE_ID_TOOLBARACTION + getId();
		}
	}

}
