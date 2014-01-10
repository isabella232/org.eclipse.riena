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

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;

/**
 * Filter rule to provide a mandatory marker for a ridget.<br>
 * <i>Note: For every ridget an own instance of the marker is necessary (
 * {@link #getMarker()}).</i>
 */
public class UIFilterRuleRidgetMandatoryMarker extends AbstractUIFilterRuleRidgetMarker {

	/**
	 * Creates a new instance of {@code UIFilterRuleRidgetMandatoryMarker}.
	 * 
	 * @param id
	 *            ID
	 */
	public UIFilterRuleRidgetMandatoryMarker() {
		super(null, null);
	}

	/**
	 * Creates a new instance of {@code UIFilterRuleRidgetMandatoryMarker}.
	 * 
	 * @param ridgetIdPattern
	 *            ID
	 */
	public UIFilterRuleRidgetMandatoryMarker(final String ridgetIdPattern) {
		super(ridgetIdPattern, null);
	}

	/**
	 * Returns always a new instance of the {@code MandatoryMarker}. This is
	 * necessary because every ridget needs its own instance of the marker.
	 * Dependent on the content of the ridget (is empty of not) the marker
	 * affects the appearance of the ridget (e.g. yellow background color or
	 * default background color).
	 * 
	 * @see org.eclipse.riena.ui.filter.impl.AbstractUIFilterRuleMarker#getMarker()
	 */
	@Override
	public IMarker getMarker() {
		return new MandatoryMarker(false);
	}

}