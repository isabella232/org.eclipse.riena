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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.ui.core.marker.HiddenMarker;

/**
 * Filter rule to provide a hidden marker for a menu and tool bar item.
 */
public class UIFilterRuleMenuItemHiddenMarker extends AbstractUIFilterRuleMenuItemMarker {

	/**
	 * Creates a new instance of {@code UIFilterRuleMenuItemHiddenMarker}.
	 */
	public UIFilterRuleMenuItemHiddenMarker() {
		super(null, new HiddenMarker(false));
	}

	/**
	 * Creates a new instance of {@code UIFilterRuleMenuActionHiddenMarker}.
	 * 
	 * @param idPattern
	 *            ID
	 */
	public UIFilterRuleMenuItemHiddenMarker(final String idPattern) {
		super(idPattern, new HiddenMarker(false));
	}

}