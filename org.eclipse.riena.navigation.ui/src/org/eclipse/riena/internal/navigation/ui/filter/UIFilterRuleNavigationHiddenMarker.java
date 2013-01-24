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

import org.eclipse.riena.ui.core.marker.HiddenMarker;

/**
 * This rule of an UI filter adds a HiddenMarker to a navigation node.
 */
public class UIFilterRuleNavigationHiddenMarker extends AbstractUIFilterRuleNavigationMarker {

	public UIFilterRuleNavigationHiddenMarker() {
		super(null, new HiddenMarker());
	}

	public UIFilterRuleNavigationHiddenMarker(final String nodeIdPattern) {
		super(nodeIdPattern, new HiddenMarker());
	}

}
