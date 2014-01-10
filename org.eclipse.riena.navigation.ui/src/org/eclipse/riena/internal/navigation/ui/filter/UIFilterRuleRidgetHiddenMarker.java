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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.ui.core.marker.HiddenMarker;

/**
 * Filter rule to provide a hidden marker for a ridget.
 */
public class UIFilterRuleRidgetHiddenMarker extends AbstractUIFilterRuleRidgetMarker {

	/**
	 * Creates a new instance of {@code UIFilterRuleRidgetHiddenMarker}.
	 * 
	 * @param id
	 *            ID
	 */
	public UIFilterRuleRidgetHiddenMarker() {
		super(null, new HiddenMarker(false));
	}

	/**
	 * Creates a new instance of {@code UIFilterRuleRidgetHiddenMarker}.
	 * 
	 * @param ridgetIdPattern
	 *            ID
	 */
	public UIFilterRuleRidgetHiddenMarker(final String ridgetIdPattern) {
		super(ridgetIdPattern, new HiddenMarker(false));
	}

}
