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

import org.eclipse.riena.ui.core.marker.DisabledMarker;

/**
 * Filter rule to provide a disabled marker for a ridget.
 */
public class UIFilterRuleRidgetDisabledMarker extends AbstractUIFilterRuleRidgetMarker {

	/**
	 * Creates a new instance of {@code UIFilterRuleRidgetDisabledMarker}.
	 * 
	 * @param id
	 *            ID
	 */
	public UIFilterRuleRidgetDisabledMarker() {
		super(null, new DisabledMarker(false));
	}

	/**
	 * Creates a new instance of {@code UIFilterRuleRidgetDisabledMarker}.
	 * 
	 * @param ridgetIdPattern
	 *            ID
	 * @param marker
	 *            marker
	 */
	public UIFilterRuleRidgetDisabledMarker(final String ridgetIdPattern) {
		super(ridgetIdPattern, new DisabledMarker(false));
	}

}